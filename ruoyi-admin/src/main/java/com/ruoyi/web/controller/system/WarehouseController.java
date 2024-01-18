package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;


import com.ruoyi.system.domain.Action;
import com.ruoyi.system.enumType.CarType;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.result.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2021-06-24
 */
@Controller
public class WarehouseController extends BaseController {




    @PostMapping("/getStorageList")
    @ResponseBody
    public TableDataInfo getStorageList(HttpServletRequest req) {

        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double goods_num = 3;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<Result1> list = new ArrayList<>();
        List<Result1> list1 = new ArrayList<>();
        list = Action.getInventory(storageNum, height, goods_num, shelf_space, shelf_height);
        for (int i = 0; i < 10; i++) {
            list1.add(list.get(i));
        }
        return getDataTable(list1);
    }

    @PostMapping("/getStorageList1")
    @ResponseBody
    public AjaxResult getStorageList1(HttpServletRequest req) {

        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double goods_num = 3;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<Result1> list = new ArrayList<>();
        List<Result1> list1 = new ArrayList<>();
        list = Action.getInventory(storageNum, height, goods_num, shelf_space, shelf_height);
        ExcelUtil<Result1> util = new ExcelUtil<Result1>(Result1.class);
        return util.exportExcel(list, "result1");
    }

    @PostMapping("/getUpload")
    @ResponseBody
    public TableDataInfo dsde(HttpServletRequest req) {
        String carLength = req.getParameter("carlength");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double unloading_time = 0.5;
        if (req.getParameter("unloading_time") != null && !req.getParameter("unloading_time").equals("")) {
            unloading_time = Double.parseDouble(req.getParameter("unloading_time"));
        }
        double everyDay_unloading_time = 8;
        if (req.getParameter("everyDay_unloading_time") != null && !req.getParameter("everyDay_unloading_time").equals("")) {
            everyDay_unloading_time = Double.parseDouble(req.getParameter("everyDay_unloading_time"));
        }
        double platform_width = 3;
        if (req.getParameter("platform_width") != null && !req.getParameter("platform_width").equals("")) {
            platform_width = Double.parseDouble(req.getParameter("platform_width"));
        }
        double platform_length = 3;
        if (req.getParameter("platform_length") != null && !req.getParameter("platform_length").equals("")) {
            platform_length = Double.parseDouble(req.getParameter("platform_length"));
        }
        double supplier = 300;
        if (req.getParameter("supplier") != null && !req.getParameter("supplier").equals("")) {
            supplier = Double.parseDouble(req.getParameter("supplier"));
        }
        double goods_num = 100;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        List<Result> list = new ArrayList<>();
        Result result = Action.runPlatform(carLength, transportNum, unloading_time, everyDay_unloading_time, platform_width, platform_length, supplier, goods_num, utilization, 2, unloading_time * 60 * 60 / Integer.parseInt(CarType.valueOf(carLength).getCode()));


        list.add(result);
        return getDataTable(list);
    }

    @PostMapping("/getUploadList")
    @ResponseBody
    public TableDataInfo getUploadList(HttpServletRequest req) {
        startPage();
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double supplier = 300;
        if (req.getParameter("supplier") != null && !req.getParameter("supplier").equals("")) {
            supplier = Double.parseDouble(req.getParameter("supplier"));
        }
        double goods_num = 100;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double volatility = 100;
        if (req.getParameter("volatility") != null && !req.getParameter("volatility").equals("")) {
            volatility = Double.parseDouble(req.getParameter("volatility"));
        }
        double iq = 100;
        if (req.getParameter("iq") != null && !req.getParameter("iq").equals("")) {
            iq = Double.parseDouble(req.getParameter("iq"));
        }
        List<Result> list = new ArrayList<>();
        List<Result> list1 = new ArrayList<>();
        list = Action.runRuOrder(transportNum, supplier, goods_num, volatility, iq);
        for (int i = 0; i < 10; i++) {
            list1.add(list.get(i));
        }
        return getDataTable(list1);
    }

