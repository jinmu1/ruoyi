package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.algorithm.kmeans.Cluster;
import com.ruoyi.system.form.GlcPoint;
import com.ruoyi.system.node.*;
import com.ruoyi.system.result.Result;
import com.ruoyi.system.result.ResultMsg;
import com.ruoyi.system.utils.NetWorkPlanUtils;
import com.ruoyi.system.utils.NetworkUtils;
import com.ruoyi.system.service.IGlcPointService;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2021-06-24
 */
@Controller
@RequestMapping("/system/netWork")
public class NetWorkController extends BaseController
{
    @Autowired
    private IGlcPointService glcPointService;

    @PostMapping("/network")
    @ResponseBody
    public TableDataInfo  network1(HttpServletRequest req){

        int num1 = Integer.parseInt(req.getParameter("num"));
        String provinces = req.getParameter("provinces");
        String carLength = req.getParameter("carLength");

        double transportNum = 80000;
        if (req.getParameter("transportNum")!=null&&!req.getParameter("transportNum").equals("")){
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double warehousing = 80000;
        if (req.getParameter("warehousing")!=null&&!req.getParameter("warehousing").equals("")) {
            warehousing = Double.parseDouble(req.getParameter("warehousing"));
        }
        double h_price = 15;
        if(req.getParameter("h_price")!=null&&!req.getParameter("h_price").equals("")) {
            h_price = Double.parseDouble(req.getParameter("h_price"));
        }
        double m_price = 15;
        if(req.getParameter("m_price")!=null&&!req.getParameter("m_price").equals("")) {
            m_price = Double.parseDouble(req.getParameter("m_price"));
        }
        double l_price = 60;
        if(req.getParameter("s_price")!=null&&!req.getParameter("s_price").equals("")) {
            l_price = Double.parseDouble(req.getParameter("s_price"));
        }
        double inventory_loss = 0.5;
        if(req.getParameter("inventory_loss")!=null&&!req.getParameter("inventory_loss").equals("")) {
            inventory_loss = Double.parseDouble(req.getParameter("inventory_loss"));
        }
        double managementFee = 0;
        if(req.getParameter("managementFee")!=null&&!req.getParameter("managementFee").equals("")) {
            managementFee = Double.parseDouble(req.getParameter("managementFee"));
        }
        double times = 72;
        if(req.getParameter("times")!=null&&!req.getParameter("times").equals("")) {
            times = Integer.parseInt(req.getParameter("times"));
        }
        double order = 95;
        if(req.getParameter("order")!=null&&!req.getParameter("order").equals("")) {
            order = Integer.parseInt(req.getParameter("order"));
        }
        double goods_num = 6000;
        if(req.getParameter("goods_num")!=null&&!req.getParameter("goods_num").equals("")) {
            goods_num = Integer.parseInt(req.getParameter("goods_num"));
        }
                double b_goods_size = 20;
        if(req.getParameter("b_goods_size")!=null&&!req.getParameter("b_goods_size").equals("")) {
            b_goods_size = Double.parseDouble(req.getParameter("b_goods_size"));
        }
        double m_goods_size = 20;
        if(req.getParameter("m_goods_size")!=null&&!req.getParameter("m_goods_size").equals("")) {
            m_goods_size = Double.parseDouble(req.getParameter("m_goods_size"));
        }
        double s_goods_size = 60;
        if(req.getParameter("s_goods_size")!=null&&!req.getParameter("s_goods_size").equals("")) {
            s_goods_size = Double.parseDouble(req.getParameter("s_goods_size"));
        }
        int emp_quantity = 50;//一天处理50托

        List<ResultMsg> resultMsgs = new ArrayList<>();
        List<JSONObject> listd;
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.select(2);
        Boolean exists= jedis.exists(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+b_goods_size+m_goods_size+s_goods_size);
        if (exists) {
            String in = jedis.get(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+b_goods_size+m_goods_size+s_goods_size);
            listd=  (List<JSONObject>) JSON.parse(in);
            for(JSON json:listd){
                ResultMsg status =JSON.toJavaObject(json, ResultMsg.class);
                resultMsgs.add(status);
            }
        }else {
            List<GlcPoint> cityList = glcPointService.selectGlcPointList(new GlcPoint(provinces));  //获取省份内城市
            List<City> points = new ArrayList<>();
            for (GlcPoint glcPoint : cityList) {//需求点
                points.add(new City(glcPoint.getCity(), glcPoint.getLat(), glcPoint.getLng(), glcPoint.getGdp()));//备选点
            }
            List<Material> list = NetworkUtils.createGoods(goods_num);
            List<Supplier> suppliers = NetworkUtils.initSupplier(list.size(),cityList);
            list = NetworkUtils.initMaterialPrice(list, h_price, m_price, l_price);
            list = NetworkUtils.initMaterialVolume(list, b_goods_size, m_goods_size, s_goods_size);
            list = NetworkUtils.initMaterialSupplier(suppliers,list);
            List<Order> orderList = NetworkUtils.initOrders(list, transportNum);
            List<Customer> customerList = NetworkUtils.initCustomer(cityList, 200);

            orderList = NetworkUtils.initOrdersCustomerList(orderList, customerList);
            Map<City, List<Order>> outOrdersList = orderList.stream().collect(Collectors.groupingBy(Order::getCustomerCity));//出库单


            for (int i = 1; i <= cityList.size(); i++) {
                List<Result> results = new ArrayList<>();
                Result allresult = new Result();

                List<List<Cluster>> clusters1 = initCentroides(points, i);
                List<Cluster> cluster2 = new ArrayList<>();
                double max1 = Double.MAX_VALUE;
                List<List<Cluster>> clusters = new ArrayList<>();
                List<City> rdcPoint = new ArrayList<>();
                for(List<Cluster> cluster1 : clusters1) {

                    results = new ArrayList<>();
                    rdcPoint = new ArrayList<City>();
                    for (Cluster rdc: cluster1) {
                        rdcPoint.add(rdc.getCentroid()); //将选取的备选点当做新增的RDC
                    }
                    // 开始计算
                    double distance = NetWorkPlanUtils.chooseCity1(rdcPoint, points);//选择城市
                    if (distance < max1) {
                        max1 = distance;
                        clusters.add(cluster1);
                    }

                }

                double max = Double.MAX_VALUE;
                for(List<Cluster> cluster1 : clusters) { // 所有分类是否全部收敛
                    // 1.计算距离对每个点进行分类
                    // 2.判断质心点是否改变,未改变则该分类已经收敛
                    // 3.重新生成质心点
                    initClusters(cluster1); // 重置分类中的点
                    classifyPoint(points, cluster1);// 计算距离进行分类
                   double distance =  recalcularCentroides(cluster1); // 重新计算质心点
                    if (distance<max) {
                        cluster2 = cluster1;
                        max = distance;
                    }
                }
                List<City> cities = new ArrayList<>();
                for (Cluster cluster : cluster2) {
                    Result result = new Result();
                    List<Order> outOrders = new ArrayList<>();
                    result.setRange(cluster.getCentroid().getCity());
                    for (City city : outOrdersList.keySet()) {
                        for (City city1 : cluster.getPoints()) {
                            if (city.getCity().equals(city1.getCity())) {
                                outOrders.addAll(outOrdersList.get(city));
                            }
                        }
                    }
                    result.setCity(cluster.getCentroid().getCity());
                    List<Order> inOrders = NetWorkPlanUtils.getReplenishment(outOrders, list); //获取补货单数据
                    result = NetWorkPlanUtils.getStorageCost(inOrders, outOrders, emp_quantity, warehousing, result,i);//获取仓储成本数据
                    result = NetWorkPlanUtils.getTransportCost(cluster.getCentroid(), cluster.getPoints(), outOrders,inOrders, result,times);//获取运输成本数据
                    result = NetWorkPlanUtils.inventoryCost(outOrders, suppliers, order, inventory_loss, result); // 获取库存成本数据
                    result = NetWorkPlanUtils.buildCost(inOrders, outOrders, result, cluster.getCentroid(), carLength);
                    result.setAll(Math.round(result.getTransportCost() + result.getBuildCost() + result.getInventoryCost() + result.getStorageCost()));
//                    result.setOrder_rate(result.getAll()/result.getSales_account()); //订单费率
                    result.setRate(result.getAll() / result.getSales_account());  //计算费率
                    result.setStorageCost(result.getStorageCost()+managementFee);
                    result.setOrder_cost(result.getAll()/list.size());
                    result.setStorage_area(result.getInventory_num()/result.getArea());
                    results.add(result);
                    for (City city : cluster.getPoints()) {
                        city.setCity1(cluster.getCentroid().getCity());
                        city.setLat1(cluster.getCentroid().getLat());
                        city.setLng1(cluster.getCentroid().getLng());
                        cities.add(city);
                    }
                }

                // 计算目标函数值

                for (Result result : results) {
                    allresult.setCity(result.getCity());
                    allresult.setStorage(Math.round(result.getStorage() + allresult.getStorage()));
                    allresult.setArea(Math.round(allresult.getArea() + result.getArea()));
                    allresult.setStorageCost(Math.round(allresult.getStorageCost() + result.getStorageCost()));
                    allresult.setBuildCost(Math.round(allresult.getBuildCost() + result.getBuildCost()));
                    allresult.setInventoryCost(Math.round(allresult.getInventoryCost() + result.getInventoryCost()));
                    allresult.setTransportCost(Math.round(allresult.getTransportCost() + result.getTransportCost()));
                    allresult.setAll(Math.round(allresult.getAll() + result.getAll()));
                    allresult.setThroughput_num(Math.round(allresult.getThroughput_num() + result.getThroughput_num()));
                    allresult.setEmp(Math.round(allresult.getEmp() + result.getEmp()));
                    allresult.setCar(Math.round(allresult.getCar() + result.getCar()));
//                    allresult.setOrder_rate(allresult.getOrder_rate() + result.getOrder_rate());
                    allresult.setInventory_num(Math.round(result.getInventory_num() / allresult.getArea()));
                    allresult.setSales_account(Math.round(allresult.getSales_account() + result.getSales_account()));
                    allresult.setRate1(allresult.getRate1() + result.getRate1());
                    allresult.setStorage_area(allresult.getStorage_area() + result.getStorage_area());
                    allresult.setPlat_cost(allresult.getAll() / transportNum);
                    allresult.setArea_cost(allresult.getAll() / result.getArea());   //单位面积租金
                }
                allresult.setStorageCost(allresult.getStorageCost());
                allresult.setAll(allresult.getAll());
                allresult.setOrder_rate(allresult.getOrder_rate() / i);
                allresult.setInventory_num(Math.round(allresult.getInventory_num() / i));
                allresult.setStorage_area(Math.round(allresult.getStorage_area() / i));
                allresult.setRate(allresult.getAll() / allresult.getSales_account());
                allresult.setArea_cost(allresult.getAll() / allresult.getArea());
                allresult.setPlat_cost(allresult.getPlat_cost());
                allresult.setRate1(allresult.getRate1() / i);
                allresult.setPlat_storage(Math.round(allresult.getStorage() / transportNum / i));
                allresult.setOrder_cost(allresult.getAll() / list.size());   //总订单成本除以订单数量
                allresult.setPlat_transport(Math.round(allresult.getTransportCost() / transportNum / i));
                if (allresult.getAll()<max)
                {
                    ResultMsg resultMsg = new ResultMsg();
                    resultMsg.setRdc(i);
                    resultMsg.setCost(allresult.getAll());
                    resultMsg.setCities(cities);
                    resultMsg.setResult(allresult);
                    resultMsg.setResultList(results);
                    resultMsgs.add(resultMsg);
                }
                allresult = new Result();

            }
            jedis.select(2);
            jedis.set(provinces + carLength + transportNum + warehousing + h_price + m_price + inventory_loss + times + order + managementFee + goods_num + "" + b_goods_size + m_goods_size + s_goods_size, JSON.toJSON(resultMsgs).toString());
        }
        resultMsgs = resultMsgs.stream().sorted(Comparator.comparing(ResultMsg::getCost)).collect(Collectors.toList());
        List<List<City>> listList = new ArrayList<>();
        for (ResultMsg resultMsg : resultMsgs){
            listList.add(resultMsg.getCities());
        }
        double[] num = {1, 25, 50, 75, 90, 100};
        for (List<City> list1 : listList) {
            Map<String, List<City>> linkCity = list1.stream().collect(Collectors.groupingBy(City::getCity1));
            int i = 0;
            for (String str : linkCity.keySet()) {
                List<City> cities1 = linkCity.get(str);
                for (City city : cities1) {
                    city.setGdp(num[i % 5] + "");
                }
                i++;
            }
        }
        return getDataTable(listList.get(listList.size()-1));

    }


    @PostMapping("/network1")
    @ResponseBody
    public TableDataInfo  network2(HttpServletRequest req) {
        int num1 = Integer.parseInt(req.getParameter("num"));

        String carLength = req.getParameter("carLength");

        double transportNum = 80000;
        if (req.getParameter("transportNum")!=null&&!req.getParameter("transportNum").equals("")){
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double warehousing = 80000;
        if (req.getParameter("warehousing")!=null&&!req.getParameter("warehousing").equals("")) {
            warehousing = Double.parseDouble(req.getParameter("warehousing"));
        }
        double h_price = 15;
        if(req.getParameter("h_price")!=null&&!req.getParameter("h_price").equals("")) {
            h_price = Double.parseDouble(req.getParameter("h_price"));
        }
        double m_price = 15;
        if(req.getParameter("m_price")!=null&&!req.getParameter("m_price").equals("")) {
            m_price = Double.parseDouble(req.getParameter("m_price"));
        }
        double l_price = 60;
        if(req.getParameter("s_price")!=null&&!req.getParameter("s_price").equals("")) {
            l_price = Double.parseDouble(req.getParameter("s_price"));
        }
        double inventory_loss = 0.5;
        if(req.getParameter("inventory_loss")!=null&&!req.getParameter("inventory_loss").equals("")) {
            inventory_loss = Double.parseDouble(req.getParameter("inventory_loss"));
        }
        double managementFee = 0;
        if(req.getParameter("managementFee")!=null&&!req.getParameter("managementFee").equals("")) {
            managementFee = Double.parseDouble(req.getParameter("managementFee"));
        }
        double times = 72;
        if(req.getParameter("times")!=null&&!req.getParameter("times").equals("")) {
            times = Integer.parseInt(req.getParameter("times"));
        }
        double order = 95;
        if(req.getParameter("order")!=null&&!req.getParameter("order").equals("")) {
            order = Integer.parseInt(req.getParameter("order"));
        }
        double goods_num = 6000;
        if(req.getParameter("goods_num")!=null&&!req.getParameter("goods_num").equals("")) {
            goods_num = Integer.parseInt(req.getParameter("goods_num"));
        }
        double b_goods_size = 20;
        if(req.getParameter("b_goods_size")!=null&&!req.getParameter("b_goods_size").equals("")) {
            b_goods_size = Double.parseDouble(req.getParameter("b_goods_size"));
        }
        double m_goods_size = 20;
        if(req.getParameter("m_goods_size")!=null&&!req.getParameter("m_goods_size").equals("")) {
            m_goods_size = Double.parseDouble(req.getParameter("m_goods_size"));
        }
        double s_goods_size = 60;
        if(req.getParameter("s_goods_size")!=null&&!req.getParameter("s_goods_size").equals("")) {
            s_goods_size = Double.parseDouble(req.getParameter("s_goods_size"));
        }
        int emp_quantity = 50;//一天处理50托
        List<City> points = new ArrayList<>();
        List<Material> list = NetworkUtils.createGoods(goods_num);
        list = NetworkUtils.initMaterialPrice(list, h_price, m_price, l_price);
        list = NetworkUtils.initMaterialVolume(list, b_goods_size, m_goods_size, s_goods_size);
        List<Order> orderList = NetworkUtils.initOrders(list, transportNum);
//        List<Customer> customerList = NetworkUtils.initCustomer(cityList, 200);
        List<Supplier> suppliers = NetworkUtils.initSupplier(list.size());
        List<Order> list1 = NetWorkPlanUtils.getTransportCost1(points.get(0),points,orderList);

        return getDataTable(list1);
    }


    @PostMapping("/network2")
    @ResponseBody
    public TableDataInfo  network3(HttpServletRequest req) {
        int num1 = Integer.parseInt(req.getParameter("num"));
        String provinces = req.getParameter("provinces");
        String carLength = req.getParameter("carLength");
        double transportNum = 80000;
        if (req.getParameter("transportNum")!=null&&!req.getParameter("transportNum").equals("")){
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double warehousing = 80000;
        if (req.getParameter("warehousing")!=null&&!req.getParameter("warehousing").equals("")) {
            warehousing = Double.parseDouble(req.getParameter("warehousing"));
        }
        double h_price = 15;
        if(req.getParameter("h_price")!=null&&!req.getParameter("h_price").equals("")) {
            h_price = Double.parseDouble(req.getParameter("h_price"));
        }
        double m_price = 15;
        if(req.getParameter("m_price")!=null&&!req.getParameter("m_price").equals("")) {
            m_price = Double.parseDouble(req.getParameter("m_price"));
        }
        double l_price = 60;
        if(req.getParameter("s_price")!=null&&!req.getParameter("s_price").equals("")) {
            l_price = Double.parseDouble(req.getParameter("s_price"));
        }
        double inventory_loss = 0.5;
        if(req.getParameter("inventory_loss")!=null&&!req.getParameter("inventory_loss").equals("")) {
            inventory_loss = Double.parseDouble(req.getParameter("inventory_loss"));
        }
        double managementFee = 0;
        if(req.getParameter("managementFee")!=null&&!req.getParameter("managementFee").equals("")) {
            managementFee = Double.parseDouble(req.getParameter("managementFee"));
        }
        double times = 72;
        if(req.getParameter("times")!=null&&!req.getParameter("times").equals("")) {
            times = Integer.parseInt(req.getParameter("times"));
        }
        double order = 95;
        if(req.getParameter("order")!=null&&!req.getParameter("order").equals("")) {
            order = Integer.parseInt(req.getParameter("order"));
        }
        double goods_num = 6000;
        if(req.getParameter("goods_num")!=null&&!req.getParameter("goods_num").equals("")) {
            goods_num = Integer.parseInt(req.getParameter("goods_num"));
        }
        double b_goods_size = 20;
        if(req.getParameter("b_goods_size")!=null&&!req.getParameter("b_goods_size").equals("")) {
            b_goods_size = Double.parseDouble(req.getParameter("b_goods_size"));
        }
        double m_goods_size = 20;
        if(req.getParameter("m_goods_size")!=null&&!req.getParameter("m_goods_size").equals("")) {
            m_goods_size = Double.parseDouble(req.getParameter("m_goods_size"));
        }
        double s_goods_size = 60;
        if(req.getParameter("s_goods_size")!=null&&!req.getParameter("s_goods_size").equals("")) {
            s_goods_size = Double.parseDouble(req.getParameter("s_goods_size"));
        }
        int emp_quantity = 50;//一天处理50托

        List<GlcPoint> cityList = glcPointService.selectGlcPointList(new GlcPoint(provinces));  //获取省份内城市
        List<City> points = new ArrayList<>();
        for (GlcPoint glcPoint : cityList) {//需求点
            points.add(new City(glcPoint.getCity(), glcPoint.getLat(), glcPoint.getLng(), glcPoint.getGdp()));//备选点
        }
        List<Material> list = NetworkUtils.createGoods(goods_num);
        list = NetworkUtils.initMaterialPrice(list, h_price, m_price, l_price);
        list = NetworkUtils.initMaterialVolume(list, b_goods_size, m_goods_size, s_goods_size);
        List<Order> orderList = NetworkUtils.initOrders(list, transportNum);
        List<Customer> customerList = NetworkUtils.initCustomer(cityList, 200);
        List<Supplier> suppliers = NetworkUtils.initSupplier(list.size(),cityList);
        orderList = NetworkUtils.initOrdersCustomerList(orderList, customerList);
//        Map<City, List<Order>> outOrdersList = orderList.stream().collect(Collectors.groupingBy(Order::getCustomerCity));//出库单

        return getDataTable(orderList);
    }



    @PostMapping("/network3")
    @ResponseBody
    public TableDataInfo  network4(HttpServletRequest req) {
        int num1 = Integer.parseInt(req.getParameter("num"));
        String provinces = req.getParameter("provinces");
        String carLength = req.getParameter("carLength");

        double transportNum = 80000;
        if (req.getParameter("transportNum")!=null&&!req.getParameter("transportNum").equals("")){
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double warehousing = 80000;
        if (req.getParameter("warehousing")!=null&&!req.getParameter("warehousing").equals("")) {
            warehousing = Double.parseDouble(req.getParameter("warehousing"));
        }
        double h_price = 15;
        if(req.getParameter("h_price")!=null&&!req.getParameter("h_price").equals("")) {
            h_price = Double.parseDouble(req.getParameter("h_price"));
        }
        double m_price = 15;
        if(req.getParameter("m_price")!=null&&!req.getParameter("m_price").equals("")) {
            m_price = Double.parseDouble(req.getParameter("m_price"));
        }
        double l_price = 60;
        if(req.getParameter("s_price")!=null&&!req.getParameter("s_price").equals("")) {
            l_price = Double.parseDouble(req.getParameter("s_price"));
        }
        double inventory_loss = 0.5;
        if(req.getParameter("inventory_loss")!=null&&!req.getParameter("inventory_loss").equals("")) {
            inventory_loss = Double.parseDouble(req.getParameter("inventory_loss"));
        }
        double managementFee = 0;
        if(req.getParameter("managementFee")!=null&&!req.getParameter("managementFee").equals("")) {
            managementFee = Double.parseDouble(req.getParameter("managementFee"));
        }
        double times = 72;
        if(req.getParameter("times")!=null&&!req.getParameter("times").equals("")) {
            times = Integer.parseInt(req.getParameter("times"));
        }
        double order = 95;
        if(req.getParameter("order")!=null&&!req.getParameter("order").equals("")) {
            order = Integer.parseInt(req.getParameter("order"));
        }
        double goods_num = 6000;
        if(req.getParameter("goods_num")!=null&&!req.getParameter("goods_num").equals("")) {
            goods_num = Integer.parseInt(req.getParameter("goods_num"));
        }
        double b_goods_size = 20;
        if(req.getParameter("b_goods_size")!=null&&!req.getParameter("b_goods_size").equals("")) {
            b_goods_size = Double.parseDouble(req.getParameter("b_goods_size"));
        }
        double m_goods_size = 20;
        if(req.getParameter("m_goods_size")!=null&&!req.getParameter("m_goods_size").equals("")) {
            m_goods_size = Double.parseDouble(req.getParameter("m_goods_size"));
        }
        double s_goods_size = 60;
        if(req.getParameter("s_goods_size")!=null&&!req.getParameter("s_goods_size").equals("")) {
            s_goods_size = Double.parseDouble(req.getParameter("s_goods_size"));
        }
        int emp_quantity = 50;//一天处理50托

        List<GlcPoint> cityList = glcPointService.selectGlcPointList(new GlcPoint(provinces));  //获取省份内城市
        List<City> points = new ArrayList<>();
        for (GlcPoint glcPoint : cityList) {//需求点
            points.add(new City(glcPoint.getCity(), glcPoint.getLat(), glcPoint.getLng(), glcPoint.getGdp()));//备选点
        }
        List<Material> list = NetworkUtils.createGoods(goods_num);
        list = NetworkUtils.initMaterialPrice(list, h_price, m_price, l_price);
        list = NetworkUtils.initMaterialVolume(list, b_goods_size, m_goods_size, s_goods_size);
        List<Order> orderList = NetworkUtils.initOrders(list, transportNum);
        List<Customer> customerList = NetworkUtils.initCustomer(cityList, 200);
        List<Supplier> suppliers = NetworkUtils.initSupplier(list.size(),cityList);
        orderList = NetworkUtils.initOrdersCustomerList(orderList, customerList);
//        Map<City, List<Order>> outOrdersList = orderList.stream().collect(Collectors.groupingBy(Order::getCustomerCity));//出库单

        return getDataTable(orderList);
    }


        /**
         * 初始化k个质心点
         *
         * @param points 点集
         * @param k      K值
         * @return 分类集合对象
         */
    private static  List<List<Cluster>> initCentroides(List<City> points, Integer k) {
        List<List<Cluster>> centroides = new ArrayList<>();

        // 在范围内随机初始化k个质心点

        List<List<City>> combinations = NetworkUtils.combinations(points, k);
        for (int j =0;j<combinations.size();j++) {
            // 随机初始化k个中心点
            List<Cluster> centroide = new ArrayList<>();
            for (int i = 0; i < combinations.get(j).size(); i++) {
                Cluster c = new Cluster();
                City city = combinations.get(j).get(i); // 初始化的随机中心点
                c.setCentroid(city);
                centroide.add(c);
            }
            centroides.add(centroide);
        }
        return centroides;
    }


    /**
     * 检查收敛
     *
     * @param clusters
     * @return
     */
    private static boolean checkConvergence(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            if (!cluster.isConvergence()) {
                return false;
            }
        }
        return true;
    }
    private static void initClusters(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            cluster.initPoint();
        }
    }

    /**
     * 计算距离,对点集进行分类
     *
     * @param points   点集
     * @param clusters 分类
     * @param
     */
    private static void classifyPoint(List<City> points, List<Cluster> clusters) {
        for (City point : points) {
            Cluster masCercano = clusters.get(0); // 该点计算距离后所属的分类
            Double minDistancia = Double.MAX_VALUE; // 最小距离
            for (Cluster cluster : clusters) {
                Double distancia = NetWorkPlanUtils.twoJuLi(point,cluster.getCentroid()); // 点和每个分类质心点的距离
                if (minDistancia > distancia) { // 得到该点和k个质心点最小的距离
                    minDistancia = distancia;
                    masCercano = cluster; // 得到该点的分类
                }
            }
            masCercano.getPoints().add(point); // 将该点添加到距离最近的分类中
        }
    }

    /**
     * 重新计算质心点
     *
     * @param clusters
     */

    private static double recalcularCentroides(List<Cluster> clusters) {
        double distance1 = 0.0;
        for (Cluster c : clusters) {
            if (c.getPoints().isEmpty()) {
                c.setConvergence(true);
                continue;
            }

            // 求均值,作为新的质心点
            Float x;
            Float y;
            Float sum_x = 0f;
            Float sum_y = 0f;
            for (City point : c.getPoints()) {
                sum_x += Float.parseFloat(point.getLat());
                sum_y += Float.parseFloat(point.getLng());
            }
            x = sum_x / c.getPoints().size();
            y = sum_y / c.getPoints().size();
            City nuevoCentroide = new City(String.valueOf(x), String.valueOf(y)); // 新的质心点
            double max = Double.MAX_VALUE;

            for (City cluster:c.getPoints()){
                 double distance = getDistance(cluster,nuevoCentroide);
                 if (distance<max){
                     max = distance;
                     nuevoCentroide = cluster;
                 }
            }
            for (City city:c.getPoints()){
                distance1 += getDistance(nuevoCentroide,city)*Double.parseDouble(city.getGdp());
            }


            if (nuevoCentroide.equals(c.getCentroid())) { // 如果质心点不再改变 则该分类已经收敛
                c.setConvergence(true);
            } else {
                c.setCentroid(nuevoCentroide);
            }
        }

        return distance1;
    }

    private static double getDistance(City cluster, City nuevoCentroide) {
        GlobalCoordinates userSource = new GlobalCoordinates(Double.parseDouble(cluster.getLat()),Double.parseDouble(cluster.getLng()));
        GlobalCoordinates businessTarget = new GlobalCoordinates(Double.parseDouble(nuevoCentroide.getLat()),Double.parseDouble(nuevoCentroide.getLng()));
        double meterDouble = NetWorkPlanUtils.getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere);
        return meterDouble;
    }


}
