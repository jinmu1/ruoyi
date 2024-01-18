package com.ruoyi.system.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.system.enumType.CarType;
import com.ruoyi.system.form.GlcPoint;
import com.ruoyi.system.node.City;
import com.ruoyi.system.node.Material;
import com.ruoyi.system.node.Order;
import com.ruoyi.system.node.Supplier;
import com.ruoyi.system.result.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 网络规划类
 */
public class NetWorkPlanUtils {


    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    private static double workTime = 60 * 12;

    private static double plat = 1.1 * 1.25 * 1.5;

    /**
     * 通过运输单计算运输成本
     *
     * @param outorders
     * @param cities
     * @return
     */

    public static Result getTransportCost(City point, List<City> cities, List<Order> outorders, List<Order> inOrders, Result result, double time) {
        double transportCost = 0.0;
        double replenishmentCost = 0.0;
        double transportDate = 0.0; //运输周期
        double times = 0.0;
        double all = 0.0;
        int i = 0;
        Map<String, List<Order>> orderMaps = outorders.stream().collect(Collectors.groupingBy(Order::getOrderCode));//出库单
        Map<String, List<Order>> inorderMaps = inOrders.stream().collect(Collectors.groupingBy(Order::getOrderCode));//入库单

        double[] transportfee = new double[orderMaps.keySet().size()];
        for (String orderCode : inorderMaps.keySet()) {
            double orderCost = 0.0;
            List<Order> orderList = inorderMaps.get(orderCode);
            for (Order order : orderList) {
                for (City city : cities) {
                    if (city.getCity().equals(order.getSupplierCity())) {
                        double meterDouble = twoJuLi(order.getSupplierCity(), city) / 1000;
                        orderCost += order.getGoodsNum() * order.getVolume() * Math.ceil(meterDouble) * order.getSupplierCity().getTransportfee() * (1 + 0.11);//运输体积与运输量以及运输费率得到运输成本
                    }
                }
            }
            replenishmentCost += orderCost;

        }

        for (String ordercode : orderMaps.keySet()) {
            double orderCost = 0.0;
            List<Order> orderList = orderMaps.get(ordercode);
            /**
             * 配送运输成本计算
             */
            for (Order order : orderList) {
                for (City fee : cities) {
                    if (order.getCustomerCity().getCity().equals(fee.getCity())) {
                        GlobalCoordinates userSource = new GlobalCoordinates(Double.parseDouble(order.getCustomerCity().getLat()), Double.parseDouble(order.getCustomerCity().getLng()));
                        GlobalCoordinates businessTarget = new GlobalCoordinates(Double.parseDouble(point.getLat()), Double.parseDouble(point.getLng()));
//                         double meterDouble = getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere)/1000;
                        double meterDouble = twoJuLi(order.getCustomerCity(), fee) / 1000;
                        if (fee.getCity().equals(fee.getCity1())) {
                            meterDouble = 20;
                        }
                        orderCost += order.getGoodsNum() * order.getVolume() * Math.ceil(meterDouble) * fee.getTransportfee() * (1 + 0.11);//运输体积与运输量以及运输费率得到运输成本
                        transportDate = meterDouble / workTime; //运输周期
                        transportCost += orderCost;
                    }
                }
                all++;
                order.setDeliveryDate(DateUtils.getExpiredDay(order.getDeliveryDate(), (int) transportDate));
                double replenishmentDate = DateUtils.sub(sdf1.format(order.getOrderDate()), sdf1.format(order.getReplenishmentDate()), "d");
                double storageDate = DateUtils.sub(sdf1.format(order.getReplenishmentDate()), sdf1.format(order.getStorageDate()), "d");
                if ((replenishmentDate + storageDate + transportDate) < time) {
                    times++;
                }
//                 if(DateUtils.sub(sdf1.format(order.getOrderDate()),sdf1.format(order.getDeliveryDate()),"d")>transportDate){
//                   times++;
//                 }
            }
            transportfee[i] = transportCost;
            i++;
        }


        result.setTransportCost(transportCost + replenishmentCost);
        result.setRate1(times / all * 100);
        result.setOrder_rate(MathUtils.avg(transportfee));
        return result;
    }

