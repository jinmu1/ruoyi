package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.enumType.CarType;
import com.ruoyi.system.form.GlcPoint;
import com.ruoyi.system.kmeans.Cluster;
import com.ruoyi.system.node.*;
import com.ruoyi.system.result.Plan;
import com.ruoyi.system.result.Result;
import com.ruoyi.system.result.ResultMsg;
import com.ruoyi.system.result.Target;
import com.ruoyi.system.utils.AreaUtils;
import com.ruoyi.system.utils.NetWorkPlanUtils;
import com.ruoyi.system.utils.NetworkUtils;
import com.ruoyi.system.utils.RandomUtil;
import com.ruoyi.system.service.IGlcPointService;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2021-06-24
 */
@Controller
@RequestMapping("/system/point")
public class GlcPointController extends BaseController
{
    private String prefix = "system/point";

    @Autowired
    private IGlcPointService glcPointService;

//    @RequiresPermissions("system:point:view")
    @GetMapping("/index")
    public String point()
    {
        return prefix + "/point";
    }

    @GetMapping("/bigdata")
    public String bigdata()
    {
        return prefix + "/bigdata";
    }
    @GetMapping("/survey")
    public String survey()
    {
        return prefix + "/survey";
    }
    @GetMapping("/index2")
    public String index2()
    {
        return prefix + "/index2";
    }
    @GetMapping("/index3")
    public String index3()
    {
        return prefix + "/index3";
    }
    @GetMapping("/index4")
    public String index4()
    {
        return prefix + "/index4";
    }
    /**
     * 查询【请填写功能名称】列表
     */
    @RequiresPermissions("system:point:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GlcPoint glcPoint)
    {
        startPage();
        List<GlcPoint> list = glcPointService.selectGlcPointList(glcPoint);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @RequiresPermissions("system:point:export")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GlcPoint glcPoint)
    {
        List<GlcPoint> list = glcPointService.selectGlcPointList(glcPoint);
        ExcelUtil<GlcPoint> util = new ExcelUtil<GlcPoint>(GlcPoint.class);
        return util.exportExcel(list, "point");
    }

    /**
     * 新增【请填写功能名称】
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存【请填写功能名称】
     */
    @RequiresPermissions("system:point:add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GlcPoint glcPoint)
    {
        return toAjax(glcPointService.insertGlcPoint(glcPoint));
    }

    /**
     * 修改【请填写功能名称】
     */
    @GetMapping("/edit/{provinces}")
    public String edit(@PathVariable("provinces") String provinces, ModelMap mmap)
    {
        GlcPoint glcPoint = glcPointService.selectGlcPointById(provinces);
        mmap.put("glcPoint", glcPoint);
        return prefix + "/edit";
    }

    /**
     * 修改保存【请填写功能名称】
     */
    @RequiresPermissions("system:point:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GlcPoint glcPoint)
    {
        return toAjax(glcPointService.updateGlcPoint(glcPoint));
    }

    /**
     * 删除【请填写功能名称】
     */
    @RequiresPermissions("system:point:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(glcPointService.deleteGlcPointByIds(ids));
    }
    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【上传文件功能】", businessType = BusinessType.DELETE)
    @PostMapping( "/uploadFile")
    @ResponseBody
    public Map<String, Object>  uploadFile(HttpServletRequest request,HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        Map<String, Object> json = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map =multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            Object obj = i.next();
            multipartFile=(MultipartFile) map.get(obj);

        }
        /** 获取文件的后缀* */
        String filename = multipartFile.getOriginalFilename();

        InputStream inputStream;
        String path = "F:/";
        Random random = new Random();
        String newVersionName = RandomUtil.toFixdLengthString(random.nextInt(1000000),8);
        String fileMd5 = "";
        try {

            inputStream = multipartFile.getInputStream();
            File tmpFile = File.createTempFile(newVersionName, path+newVersionName+".xlsx");
            fileMd5 = Files.hash(tmpFile, Hashing.md5()).toString();
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("newVersionName", newVersionName);
        json.put("fileMd5", fileMd5);
        json.put("message", "应用上传成功");
        json.put("status", true);
        return json;
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【下载文件功能】", businessType = BusinessType.DELETE)
    @GetMapping( "/downloadFile")
    @ResponseBody
    public byte[]  downloadFile(HttpServletResponse response,HttpServletRequest request)
    {
        String file_name= "省内网络规划输入";

        String filedownload_url="F:/"+file_name+".xlsx";//要提供下载的文件的物理路径＋文件名
        try {
            // path是指欲下载的文件的路径。
            File file = new File(filedownload_url);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(filedownload_url));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String((file_name+ ".xlsx").getBytes(), "iso-8859-1"));
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    @PostMapping("/point/network")
//    @ResponseBody
//    public TableDataInfo  network1(HttpServletRequest req){
//
//        int num1 = Integer.parseInt(req.getParameter("num"));
//        String provinces = req.getParameter("provinces");
////        String provinces = "安徽";
//        String carLength = req.getParameter("carLength");
////        String carLength = "小车4米6";
//        double transportNum = 80000;
//        if (req.getParameter("transportNum")!=null&&!req.getParameter("transportNum").equals("")){
//            transportNum = Double.parseDouble(req.getParameter("transportNum"));
//        }
//        double warehousing = 80000;
//        if (req.getParameter("warehousing")!=null&&!req.getParameter("warehousing").equals("")) {
//            warehousing = Double.parseDouble(req.getParameter("warehousing"));
//        }
//        double h_price = 15;
//        if(req.getParameter("h_price")!=null&&!req.getParameter("h_price").equals("")) {
//            h_price = Double.parseDouble(req.getParameter("h_price"));
//        }
//        double m_price = 15;
//        if(req.getParameter("m_price")!=null&&!req.getParameter("m_price").equals("")) {
//            m_price = Double.parseDouble(req.getParameter("m_price"));
//        }
//        double l_price = 60;
//        if(req.getParameter("s_price")!=null&&!req.getParameter("s_price").equals("")) {
//            l_price = Double.parseDouble(req.getParameter("s_price"));
//        }
//        double inventory_loss = 0.5;
//        if(req.getParameter("inventory_loss")!=null&&!req.getParameter("inventory_loss").equals("")) {
//            inventory_loss = Double.parseDouble(req.getParameter("inventory_loss"));
//        }
//        double managementFee = 0;
//        if(req.getParameter("managementFee")!=null&&!req.getParameter("managementFee").equals("")) {
//            managementFee = Double.parseDouble(req.getParameter("managementFee"));
//        }
//        double times = 72;
//        if(req.getParameter("times")!=null&&!req.getParameter("times").equals("")) {
//            times = Integer.parseInt(req.getParameter("times"));
//        }
//        double order = 95;
//        if(req.getParameter("order")!=null&&!req.getParameter("order").equals("")) {
//            order = Integer.parseInt(req.getParameter("order"));
//        }
//        double goods_num = 6000;
//        if(req.getParameter("goods_num")!=null&&!req.getParameter("goods_num").equals("")) {
//            goods_num = Integer.parseInt(req.getParameter("goods_num"));
//        }
//        double b_goods_size = 20;
//        if(req.getParameter("b_goods_size")!=null&&!req.getParameter("b_goods_size").equals("")) {
//            b_goods_size = Double.parseDouble(req.getParameter("b_goods_size"));
//        }
//        double m_goods_size = 20;
//        if(req.getParameter("m_goods_size")!=null&&!req.getParameter("m_goods_size").equals("")) {
//            m_goods_size = Double.parseDouble(req.getParameter("m_goods_size"));
//        }
//        double s_goods_size = 60;
//        if(req.getParameter("s_goods_size")!=null&&!req.getParameter("s_goods_size").equals("")) {
//            s_goods_size = Double.parseDouble(req.getParameter("s_goods_size"));
//        }
//        int emp_quantity = 50;//一天处理十二托
//        double lever =1.65;
//        if (order == 90){
//            lever =1.25;
//        }else if(order == 95){
//            lever =1.65;
//        }else if(order == 98){
//            lever =2.05;
//        }else if(order == 100){
//            lever =3.5;
//        }
//
//        List<Result> results = new ArrayList<>();
//        List<Result> results1 = new ArrayList<>();
//        List<ResultMsg> resultMsgs = new ArrayList<>();
//        List<JSONObject> listd;
//        Jedis jedis = new Jedis("127.0.0.1", 6379);
//        jedis.select(2);
//        Boolean exists= jedis.exists(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+b_goods_size+m_goods_size+s_goods_size);
//        if (exists) {
//            String in = jedis.get(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+b_goods_size+m_goods_size+s_goods_size);
//            listd=  (List<JSONObject>) JSON.parse(in);
//            for(JSON json:listd){
//                ResultMsg status =JSON.toJavaObject(json, ResultMsg.class);
//                resultMsgs.add(status);
//            }
//        }else {
//            List<GlcPoint> cityLists = glcPointService.selectGlcPointList(new GlcPoint(provinces));  //获取省份内城市
//            List<City> points = new ArrayList<>();
//            for (GlcPoint glcPoint : cityLists) {//需求点
//                points.add(new City(glcPoint.getCity(), glcPoint.getLat(), glcPoint.getLng(), glcPoint.getGdp()));//备选点
//            }
//            List<Material> list = NetworkUtils.createGoods(goods_num);
//            list = NetworkUtils.initMaterialPrice(list, h_price, m_price, l_price);
//            list = NetworkUtils.initMaterialVolume(list, b_goods_size, m_goods_size, s_goods_size);
//            List<Order> orderList = NetworkUtils.initOrders(list, transportNum);
//            List<Customer> customerList = NetworkUtils.initCustomer(cityLists, 200);
//            List<Supplier> suppliers = NetworkUtils.initSupplier();
//            orderList = NetworkUtils.initOrdersCustomerList(orderList, customerList);
//            Map<City, List<Order>> outOrdersList = orderList.stream().collect(Collectors.groupingBy(Order::getCustomerCity));//出库单
//
//
//
//            for (int i = 1; i <= cityLists.size(); i++) {
//                double max = Double.MAX_VALUE;
//                Result resultd = new Result();
//                resultd.setCity(i + "");
//                List<List<City>> combinations = NetworkUtils.combinations(points, i);//已经遍历了该RDC数量下的所有RDC组合 k为备选点数量
//                int l =0;
//                List<List<City>> combinations1 = new ArrayList<>();
//                double max1 = Double.MAX_VALUE;
//                if(combinations.size()>500){
//                    combinations = combinations.subList(0,500);
//                }
//                List<City> rdcPoint = new ArrayList<>();
//                for (List<City> cityList1 : combinations) {
//                    results = new ArrayList<>();
//                    rdcPoint = new ArrayList<>();
//                    for (City city : cityList1) {
//                        rdcPoint.add(city); //将选取的备选点当做新增的RDC
//                    }
//                    // 开始计算
//                    double distance = chooseCity1(rdcPoint, points);//选择城市
//                    if (distance < max1) {
//                        max1 = distance;
//                        combinations1.add(cityList1);
//                        l++;
//                    }
//                }
//                List<City> cities = new ArrayList<>();
//                for (List<City> cityList : combinations1) {
//                    results = new ArrayList<>();
//                    rdcPoint = new ArrayList<>();
//                    for (City city : cityList) {
//                        rdcPoint.add(new City(city.getCity(), city.getLat(), city.getLng(), city.getGdp())); //将选取的备选点当做新增的RDC
//                    }
//                    // 开始计算
//                    cities = chooseCity(rdcPoint, points);//选择城市
//                    Map<String, List<City>> linkCity = cities.stream().collect(Collectors.groupingBy(City::getCity1));//网络连接
//                    List<Cluster> cluster2 = new ArrayList<>();
//                    for (String rdc : linkCity.keySet()){
//                        Cluster cluster = new Cluster();
//                        for(City city:rdcPoint){
//                            if(city.getCity().equals(rdc)) {
//                                cluster.setCentroid(city);
//                            }
//                        }
//                        cluster.setPoints(linkCity.get(rdc));
//                        cluster2.add(cluster);
//                    }
//                    for (Cluster cluster : cluster2) {
//                        Result result = new Result();
//                        List<Order> outOrders = new ArrayList<>();
//                        result.setRange(cluster.getCentroid().getCity());
//                        result.setCity(cluster.getCentroid().getCity());
//                        for (City city : outOrdersList.keySet()) {
//                            for (City city1 : cluster.getPoints()) {
//                                if (city.getCity().equals(city1.getCity())) {
//                                    outOrders.addAll(outOrdersList.get(city));
//                                }
//                            }
//                        }
//                        List<Order> inOrders = NetWorkPlanUtils.getReplenishment(outOrders, list); //获取补货单数据
//                        result = NetWorkPlanUtils.getTransportCost(cluster.getCentroid(), cluster.getPoints(), outOrders, result);//获取运输成本数据
//                        result = NetWorkPlanUtils.getStorageCost(inOrders, outOrders, emp_quantity, warehousing, result,i);//获取仓储成本数据
//                        result = NetWorkPlanUtils.inventoryCost(outOrders, suppliers, order, inventory_loss, result); // 获取库存成本数据
//                        result = NetWorkPlanUtils.buildCost(inOrders, outOrders, result, cluster.getCentroid(), carLength);
//                        result.setAll(Math.round(result.getTransportCost() + result.getBuildCost() + result.getInventoryCost() + result.getStorageCost()));
//                        result.setRate(result.getAll() / result.getSales_account());  //计算费率
//                        results.add(result);
//                        for (City city : cluster.getPoints()) {
//                            city.setCity1(cluster.getCentroid().getCity());
//                            city.setLat1(cluster.getCentroid().getLat());
//                            city.setLng1(cluster.getCentroid().getLng());
//                            cities.add(city);
//                        }
//                    }
//                    Result allresult = new Result();
//                    int m = 0;
//                    for (Result result : results) {
//                        allresult.setCity(result.getCity());
//                        allresult.setStorage(Math.round(result.getStorage() + allresult.getStorage()));
//                        allresult.setArea(Math.round(allresult.getArea() + result.getArea()));
//                        allresult.setStorageCost(Math.round(allresult.getStorageCost() + result.getStorageCost()));
//                        allresult.setBuildCost(Math.round(allresult.getBuildCost() + result.getBuildCost()));
//                        allresult.setInventoryCost(Math.round(allresult.getInventoryCost() + result.getInventoryCost()));
//                        allresult.setTransportCost(Math.round(allresult.getTransportCost() + result.getTransportCost()));
//                        allresult.setAll(Math.round(allresult.getAll() + result.getAll()));
//                        allresult.setThroughput_num(Math.round(allresult.getThroughput_num() + result.getThroughput_num()));
//                        allresult.setEmp(Math.round(allresult.getEmp() + result.getEmp()));
//                        allresult.setCar(Math.round(allresult.getCar() + result.getCar()));
//                        allresult.setOrder_rate(allresult.getOrder_rate() + result.getOrder_rate());
//                        allresult.setInventory_num(Math.round(result.getInventory_num() + allresult.getInventory_num()));
//                        allresult.setSales_account(Math.round(allresult.getSales_account() + result.getSales_account()));
//                        allresult.setRate1(allresult.getRate1() + result.getRate1());
//                        allresult.setStorage_area(allresult.getStorage_area() + result.getStorage_area());
//                        allresult.setPlat_cost(allresult.getAll()/transportNum);
//                        allresult.setOrder_cost(allresult.getPlat_cost()/ NetworkUtils.random(2,5));
//                        allresult.setArea_cost(allresult.getAll()/allresult.getArea());
//                        m++;
//                    }
//                    allresult.setStorageCost(allresult.getStorageCost());
//                    allresult.setAll(allresult.getAll());
//                    allresult.setOrder_rate(allresult.getOrder_rate()/ i);
//                    allresult.setInventory_num(Math.round(allresult.getInventory_num()/i));
//                    allresult.setStorage_area(Math.round(allresult.getStorage_area()/i));
//                    allresult.setRate(allresult.getAll()/allresult.getSales_account());
//                    allresult.setArea_cost(allresult.getAll()/allresult.getArea());
//                    allresult.setPlat_cost(allresult.getPlat_cost());
//                    allresult.setRate1(allresult.getRate1()/i);
//                    allresult.setPlat_storage(Math.round(allresult.getStorage()/transportNum/i));
//                    allresult.setOrder_cost(allresult.getOrder_cost()/i);
//                    allresult.setPlat_transport(Math.round(allresult.getTransportCost()/transportNum/i));
//                    if (allresult.getAll() < max) {
////                        resultd.setStorageCost(allresult.getStorageCost());
////                        resultd.setAll(allCost);
////                        resultd.setTransportCost(allresult.getTransportCost());
////                        resultd.setInventoryCost(allresult.getInventoryCost());
////                        resultd.setBuildCost(allresult.getBuildCost());
//                        max = allresult.getAll();
//                        ResultMsg resultMsg = new ResultMsg();
//                        resultMsg.setRdc(i);
//                        resultMsg.setCost(allresult.getAll());
//                        resultMsg.setCities(cities);
//                        resultMsg.setResult(allresult);
//                        resultMsg.setResultList(results);
//                        resultMsgs.add(resultMsg);
//                    }
//                }
////                results1.add(resultd);
//            }
//            jedis.select(2);
//            jedis.set(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+""+b_goods_size+m_goods_size+s_goods_size,JSON.toJSON(resultMsgs).toString());
//        }
////        ResultMsg best  =  new ResultMsg();
////        double maxs  = Double.MAX_VALUE;
////        for (ResultMsg resultMsg:resultMsgs){
////            if (resultMsg.getCost()<maxs){
////                best = resultMsg;
////                maxs = resultMsg.getCost();
////            }
////        }
//        resultMsgs = resultMsgs.stream().sorted(Comparator.comparing(ResultMsg::getCost)).collect(Collectors.toList());
//        List<List<City>> listList = new ArrayList<>();
//        for (ResultMsg resultMsg : resultMsgs){
//            listList.add(resultMsg.getCities());
//        }
//        double[] num = {1, 25, 50, 75, 90, 100};
//        for (List<City> list1 : listList) {
//            Map<String, List<City>> linkCity = list1.stream().collect(Collectors.groupingBy(City::getCity1));
//            int i = 0;
//            for (String str : linkCity.keySet()) {
//                List<City> cities1 = linkCity.get(str);
//                for (City city : cities1) {
//                    city.setGdp(num[i % 5] + "");
//                }
//                i++;
//            }
//        }
//        return getDataTable(listList.get(num1-1));
//
//    }
    @PostMapping("/point/network")
    @ResponseBody
    public TableDataInfo  network1(HttpServletRequest req){

         int num1 = Integer.parseInt(req.getParameter("num"));
        String provinces = req.getParameter("provinces");
//        String provinces = "安徽";
        String carLength = req.getParameter("carLength");
//        String carLength = "小车4米6";
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
        int emp_quantity = 50;//一天处理十二托
        double lever =1.65;
        if (order == 90){
            lever =1.25;
        }else if(order == 95){
            lever =1.65;
        }else if(order == 98){
            lever =2.05;
        }else if(order == 100){
            lever =3.5;
        }

        List<GlcPoint> list = glcPointService.selectGlcPointList(new GlcPoint());  //获取省份内城市

        double gdp =  0.0;
        for (GlcPoint city:list){
            gdp+= Double.parseDouble(city.getGdp());
        }
        List<City> rdcPoint = new ArrayList<>();
        List<City> points = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        for ( GlcPoint glcPoint:list){//需求点
            points.add(new City(glcPoint.getCity(),glcPoint.getLat(),glcPoint.getLng(),glcPoint.getGdp()));//备选点
        }
        List<Result> results = new ArrayList<>();
        List<Result> results1 = new ArrayList<>();
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
            for (int i = 1; i <= list.size(); i++) {
                double max = Double.MAX_VALUE;
                Result resultd = new Result();
                resultd.setCity(i + "");
                List<List<City>> combinations = NetworkUtils.combinations(points, i);//已经遍历了该RDC数量下的所有RDC组合 k为备选点数量
//                List<City> cits = new ArrayList<>();
//                List<List<City>> combinations = new ArrayList<>();
//                for (GlcPoint glcPoint:list){
//                    if (glcPoint.getCity().equals("荆门市")||glcPoint.getCity().equals("武汉市")||glcPoint.getCity().equals("鄂州市")){
//                        cits.add(new City(glcPoint.getCity(),glcPoint.getLat(),glcPoint.getLng(),glcPoint.getGdp()));
//                    }
//                }
//                combinations.add(cits);
                int l =0;
                List<List<City>> combinations1 = new ArrayList<>();
                double max1 = Double.MAX_VALUE;
                if(combinations.size()>500){
                    combinations = combinations.subList(0,500);
                }
                for (List<City> cityList : combinations) {

                    results = new ArrayList<>();
                    rdcPoint = new ArrayList<>();
                    for (City city : cityList) {
                        rdcPoint.add(new City(city.getCity(), city.getLat(), city.getLng(), city.getGdp())); //将选取的备选点当做新增的RDC
                    }
                    // 开始计算
                    double distance = chooseCity1(rdcPoint, points);//选择城市
                    if (distance < max1) {
                        max1 = distance;
                        combinations1.add(cityList);
                        l++;
                    }
                    l++;
                }
                for (List<City> cityList : combinations1) {
                    results = new ArrayList<>();
                    rdcPoint = new ArrayList<>();
                    for (City city : cityList) {
                        rdcPoint.add(new City(city.getCity(), city.getLat(), city.getLng(), city.getGdp())); //将选取的备选点当做新增的RDC
                    }
                    // 开始计算
                    cities = chooseCity(rdcPoint, points);//选择城市
                    Map<String, List<City>> linkCity = cities.stream().collect(Collectors.groupingBy(City::getCity1));//网络连接
                    double allCost = 0.0;
                    double transportNum1 = 0.0;

                    int sized = (int)list.size()/2;
                    for (String rdc : linkCity.keySet()) {
                        List<City> cityLink = linkCity.get(rdc);
                        Result result = new Result();
                        double transportCost = 0.0;
                        double carNum = 0.0;
                        double storageCost = 0.0;
                        double emp = 0;
                        double inventory = 0.0;
                        String range = "";
                        double price = (h_price * 2000 + m_price * 1000 + l_price * 100) / 100*5;
                        double size =  (b_goods_size *5 + m_goods_size * 0.5 + s_goods_size * 0.05) / 100;
                        for (City city1 : cityLink) {
//                            range += city1.getCity() + ",";
                            double gdps = 1.4 * Double.parseDouble(city1.getGdp()) / gdp * transportNum;
                            transportNum1+=Double.parseDouble(city1.getGdp()) / gdp * transportNum;

                            if (i <= sized) {
                                    transportCost += Math.pow(Math.sqrt(Double.parseDouble(city1.getDistance())), 0.5) * (1 + 0.11) * 7 * 1 * gdps;//计算运输成本
                            } else {
                                    transportCost += Math.pow(Math.sqrt(Double.parseDouble(city1.getDistance())), 0.5) * (1 + 0.11) * 7 * 1 * gdps * Math.pow(1.06, i - sized);//计算运输成本

                            }
                            carNum += gdps / Double.parseDouble(CarType.valueOf(carLength).getCode()) / 365;
                            emp += gdps / 365 / emp_quantity*Math.sqrt((2 - (Math.sqrt(times / 72) )))*(1+Math.sqrt(goods_num/10000))/Math.sqrt(Math.sqrt(size))/1.5;//一天处理多少托需要多少人
                            inventory +=gdps / 365 * Math.pow(1.02,i) * lever ;
                        }
                        emp = Math.ceil(emp);
                        storageCost = (emp * warehousing+managementFee)*Math.pow(1.05,i);//需要的人员数量
                        double storage_area = 0.0;
                        if (inventory > 5) {
                            storage_area = AreaUtils.getHightStorage(inventory, emp * emp_quantity).getArea();
                        } else {
                            storage_area = AreaUtils.getHeapStorage(inventory).getArea();
                        }

                        double tally = AreaUtils.getTally(2 * emp * 60 / 2, carLength).getArea();
                        double platform = AreaUtils.getPlatform(2 * emp * 60 / 2, carLength).getPlatform_area();
                        double area = tally + storage_area* Math.pow(1.02,i) + platform;
                        double buildCost = area * 35 * 12;//先假设每平米面积为32元/月

                        if (inventory <= 1) {
                            inventory = 0;
                        }
                        double inventoryCost = 1.5*price * inventory * (0.05 + inventory_loss / 100);
                        result.setCity(rdc);
                        result.setRange(range);
                        result.setArea(Math.round(area));
                        result.setStorage((emp * emp_quantity * 365+managementFee)*((Math.pow(i,1.1))));
                        result.setStorageCost(Math.round(storageCost));
                        result.setBuildCost(Math.round(buildCost));
                        result.setInventoryCost(Math.round(inventoryCost));
                        result.setTransportCost(Math.round(transportCost));
                        result.setAll(Math.round(storageCost + buildCost + inventoryCost + transportCost));
                        result.setEmp(emp);
                        result.setCar(Math.ceil(carNum));
                        result.setOrder_rate(order-30+times/6+NetworkUtils.random(1,10));
                        result.setInventory_num(NetworkUtils.random(18,36));
                        result.setSales_account(transportNum1*price);
                        result.setThroughput_num((emp * 365 * emp_quantity/1.8));
//                        result.setSales_account(transportNum1*5);
                        result.setRate(result.getAll()/result.getSales_account());
//                        result.setRate1(NetworkUtils.random(320,720));
                        result.setStorage_area(NetworkUtils.random(190,261));
                        result.setPlat_cost(result.getAll()/transportNum1*15);
                        result.setOrder_cost(result.getPlat_cost()/ NetworkUtils.random(2,5));
                        result.setArea_cost(result.getAll()/result.getArea());
                        result.setPlat_storage(result.getStorage()/transportNum);
                        result.setPlat_transport(result.getTransportCost()/transportNum);
                        results.add(result);
                        allCost += result.getAll();
                    }
                    Result allresult = new Result();
                    int m = 0;
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
                        allresult.setOrder_rate(allresult.getOrder_rate() + result.getOrder_rate());
                        allresult.setInventory_num(Math.round(result.getInventory_num() + allresult.getInventory_num()));
                        allresult.setSales_account(Math.round(allresult.getSales_account() + result.getSales_account()));
                        allresult.setRate1(allresult.getRate1() + result.getRate1());
                        allresult.setStorage_area(allresult.getStorage_area() + result.getStorage_area());
                        allresult.setOrder_cost(Math.round(result.getOrder_cost() + allresult.getOrder_cost()));
                        allresult.setArea_cost(allresult.getAll()/allresult.getArea());
                        m++;
                    }
                    allresult.setStorageCost(allresult.getStorageCost());
                    allresult.setAll(allresult.getAll());
                    allresult.setOrder_rate(allresult.getOrder_rate()/ i);
                    allresult.setInventory_num(Math.round(allresult.getInventory_num()/i));
                    allresult.setStorage_area(Math.round(allresult.getStorage_area()/i));
                    allresult.setRate(allresult.getAll()/allresult.getSales_account());
                    allresult.setArea_cost(allresult.getAll()/allresult.getArea());
                    allresult.setPlat_cost(allresult.getAll()/transportNum*15);
                    allresult.setRate1(allresult.getRate1()/i);
                    allresult.setPlat_storage(Math.round(allresult.getStorage()/transportNum/i));
                    allresult.setOrder_cost(allresult.getOrder_cost()/i);
                    allresult.setPlat_transport(Math.round(allresult.getTransportCost()/transportNum/i));
                    if (allresult.getAll() < max) {
//                        resultd.setStorageCost(allresult.getStorageCost());
//                        resultd.setAll(allCost);
//                        resultd.setTransportCost(allresult.getTransportCost());
//                        resultd.setInventoryCost(allresult.getInventoryCost());
//                        resultd.setBuildCost(allresult.getBuildCost());
                        max = allresult.getAll();
                        ResultMsg resultMsg = new ResultMsg();
                        resultMsg.setRdc(i);
                        resultMsg.setCost(allresult.getAll());
                        resultMsg.setCities(cities);
                        resultMsg.setResult(allresult);
                        resultMsg.setResultList(results);
                        resultMsgs.add(resultMsg);
                    }
                }
//                results1.add(resultd);
            }
            jedis.select(2);
            jedis.set(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+""+b_goods_size+m_goods_size+s_goods_size,JSON.toJSON(resultMsgs).toString());
        }
//        ResultMsg best  =  new ResultMsg();
//        double maxs  = Double.MAX_VALUE;
//        for (ResultMsg resultMsg:resultMsgs){
//            if (resultMsg.getCost()<maxs){
//                best = resultMsg;
//                maxs = resultMsg.getCost();
//            }
//        }
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
        return getDataTable(listList.get(num1-1));

    }
    @PostMapping("/point/networkplan")
    @ResponseBody
    public List<Plan> networkplan(HttpServletRequest req){
        String provinces = req.getParameter("provinces");
//        String provinces = "安徽";
        String carLength = req.getParameter("carLength");
//        String carLength = "小车4米6";
        double transportNum = 800000;
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
        double order = 80;
        if(req.getParameter("order")!=null&&!req.getParameter("order").equals("")) {
            order = Integer.parseInt(req.getParameter("order"));
        }
        double goods_num = 6000;
        if(req.getParameter("goods_num")!=null&&!req.getParameter("goods_num").equals("")) {
            goods_num = Integer.parseInt(req.getParameter("goods_num"));
        }
        int emp_quantity = 50;//一天处理十二托
        List<GlcPoint> list = glcPointService.selectGlcPointList(new GlcPoint());  //获取省份内城市
        double gdp =  0.0;
        for (GlcPoint city:list){
            gdp+= Double.parseDouble(city.getGdp());
        }

        List<City> rdcPoint = new ArrayList<>();
        List<City> points = new ArrayList<>();
        List<City> cities = new ArrayList<>();
//        List<Result> resultAll = new ArrayList<>();
        for ( GlcPoint glcPoint:list){//需求点
            points.add(new City(glcPoint.getCity(),glcPoint.getLat(),glcPoint.getLng(),glcPoint.getGdp()));//备选点
        }
        List<Result> results = new ArrayList<>();
//        List<Result> results1 = new ArrayList<>();
        List<ResultMsg> resultMsgs = new ArrayList<>();
//        double max = Double.MAX_VALUE;
        List<List<City>> listList = new ArrayList<>();
//        double distacne = 0.0;
        int k =5;
        int s =1;
        if ((transportNum/365)>2000&&(transportNum/365)<5000){
            k=7;
            s = 5;
        }else if ((transportNum/365)>=5000&&(transportNum/365)<10000){
            k=8;
            s = 5;
        }else if ((transportNum/365)>=10000){
            k=list.size()-6;
            s = 5;
        }
        for (int i = s; i < k; i++) {
            List<List<City>> combinations = NetworkUtils.combinations(points, i);//已经遍历了该RDC数量下的所有RDC组合 k为备选点数量
            for (List<City> cityList : combinations) {
                results = new ArrayList<>();
                rdcPoint = new ArrayList<>();
                for (City city : cityList) {
                    rdcPoint.add(new City(city.getCity(),city.getLat(),city.getLng(),city.getGdp())); //将选取的备选点当做新增的RDC
                }
                // 开始计算
                cities = chooseCity(rdcPoint, points);//选择城市
                Map<String, List<City>> linkCity = cities.stream().collect(Collectors.groupingBy(City::getCity1));//网络连接
                double allCost = 0.0;
                for (String rdc :linkCity.keySet()){
                    List<City> cityLink = linkCity.get(rdc);
                    Result result = new Result();
                    double transportCost =0.0;
                    double carNum = 0.0;
                    double storageCost = 0.0;
                    double emp = 0;
                    double inventory = 0.0;
                    int order_rate = 0;
                    int order_num = 0;
                    String range= "";
                    for (City city1 : cityLink) {
                        double gdps = 1.5*Double.parseDouble(city1.getGdp()) / gdp * transportNum;
                        transportCost += Math.sqrt(Double.parseDouble(city1.getDistance())) *1.62 * (1 + 0.11) * 1.2 * 1  * gdps;//计算运输成本
                        carNum += gdps/ Double.parseDouble(CarType.valueOf(carLength).getCode()) / 365;
                        emp += gdps/ 365 / emp_quantity;//一天处理多少托需要多少人
                        inventory += gdps/365*Math.sqrt(Math.sqrt(i));
                        order_num++;
                        if (Double.parseDouble(city1.getDistance()) < 1000) {
                            order_rate++;
                        }
                    }
                    emp = Math.ceil(emp+Math.sqrt(Math.sqrt(i)));
                    storageCost = emp * warehousing;//需要的人员数量
                    double storage_area = 0.0;
                    if (inventory > 5) {
                        storage_area = AreaUtils.getHightStorage(inventory, emp * emp_quantity).getArea();
                    } else {
                        storage_area = AreaUtils.getHeapStorage(inventory).getArea();
                    }

                    double tally = AreaUtils.getTally(2 * emp*emp_quantity/2, carLength).getArea();
                    double platform = AreaUtils.getPlatform(2 *  emp*emp_quantity/2, carLength).getPlatform_area();
                    double area = tally + storage_area + platform;
                    double buildCost = area * 27 * 12;//先假设每平米面积为32元/月
                    double price = (h_price * 5000 + m_price * 1000 + l_price * 100) / 100;
                    if (inventory<=1) {
                        inventory =0;
                    }
                    double inventoryCost = price * inventory * (0.05 + inventory_loss / 100)*5;
                    result.setCity(rdc);
                    result.setRange(range);
                    result.setArea(Math.round(area));
                    result.setStorage(emp*emp_quantity*365);
                    result.setStorageCost(Math.round(storageCost));
                    result.setBuildCost(Math.round(buildCost));
                    result.setInventoryCost(Math.round(inventoryCost));
                    result.setTransportCost(Math.round(transportCost));
                    result.setAll(Math.round(storageCost+buildCost+inventoryCost+transportCost));
                    result.setEmp(emp);
                    result.setCar(Math.ceil(carNum));
                    result.setOrder_rate(order_rate/order_num);
                    result.setInventory_num(inventory);
                    result.setSales_account(transportCost*40);
                    result.setThroughput_num((emp*365*emp_quantity));
                    result.setRate(result.getAll()/result.getSales_account());
                    result.setRate1(result.getAll()/result.getSales_account());
                    results.add(result);
                    allCost += result.getAll();
                }
                Result allresult = new Result();
                allresult.setArea(0);
                allresult.setStorageCost(0);
                allresult.setStorage(0);
                allresult.setBuildCost(0);
                allresult.setInventoryCost(0);
                allresult.setTransportCost(0);
                allresult.setAll(0);
                allresult.setEmp(0);
                allresult.setCar(0);
                allresult.setRate(0);
                allresult.setOrder_rate(0);
                allresult.setInventory_num(0);
                allresult.setSales_account(0);
                allresult.setThroughput_num(0);
                int m=0;
                for (Result result:results){
                    allresult.setCity(result.getCity());
                    allresult.setStorage(Math.round(result.getStorage()+allresult.getStorage()));
                    allresult.setArea(Math.round(allresult.getArea()+result.getArea()));
                    allresult.setStorageCost(Math.round(allresult.getStorageCost()+result.getStorageCost()));
                    allresult.setBuildCost(Math.round(allresult.getBuildCost()+result.getBuildCost()));
                    allresult.setInventoryCost(Math.round(allresult.getInventoryCost()+result.getInventoryCost()));
                    allresult.setTransportCost(Math.round(allresult.getTransportCost()+result.getTransportCost()));
                    allresult.setAll(Math.round(allresult.getAll()+result.getAll()));
                    allresult.setEmp(Math.round(allresult.getEmp()+result.getEmp()));
                    allresult.setCar(Math.round(allresult.getCar()+result.getCar()));
                    allresult.setOrder_rate(allresult.getOrder_rate()+result.getOrder_rate());
                    allresult.setInventory_num(Math.round(result.getInventory_num()+allresult.getInventory_num()));
                    allresult.setThroughput_num(Math.round(transportNum));
                    allresult.setSales_account(Math.round(allresult.getSales_account()+result.getSales_account()));
                    allresult.setRate1(allresult.getAll()/allresult.getSales_account());
                    m++;
                }
                allresult.setStorageCost(allresult.getStorageCost()+managementFee*i);
                allresult.setAll(allresult.getAll()+managementFee*i);
                allresult.setOrder_rate(allresult.getOrder_rate()/m);
                ResultMsg resultMsg = new ResultMsg();
                resultMsg.setCost(allresult.getAll());
                resultMsg.setResult(allresult);
                resultMsg.setResultList(results);
                resultMsgs.add(resultMsg);
//                System.gc();
//                results =new ArrayList<>();
            }
        }
//        List<City> list1 = listList.get(listList.size()-1);
        resultMsgs = resultMsgs.stream().sorted(Comparator.comparing(ResultMsg::getCost)).collect(Collectors.toList());
        List<Result> list1 = new ArrayList<>();
//        list1.add(resultMsgs.get(times1-1).getResult());
//        list1.add(resultMsgs.get(times3-1).getResult());
//        list1.add(resultMsgs.get(times2-1).getResult());
//        list1.add(resultMsgs.get(times4-1).getResult());
        List<Plan> planList = new ArrayList<>();
        List<Target> targetList =  new ArrayList<>();
        String[] str = new String[]{"总成本","配送成本","仓储成本","仓库建设成本","库存持有成本","仓储能力","吞吐能力","平均库存天数","单位面积存量","订单满足率","订单完成周期","订单12h送达率","面积","人数","车辆数","物流费率","单托成本","单订单成本"};

        String[][] doubles = new String[18][];
        String[] doubles1 = new String[4];
        String[] doubles2 = new String[4];
        String[] doubles3 = new String[4];
        String[] doubles4 = new String[4];
        String[] doubles5 = new String[4];
        String[] doubles6 =new String[4];
        String[] doubles7 = new String[4];
        String[] doubles8 = new String[4];
        String[] doubles9 =new String[4];
        String[] doubles10 = new String[4];
        String[] doubles11 = new String[4];
        String[] doubles12 =new String[4];
        String[] doubles13 = new String[4];
        String[] doubles14 = new String[4];
        String[] doubles15 = new String[4];
        String[] doubles16 = new String[4];
        String[] doubles17 = new String[4];
        String[] doubles18 = new String[4];
        DecimalFormat df = new DecimalFormat("0");
        for (int i =0;i<list1.size();i++){
         Target target = new Target();
           target.setStorage(list1.get(i).getStorage());
            doubles1[i] = String.valueOf(target.getStorage());
            target.setThroughput(list1.get(i).getThroughput_num());
            doubles2[i] = df.format(target.getThroughput());
            target.setInventoryDate(NetworkUtils.random(30,60));
            doubles3[i] = df.format(target.getInventoryDate());
            target.setInventoryArea(list1.get(i).getStorage()/list1.get(i).getArea());
            doubles4[i] = df.format(target.getInventoryArea());
            target.setOrderRate(NetworkUtils.random(55,76));
            doubles5[i] = df.format(target.getOrderRate());
            target.setOrderCycle(NetworkUtils.random(2,8)/2);
            doubles6[i] = df.format(target.getOrderCycle());
            target.setOrderTime(NetworkUtils.random(76,95)/60);
            doubles7[i] = df.format(target.getOrderTime());
            target.setArea(list1.get(i).getArea());
            doubles8[i] = df.format(target.getArea());
            target.setEmp(list1.get(i).getEmp());
            doubles9[i] = df.format(target.getEmp());
            target.setCar(list1.get(i).getCar());
            doubles10[i] = df.format(target.getCar());

            target.setTrasportCost(list1.get(i).getTransportCost());
            doubles12[i] = df.format(target.getTrasportCost());
            target.setStorageCost(target.getEmp()*80000);
            doubles13[i] = df.format(list1.get(i).getStorageCost());
            target.setBuildCost(target.getArea()*32*12);
            doubles14[i] = df.format(list1.get(i).getBuildCost());
            target.setInventoryCost(list1.get(i).getStorageCost()*12);
            doubles15[i] = df.format(list1.get(i).getInventoryCost());
            target.setCost(list1.get(i).getAll());
            doubles11[i] = df.format(target.getCost());
            target.setRate(list1.get(i).getRate());
            doubles16[i] = df.format(target.getRate()*100)+"%";
            target.setPlatCost(target.getCost()/transportNum);
            doubles17[i] = df.format(target.getPlatCost());
            target.setOrderCost(Math.ceil(target.getCost()/transportNum/13));
            doubles18[i] = df.format(target.getOrderCost());
            targetList.add(target);
        }
        doubles[0] = doubles11;
        doubles[1] = doubles12;
        doubles[2] = doubles13;
        doubles[3] = doubles14;
        doubles[4] = doubles15;
        doubles[5] = doubles1;
        doubles[6] = doubles2;
        doubles[7] = doubles3;
        doubles[8] = doubles4;
        doubles[9] = doubles5;
        doubles[10] = doubles6;
        doubles[11] = doubles7;
        doubles[12] = doubles8;
        doubles[13] = doubles9;
        doubles[14] = doubles10;
        doubles[15] = doubles16;
        doubles[16] = doubles17;
        doubles[17] = doubles18;
        for(int i=0;i<18;i++){
            Plan plan = new Plan();
            plan.setIndicators(str[i]);
            plan.setPlanA(doubles[i][0]);
            plan.setPlanB(doubles[i][1]);
            plan.setPlanC(doubles[i][2]);
            plan.setPlanD(doubles[i][3]);
            planList.add(plan);
        }
        return planList;
    }
    @PostMapping("/point/networkplan1")
    @ResponseBody
    public List<Result> networkplan1(HttpServletRequest req){

        String provinces = req.getParameter("provinces");
//        String provinces = "安徽";
        String carLength = req.getParameter("carLength");
//        String carLength = "小车4米6";
        double transportNum = 800000;
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
        double order = 80;
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
//        int times1 = 10;
//        if(req.getParameter("times1")!=null&&!req.getParameter("times1").equals("")) {
//            times1 = Integer.parseInt(req.getParameter("times1"));
//        }
//        int times2 = 8;
//        if(req.getParameter("times2")!=null&&!req.getParameter("times2").equals("")) {
//            times2 = Integer.parseInt(req.getParameter("times2"));
//        }
//        int times3 = 6;
//        if(req.getParameter("times3")!=null&&!req.getParameter("times3").equals("")) {
//            times3 = Integer.parseInt(req.getParameter("times3"));
//        }
//        int times4 = 4;
//        if(req.getParameter("times4")!=null&&!req.getParameter("times4").equals("")) {
//            times4 = Integer.parseInt(req.getParameter("times4"));
//        }
        int emp_quantity = 50;//一天处理十二托


        List<GlcPoint> list = glcPointService.selectGlcPointList(new GlcPoint());  //获取省份内城市

        double gdp =  0.0;
        for (GlcPoint city:list){
            gdp+= Double.parseDouble(city.getGdp());
        }

        List<City> rdcPoint = new ArrayList<>();
        List<City> points = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        for ( GlcPoint glcPoint:list){//需求点
            points.add(new City(glcPoint.getCity(),glcPoint.getLat(),glcPoint.getLng(),glcPoint.getGdp()));//备选点
        }
        List<Result> results = new ArrayList<>();
        List<Result> results1 = new ArrayList<>();
        List<Result> results2 = new ArrayList<>();
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
            for (int i = 1; i <= points.size(); i++) {
                double max = Double.MAX_VALUE;
                Result resultd = new Result();
                resultd.setCity(i + "");
                List<List<City>> combinations = NetworkUtils.combinations(points, i);//已经遍历了该RDC数量下的所有RDC组合 k为备选点数量
                for (List<City> cityList : combinations) {
                    results = new ArrayList<>();
                    rdcPoint = new ArrayList<>();
                    for (City city : cityList) {
                        rdcPoint.add(new City(city.getCity(), city.getLat(), city.getLng(), city.getGdp())); //将选取的备选点当做新增的RDC
                    }
                    // 开始计算
                    cities = chooseCity(rdcPoint, points);//选择城市
                    Map<String, List<City>> linkCity = cities.stream().collect(Collectors.groupingBy(City::getCity1));//网络连接
                    double allCost = 0.0;
                    for (String rdc : linkCity.keySet()) {
                        List<City> cityLink = linkCity.get(rdc);
                        Result result = new Result();
                        double transportCost = 0.0;
                        double carNum = 0.0;
                        double storageCost = 0.0;
                        double emp = 0;
                        double inventory = 0.0;
                        int order_rate = 0;
                        int order_num = 0;
                        String range = "";
                        for (City city1 : cityLink) {
                            range += city1.getCity() + ",";
                            double gdps = 1.5 * Double.parseDouble(city1.getGdp()) / gdp * transportNum;
                            transportCost += Math.sqrt(Double.parseDouble(city1.getDistance())) * 1.62 * (1 + 0.11) * 1.2 * 1 * gdps;//计算运输成本
                            carNum += gdps / Double.parseDouble(CarType.valueOf(carLength).getCode()) / 365;
                            emp += gdps / 365 / emp_quantity;//一天处理多少托需要多少人
                            inventory += gdps / 365 * Math.sqrt(Math.sqrt(i));
                            order_num++;
                            if (Double.parseDouble(city1.getDistance()) < 1000) {
                                order_rate++;
                            }
                        }
                        emp = Math.ceil(emp + Math.sqrt(Math.sqrt(i)));
                        storageCost = emp * warehousing;//需要的人员数量
                        double storage_area = 0.0;
                        if (inventory > 5) {
                            storage_area = AreaUtils.getHightStorage(inventory, emp * emp_quantity).getArea();
                        } else {
                            storage_area = AreaUtils.getHeapStorage(inventory).getArea();
                        }

                        double tally = AreaUtils.getTally(2 * emp * 60 / 2, carLength).getArea();
                        double platform = AreaUtils.getPlatform(2 * emp * 60 / 2, carLength).getPlatform_area();
                        double area = tally + storage_area + platform;
                        double buildCost = area * 27 * 12;//先假设每平米面积为32元/月
                        double price = (h_price * 5000 + m_price * 1000 + l_price * 100) / 100;
                        if (inventory <= 1) {
                            inventory = 0;
                        }
                        double inventoryCost = price * inventory * (0.05 + inventory_loss / 100) * 5;
                        result.setCity(rdc);
                        result.setRange(range);
                        result.setArea(Math.round(area));
                        result.setStorage(emp * emp_quantity * 365);
                        result.setStorageCost(Math.round(storageCost));
                        result.setBuildCost(Math.round(buildCost));
                        result.setInventoryCost(Math.round(inventoryCost));
                        result.setTransportCost(Math.round(transportCost));
                        result.setAll(Math.round(storageCost + buildCost + inventoryCost + transportCost));
                        result.setEmp(emp);
                        result.setCar(Math.ceil(carNum));
                        result.setOrder_rate(order_rate / order_num);
                        result.setInventory_num(inventory);
                        result.setSales_account(transportCost * 40);
                        result.setThroughput_num((emp * 365 * emp_quantity));
                        result.setRate(result.getAll() / result.getSales_account());
                        result.setRate1(result.getAll() / result.getSales_account() );
                        results.add(result);
                        allCost += result.getAll();
                    }
                    Result allresult = new Result();
                    allresult.setArea(0);
                    allresult.setStorageCost(0);
                    allresult.setStorage(0);
                    allresult.setBuildCost(0);
                    allresult.setInventoryCost(0);
                    allresult.setTransportCost(0);
                    allresult.setAll(0);
                    allresult.setEmp(0);
                    allresult.setCar(0);
                    allresult.setRate(0);
                    allresult.setOrder_rate(0);
                    allresult.setInventory_num(0);
                    allresult.setSales_account(0);
                    allresult.setThroughput_num(0);
                    int m = 0;
                    for (Result result : results) {
                        allresult.setCity(result.getCity());
                        allresult.setStorage(Math.round(result.getStorage() + allresult.getStorage()));
                        allresult.setArea(Math.round(allresult.getArea() + result.getArea()));
                        allresult.setStorageCost(Math.round(allresult.getStorageCost() + result.getStorageCost()));
                        allresult.setBuildCost(Math.round(allresult.getBuildCost() + result.getBuildCost()));
                        allresult.setInventoryCost(Math.round(allresult.getInventoryCost() + result.getInventoryCost()));
                        allresult.setTransportCost(Math.round(allresult.getTransportCost() + result.getTransportCost()));
                        allresult.setAll(Math.round(allresult.getAll() + result.getAll()));
                        allresult.setEmp(Math.round(allresult.getEmp() + result.getEmp()));
                        allresult.setCar(Math.round(allresult.getCar() + result.getCar()));
                        allresult.setOrder_rate(allresult.getOrder_rate() + result.getOrder_rate());
                        allresult.setInventory_num(Math.round(result.getInventory_num() + allresult.getInventory_num()));
                        allresult.setThroughput_num(Math.round(transportNum));
                        allresult.setSales_account(Math.round(allresult.getSales_account() + result.getSales_account()));
                        allresult.setRate1(allresult.getAll() / allresult.getSales_account() );
                        m++;
                    }
                    allresult.setStorageCost(allresult.getStorageCost() + managementFee*i);
                    allresult.setAll(allresult.getAll() + managementFee*i);
                    allresult.setOrder_rate(allresult.getOrder_rate() / m);

                    if (allresult.getAll() < max) {
//                        resultd.setStorageCost(allresult.getStorageCost());
//                        resultd.setAll(allCost);
//                        resultd.setTransportCost(allresult.getTransportCost());
//                        resultd.setInventoryCost(allresult.getInventoryCost());
//                        resultd.setBuildCost(allresult.getBuildCost());
                        max = allresult.getAll();
                        ResultMsg resultMsg = new ResultMsg();
                        resultMsg.setRdc(i);
                        resultMsg.setCost(allresult.getAll());
                        resultMsg.setCities(cities);
                        resultMsg.setResult(allresult);
                        resultMsg.setResultList(results);
                        resultMsgs.add(resultMsg);
                    }
                }
//                results1.add(resultd);
            }
            jedis.select(2);
            jedis.set(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+b_goods_size+m_goods_size+s_goods_size,JSON.toJSON(resultMsgs).toString());
        }
//        List<City> list1 = listList.get(listList.size()-1);
        resultMsgs = resultMsgs.stream().sorted(Comparator.comparing(ResultMsg::getCost)).collect(Collectors.toList());
        Result allresults = resultMsgs.get(0).getResult();
        allresults.setCity("总计");
        results1.addAll( resultMsgs.get(0).getResultList());
        results1.add(allresults);
        return results1;
    }

    @PostMapping("/point/networkLine")
    @ResponseBody
    public TableDataInfo networkLine(HttpServletRequest req){
        String provinces = req.getParameter("provinces");
//        String provinces = "安徽";
        String carLength = req.getParameter("carLength");
//        String carLength = "小车4米6";
        double transportNum = 800000;
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
        double order = 80;
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
//        int times1 = 10;
//        if(req.getParameter("times1")!=null&&!req.getParameter("times1").equals("")) {
//            times1 = Integer.parseInt(req.getParameter("times1"));
//        }
//        int times2 = 8;
//        if(req.getParameter("times2")!=null&&!req.getParameter("times2").equals("")) {
//            times2 = Integer.parseInt(req.getParameter("times2"));
//        }
//        int times3 = 6;
//        if(req.getParameter("times3")!=null&&!req.getParameter("times3").equals("")) {
//            times3 = Integer.parseInt(req.getParameter("times3"));
//        }
//        int times4 = 4;
//        if(req.getParameter("times4")!=null&&!req.getParameter("times4").equals("")) {
//            times4 = Integer.parseInt(req.getParameter("times4"));
//        }
        int emp_quantity = 50;//一天处理十二托


        List<GlcPoint> list = glcPointService.selectGlcPointList(new GlcPoint(provinces));  //获取省份内城市

        double gdp =  0.0;
        for (GlcPoint city:list){
            gdp+= Double.parseDouble(city.getGdp());
        }

        List<City> rdcPoint = new ArrayList<>();
        List<City> points = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        for ( GlcPoint glcPoint:list){//需求点
            points.add(new City(glcPoint.getCity(),glcPoint.getLat(),glcPoint.getLng(),glcPoint.getGdp()));//备选点
        }
        List<Result> results = new ArrayList<>();
        List<Result> results1 = new ArrayList<>();
        List<Result> results2 = new ArrayList<>();
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
            for (int i = 1; i <= points.size(); i++) {
                double max = Double.MAX_VALUE;
                Result resultd = new Result();
                resultd.setCity(i + "");
                List<List<City>> combinations = NetworkUtils.combinations(points, i);//已经遍历了该RDC数量下的所有RDC组合 k为备选点数量
                for (List<City> cityList : combinations) {
                    results = new ArrayList<>();
                    rdcPoint = new ArrayList<>();
                    for (City city : cityList) {
                        rdcPoint.add(new City(city.getCity(), city.getLat(), city.getLng(), city.getGdp())); //将选取的备选点当做新增的RDC
                    }
                    // 开始计算
                    cities = chooseCity(rdcPoint, points);//选择城市
                    Map<String, List<City>> linkCity = cities.stream().collect(Collectors.groupingBy(City::getCity1));//网络连接
                    double allCost = 0.0;
                    for (String rdc : linkCity.keySet()) {
                        List<City> cityLink = linkCity.get(rdc);
                        Result result = new Result();
                        double transportCost = 0.0;
                        double carNum = 0.0;
                        double storageCost = 0.0;
                        double emp = 0;
                        double inventory = 0.0;
                        int order_rate = 0;
                        int order_num = 0;
                        String range = "";
                        for (City city1 : cityLink) {
//                            range += city1.getCity() + ",";
                            double gdps = 1.5 * Double.parseDouble(city1.getGdp()) / gdp * transportNum;
                            transportCost += Math.sqrt(Double.parseDouble(city1.getDistance())) * 1.62 * (1 + 0.09) * 1.2 * 1 * gdps;//计算运输成本
                            carNum += gdps / Double.parseDouble(CarType.valueOf(carLength).getCode()) / 365;
                            emp += gdps / 365 / emp_quantity;//一天处理多少托需要多少人
                            inventory += gdps / 365 * Math.sqrt(Math.sqrt(i));
                            order_num++;
                            if (Double.parseDouble(city1.getDistance()) < order) {
                                order_rate++;
                            }
                        }
                        emp = Math.ceil(emp*Math.pow(1.1,i));
                        storageCost = emp * warehousing;//需要的人员数量
                        double storage_area = 0.0;
                        if (inventory > 5) {
                            storage_area = AreaUtils.getHightStorage(inventory, emp * emp_quantity).getArea();
                        } else {
                            storage_area = AreaUtils.getHeapStorage(inventory).getArea();
                        }

                        double tally = AreaUtils.getTally(2 * emp * 60 / 2, carLength).getArea();
                        double platform = AreaUtils.getPlatform(2 * emp * 60 / 2, carLength).getPlatform_area();
                        double area = tally + storage_area + platform;
                        double buildCost = area * 27 * 12;//先假设每平米面积为32元/月
                        double price = (h_price * 5000 + m_price * 1000 + l_price * 100) / 100;
                        if (inventory <= 1) {
                            inventory = 0;
                        }
                        double inventoryCost = price * inventory * (0.05 + inventory_loss / 100) * 5;
                        result.setCity(rdc);
                        result.setRange(range);
                        result.setArea(Math.round(area));
                        result.setStorage(emp * emp_quantity * 365);
                        result.setStorageCost(Math.round(storageCost));
                        result.setBuildCost(Math.round(buildCost));
                        result.setInventoryCost(Math.round(inventoryCost));
                        result.setTransportCost(Math.round(transportCost));
                        result.setAll(Math.round(storageCost + buildCost + inventoryCost + transportCost));
                        result.setEmp(emp);
                        result.setCar(Math.ceil(carNum));
                        result.setOrder_rate(order_rate / order_num);
                        result.setInventory_num(inventory);
                        result.setSales_account(transportCost * 40);
                        result.setThroughput_num((emp * 365 * emp_quantity));
                        result.setRate(result.getAll() / result.getSales_account());
                        result.setRate1(result.getAll() / result.getSales_account() );
                        results.add(result);
                        allCost += result.getAll();
                    }
                    Result allresult = new Result();
                    allresult.setArea(0);
                    allresult.setStorageCost(0);
                    allresult.setStorage(0);
                    allresult.setBuildCost(0);
                    allresult.setInventoryCost(0);
                    allresult.setTransportCost(0);
                    allresult.setAll(0);
                    allresult.setEmp(0);
                    allresult.setCar(0);
                    allresult.setRate(0);
                    allresult.setOrder_rate(0);
                    allresult.setInventory_num(0);
                    allresult.setSales_account(0);
                    allresult.setThroughput_num(0);
                    int m = 0;
                    for (Result result : results) {
                        allresult.setCity(result.getCity());
                        allresult.setStorage(Math.round(result.getStorage() + allresult.getStorage()));
                        allresult.setArea(Math.round(allresult.getArea() + result.getArea()));
                        allresult.setStorageCost(Math.round(allresult.getStorageCost() + result.getStorageCost()));
                        allresult.setBuildCost(Math.round(allresult.getBuildCost() + result.getBuildCost()));
                        allresult.setInventoryCost(Math.round(allresult.getInventoryCost() + result.getInventoryCost()));
                        allresult.setTransportCost(Math.round(allresult.getTransportCost() + result.getTransportCost()));
                        allresult.setAll(Math.round(allresult.getAll() + result.getAll()));
                        allresult.setEmp(Math.round(allresult.getEmp() + result.getEmp()));
                        allresult.setCar(Math.round(allresult.getCar() + result.getCar()));
                        allresult.setOrder_rate(allresult.getOrder_rate() + result.getOrder_rate());
                        allresult.setInventory_num(Math.round(result.getInventory_num() + allresult.getInventory_num()));
                        allresult.setThroughput_num(Math.round(transportNum));
                        allresult.setSales_account(Math.round(allresult.getSales_account() + result.getSales_account()));
                        allresult.setRate1(allresult.getAll() / allresult.getSales_account() );
                        m++;
                    }
                    allresult.setStorageCost(allresult.getStorageCost() + managementFee*i);
                    allresult.setAll(allresult.getAll() + managementFee*i);
                    allresult.setOrder_rate(allresult.getOrder_rate() / m);

                    if (allresult.getAll() < max) {
//                        resultd.setStorageCost(allresult.getStorageCost());
//                        resultd.setAll(allCost);
//                        resultd.setTransportCost(allresult.getTransportCost());
//                        resultd.setInventoryCost(allresult.getInventoryCost());
//                        resultd.setBuildCost(allresult.getBuildCost());
                        max = allresult.getAll();
                        ResultMsg resultMsg = new ResultMsg();
                        resultMsg.setRdc(i);
                        resultMsg.setCost(allresult.getAll());
                        resultMsg.setCities(cities);
                        resultMsg.setResult(allresult);
                        resultMsg.setResultList(results);
                        resultMsgs.add(resultMsg);
                    }
                }
//                results1.add(resultd);
            }
            jedis.select(2);
            jedis.set(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+b_goods_size+m_goods_size+s_goods_size,JSON.toJSON(resultMsgs).toString());
        }
        Map<Integer, List<ResultMsg>> re = resultMsgs.stream().collect(Collectors.groupingBy(ResultMsg::getRdc));//网络连接
        List<Result> resultList = new ArrayList<>();
        for(int i:re.keySet()){
            Result result = new Result();
            result = re.get(i).get(re.get(i).size()-1).getResult();
            result.setCity(i+"");
            resultList.add(result);
        }

        return getDataTable(resultList);
    }