    @PostMapping("/getUploadList1")
    @ResponseBody
    public AjaxResult getUploadList1(Result result, HttpServletRequest req) {
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double supplier = 300;
        if (req.getParameter("supplier") != null && !req.getParameter("supplier").equals("")) {
            supplier = Double.parseDouble(req.getParameter("supplier"));
        }
        double goods_num = 100;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double volatility = 100;
        if (req.getParameter("volatility") != null && !req.getParameter("volatility").equals("")) {
            volatility = Double.parseDouble(req.getParameter("volatility"));
        }
        double iq = 100;
        if (req.getParameter("iq") != null && !req.getParameter("iq").equals("")) {
            iq = Double.parseDouble(req.getParameter("iq"));
        }
        List<Result> list = new ArrayList<>();
        list = Action.runRuOrder(transportNum, supplier, goods_num, volatility, iq);
        ExcelUtil<Result> util = new ExcelUtil<Result>(Result.class);
        return util.exportExcel(list, "卸货");
    }

    @PostMapping("/getUploadLine")
    @ResponseBody
    public TableDataInfo getUploadLine(HttpServletRequest req) {
        String carLength = req.getParameter("carlength");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double unloading_time = 0.5;
        if (req.getParameter("unloading_time") != null && !req.getParameter("unloading_time").equals("")) {
            unloading_time = Double.parseDouble(req.getParameter("unloading_time"));
        }
        double everyDay_unloading_time = 8;
        if (req.getParameter("everyDay_unloading_time") != null && !req.getParameter("everyDay_unloading_time").equals("")) {
            everyDay_unloading_time = Double.parseDouble(req.getParameter("everyDay_unloading_time"));
        }
        double platform_width = 3;
        if (req.getParameter("platform_width") != null && !req.getParameter("platform_width").equals("")) {
            platform_width = Double.parseDouble(req.getParameter("platform_width"));
        }
        double platform_length = 3;
        if (req.getParameter("platform_length") != null && !req.getParameter("platform_length").equals("")) {
            platform_length = Double.parseDouble(req.getParameter("platform_length"));
        }
        double supplier = 300;
        if (req.getParameter("supplier") != null && !req.getParameter("supplier").equals("")) {
            supplier = Double.parseDouble(req.getParameter("supplier"));
        }
        double goods_num = 100;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double platform_a = 10;
        if (req.getParameter("platform_a") != null && !req.getParameter("platform_a").equals("")) {
            platform_a = Double.parseDouble(req.getParameter("platform_a"));
        }
        List<Result> list = new ArrayList<>();
        for (double i = 1; i < platform_a + 1; i++) {
            Result result = Action.runPlatform(carLength, transportNum, unloading_time, everyDay_unloading_time, platform_width, platform_length, supplier, goods_num, utilization, i, unloading_time * 60 * 60 / Integer.parseInt(CarType.valueOf(carLength).getCode()));
            result.setCost(i);
            list.add(result);
        }

        return getDataTable(list);
    }

