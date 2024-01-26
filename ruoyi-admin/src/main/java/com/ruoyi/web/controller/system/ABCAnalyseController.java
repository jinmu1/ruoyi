package com.ruoyi.web.controller.system;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.data.abc.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system/abc")
public class ABCAnalyseController  extends BaseController{


    @PostMapping("/importStaticData")
    @ResponseBody
    public AjaxResult importStaticData() throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        String filePath =
                getClass().getResource("/static/file/abc.json").getPath();;
        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        List<InventoryInfoTable>  tableList = new ArrayList<>();
        List<OutboundTable>  outboundTables = new ArrayList<>();
        try {
            /**
             * 获取老师给的数据，将数据计算后转换位对象Data2Entry
             */
            // 读取整个JSON文件为JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            // 获取 "data1" 部分
            JsonNode data1Node = rootNode.get("data1");
            // 将 "data1" 转换为数组
            InventoryInfoTable[] tables = objectMapper.treeToValue(data1Node, InventoryInfoTable[].class);
            tableList = Arrays.asList(tables.clone());
            JsonNode data5Node = rootNode.get("data5");
            OutboundTable[] outboundTables1 = objectMapper.treeToValue(data5Node, OutboundTable[].class);
            outboundTables = Arrays.asList(outboundTables1.clone());
            /***
             * 将转换后的数据穿给Json对象
             */
        }catch (Exception e){

        }
        // 将 "data1" 转换为数组

        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data1", praseJson1(tableList)); // 将导入的数据放到data1中
        json.put("data2", praseJson2(getDate2(tableList))); // 将转换后的数据放到data2中
        json.put("data3", praseJson3(getDate3(outboundTables))); // 将导入的数据放到data1中
        json.put("data4", praseJson4(getDate4(outboundTables))); // 将转换后的数据放到data2中
        json.put("data5", praseJson5(outboundTables)); // 将转换后的数据放到data2中
        return AjaxResult.success(json);
    }
    /**
     * ABC出库金额分析的导入数据
     */
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        ExcelUtil<InventoryInfoTable> util = new ExcelUtil<>(InventoryInfoTable.class);

        List<InventoryInfoTable> data1Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组

        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data1", praseJson1(data1Entries)); // 将导入的数据放到data1中
        json.put("data2", praseJson2(getDate2(data1Entries))); // 将转换后的数据放到data2中
        return AjaxResult.success(json);
    }
    /**
     * ABC频次和数量分析的导入数据
     */
    @PostMapping("/importData1")
    @ResponseBody
    public AjaxResult importData1(MultipartFile file) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        ExcelUtil<OutboundTable> util = new ExcelUtil<>(OutboundTable.class);
        List<OutboundTable> data5Entries = util.importExcel(file.getInputStream());
        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data3", praseJson3(getDate3(data5Entries))); // 将导入的数据放到data1中
        json.put("data5", praseJson5(data5Entries)); // 将转换后的数据放到data2中
        return AjaxResult.success(json);
    }
    /**
     * ABC频次和数量分析的导入数据
     */
    @PostMapping("/importData2")
    @ResponseBody
    public AjaxResult importData2(MultipartFile file) throws Exception
    {
        /**
         *将导入的EXCEL文件转换为List对象，
         * todo:返回导入和转换的错误参数
         */
        ExcelUtil<OutboundTable> util = new ExcelUtil<>(OutboundTable.class);
        List<OutboundTable> data5Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组
        // 使用流式操作和Collectors按照物料编码属性分类
        JSONObject json = new JSONObject(); // 创建一个空的JSON对象
        json.put("data4", praseJson4(getDate4(data5Entries))); // 将转换后的数据放到data2中
        json.put("data5", praseJson5(data5Entries)); // 将转换后的数据放到data2中
        return AjaxResult.success(json);
    }

    /***
     * ABC分析中累计出库金额分析
     * @param data1Entries
     * @return
     */
    public List<InventoryInfo> getDate2(List<InventoryInfoTable> data1Entries){
        List<InventoryInfo> list = new ArrayList<>();
        // 现在你可以使用data1Entries了
        //step1：首先计算总销售金额
        for (InventoryInfoTable entry : data1Entries) {
            InventoryInfo InventoryInfo = new InventoryInfo();
            InventoryInfo.setMaterialCode(entry.getMaterialCode());
            InventoryInfo.setUnitPrice(entry.getSellingPrice());
            InventoryInfo.setAverageInventory(entry.getAverageInventory());
            InventoryInfo.setAverageFundsOccupied(entry.getSellingPrice()*entry.getAverageInventory());
            list.add(InventoryInfo);
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
        for (InventoryInfo InventoryInfo :list){
            all = all+ 1;
            allInventory = allInventory+ InventoryInfo.getAverageFundsOccupied();
        }
        for(InventoryInfo InventoryInfo :list){
            occupied =occupied + InventoryInfo.getAverageFundsOccupied();
            InventoryInfo.setCumulativeAverageFundsOccupied(occupied);
            InventoryInfo.setCumulativeAverageFundsOccupiedPercentage(new BigDecimal(occupied*100/allInventory).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            next=next+1;
            InventoryInfo.setCumulativeItemNumber(next);
            InventoryInfo.setCumulativeItemNumberPercentage(new BigDecimal(next*100/all).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
        }
        return list;
    }

    /***
     * 将原始表单转换为ABC频次分析表单
     * @param data5Entries
     * @return
     */
    public List<OutboundFrequencyTable> getDate3(List<OutboundTable> data5Entries){
        // 将 "data1" 转换为数组
        // 使用流式操作和Collectors按照物料编码属性分类
        Map<String, List<OutboundTable>> categorizedMap = data5Entries.stream()
                .collect(Collectors.groupingBy(OutboundTable::getMaterialNumber));
        // 输出分类结果
        List<OutboundFrequencyTable> data3Entries= new ArrayList<>();

        int tatol = categorizedMap.size();//总物料数量
        int AllOutboundFrequency =0;//总出库频次
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
            outboundFrequencyTable.setOutboundFrequency(count);
            outboundQuantityTable.setOutboundQuantity(num);
            data3Entries.add(outboundFrequencyTable);
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
        return data3Entries;
    }
    public List<OutboundQuantityTable> getDate4(List<OutboundTable> data5Entries){
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
        return data4Entries;
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson1(List<InventoryInfoTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson2(List<InventoryInfo> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson3(List<OutboundFrequencyTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson4(List<OutboundQuantityTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
    /**
     * 将List对象数据转换为字符串
     * @param data
     * @return
     */
    private String praseJson5(List<OutboundTable> data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(data);
    }
}