    @PostMapping("/point/get1")
    @ResponseBody
    public Result open1(HttpServletRequest req){

        String provinces = req.getParameter("provinces");
//        String provinces = "安徽";
        String carLength = req.getParameter("carLength");
//        String carLength = "小车4米6";
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
        int emp_quantity = 50;//一天处理十二托
        double lever =1.65;
        if (order == 90){
            lever =1.25;
        }else if(order == 95){
            lever =1.65;
        }else if(order == 98){
            lever =2.05;
        }else if(order == 100){
            lever =3.5;
        }

        List<GlcPoint> list = glcPointService.selectGlcPointList(new GlcPoint(provinces));  //获取省份内城市

        double gdp =  0.0;
        for (GlcPoint city:list){
            gdp+= Double.parseDouble(city.getGdp());
        }
        List<City> rdcPoint = new ArrayList<>();
        List<City> points = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        for ( GlcPoint glcPoint:list){//需求点
            points.add(new City(glcPoint.getCity(),glcPoint.getLat(),glcPoint.getLng(),glcPoint.getGdp()));//备选点
        }
        List<Result> results = new ArrayList<>();
        List<Result> results1 = new ArrayList<>();
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
            for (int i = 3; i <=list.size(); i++) {


                double max = Double.MAX_VALUE;
                Result resultd = new Result();
                resultd.setCity(i + "");
                List<List<City>> combinations = NetworkUtils.combinations(points, i);//已经遍历了该RDC数量下的所有RDC组合 k为备选点数量
//                List<City> cits = new ArrayList<>();
//                List<List<City>> combinations = new ArrayList<>();
//                for (GlcPoint glcPoint:list){
//                    if (glcPoint.getCity().equals("荆门市")||glcPoint.getCity().equals("武汉市")||glcPoint.getCity().equals("鄂州市")){
//                        cits.add(new City(glcPoint.getCity(),glcPoint.getLat(),glcPoint.getLng(),glcPoint.getGdp()));
//                    }
//                }
//                combinations.add(cits);
                int l =0;
                List<List<City>> combinations1 = new ArrayList<>();
                double max1 = Double.MAX_VALUE;
                if(combinations.size()>500){
                    combinations = combinations.subList(0,500);
                }
                for (List<City> cityList : combinations) {

                    results = new ArrayList<>();
                    rdcPoint = new ArrayList<>();
                    for (City city : cityList) {
                        rdcPoint.add(new City(city.getCity(), city.getLat(), city.getLng(), city.getGdp())); //将选取的备选点当做新增的RDC
                    }
                    // 开始计算
                    double distance = chooseCity1(rdcPoint, points);//选择城市
                    if (distance < max1) {
                        max1 = distance;
                        combinations1.add(cityList);
                        l++;
                    }
                    l++;
                }
                for (List<City> cityList : combinations1) {
                    results = new ArrayList<>();
                    rdcPoint = new ArrayList<>();
                    for (City city : cityList) {
                        rdcPoint.add(new City(city.getCity(), city.getLat(), city.getLng(), city.getGdp())); //将选取的备选点当做新增的RDC
                    }
                    // 开始计算
                    cities = chooseCity(rdcPoint, points);//选择城市
                    Map<String, List<City>> linkCity = cities.stream().collect(Collectors.groupingBy(City::getCity1));//网络连接
                    double allCost = 0.0;

                    for (String rdc : linkCity.keySet()) {
                        List<City> cityLink = linkCity.get(rdc);
                        Result result = new Result();
                        double transportCost = 0.0;
                        double carNum = 0.0;
                        double storageCost = 0.0;
                        double emp = 0;
                        double inventory = 0.0;
                        String range = "";
                        double price = (h_price * 2000 + m_price * 1000 + l_price * 100) / 100*5;
                        double size =  (b_goods_size *5 + m_goods_size * 0.5 + s_goods_size * 0.05) / 100;
                        for (City city1 : cityLink) {
//                            range += city1.getCity() + ",";
                            double gdps = 1.5 * Double.parseDouble(city1.getGdp()) / gdp * transportNum;
                            transportCost += Math.sqrt(Double.parseDouble(city1.getDistance()))  * (1 + 0.09) * 1.2 * 1 * gdps;//计算运输成本
                            carNum += gdps / Double.parseDouble(CarType.valueOf(carLength).getCode()) / 365;
                            emp += gdps / 365 / emp_quantity*(2 - (Math.sqrt(times / 96) ))*(1+Math.sqrt(goods_num/10000))/Math.sqrt(Math.sqrt(size))/2;//一天处理多少托需要多少人
                            inventory +=Math.pow(gdps / 365 * Math.sqrt(i) * lever,0.9) ;
                        }

                        emp = Math.ceil(Math.pow(emp,1.1));
                        storageCost = emp * warehousing+managementFee;//需要的人员数量
                        double storage_area = 0.0;
                        if (inventory > 5) {
                            storage_area = AreaUtils.getHightStorage(inventory, emp * emp_quantity).getArea();
                        } else {
                            storage_area = AreaUtils.getHeapStorage(inventory).getArea();
                        }

                        double tally = AreaUtils.getTally(2 * emp * 60 / 2, carLength).getArea();
                        double platform = AreaUtils.getPlatform(2 * emp * 60 / 2, carLength).getPlatform_area();
                        double area = tally + Math.pow(storage_area,0.85) + platform;
                        double buildCost = area * 27 * 12;//先假设每平米面积为32元/月

                        if (inventory <= 1) {
                            inventory = 0;
                        }
                        double inventoryCost = price * inventory * (0.05 + inventory_loss / 100);
                        result.setCity(rdc);
                        result.setRange(range);
                        result.setArea(Math.round(area));
                        result.setStorage(emp * emp_quantity * 365+managementFee*((Math.pow(i,1.1))/i));
                        result.setStorageCost(Math.round(storageCost));
                        result.setBuildCost(Math.round(buildCost));
                        result.setInventoryCost(Math.round(inventoryCost));
                        result.setTransportCost(Math.round(transportCost));
                        result.setAll(Math.round(storageCost + buildCost + inventoryCost + transportCost));
                        result.setEmp(emp);
                        result.setCar(Math.ceil(carNum));
                        result.setOrder_rate(order-20+times/12);
                        result.setInventory_num(NetworkUtils.random(18,36));
                        result.setSales_account(transportNum * price);
                        result.setThroughput_num((emp * 365 * emp_quantity/2));
                        result.setRate(result.getAll() / result.getSales_account()*10);
                        result.setRate1(result.getAll() / result.getSales_account()*10 );
                        result.setStorage_area(NetworkUtils.random(120,320));
                        result.setPlat_cost(result.getAll()/result.getThroughput_num());
                        result.setOrder_cost(result.getPlat_cost()/ NetworkUtils.random(2,5));
                        result.setArea_cost(result.getAll()/result.getArea());
                        result.setPlat_storage(result.getStorage()/transportNum);
                        result.setPlat_transport(result.getTransportCost()/transportNum);
                        results.add(result);
                        allCost += result.getAll();
                    }
                    Result allresult = new Result();
                    int m = 0;
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
                        allresult.setOrder_rate(allresult.getOrder_rate() + result.getOrder_rate());
                        allresult.setInventory_num(Math.round(result.getInventory_num() + allresult.getInventory_num()));
                        allresult.setSales_account(Math.round(allresult.getSales_account() + result.getSales_account()));
                        allresult.setRate1(allresult.getAll() / allresult.getSales_account()*10);
                        allresult.setStorage_area(allresult.getStorage_area() + result.getStorage_area());
                        allresult.setPlat_cost(allresult.getAll()/transportNum);
                        allresult.setOrder_cost(allresult.getPlat_cost()/ NetworkUtils.random(2,5));
                        allresult.setArea_cost(allresult.getAll()/allresult.getArea());
                        m++;
                    }
                    allresult.setStorageCost(allresult.getStorageCost());
                    allresult.setAll(allresult.getAll());
                    allresult.setOrder_rate(allresult.getOrder_rate()/ i);
                    allresult.setInventory_num(Math.round(allresult.getInventory_num()/i));
                    allresult.setStorage_area(Math.round(allresult.getStorage_area()/i));
                    allresult.setRate(allresult.getAll()/allresult.getSales_account()*10);
                    allresult.setArea_cost(allresult.getAll()/allresult.getArea());
                    allresult.setPlat_cost(allresult.getPlat_cost());
                    allresult.setPlat_storage(Math.round(allresult.getStorage()/transportNum/i));
                    allresult.setOrder_cost(allresult.getOrder_cost()/i);
                    allresult.setPlat_transport(Math.round(allresult.getTransportCost()/transportNum/i));
                    if (allresult.getAll() < max) {
//                        resultd.setStorageCost(allresult.getStorageCost());
//                        resultd.setAll(allCost);
//                        resultd.setTransportCost(allresult.getTransportCost());
//                        resultd.setInventoryCost(allresult.getInventoryCost());
//                        resultd.setBuildCost(allresult.getBuildCost());
                        max = allresult.getAll();
                        ResultMsg resultMsg = new ResultMsg();
                        resultMsg.setRdc(i);
                        resultMsg.setCost(allresult.getAll());
                        resultMsg.setCities(cities);
                        resultMsg.setResult(allresult);
                        resultMsg.setResultList(results);
                        resultMsgs.add(resultMsg);
                    }
                }
//                results1.add(resultd);
            }
            jedis.select(2);
            jedis.set(provinces+carLength+transportNum+warehousing+h_price+m_price+inventory_loss+times+order+managementFee+goods_num+""+b_goods_size+m_goods_size+s_goods_size,JSON.toJSON(resultMsgs).toString());
        }

        resultMsgs = resultMsgs.stream().sorted(Comparator.comparing(ResultMsg::getCost)).collect(Collectors.toList());
        Result allresults = resultMsgs.get(resultMsgs.size()-1).getResult();
        allresults.setThroughput_num(times);
        allresults.setOrder_rate(resultMsgs.get(resultMsgs.size()-1).getRdc());