    @PostMapping("/takeDown")
    @ResponseBody
    public TableDataInfo takeDown(HttpServletRequest req) {

        double take_downNum = 2370;
        if (req.getParameter("take_downNum") != null && !req.getParameter("take_downNum").equals("")) {
            take_downNum = Double.parseDouble(req.getParameter("take_downNum"));
        }
        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double forklift_channel = 3;
        if (req.getParameter("forklift_channel") != null && !req.getParameter("forklift_channel").equals("")) {
            forklift_channel = Double.parseDouble(req.getParameter("forklift_channel"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double putaway_speed = 1.1;
        if (req.getParameter("putaway_speed") != null && !req.getParameter("putaway_speed").equals("")) {
            putaway_speed = Double.parseDouble(req.getParameter("putaway_speed"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<Result> list = new ArrayList<>();
        Result result = Action.getTakeDown(take_downNum, storageNum, height, forklift_channel, utilization, putaway_speed, shelf_space, shelf_height);

        list.add(result);
        return getDataTable(list);
    }

    @PostMapping("/getSorting")
    @ResponseBody
    public TableDataInfo getSorting(HttpServletRequest req) {
        String sort_type = req.getParameter("sort_type");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double batch = 3;
        if (req.getParameter("batch") != null && !req.getParameter("batch").equals("")) {
            batch = Double.parseDouble(req.getParameter("batch"));
        }
        double sortingSpeed = 1.1;
        if (req.getParameter("sortingSpeed") != null && !req.getParameter("sortingSpeed").equals("")) {
            sortingSpeed = Double.parseDouble(req.getParameter("sortingSpeed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        int orderLine = 7;
        if (req.getParameter("orderLine") != null && !req.getParameter("orderLine").equals("")) {
            orderLine = Integer.parseInt(req.getParameter("orderLine"));
        }
        double goods_num = 1000;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double sortingTime = 80;
        if (req.getParameter("sortingTime") != null && !req.getParameter("sortingTime").equals("")) {
            sortingTime = Double.parseDouble(req.getParameter("sortingTime"));
        }

        List<Result> list = new ArrayList<>();
        Result result = Action.getSorting(transportNum, batch, orderLine, sortingSpeed, tally_channel, tray_clearance, utilization, sort_type, goods_num, sortingTime);

        list.add(result);
        return getDataTable(list);

    }

    @PostMapping("/getSortingList")
    @ResponseBody
    public TableDataInfo getSortingList(HttpServletRequest req) {
        String sort_type = req.getParameter("sort_type");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double sortingSpeed = 1.1;
        if (req.getParameter("sortingSpeed") != null && !req.getParameter("sortingSpeed").equals("")) {
            sortingSpeed = Double.parseDouble(req.getParameter("sortingSpeed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        int orderLine = 7;
        if (req.getParameter("orderLine") != null && !req.getParameter("orderLine").equals("")) {
            orderLine = Integer.parseInt(req.getParameter("orderLine"));
        }
        double goods_num = 1000;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double sortingTime = 80;
        if (req.getParameter("sortingTime") != null && !req.getParameter("sortingTime").equals("")) {
            sortingTime = Double.parseDouble(req.getParameter("sortingTime"));
        }
        double iq = 100;
        if (req.getParameter("iq") != null && !req.getParameter("iq").equals("")) {
            iq = Double.parseDouble(req.getParameter("iq"));
        }
        List<Result2> list = new ArrayList<>();
        List<Result2> list1 = new ArrayList<>();
        list = Action.getSortingOrder(transportNum, orderLine, goods_num, iq);
        for (int i = 0; i < 10; i++) {
            list1.add(list.get(i));
        }
        return getDataTable(list1);
    }


    @PostMapping("/getSortingList1")
    @ResponseBody
    public AjaxResult getSortingList1(HttpServletRequest req) {
        String sort_type = req.getParameter("sort_type");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double sortingSpeed = 1.1;
        if (req.getParameter("sortingSpeed") != null && !req.getParameter("sortingSpeed").equals("")) {
            sortingSpeed = Double.parseDouble(req.getParameter("sortingSpeed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        int orderLine = 7;
        if (req.getParameter("orderLine") != null && !req.getParameter("orderLine").equals("")) {
            orderLine = Integer.parseInt(req.getParameter("orderLine"));
        }
        double goods_num = 1000;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double sortingTime = 80;
        if (req.getParameter("sortingTime") != null && !req.getParameter("sortingTime").equals("")) {
            sortingTime = Double.parseDouble(req.getParameter("sortingTime"));
        }
        double iq = 100;
        if (req.getParameter("iq") != null && !req.getParameter("iq").equals("")) {
            iq = Double.parseDouble(req.getParameter("iq"));
        }
        List<Result2> list = new ArrayList<>();
        list = Action.getSortingOrder(transportNum, orderLine, goods_num, iq);
        ExcelUtil<Result2> util = new ExcelUtil<Result2>(Result2.class);
        return util.exportExcel(list, "分拣");
    }

    @PostMapping("/getSortingLine")
    @ResponseBody
    public TableDataInfo getSortingLine(HttpServletRequest req) {
        String sort_type = req.getParameter("sort_type");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double batch = 3;
        if (req.getParameter("batch") != null && !req.getParameter("batch").equals("")) {
            batch = Double.parseDouble(req.getParameter("batch"));
        }
        double sortingSpeed = 1.1;
        if (req.getParameter("sortingSpeed") != null && !req.getParameter("sortingSpeed").equals("")) {
            sortingSpeed = Double.parseDouble(req.getParameter("sortingSpeed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        int orderLine = 7;
        if (req.getParameter("orderLine") != null && !req.getParameter("orderLine").equals("")) {
            orderLine = Integer.parseInt(req.getParameter("orderLine"));
        }
        double goods_num = 1000;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double sortingTime = 80;
        if (req.getParameter("sortingTime") != null && !req.getParameter("sortingTime").equals("")) {
            sortingTime = Double.parseDouble(req.getParameter("sortingTime"));
        }
        double batchNum = 6;
        if (req.getParameter("batchNum") != null && !req.getParameter("batchNum").equals("")) {
            batchNum = Double.parseDouble(req.getParameter("batchNum"));
        }
        List<Result> list = new ArrayList<>();
        for (int i = 1; i <= batchNum; i++) {
            Result result = Action.getSorting(transportNum, i, orderLine, sortingSpeed, tally_channel, tray_clearance, utilization, sort_type, goods_num, sortingTime);

            result.setCost(i);
            list.add(result);
        }
        return getDataTable(list);
    }

    @PostMapping("/getDelivery")
    @ResponseBody
    public TableDataInfo getDelivery(HttpServletRequest req) {

        double deliveryNum = 2370;
        if (req.getParameter("deliveryNum") != null && !req.getParameter("deliveryNum").equals("")) {
            deliveryNum = Double.parseDouble(req.getParameter("deliveryNum"));
        }
        double batch = 3;
        if (req.getParameter("batch") != null && !req.getParameter("batch").equals("")) {
            batch = Double.parseDouble(req.getParameter("batch"));
        }
        double delivery_speed = 1.1;
        if (req.getParameter("delivery_speed") != null && !req.getParameter("delivery_speed").equals("")) {
            delivery_speed = Double.parseDouble(req.getParameter("delivery_speed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double delivery_Time = 150;
        if (req.getParameter("delivery_Time") != null && !req.getParameter("delivery_Time").equals("")) {
            delivery_Time = Double.parseDouble(req.getParameter("delivery_Time"));
        }
        List<Result> list = new ArrayList<>();
        Result result = Action.getDelivery(deliveryNum, batch, delivery_speed, tally_channel, tray_clearance, utilization, delivery_Time);

        list.add(result);
        return getDataTable(list);
    }

    @PostMapping("/getDeliveryList")
    @ResponseBody
    public TableDataInfo getDeliveryList(HttpServletRequest req) {

        double deliveryNum = 2370;
        if (req.getParameter("deliveryNum") != null && !req.getParameter("deliveryNum").equals("")) {
            deliveryNum = Double.parseDouble(req.getParameter("deliveryNum"));
        }
        double batch = 3;
        if (req.getParameter("batch") != null && !req.getParameter("batch").equals("")) {
            batch = Double.parseDouble(req.getParameter("batch"));
        }
        double delivery_speed = 1.1;
        if (req.getParameter("delivery_speed") != null && !req.getParameter("delivery_speed").equals("")) {
            delivery_speed = Double.parseDouble(req.getParameter("delivery_speed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double delivery_Time = 120;
        if (req.getParameter("delivery_Time") != null && !req.getParameter("delivery_Time").equals("")) {
            delivery_Time = Double.parseDouble(req.getParameter("delivery_Time"));
        }
        double batchNum = 6;
        if (req.getParameter("batchNum") != null && !req.getParameter("batchNum").equals("")) {
            batchNum = Double.parseDouble(req.getParameter("batchNum"));
        }
        List<Result> list = new ArrayList<>();
        for (int i = 1; i <= batchNum; i++) {
            Result result = Action.getDelivery(deliveryNum, i, delivery_speed, tally_channel, tray_clearance, utilization, delivery_Time);
            result.setCost(i);
            list.add(result);
        }
        return getDataTable(list);
    }

    @PostMapping("/getDeliveryLine")
    @ResponseBody
    public TableDataInfo getDeliveryLine(HttpServletRequest req) {

        String sort_type = req.getParameter("sort_type");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double sortingSpeed = 1.1;
        if (req.getParameter("sortingSpeed") != null && !req.getParameter("sortingSpeed").equals("")) {
            sortingSpeed = Double.parseDouble(req.getParameter("sortingSpeed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        int orderLine = 7;
        if (req.getParameter("orderLine") != null && !req.getParameter("orderLine").equals("")) {
            orderLine = Integer.parseInt(req.getParameter("orderLine"));
        }
        double goods_num = 1000;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double sortingTime = 80;
        if (req.getParameter("sortingTime") != null && !req.getParameter("sortingTime").equals("")) {
            sortingTime = Double.parseDouble(req.getParameter("sortingTime"));
        }
        double iq = 100;
        if (req.getParameter("iq") != null && !req.getParameter("iq").equals("")) {
            iq = Double.parseDouble(req.getParameter("iq"));
        }
        List<Result3> list = new ArrayList<>();
        List<Result3> list1 = new ArrayList<>();
        list = Action.getDeliveryOrder(transportNum, orderLine, goods_num, iq);
        for (int i = 0; i < 10; i++) {
            list1.add(list.get(i));
        }
        return getDataTable(list1);
    }

    @PostMapping("/getDeliveryLine1")
    @ResponseBody
    public AjaxResult getDeliveryLine1(HttpServletRequest req) {

        String sort_type = req.getParameter("sort_type");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double sortingSpeed = 1.1;
        if (req.getParameter("sortingSpeed") != null && !req.getParameter("sortingSpeed").equals("")) {
            sortingSpeed = Double.parseDouble(req.getParameter("sortingSpeed"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        int orderLine = 7;
        if (req.getParameter("orderLine") != null && !req.getParameter("orderLine").equals("")) {
            orderLine = Integer.parseInt(req.getParameter("orderLine"));
        }
        double goods_num = 1000;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double sortingTime = 80;
        if (req.getParameter("sortingTime") != null && !req.getParameter("sortingTime").equals("")) {
            sortingTime = Double.parseDouble(req.getParameter("sortingTime"));
        }
        double iq = 100;
        if (req.getParameter("iq") != null && !req.getParameter("iq").equals("")) {
            iq = Double.parseDouble(req.getParameter("iq"));
        }
        List<Result3> list = new ArrayList<>();
        list = Action.getDeliveryOrder(transportNum, orderLine, goods_num, iq);
        ExcelUtil<Result3> util = new ExcelUtil<Result3>(Result3.class);
        return util.exportExcel(list, "出库");
    }

    @PostMapping("/getLoading")
    @ResponseBody
    public TableDataInfo getLoading(HttpServletRequest req) {
        String carLength = req.getParameter("carlength");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double unloading_time = 0.5;
        if (req.getParameter("unloading_time") != null && !req.getParameter("unloading_time").equals("")) {
            unloading_time = Double.parseDouble(req.getParameter("unloading_time"));
        }
        double everyDay_unloading_time = 8;
        if (req.getParameter("everyDay_unloading_time") != null && !req.getParameter("everyDay_unloading_time").equals("")) {
            everyDay_unloading_time = Double.parseDouble(req.getParameter("everyDay_unloading_time"));
        }
        double platform_width = 3;
        if (req.getParameter("platform_width") != null && !req.getParameter("platform_width").equals("")) {
            platform_width = Double.parseDouble(req.getParameter("platform_width"));
        }
        double platform_length = 3;
        if (req.getParameter("platform_length") != null && !req.getParameter("platform_length").equals("")) {
            platform_length = Double.parseDouble(req.getParameter("platform_length"));
        }
        double customer = 200;
        if (req.getParameter("supplier") != null && !req.getParameter("supplier").equals("")) {
            customer = Double.parseDouble(req.getParameter("supplier"));
        }
        double goods_num = 100;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        List<Result> list = new ArrayList<>();
        Result result = Action.runLoading(carLength, transportNum, unloading_time, everyDay_unloading_time, platform_width, platform_length, customer, goods_num, utilization, unloading_time * 60 * 60 / Integer.parseInt(CarType.valueOf(carLength).getCode()));


        list.add(result);
        return getDataTable(list);
    }

    @PostMapping("/getLoadingList")
    @ResponseBody
    public TableDataInfo getLoadingList(HttpServletRequest req) {
        String carLength = req.getParameter("carlength");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double unloading_time = 0.5;
        if (req.getParameter("unloading_time") != null && !req.getParameter("unloading_time").equals("")) {
            unloading_time = Double.parseDouble(req.getParameter("unloading_time"));
        }
        double everyDay_unloading_time = 8;
        if (req.getParameter("everyDay_unloading_time") != null && !req.getParameter("everyDay_unloading_time").equals("")) {
            everyDay_unloading_time = Double.parseDouble(req.getParameter("everyDay_unloading_time"));
        }
        double platform_width = 3;
        if (req.getParameter("platform_width") != null && !req.getParameter("platform_width").equals("")) {
            platform_width = Double.parseDouble(req.getParameter("platform_width"));
        }
        double platform_length = 3;
        if (req.getParameter("platform_length") != null && !req.getParameter("platform_length").equals("")) {
            platform_length = Double.parseDouble(req.getParameter("platform_length"));
        }
        double customer = 200;
        if (req.getParameter("supplier") != null && !req.getParameter("supplier").equals("")) {
            customer = Double.parseDouble(req.getParameter("supplier"));
        }
        double goods_num = 100;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double platform_a = 6;
        if (req.getParameter("platform_a") != null && !req.getParameter("platform_a").equals("")) {
            platform_a = Double.parseDouble(req.getParameter("platform_a"));
        }
        List<Result> list = new ArrayList<>();
        for (double i = 1; i < platform_a; i += 10) {
            unloading_time = i / 60;

            Result result = Action.runLoading(carLength, transportNum, unloading_time, everyDay_unloading_time, platform_width, platform_length, customer, goods_num, utilization, i * 60 * 60 / Integer.parseInt(CarType.valueOf(carLength).getCode()));

            result.setCost(i);
            list.add(result);
        }

        return getDataTable(list);
    }


    @PostMapping("/getShelf")
    @ResponseBody
    public TableDataInfo getShelf(HttpServletRequest req) {
        String carLength = req.getParameter("carlength");
        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double unloading_time = 0.5;
        if (req.getParameter("unloading_time") != null && !req.getParameter("unloading_time").equals("")) {
            unloading_time = Double.parseDouble(req.getParameter("unloading_time"));
        }
        double everyDay_unloading_time = 8;
        if (req.getParameter("everyDay_unloading_time") != null && !req.getParameter("everyDay_unloading_time").equals("")) {
            everyDay_unloading_time = Double.parseDouble(req.getParameter("everyDay_unloading_time"));
        }
        double platform_width = 3;
        if (req.getParameter("platform_width") != null && !req.getParameter("platform_width").equals("")) {
            platform_width = Double.parseDouble(req.getParameter("platform_width"));
        }
        double platform_length = 3;
        if (req.getParameter("platform_length") != null && !req.getParameter("platform_length").equals("")) {
            platform_length = Double.parseDouble(req.getParameter("platform_length"));
        }
        double customer = 200;
        if (req.getParameter("supplier") != null && !req.getParameter("supplier").equals("")) {
            customer = Double.parseDouble(req.getParameter("supplier"));
        }
        double goods_num = 100;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double platform_a = 6;
        if (req.getParameter("platform_a") != null && !req.getParameter("platform_a").equals("")) {
            platform_a = Double.parseDouble(req.getParameter("platform_a"));
        }
        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double forklift_channel = 3;
        if (req.getParameter("forklift_channel") != null && !req.getParameter("forklift_channel").equals("")) {
            forklift_channel = Double.parseDouble(req.getParameter("forklift_channel"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }

        List<Result> list = new ArrayList<>();
        for (double i = 1; i < platform_a; i += 10) {
            unloading_time = i / 60;
            Result result = Action.runLoading(carLength, transportNum, unloading_time, everyDay_unloading_time, platform_width, platform_length, customer, goods_num, utilization, i * 60 * 60 / Integer.parseInt(CarType.valueOf(carLength).getCode()));

            result.setCost(i);
            list.add(result);
        }

        return getDataTable(list);
    }


    @PostMapping("/getTally")
    @ResponseBody
    public TableDataInfo getTally(HttpServletRequest req) {
        String carLength = "小车4米6";
        if (req.getParameter("carLength") != null && !req.getParameter("carLength").equals("")) {
            carLength = req.getParameter("carLength");
        }


        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double batch = 3;
        if (req.getParameter("batch") != null && !req.getParameter("batch").equals("")) {
            batch = Double.parseDouble(req.getParameter("batch"));
        }
        double tallyEmpCapacity = 2370;
        if (req.getParameter("tallyEmpCapacity") != null && !req.getParameter("tallyEmpCapacity").equals("")) {
            tallyEmpCapacity = Double.parseDouble(req.getParameter("tallyEmpCapacity"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double tallyTime = 80;
        if (req.getParameter("tallyTime") != null && !req.getParameter("tallyTime").equals("")) {
            tallyTime = Double.parseDouble(req.getParameter("tallyTime"));
        }
        double goods_num = 80;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        List<Result> list = new ArrayList<>();
        Result result = Action.getTally(transportNum, carLength, batch, tallyEmpCapacity, tally_channel, tray_clearance, utilization, tallyTime, goods_num);

        list.add(result);
        return getDataTable(list);
    }

    @PostMapping("/getTallyLine")
    @ResponseBody
    public TableDataInfo getTallyLine(HttpServletRequest req) {
        String carLength = "小车4米6";
        if (req.getParameter("carLength") != null && !req.getParameter("carLength").equals("")) {
            carLength = req.getParameter("carLength");
        }


        double transportNum = 2370;
        if (req.getParameter("transportNum") != null && !req.getParameter("transportNum").equals("")) {
            transportNum = Double.parseDouble(req.getParameter("transportNum"));
        }
        double batch = 3;
        if (req.getParameter("batch") != null && !req.getParameter("batch").equals("")) {
            batch = Double.parseDouble(req.getParameter("batch"));
        }
        double tallyEmpCapacity = 2370;
        if (req.getParameter("tallyEmpCapacity") != null && !req.getParameter("tallyEmpCapacity").equals("")) {
            tallyEmpCapacity = Double.parseDouble(req.getParameter("tallyEmpCapacity"));
        }
        double tally_channel = 3;
        if (req.getParameter("tally_channel") != null && !req.getParameter("tally_channel").equals("")) {
            tally_channel = Double.parseDouble(req.getParameter("tally_channel"));
        }
        double tray_clearance = 0.2;
        if (req.getParameter("tray_clearance") != null && !req.getParameter("tray_clearance").equals("")) {
            tray_clearance = Double.parseDouble(req.getParameter("tray_clearance"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double tallyTime = 80;
        if (req.getParameter("tallyTime") != null && !req.getParameter("tallyTime").equals("")) {
            tallyTime = Double.parseDouble(req.getParameter("tallyTime"));
        }
        double batchNum = 80;
        if (req.getParameter("batchNum") != null && !req.getParameter("batchNum").equals("")) {
            batchNum = Double.parseDouble(req.getParameter("batchNum"));
        }
        double goods_num = 80;
        if (req.getParameter("goods_num") != null && !req.getParameter("goods_num").equals("")) {
            goods_num = Double.parseDouble(req.getParameter("goods_num"));
        }
        List<Result> list = new ArrayList<>();
        for (int i = 1; i <= batchNum; i++) {
            Result result = Action.getTally(transportNum, carLength, i, tallyEmpCapacity, tally_channel, tray_clearance, utilization, tallyTime, goods_num);
            result.setCost(i);
            list.add(result);
        }

        return getDataTable(list);
    }

    @PostMapping("/putaway")
    @ResponseBody
    public TableDataInfo putaway(HttpServletRequest req) {

        double putawayNum = 2370;
        if (req.getParameter("putawayNum") != null && !req.getParameter("putawayNum").equals("")) {
            putawayNum = Double.parseDouble(req.getParameter("putawayNum"));
        }
        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double forklift_channel = 3;
        if (req.getParameter("forklift_channel") != null && !req.getParameter("forklift_channel").equals("")) {
            forklift_channel = Double.parseDouble(req.getParameter("forklift_channel"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double putaway_speed = 1.1;
        if (req.getParameter("putaway_speed") != null && !req.getParameter("putaway_speed").equals("")) {
            putaway_speed = Double.parseDouble(req.getParameter("putaway_speed"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<Result> list = new ArrayList<>();
        Result result = Action.getPutaway(putawayNum, storageNum, height, forklift_channel, utilization, putaway_speed, shelf_space, shelf_height);

        list.add(result);
        return getDataTable(list);
    }

    @PostMapping("/putawayList")
    @ResponseBody
    public TableDataInfo putawayList(HttpServletRequest req) {

        double putawayNum = 2370;
        if (req.getParameter("putawayNum") != null && !req.getParameter("putawayNum").equals("")) {
            putawayNum = Double.parseDouble(req.getParameter("putawayNum"));
        }
        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double forklift_channel = 3;
        if (req.getParameter("forklift_channel") != null && !req.getParameter("forklift_channel").equals("")) {
            forklift_channel = Double.parseDouble(req.getParameter("forklift_channel"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double putaway_speed = 1.1;
        if (req.getParameter("putaway_speed") != null && !req.getParameter("putaway_speed").equals("")) {
            putaway_speed = Double.parseDouble(req.getParameter("putaway_speed"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<EmpLog> list = new ArrayList<>();
        list = Action.getPutawayLog(putawayNum, storageNum, height, forklift_channel, utilization, putaway_speed, shelf_space, shelf_height);
        return getDataTable(list);
    }

    @PostMapping("/putawayOrder")
    @ResponseBody
    public TableDataInfo putawayOrder(HttpServletRequest req) {

        double putawayNum = 2370;
        if (req.getParameter("putawayNum") != null && !req.getParameter("putawayNum").equals("")) {
            putawayNum = Double.parseDouble(req.getParameter("putawayNum"));
        }
        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double forklift_channel = 3;
        if (req.getParameter("forklift_channel") != null && !req.getParameter("forklift_channel").equals("")) {
            forklift_channel = Double.parseDouble(req.getParameter("forklift_channel"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double putaway_speed = 1.1;
        if (req.getParameter("putaway_speed") != null && !req.getParameter("putaway_speed").equals("")) {
            putaway_speed = Double.parseDouble(req.getParameter("putaway_speed"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<Result> list = new ArrayList<>();
        list = Action.getPutawayOrder(putawayNum, storageNum, height, forklift_channel, utilization, putaway_speed, shelf_space, shelf_height);
        return getDataTable(list);
    }

    @PostMapping("/putawayEmp")
    @ResponseBody
    public TableDataInfo putawayEmp(HttpServletRequest req) {

        double putawayNum = 2370;
        if (req.getParameter("putawayNum") != null && !req.getParameter("putawayNum").equals("")) {
            putawayNum = Double.parseDouble(req.getParameter("putawayNum"));
        }
        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double forklift_channel = 3;
        if (req.getParameter("forklift_channel") != null && !req.getParameter("forklift_channel").equals("")) {
            forklift_channel = Double.parseDouble(req.getParameter("forklift_channel"));
        }
        double utilization = 80;
        if (req.getParameter("utilization") != null && !req.getParameter("utilization").equals("")) {
            utilization = Double.parseDouble(req.getParameter("utilization"));
        }
        double putaway_speed = 1.1;
        if (req.getParameter("putaway_speed") != null && !req.getParameter("putaway_speed").equals("")) {
            putaway_speed = Double.parseDouble(req.getParameter("putaway_speed"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<EmpLog> list = new ArrayList<>();
        list = Action.getPutawayEmp(putawayNum, storageNum, height, forklift_channel, utilization, putaway_speed, shelf_space, shelf_height);
        return getDataTable(list);
    }

    @PostMapping("/getStorage")
    @ResponseBody
    public TableDataInfo getStorage(HttpServletRequest req) {

        double storageNum = 40000;
        if (req.getParameter("storageNum") != null && !req.getParameter("storageNum").equals("")) {
            storageNum = Double.parseDouble(req.getParameter("storageNum"));
        }

        double height = 12;
        if (req.getParameter("height") != null && !req.getParameter("height").equals("")) {
            height = Double.parseDouble(req.getParameter("height"));
        }
        double forklift_channel = 3;
        if (req.getParameter("forklift_channel") != null && !req.getParameter("forklift_channel").equals("")) {
            forklift_channel = Double.parseDouble(req.getParameter("forklift_channel"));
        }
        double shelf_space = 0.2;
        if (req.getParameter("shelf_space") != null && !req.getParameter("shelf_space").equals("")) {
            shelf_space = Double.parseDouble(req.getParameter("shelf_space"));
        }
        double shelf_height = 1.5;
        if (req.getParameter("shelf_height") != null && !req.getParameter("shelf_height").equals("")) {
            shelf_height = Double.parseDouble(req.getParameter("shelf_height"));
        }
        List<Result> list = new ArrayList<>();
        Result result = Action.getStorage(storageNum, height, forklift_channel, shelf_space, shelf_height);

        list.add(result);
        return getDataTable(list);
    }













    /**
     * 根据n将集合分成两组
     **/
    public static <T> Map<Boolean, List<T>> split(List<T> list, int n) {
        return IntStream
                .range(0, list.size())
                .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, list.get(i)))
                .collect(partitioningBy(entry -> entry.getKey() < n, mapping(AbstractMap.SimpleEntry::getValue, toList())));
    }
}
