package com.ruoyi.web.controller.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.data.abc.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system/abc")
public class ABCAnalyseController {


    /**
     * ABC出库金额分析的导入数据
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        ExcelUtil<InventoryInfoTable> util = new ExcelUtil<>(InventoryInfoTable.class);

        List<InventoryInfoTable> data1Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组

        List<InventoryInfo> list = new ArrayList<>();
        // 现在你可以使用data1Entries了
        //step1：首先计算总销售金额
        for (InventoryInfoTable entry : data1Entries) {
            InventoryInfo inventoryInfo = new InventoryInfo();
            inventoryInfo.setMaterialCode(entry.getMaterialCode());
            inventoryInfo.setUnitPrice(entry.getSellingPrice());
            inventoryInfo.setAverageInventory(entry.getAverageInventory());
            inventoryInfo.setAverageFundsOccupied(entry.getSellingPrice()*entry.getAverageInventory());
            list.add(inventoryInfo);
        }
        //step2：对总销售金额进行降序排序
        Collections.sort(list,(m1, m2) -> Double.compare(m2.getAverageFundsOccupied(),m1.getAverageFundsOccupied()));
        //step3：对降序排序后的占比进行计算，下面都是计算过程
        double occupied=0;
        int next =0;
        double allInventory = 0d;
        int all = 0;
        /**
         * all：统计总数据条数
         * allInventory：统计数据总的库存金额
         */
        for (InventoryInfo inventoryInfo :list){
            all = all+ 1;
            allInventory = allInventory+ inventoryInfo.getAverageFundsOccupied();
        }
        for(InventoryInfo inventoryInfo :list){
            occupied =occupied + inventoryInfo.getAverageFundsOccupied();
            inventoryInfo.setCumulativeAverageFundsOccupied(occupied);
            inventoryInfo.setCumulativeAverageFundsOccupiedPercentage(new BigDecimal(occupied*100/allInventory).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            next=next+1;
            inventoryInfo.setCumulativeItemNumber(next);
            inventoryInfo.setCumulativeItemNumberPercentage(new BigDecimal(next*100/all).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(data1Entries);
        ObjectMapper objectMapper1 = new ObjectMapper();
        String jsonString1 = objectMapper1.writeValueAsString(list);
        JSONObject json = new JSONObject(); // 创建一个空的JSON对象

        json.put("data1", jsonString); // 将导入的数据放到data1中
        json.put("data2", jsonString1); // 将转换后的数据放到data2中

        return AjaxResult.success(json);
    }

    /**
     * ABC频次和数量分析的导入数据
     */
    @PostMapping("/importData1")
    @ResponseBody
    public AjaxResult importData1(MultipartFile file, boolean updateSupport, HttpServletRequest request) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        ExcelUtil<OutboundTable> util = new ExcelUtil<>(OutboundTable.class);

        List<OutboundTable> data5Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组


        // 使用流式操作和Collectors按照物料编码属性分类
        Map<String, List<OutboundTable>> categorizedMap = data5Entries.stream()
                .collect(Collectors.groupingBy(OutboundTable::getMaterialNumber));

        // 输出分类结果
        List<OutboundFrequencyTable> data3Entries= new ArrayList<>();
        List<OutboundQuantityTable> data4Entries =new ArrayList<>();
        int tatol = categorizedMap.size();//总物料数量
        int AllOutboundFrequency =0;//总出库频次
        double AllShippedQuantity = 0.0;//总出库数量
        for(String key : categorizedMap.keySet()){
            List<OutboundTable> outboundTableList1 = categorizedMap.get(key);
            OutboundFrequencyTable outboundFrequencyTable = new OutboundFrequencyTable();
            OutboundQuantityTable outboundQuantityTable = new OutboundQuantityTable();
            outboundFrequencyTable.setMaterialCode(outboundTableList1.get(0).getMaterialNumber());
            outboundQuantityTable.setMaterialCode(outboundTableList1.get(0).getMaterialNumber());
            outboundQuantityTable.setMaterialDescription(outboundTableList1.get(0).getMaterialName());
            int count = 0;//出库频次统计
            double num = 0.0;//出库数量统计
            for(OutboundTable outboundTable : outboundTableList1){
                count++;
                num = num+ outboundTable.getShippedQuantity();
            }
            AllOutboundFrequency+=count;
            AllShippedQuantity+=num;
            outboundFrequencyTable.setOutboundFrequency(count);
            outboundQuantityTable.setOutboundQuantity(num);
            data3Entries.add(outboundFrequencyTable);
            data4Entries.add(outboundQuantityTable);
        }
        Collections.sort(data3Entries,(m1, m2) -> Double.compare(m2.getOutboundFrequency(),m1.getOutboundFrequency()));//按照出库频次降序排序

        int frequency =0;//累计频次
        int num1=0;
        for(OutboundFrequencyTable outboundFrequencyTable :data3Entries){
            num1 ++;
            frequency += outboundFrequencyTable.getOutboundFrequency();
            outboundFrequencyTable.setCumulativeOutboundFrequency(frequency);
            outboundFrequencyTable.setCumulativeOutboundFrequencyPercentage(new BigDecimal(frequency*100/AllOutboundFrequency).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            outboundFrequencyTable.setCumulativeItemCount(num1);
            outboundFrequencyTable.setCumulativeItemCountPercentage(new BigDecimal(num1*100/tatol).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
        }


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(data5Entries);
        ObjectMapper objectMapper2 = new ObjectMapper();
        String jsonString2 = objectMapper2.writeValueAsString(data3Entries);
        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data3", jsonString2); // 将导入的数据放到data1中
        json.put("data5", jsonString); // 将转换后的数据放到data2中
        return AjaxResult.success(json);
    }
    /**
     * ABC频次和数量分析的导入数据
     */
    @PostMapping("/importData2")
    @ResponseBody
    public AjaxResult importData2(MultipartFile file, boolean updateSupport, HttpServletRequest request) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        ExcelUtil<OutboundTable> util = new ExcelUtil<>(OutboundTable.class);

        List<OutboundTable> data5Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组


        // 使用流式操作和Collectors按照物料编码属性分类
        Map<String, List<OutboundTable>> categorizedMap = data5Entries.stream()
                .collect(Collectors.groupingBy(OutboundTable::getMaterialNumber));

        // 输出分类结果

        List<OutboundQuantityTable> data4Entries =new ArrayList<>();
        int tatol = categorizedMap.size();//总物料数量
        int AllOutboundFrequency =0;//总出库频次
        double AllShippedQuantity = 0.0;//总出库数量
        for(String key : categorizedMap.keySet()){
            List<OutboundTable> outboundTableList1 = categorizedMap.get(key);
            OutboundFrequencyTable outboundFrequencyTable = new OutboundFrequencyTable();
            OutboundQuantityTable outboundQuantityTable = new OutboundQuantityTable();
            outboundFrequencyTable.setMaterialCode(outboundTableList1.get(0).getMaterialNumber());
            outboundQuantityTable.setMaterialCode(outboundTableList1.get(0).getMaterialNumber());
            outboundQuantityTable.setMaterialDescription(outboundTableList1.get(0).getMaterialName());
            int count = 0;//出库频次统计
            double num = 0.0;//出库数量统计
            for(OutboundTable outboundTable : outboundTableList1){
                count++;
                num = num+ outboundTable.getShippedQuantity();
            }
            AllOutboundFrequency+=count;
            AllShippedQuantity+=num;
            outboundQuantityTable.setOutboundQuantity(num);
            data4Entries.add(outboundQuantityTable);
        }

        Collections.sort(data4Entries,(m1, m2) -> Double.compare(m2.getOutboundQuantity(),m1.getOutboundQuantity()));//按照出库数量降序排序


        int num2=0;//排行
        double quantity = 0.0;//累计数量
        for (OutboundQuantityTable outboundQuantityTable :data4Entries){
            num2 ++;
            quantity += outboundQuantityTable.getOutboundQuantity();
            outboundQuantityTable.setCumulativeOutboundQuantity(quantity);
            outboundQuantityTable.setCumulativeOutboundQuantityPercentage(new BigDecimal(quantity*100/AllShippedQuantity).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            outboundQuantityTable.setCumulativeItemCount(num2);
            outboundQuantityTable.setCumulativeItemCountPercentage(new BigDecimal(num2*100/tatol).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(data5Entries);
        ObjectMapper objectMapper2 = new ObjectMapper();
        String jsonString2 = objectMapper2.writeValueAsString(data4Entries);
        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data4", jsonString2); // 将转换后的数据放到data2中
        json.put("data5", jsonString); // 将转换后的数据放到data2中
        return AjaxResult.success(json);
    }
}