    /**
     * 计算仓储成本
     *
     * @return
     */
    public static Result getStorageCost(List<Order> inorders, List<Order> outOrders, double emp_quantity, double warehousing, Result result, int pointNum) {
        double storageCost = 0.0;
        Map<Date, List<Order>> inordersList = inorders.stream().collect(Collectors.groupingBy(Order::getDeliveryDate));//入库单
        Map<Date, List<Order>> outOrdersList = outOrders.stream().collect(Collectors.groupingBy(Order::getDeliveryDate));//出库单
        double stroageDate = 0.0;
        double[] work = new double[inordersList.keySet().size()];
        int i = 0;
        for (Date date : inordersList.keySet()) {
            List<Order> in = inordersList.get(date);
            List<Order> out = outOrdersList.get(date);
            double worknum = 0.0;
            for (Order order : in) {
                worknum += Math.abs(order.getGoodsNum() * order.getVolume() / plat);
            }
            for (Order order : out) {
                worknum += Math.abs(order.getGoodsNum() * order.getVolume() / plat);
            }
            stroageDate += worknum * emp_quantity / workTime;//仓库作业时间
            work[i] = worknum;
            i++;
        }
        double num = MathUtils.avg(work);
        double emp = Math.ceil(num / emp_quantity);
        double point = Math.pow(1.01, pointNum);


        storageCost = emp * warehousing * point;
        result.setEmp(emp);
        result.setStorageCost(storageCost);
        return result;
    }

    /**
     * 库存持有成本
     *
     * @param
     * @param outOrders
     * @param order
     * @return
     */
    public static Result inventoryCost(List<Order> outOrders, List<Supplier> supplierList, double order, double inventory_loss, Result result) {
        double inventory = 0.0;
        double inventory1 = 0.0;
        double inventoryCost = 0.0;
        double sales_account = 0.0;
        double lever = 1.65;
        List<Order> runOrder = new ArrayList<>();

        Map<String, List<Order>> outOrdersList = outOrders.stream().collect(Collectors.groupingBy(Order::getGoodsCode));//出库单
        Map<String, List<Supplier>> stringListMap = supplierList.stream().collect(Collectors.groupingBy(Supplier::getGoodsCode));//出库单
        for (String goodsCode : outOrdersList.keySet()) {
            List<Order> orders = outOrdersList.get(goodsCode);
            double safeInventory = getSafeInventory(orders, order); //获取安全库存
            double orderNum = getOrderNum(orders);
            inventory += (safeInventory + orderNum / 2);
            inventory1 += (safeInventory + orderNum / 2) * orders.get(0).getVolume() / plat;
            inventoryCost += inventory * orders.get(0).getGoodsPrice() / 1000 * (0.05 + inventory_loss / 100);
            for (Order order1 : orders) {
                sales_account += order1.getGoodsNum() * orders.get(0).getGoodsPrice();
            }
        }
        result.setInventory_num(inventory1);

        result.setInventoryCost(inventoryCost);
        result.setSales_account(sales_account);
        return result;
    }