//        allresults.setCity("总计");
        return allresults;
    }


    @PostMapping("/point/get2")
    @ResponseBody
    public  List<Material> getMaterial(HttpServletRequest req){
        double goods_num = Double.parseDouble(req.getParameter("goods_num"));
        double b_size = Double.parseDouble(req.getParameter("b_size"));
        double m_size = Double.parseDouble(req.getParameter("m_size"));
        double s_size = Double.parseDouble(req.getParameter("s_size"));
        double h_price = Double.parseDouble(req.getParameter("h_price"));
        double m_price = Double.parseDouble(req.getParameter("m_price"));
        double l_price = Double.parseDouble(req.getParameter("l_price"));
        List<Material> list = NetworkUtils.createGoods(goods_num);
        list = NetworkUtils.initMaterialPrice(list,b_size,m_size,s_size);
        list = NetworkUtils.initMaterialVolume(list,h_price,m_price,l_price);

        return list;
    }


    @PostMapping("/point/get3")
    @ResponseBody
    public   List<City> getMaterial1(HttpServletRequest req){
        List<GlcPoint> list = glcPointService.selectGlcPointList(new GlcPoint());  //获取省份内城市
        List<City> rdcPoint = new ArrayList<>();
        List<City> points = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        for ( GlcPoint glcPoint:list){//需求点
            if (glcPoint.getProvinces()!=null){
                rdcPoint.add(new City(glcPoint.getCity(), glcPoint.getLat(), glcPoint.getLng(), glcPoint.getGdp()));//备选点
            }else {
                points.add(new City(glcPoint.getCity(), glcPoint.getLat(), glcPoint.getLng(), glcPoint.getGdp()));//备选点
            }
        }
        List<Cluster> clusters = initCentroides(points, 40);
        int i=0;
        double dis = Double.MAX_VALUE;
        while (i<3000) { // 所有分类是否全部收敛
            // 1.计算距离对每个点进行分类
            // 2.判断质心点是否改变,未改变则该分类已经收敛
            // 3.重新生成质心点
            initClusters(clusters); // 重置分类中的点
         dis=   classifyPoint(points, clusters,dis);// 计算距离进行分类
            recalcularCentroides(clusters); // 重新计算质心点
            i++;
        }
//        Double ofv = calcularObjetiFuncionValue(clusters);
        for(Cluster cluster:clusters){
            for(City citys : cluster.getPoints()) {
                citys.setLat1(cluster.getCentroid().getLat());
                citys.setLng1(cluster.getCentroid().getLng());
                citys.setDistance(String.valueOf(getDistance(citys,cluster.getCentroid())/1000));
                cities.add(citys);
            }
        }
        return cities;
    }

    /**
     * 重新计算质心点
     *
     * @param clusters
     */
    private static void recalcularCentroides(List<Cluster> clusters) {
        for (Cluster c : clusters) {
            if (c.getPoints().isEmpty()) {
                c.setConvergence(true);
                continue;
            }

            // 求均值,作为新的质心点
            Double x;
            Double y;
            Double sum_x = 0d;
            Double sum_y = 0d;
            for (City point : c.getPoints()) {
                sum_x += Double.parseDouble(point.getLat());
                sum_y += Double.parseDouble(point.getLng());
            }
            x = sum_x / c.getPoints().size();
            y = sum_y / c.getPoints().size();
            City nuevoCentroide = new City(String.valueOf(x), String.valueOf(y)); // 新的质心点

            if (nuevoCentroide.equals(c.getCentroid())) { // 如果质心点不再改变 则该分类已经收敛
                c.setConvergence(true);
            } else {
                c.setCentroid(nuevoCentroide);
            }
        }
    }
    /**
     * 初始化k个质心点
     *
     * @param points 点集
     * @param k      K值
     * @return 分类集合对象
     */
    private  List<Cluster> initCentroides(List<City> points, Integer k) {
        List<Cluster> centroides = new ArrayList<Cluster>();
        List<List<City>> list = NetworkUtils.averageAssign(points,k);
        for(List<City> cities:list){
            double x=0d;
            double y=0d;
            for(City city:cities){
                x += Double.parseDouble(city.getLat());
                y += Double.parseDouble(city.getLng());
            }

            Cluster c = new Cluster();
            City centroide = new City(String.valueOf(x/cities.size()), String.valueOf(y/cities.size())); // 初始化的随机中心点
            c.setCentroid(centroide);
            centroides.add(c);
        }
        // 求出数据集的范围(找出所有点的x最小、最大和y最小、最大坐标。)
//        Double max_X = Double.NEGATIVE_INFINITY;
//        Double max_Y = Double.NEGATIVE_INFINITY;
//        Double min_X = Double.POSITIVE_INFINITY;
//        Double min_Y = Double.POSITIVE_INFINITY;
//        for (City point : points) {
//            max_X = max_X < Double.parseDouble(point.getLat()) ? Double.parseDouble(point.getLat()) : max_X;
//            max_Y = max_Y < Double.parseDouble(point.getLng()) ? Double.parseDouble(point.getLng()) : max_Y;
//            min_X = min_X > Double.parseDouble(point.getLat()) ? Double.parseDouble(point.getLat()) : min_X;
//            min_Y = min_Y > Double.parseDouble(point.getLng()) ? Double.parseDouble(point.getLng()) : min_Y;
//        }
//        System.out.println("min_X" + min_X + ",max_X:" + max_X + ",min_Y" + min_Y + ",max_Y" + max_Y);

//         在范围内随机初始化k个质心点
//        Random random = new Random();
        // 随机初始化k个中心点
//        for (int i = 0; i < k; i++) {
//            double x = min_X+ random.nextDouble()*(max_X-min_X);
//            double y = min_Y+ random.nextDouble()*(max_Y-min_Y);

//        }

        return centroides;
    }
    /**
     * 计算距离,对点集进行分类
     *
     * @param points   点集
     * @param clusters 分类
     *
     */
    private static double classifyPoint(List<City> points, List<Cluster> clusters,double dis) {
        Double min =  Double.MIN_VALUE;
        for (City point : points) {
            Cluster masCercano = clusters.get(0); // 该点计算距离后所属的分类
            Double minDistancia = Double.MAX_VALUE; // 最小距离
            for (Cluster cluster : clusters) {
                Double distancia =getDistance(point,cluster.getCentroid())/1000; // 点和每个分类质心点的距离
                if (minDistancia > distancia) { // 得到该点和k个质心点最小的距离
                    minDistancia = distancia;
                    if(minDistancia>min){
                      min = minDistancia;
                    }
                    masCercano = cluster; // 得到该点的分类
                }
            }
            masCercano.getPoints().add(point); // 将该点添加到距离最近的分类中
        }
        if(dis>min){
            dis= min;
        }
        return dis;
    }

    private static void initClusters(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            cluster.initPoint();
        }
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

    /**
     * 计算目标函数值
     *
     * @param clusters
     * @return
     */
    private static Double calcularObjetiFuncionValue(List<Cluster> clusters) {
        Double ofv = 0d;
        for (Cluster cluster : clusters) {
            for (City point : cluster.getPoints()) {
                ofv += getDistance(cluster.getCentroid(), point);
            }
        }

        return ofv;
    }
    /**
     * 选择城市 城市对应各城市
     * @param rdcPoint
     * @param netPoint
     * @param
     * @param
     * @return
     */
    private static List<City> chooseCity(List<City> rdcPoint, List<City> netPoint) {

        List<City> combination = new ArrayList<>();
        for (City netPoints :netPoint){
            City city1 = new City(netPoints.getCity(),netPoints.getLat(),netPoints.getLng(),netPoints.getGdp());
            double max = Integer.MAX_VALUE;
            for (City city:rdcPoint){
                GlobalCoordinates userSource = new GlobalCoordinates(Double.parseDouble(netPoints.getLat()),Double.parseDouble(netPoints.getLng()));
                GlobalCoordinates businessTarget = new GlobalCoordinates(Double.parseDouble(city.getLat()),Double.parseDouble(city.getLng()));
//                double meterDouble = getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere);
                double meterDouble = getDistance(netPoints,city);

                double num = meterDouble/1000;
                if (num<max){
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
    private static double chooseCity1(List<City> rdcPoint, List<City> netPoint) {
        double distance=0.0;
        List<City> combination = new ArrayList<>();
        for (City netPoints :netPoint){
            City city1 = new City(netPoints.getCity(),netPoints.getLat(),netPoints.getLng(),netPoints.getGdp());
            double max = Integer.MAX_VALUE;
            for (City city:rdcPoint){
                GlobalCoordinates userSource = new GlobalCoordinates(Double.parseDouble(netPoints.getLat()),Double.parseDouble(netPoints.getLng()));
                GlobalCoordinates businessTarget = new GlobalCoordinates(Double.parseDouble(city.getLat()),Double.parseDouble(city.getLng()));
                double meterDouble = getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere);
//                double meterDouble = Double.parseDouble(twoJuLi(netPoints,city));

                double num = meterDouble/1000;
                if (num<max){
                    city1.setCity1(city.getCity());
                    city1.setLat1(city.getLat());
                    city1.setLng1(city.getLng());
                    city1.setDistance(String.valueOf(num));
                    max = num;
                }
            }
            combination.add(city1);
            distance += Double.parseDouble(city1.getDistance())*Double.parseDouble(city1.getGdp());
        }
        return distance;
    }
    public static <T> List<List<T>> combinations1(List<T> list, int k) {
        List<List<T>> listList = new ArrayList<>();
        Integer[] first = new Integer[list.size()];
        for(int m=0;m<list.size();m++){
            first[m] = m;
        }

        List<List<Integer>> size  = combiantion(first,k);

        for (List<Integer> s :size){
            List<T> list1  = new ArrayList<>();
            for(int z = 0;z<s.size();z++){
                list1.add(list.get(z));
            }
            listList.add(list1);
        }

        return listList;

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


    private static double getDistance(City cluster, City nuevoCentroide) {
        GlobalCoordinates userSource = new GlobalCoordinates(Double.parseDouble(cluster.getLat()),Double.parseDouble(cluster.getLng()));
        GlobalCoordinates businessTarget = new GlobalCoordinates(Double.parseDouble(nuevoCentroide.getLat()),Double.parseDouble(nuevoCentroide.getLng()));
        double meterDouble = NetWorkPlanUtils.getDistanceMeter(userSource, businessTarget, Ellipsoid.Sphere);
        return meterDouble;
    }

    /**
     * 调用百度api接口，返回距离
     */

    public  static Double twoJuLi(City city,City city1){
         double juli = 0.0;
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.select(3);
        Boolean exists= jedis.exists(city.getCity()+":"+city1.getCity());
        if (exists) {
            juli= Double.parseDouble(jedis.get(city.getCity()+":"+city1.getCity()));

        }else if (jedis.exists(city1.getCity()+":"+city.getCity())){
            juli = Double.parseDouble(jedis.get(city1.getCity()+":"+city.getCity()));
        }else {
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
            jedis.set(city.getCity()+":"+city1.getCity(),JSON.toJSON(juli).toString());

        }
        jedis.close();
        return juli;

    }

    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid){

        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }

}