    /**
     * 建设成本
     *
     * @param carLength 车长
     * @return
     */
//    public static double buildCost(double inventory ,double in,double out, double price,String carLength){
//

//    }
    public static Result buildCost(List<Order> inOrders, List<Order> outOrders, Result result, City city, String carLength) {

        Map<Date, List<Order>> inOrdersList = inOrders.stream().collect(Collectors.groupingBy(Order::getDeliveryDate));//出库单
        Map<Date, List<Order>> outOrdersList = outOrders.stream().collect(Collectors.groupingBy(Order::getDeliveryDate));//出库单
        double[] ins = new double[inOrdersList.keySet().size()];
        double[] outs = new double[inOrdersList.keySet().size()];
        int i = 0;
        for (Date date : inOrdersList.keySet()) {
            double in = 0.0;
            double out = 0.;
            for (Order order : inOrdersList.get(date)) {
                in += order.getGoodsNum() * order.getVolume() / plat;
            }
            for (Order order : outOrdersList.get(date)) {
                out += order.getGoodsNum() * order.getVolume() / plat;
            }
            ins[i] = in;
            outs[i] = out;
            i++;
        }
        double in = Math.abs(MathUtils.avg(ins));
        double out = Math.abs(MathUtils.avg(outs));
        double platform = Math.abs(AreaUtils.getPlatform((in + out), carLength).getPlatform_area());

        double storage_area = 0.0;
        double inventory = result.getInventory_num();
        if (inventory > 5) {

            storage_area = Math.abs(AreaUtils.getHightStorage(inventory, out).getArea());

        } else {

            storage_area = Math.abs(AreaUtils.getHeapStorage(inventory).getArea());

        }

//        double intally = Math.abs(AreaUtils.getTally(in, carLength).getArea());

        double outtally = Math.abs(AreaUtils.getTally(out, carLength).getArea());

        double area = outtally + storage_area + platform;
        double price = city.getPrice();
        price = 22;
        double buildCost = area * price * 12;
        result.setThroughput_num(in + out);
        result.setArea(area);
        result.setBuildCost(buildCost);

        return result;
    }

    /**
     * 获取订货量
     *
     * @param orders
     * @param
     * @return
     */
    public static double getOrderNum(List<Order> orders) {

        double ordernum = 0.0;

        double total = 0.0;

        double min = Double.MAX_VALUE;

        double max = 0.0;


        for (Order order : orders) {

            total += order.getGoodsNum();
            if (order.getDeliveryDate().getTime() > max) {
                max = order.getDeliveryDate().getTime();
            }
            if (order.getDeliveryDate().getTime() < min) {
                min = order.getDeliveryDate().getTime();
            }
        }
        int year = (int) Math.ceil((max - min) / 1000 / 60 / 60 / 24 / 365); //周期
        double price = orders.get(0).getGoodsPrice();
        double volume = orders.get(0).getVolume();
        double wareHouseFee = 32 * 12; //每年十二个月租金
        int emp_quantity = 50;//一天处理50托
        double empsalary = 80000;
        double C1 = price * 0.04 * year + volume / (2.14 * plat) * wareHouseFee * year + volume * total / emp_quantity / 365 / year * empsalary;
        double C3 = volume * total * 0.65 * 150;
        ordernum = Math.ceil(Math.sqrt(2 * total * C3 / (C1)));

        return ordernum;

    }

    /***
     * 计算安全库存
     * @param orders
     * @param
     * @return
     */
    public static double getSafeInventory(List<Order> orders, double order) {
        double lever = 1.25;
        if (order == 90) {
            lever = 1.25;
        } else if (order == 95) {
            lever = 1.65;
        } else if (order == 98) {
            lever = 2.05;
        } else if (order == 100) {
            lever = 3.5;
        }

        if (orders == null || orders.size() == 0 || orders.size() == 1) {
            return 0;
        }

        Map<Date, List<Order>> outOrdersList = orders.stream().collect(Collectors.groupingBy(Order::getDeliveryDate));//出库单
        double[] work = new double[outOrdersList.keySet().size()];
        int i = 0;

        for (Date date : outOrdersList.keySet()) {
            List<Order> out = outOrdersList.get(date);
            double worknum = 0.0;
            for (Order orderd : out) {
                worknum += orderd.getGoodsNum();
            }
            work[i] = worknum;
            i++;
        }
        if (work == null || work.length == 0 || work.length == 1) {
            return 0;
        }
        double leadTime = 3;
        return Math.ceil(lever * MathUtils.standardDeviation(work) * leadTime);
    }

    /**
     * 补货机制
     *
     * @param orders
     * @return
     */
    public static List<Order> getReplenishment(List<Order> orders, List<Material> materialList) {

        Map<Date, List<Order>> outOrdersList = orders.stream().collect(Collectors.groupingBy(Order::getDeliveryDate));//出库单
        List<Order> orderList = new ArrayList<>();
        List<Order> runOrder = new ArrayList<>();
        double replenishmentDate = 0.0;
        int i = 0;
        Random random = new Random();
        double[] in = new double[outOrdersList.keySet().size()];
        double[] out = new double[outOrdersList.keySet().size()];
        for (Date date : outOrdersList.keySet()) {
            List<Order> list = outOrdersList.get(date);
            if (runOrder.size() > 0) {
                list.addAll(runOrder);
                runOrder = new ArrayList<>();
            }
            Map<String, List<Order>> listMap = list.stream().collect(Collectors.groupingBy(Order::getGoodsCode));//出库单
            double ins = 0.0;
            double out1 = 0.0;
            for (Material material : materialList) {
                List<Order> list1 = listMap.get(material.getCode());
                if (list1 != null && list1.size() != 0) {
                    for (Order order : list1) {
                        material.setNeedNum(material.getNeedNum() - order.getGoodsNum());
                        out1 += order.getGoodsNum() * material.getVolume();
                    }
                    if (material.getNeedNum() < material.getInventory()) {
                        replenishmentDate = material.getSupplier().getLeadTime();
                        material.setNeedNum(material.getNeedNum() + material.getOrderNum());
                        orderList.add(new Order("D" + RandomUtil.toFixdLengthString(random.nextInt(10000), 4), date, material.getCode(), material.getNeedNum(), material.getVolume(), material.getPrice()));
                        for (Order order : list1) {
                            order.setReplenishmentDate(DateUtils.getExpiredSecond(order.getDeliveryDate(), (int) replenishmentDate));
                            order.setDeliveryDate(DateUtils.getNowExpiredDay(1));
                            runOrder.add(order);
                        }
                        if (DateUtils.sub(date, list1.get(0).getReplenishmentDate(), "d") == 0) {
                            ins += material.getOrderNum() * material.getVolume();
                        }

                    }


                }
            }
            in[i] = ins;
            out[i] = out1;
        }
//        for(Order order:orderList){
//            order.setCustomerCode(getCustomerCode(order.getGoodsCode()));
//        }

        return orderList;
    }


    /***
     * 通过网络参数输出网络结果数据
     * carType:车辆类型
     * transport
     * @return
     */

    public static double getNetWorkData(String carType, double transportNum, List<GlcPoint> list) {
        double sale_accout = 0.0;
        double gdp = 0.0;
        for (GlcPoint city : list) {
            gdp += Double.parseDouble(city.getGdp());
        }
        List<City> rdcPoint = new ArrayList<>();//rdc生成池
        List<City> point = new ArrayList<>();
        Map<String, List<GlcPoint>> listMap = list.stream().collect(Collectors.groupingBy(GlcPoint::getCity));//结果集合
        for (String center : listMap.keySet()) {
            GlcPoint glcPoint = listMap.get(center).get(0);            //需求点
            point.add(new City(glcPoint.getCity(), glcPoint.getLat(), glcPoint.getLng(), glcPoint.getGdp()));//备选带你
        }
        double max = Double.MAX_VALUE;
        int number = 0;
        for (int i = 1; i < list.size(); i++) {
            List<List<City>> combinations = NetworkUtils.combinations(point, new Random(list.size()).nextInt());//已经遍历了该RDC数量下的所有RDC组合 k为备选点数量
            List<City> cities = new ArrayList<>();
            for (List<City> cityList : combinations) {
                rdcPoint = new ArrayList<>();
                for (City city : cityList) {
                    rdcPoint.add(new City(city.getCity())); //将选取的备选点当做新增的RDC
                }
                // 开始计算
                cities = chooseCity(rdcPoint, point);//选择城市
                double transportCost = 0.0;
                for (City city : cities) {
                    double carCos = Double.parseDouble(CarType.valueOf(carType).getCode()); //计算车辆类型
                    double CarNum = Double.parseDouble(city.getGdp()) / gdp * transportNum * plat / carCos;//计算车辆数量(总计)
                    transportCost += Double.parseDouble(city.getDistance()) * 0.89 * 1.09 * transportNum * plat;//计算运输成本
                }
                if (transportCost < max) {
                    max = transportCost;

                }

            }
        }
        return max;
    }


    /**
     * 选择城市 城市对应各城市
     *
     * @param rdcPoint
     * @param netPoint
     * @param
     * @param
     * @return
     */
    public static List<City> chooseCity(List<City> rdcPoint, List<City> netPoint) {

        List<City> combination = new ArrayList<>();
        for (City netPoints : netPoint) {
            City city1 = new City(netPoints.getCity(), netPoints.getLat(), netPoints.getLng(), netPoints.getGdp());
            double max = Integer.MAX_VALUE;
            for (City city : rdcPoint) {

//                double meterDouble = getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere);
                double meterDouble = twoJuLi(netPoints, city);

                double num = meterDouble / 1000;
                if (num <= 40) {
                    num = 40;
                }
                if (num < max) {
                    city1.setCity1(city.getCity());
                    city1.setLat1(city.getLat());
                    city1.setLng1(city.getLng());
                    city1.setDistance(String.valueOf(num));
                    max = num;
                }
            }
            combination.add(city1);
        }
        return combination;
    }

    public static <T> List<List<T>> combiantion(T[] datas, int number) {
        if ((datas == null) || (datas.length == 0) || (datas.length < number)) {
            return null;
        }
        List<List<T>> combines = new ArrayList<List<T>>();
        List<T> temps = new ArrayList<T>();

        combine(datas, 0, number, temps, combines);
        return combines;
    }

    public static double chooseCity1(List<City> rdcPoint, List<City> netPoint) {
        double distance = 0.0;
        List<City> combination = new ArrayList<>();
        for (City netPoints : netPoint) {
            City city1 = new City(netPoints.getCity(), netPoints.getLat(), netPoints.getLng(), netPoints.getGdp());
            double max = Integer.MAX_VALUE;
            for (City city : rdcPoint) {
                GlobalCoordinates userSource = new GlobalCoordinates(Double.parseDouble(netPoints.getLat()), Double.parseDouble(netPoints.getLng()));
                GlobalCoordinates businessTarget = new GlobalCoordinates(Double.parseDouble(city.getLat()), Double.parseDouble(city.getLng()));
                double meterDouble = getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere);
//                 meterDouble = Double.parseDouble(twoJuLi(netPoints,city));

                double num = meterDouble / 1000;
                if (num == 0) {
                    num = 30;
                }
                if (num < max) {
                    city1.setCity1(city.getCity());
                    city1.setLat1(city.getLat());
                    city1.setLng1(city.getLng());
                    city1.setDistance(String.valueOf(num));
                    max = num;
                }
            }
            combination.add(city1);
            distance += Double.parseDouble(city1.getDistance()) * Double.parseDouble(city1.getGdp());
        }
        return distance;
    }


    public static <T> void combine(T[] datas, int begin, int number, List<T> temps, List<List<T>> combines) {
        if (number == 0) {
            List<T> aCombine = new ArrayList<T>();
            aCombine.addAll(temps);
            combines.add(aCombine);
            return;
        }
        if (begin == datas.length) {
            return;
        }
        temps.add(datas[begin]);
        combine(datas, begin + 1, number - 1, temps, combines);
        temps.remove(datas[begin]);
        combine(datas, begin + 1, number, temps, combines);
    }


    /**
     * 调用百度api接口，返回距离
     */

    public static Double twoJuLi(City city, City city1) {
        double juli = 0.0;
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.select(3);
        Boolean exists = jedis.exists(city.getCity() + ":" + city1.getCity());
        if (exists) {
            juli = Double.parseDouble(jedis.get(city.getCity() + ":" + city1.getCity()));

        } else if (jedis.exists(city1.getCity() + ":" + city.getCity())) {
            juli = Double.parseDouble(jedis.get(city1.getCity() + ":" + city.getCity()));
        } else {
            StringBuilder originsParam = new StringBuilder();
            BigDecimal zuobiaoY1 = BigDecimal.valueOf(Double.valueOf(city.getLat()));
            BigDecimal zuobiaoX1 = BigDecimal.valueOf(Double.valueOf(city.getLng()));
            StringBuilder destinationsParam = new StringBuilder();
            originsParam.append(zuobiaoX1.setScale(8, BigDecimal.ROUND_HALF_UP));
            originsParam.append(",");
            originsParam.append(zuobiaoY1.setScale(8, BigDecimal.ROUND_HALF_UP));
            destinationsParam.append(BigDecimal.valueOf(Double.valueOf(city1.getLng())).setScale(8, BigDecimal.ROUND_HALF_UP));
            destinationsParam.append(",");
            destinationsParam.append(BigDecimal.valueOf(Double.valueOf(city1.getLat())).setScale(8, BigDecimal.ROUND_HALF_UP));

            //调用百度地图api
            List<JSONObject> result = new ArrayList<JSONObject>();
            //   destinationsParam.deleteCharAt(destinationsParam.lastIndexOf("|"));
            String url = String.format("http://api.map.baidu.com/routematrix/v2/driving?output=json&origins=%s&destinations=%s&ak=%s", URLEncoder.encode(originsParam.toString()), URLEncoder.encode(destinationsParam.toString()), "NURx5GUAQ1IBZVBMCQSpYppRsDdTpbxG");
            CloseableHttpClient client = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = client.execute(httpGet);
                int status = 200;
                if (httpResponse.getStatusLine().getStatusCode() == status) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    String text = EntityUtils.toString(httpEntity);//取出应答字符串
                    JSONObject json = JSONObject.parseObject(text);
                    String i1 = null;
                    JSONArray array = json.getJSONArray("result");
                    JSONObject distance = array.getJSONObject(0);
                    //   distance.put("node", destinations.get(i));
                    //   result.add(distance);
                    juli = Double.parseDouble(distance.getJSONObject("distance").getString("value"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            jedis.select(3);
            jedis.set(city.getCity() + ":" + city1.getCity(), JSON.toJSON(juli).toString());

        }
        jedis.close();
        return juli;

    }

    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid) {

        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }


    /**
     * 通过运输单计算运输成本
     *
     * @param orders
     * @param cities
     * @return
     */

    public static List<Order> getTransportCost1(City point, List<City> cities, List<Order> orders) {

        double transportCost = 0.0;
        double transportDate = 0.0; //运输周期
        double times = 0.0;
        int i = 0;
        Map<String, List<Order>> orderMaps = orders.stream().collect(Collectors.groupingBy(Order::getOrderCode));//入库单
        double[] transportfee = new double[orderMaps.keySet().size()];
        for (String ordercode : orderMaps.keySet()) {
            double orderCost = 0.0;
            Order orderd = new Order();
            orderd.setOrderCode(ordercode);
            List<Order> orderList = orderMaps.get(ordercode);
            for (Order order : orderList) {
                for (City fee : cities) {
                    if (order.getCustomerCity().getCity().equals(fee.getCity())) {
                        GlobalCoordinates userSource = new GlobalCoordinates(Double.parseDouble(order.getCustomerCity().getLat()), Double.parseDouble(order.getCustomerCity().getLng()));
                        GlobalCoordinates businessTarget = new GlobalCoordinates(Double.parseDouble(point.getLat()), Double.parseDouble(point.getLng()));
                        double meterDouble = getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere) / 1000;
//                         double meterDouble = twoJuLi(order.getCustomerCity(),fee)/1000;
                        if (meterDouble < 100) {
                            meterDouble = 100;
                        }

                        orderCost += order.getGoodsNum() * order.getVolume() * Math.sqrt(meterDouble) * 0.25;//运输体积与运输量以及运输费率得到运输成本
                        transportDate = meterDouble / 60 / 24; //运输周期
                        order.setCustomerName(orderCost + "");
                        transportCost += orderCost;
                    }
                }
                order.setDeliveryDate(DateUtils.getExpiredDay(order.getDeliveryDate(), (int) transportDate));
            }
            transportfee[i] = transportCost;
            i++;
        }
        for (Order order : orders) {
            order.setCustomerCity(null);
        }
        return orders;
    }



}
