package com.ruoyi.web.controller.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.poi.ExcelUtil;
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
        ExcelUtil<Data1Entry> util = new ExcelUtil<>(Data1Entry.class);

        List<Data1Entry> data1Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组

        List<Data2Entry> list = new ArrayList<>();
        // 现在你可以使用data1Entries了
        //step1：首先计算总销售金额
        for (Data1Entry entry : data1Entries) {
            Data2Entry data2Entry = new Data2Entry();
            data2Entry.setMaterialCode(entry.getMaterialCode());
            data2Entry.setUnitPrice(entry.getSellingPrice());
            data2Entry.setAverageInventory(entry.getAverageInventory());
            data2Entry.setAverageFundsOccupied(entry.getSellingPrice()*entry.getAverageInventory());
            list.add(data2Entry);
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
        for (Data2Entry data2Entry:list){
            all = all+ 1;
            allInventory = allInventory+ data2Entry.getAverageFundsOccupied();
        }
        for(Data2Entry data2Entry:list){
            occupied =occupied + data2Entry.getAverageFundsOccupied();
            data2Entry.setCumulativeAverageFundsOccupied(occupied);
            data2Entry.setCumulativeAverageFundsOccupiedPercentage(new BigDecimal(occupied*100/allInventory).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            next=next+1;
            data2Entry.setCumulativeItemNumber(next);
            data2Entry.setCumulativeItemNumberPercentage(new BigDecimal(next*100/all).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
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
        ExcelUtil<Data5Entry> util = new ExcelUtil<>(Data5Entry.class);

        List<Data5Entry> data5Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组


        // 使用流式操作和Collectors按照物料编码属性分类
        Map<String, List<Data5Entry>> categorizedMap = data5Entries.stream()
                .collect(Collectors.groupingBy(Data5Entry::getMaterialNumber));

        // 输出分类结果
        List<Data3Entry> data3Entries= new ArrayList<>();
        List<Data4Entry> data4Entries =new ArrayList<>();
        int tatol = categorizedMap.size();//总物料数量
        int AllOutboundFrequency =0;//总出库频次
        double AllShippedQuantity = 0.0;//总出库数量
        for(String key : categorizedMap.keySet()){
            List<Data5Entry> data5EntryList1 = categorizedMap.get(key);
            Data3Entry data3Entry = new Data3Entry();
            Data4Entry data4Entry = new Data4Entry();
            data3Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
            data4Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
            data4Entry.setMaterialDescription(data5EntryList1.get(0).getMaterialName());
            int count = 0;//出库频次统计
            double num = 0.0;//出库数量统计
            for(Data5Entry data5Entry:data5EntryList1){
                count++;
                num = num+data5Entry.getShippedQuantity();
            }
            AllOutboundFrequency+=count;
            AllShippedQuantity+=num;
            data3Entry.setOutboundFrequency(count);
            data4Entry.setOutboundQuantity(num);
            data3Entries.add(data3Entry);
            data4Entries.add(data4Entry);
        }
        Collections.sort(data3Entries,(m1, m2) -> Double.compare(m2.getOutboundFrequency(),m1.getOutboundFrequency()));//按照出库频次降序排序

        int frequency =0;//累计频次
        int num1=0;
        for(Data3Entry data3Entry:data3Entries){
            num1 ++;
            frequency +=data3Entry.getOutboundFrequency();
            data3Entry.setCumulativeOutboundFrequency(frequency);
            data3Entry.setCumulativeOutboundFrequencyPercentage(new BigDecimal(frequency*100/AllOutboundFrequency).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            data3Entry.setCumulativeItemCount(num1);
            data3Entry.setCumulativeItemCountPercentage(new BigDecimal(num1*100/tatol).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
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
        ExcelUtil<Data5Entry> util = new ExcelUtil<>(Data5Entry.class);

        List<Data5Entry> data5Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组


        // 使用流式操作和Collectors按照物料编码属性分类
        Map<String, List<Data5Entry>> categorizedMap = data5Entries.stream()
                .collect(Collectors.groupingBy(Data5Entry::getMaterialNumber));

        // 输出分类结果

        List<Data4Entry> data4Entries =new ArrayList<>();
        int tatol = categorizedMap.size();//总物料数量
        int AllOutboundFrequency =0;//总出库频次
        double AllShippedQuantity = 0.0;//总出库数量
        for(String key : categorizedMap.keySet()){
            List<Data5Entry> data5EntryList1 = categorizedMap.get(key);
            Data3Entry data3Entry = new Data3Entry();
            Data4Entry data4Entry = new Data4Entry();
            data3Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
            data4Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
            data4Entry.setMaterialDescription(data5EntryList1.get(0).getMaterialName());
            int count = 0;//出库频次统计
            double num = 0.0;//出库数量统计
            for(Data5Entry data5Entry:data5EntryList1){
                count++;
                num = num+data5Entry.getShippedQuantity();
            }
            AllOutboundFrequency+=count;
            AllShippedQuantity+=num;
            data4Entry.setOutboundQuantity(num);
            data4Entries.add(data4Entry);
        }

        Collections.sort(data4Entries,(m1, m2) -> Double.compare(m2.getOutboundQuantity(),m1.getOutboundQuantity()));//按照出库数量降序排序


        int num2=0;//排行
        double quantity = 0.0;//累计数量
        for (Data4Entry data4Entry:data4Entries){
            num2 ++;
            quantity +=data4Entry.getOutboundQuantity();
            data4Entry.setCumulativeOutboundQuantity(quantity);
            data4Entry.setCumulativeOutboundQuantityPercentage(new BigDecimal(quantity*100/AllShippedQuantity).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
            data4Entry.setCumulativeItemCount(num2);
            data4Entry.setCumulativeItemCountPercentage(new BigDecimal(num2*100/tatol).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString()+"%");
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


    static public class Data1Entry {
        @Excel(name = "物料编码")
        private String materialCode;

        @Excel(name = "平均库存（件/月）")
        private int averageInventory;

        @Excel(name = "当月出库总量（件）")
        private double monthlyTotalOutbound;

        @Excel(name = "库存周转次数")
        private int inventoryTurnover;

        @Excel(name = "库存周转天数")
        private int inventoryTurnoverDays;

        @Excel(name = "销售单价（元/件）")
        private double sellingPrice;

        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public int getAverageInventory() {
            return averageInventory;
        }

        public void setAverageInventory(int averageInventory) {
            this.averageInventory = averageInventory;
        }

        public double getMonthlyTotalOutbound() {
            return monthlyTotalOutbound;
        }

        public void setMonthlyTotalOutbound(double monthlyTotalOutbound) {
            this.monthlyTotalOutbound = monthlyTotalOutbound;
        }

        public int getInventoryTurnover() {
            return inventoryTurnover;
        }

        public void setInventoryTurnover(int inventoryTurnover) {
            this.inventoryTurnover = inventoryTurnover;
        }

        public int getInventoryTurnoverDays() {
            return inventoryTurnoverDays;
        }

        public void setInventoryTurnoverDays(int inventoryTurnoverDays) {
            this.inventoryTurnoverDays = inventoryTurnoverDays;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        @Override
        public String toString() {
            return "Data1Entry{" +
                    "materialCode=" + materialCode +
                    ", averageInventory=" + averageInventory +
                    ", monthlyTotalOutbound=" + monthlyTotalOutbound +
                    ", inventoryTurnover=" + inventoryTurnover +
                    ", inventoryTurnoverDays=" + inventoryTurnoverDays +
                    ", sellingPrice=" + sellingPrice +
                    '}';
        }
    }
    static public class Data2Entry {
        @Excel(name = "物料编码")
        private String materialCode;

        @Excel(name = "销售单价")
        private double unitPrice;

        @Excel(name = "平均库存")
        private double averageInventory;

        @Excel(name = "平均资金占用额")
        private double averageFundsOccupied;

        @Excel(name = "平均资金占用额累计")
        private double cumulativeAverageFundsOccupied;

        @Excel(name = "平均资金占用额累计百分比")
        private String cumulativeAverageFundsOccupiedPercentage;

        @Excel(name = "物料累计品目数")
        private int cumulativeItemNumber;

        @Excel(name = "物料累计品目数百分比")
        private String cumulativeItemNumberPercentage;

        // 添加其他字段...

        // 添加构造函数、getter和setter方法...


        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getAverageInventory() {
            return averageInventory;
        }

        public void setAverageInventory(double averageInventory) {
            this.averageInventory = averageInventory;
        }

        public double getAverageFundsOccupied() {
            return averageFundsOccupied;
        }

        public void setAverageFundsOccupied(double averageFundsOccupied) {
            this.averageFundsOccupied = averageFundsOccupied;
        }

        public double getCumulativeAverageFundsOccupied() {
            return cumulativeAverageFundsOccupied;
        }

        public void setCumulativeAverageFundsOccupied(double cumulativeAverageFundsOccupied) {
            this.cumulativeAverageFundsOccupied = cumulativeAverageFundsOccupied;
        }

        public String getCumulativeAverageFundsOccupiedPercentage() {
            return cumulativeAverageFundsOccupiedPercentage;
        }

        public void setCumulativeAverageFundsOccupiedPercentage(String cumulativeAverageFundsOccupiedPercentage) {
            this.cumulativeAverageFundsOccupiedPercentage = cumulativeAverageFundsOccupiedPercentage;
        }

        public int getCumulativeItemNumber() {
            return cumulativeItemNumber;
        }

        public void setCumulativeItemNumber(int cumulativeItemNumber) {
            this.cumulativeItemNumber = cumulativeItemNumber;
        }

        public String getCumulativeItemNumberPercentage() {
            return cumulativeItemNumberPercentage;
        }

        public void setCumulativeItemNumberPercentage(String cumulativeItemNumberPercentage) {
            this.cumulativeItemNumberPercentage = cumulativeItemNumberPercentage;
        }
    }
    static public class Data3Entry{
        @Excel(name = "物料编码")
        private String materialCode;

        @Excel(name = "出库频次")
        private int outboundFrequency;

        @Excel(name = "累计出库频次")
        private int cumulativeOutboundFrequency;

        @Excel(name = "累计出库频次百分比")
        private String cumulativeOutboundFrequencyPercentage;

        @Excel(name = "物料累计品目数")
        private int cumulativeItemCount;

        @Excel(name = "物料累计品目数百分比")
        private String cumulativeItemCountPercentage;

        // 添加构造函数、getter和setter方法...


        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public int getOutboundFrequency() {
            return outboundFrequency;
        }

        public void setOutboundFrequency(int outboundFrequency) {
            this.outboundFrequency = outboundFrequency;
        }

        public int getCumulativeOutboundFrequency() {
            return cumulativeOutboundFrequency;
        }

        public void setCumulativeOutboundFrequency(int cumulativeOutboundFrequency) {
            this.cumulativeOutboundFrequency = cumulativeOutboundFrequency;
        }

        public String getCumulativeOutboundFrequencyPercentage() {
            return cumulativeOutboundFrequencyPercentage;
        }

        public void setCumulativeOutboundFrequencyPercentage(String cumulativeOutboundFrequencyPercentage) {
            this.cumulativeOutboundFrequencyPercentage = cumulativeOutboundFrequencyPercentage;
        }

        public int getCumulativeItemCount() {
            return cumulativeItemCount;
        }

        public void setCumulativeItemCount(int cumulativeItemCount) {
            this.cumulativeItemCount = cumulativeItemCount;
        }

        public String getCumulativeItemCountPercentage() {
            return cumulativeItemCountPercentage;
        }

        public void setCumulativeItemCountPercentage(String cumulativeItemCountPercentage) {
            this.cumulativeItemCountPercentage = cumulativeItemCountPercentage;
        }
    }
    static public class Data4Entry{
        @Excel(name = "物料编码")
        private String materialCode;

        @Excel(name = "物料描述")
        private String materialDescription;

        @Excel(name = "出库量")
        private double outboundQuantity;

        @Excel(name = "累计出库量")
        private double cumulativeOutboundQuantity;

        @Excel(name = "累计出库量百分比")
        private String cumulativeOutboundQuantityPercentage;

        @Excel(name = "物料累计品目数")
        private int cumulativeItemCount;

        @Excel(name = "物料累计品目数百分比")
        private String cumulativeItemCountPercentage;

        // 添加构造函数、getter和setter方法...


        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public String getMaterialDescription() {
            return materialDescription;
        }

        public void setMaterialDescription(String materialDescription) {
            this.materialDescription = materialDescription;
        }

        public double getOutboundQuantity() {
            return outboundQuantity;
        }

        public void setOutboundQuantity(double outboundQuantity) {
            this.outboundQuantity = outboundQuantity;
        }

        public double getCumulativeOutboundQuantity() {
            return cumulativeOutboundQuantity;
        }

        public void setCumulativeOutboundQuantity(double cumulativeOutboundQuantity) {
            this.cumulativeOutboundQuantity = cumulativeOutboundQuantity;
        }

        public String getCumulativeOutboundQuantityPercentage() {
            return cumulativeOutboundQuantityPercentage;
        }

        public void setCumulativeOutboundQuantityPercentage(String cumulativeOutboundQuantityPercentage) {
            this.cumulativeOutboundQuantityPercentage = cumulativeOutboundQuantityPercentage;
        }

        public int getCumulativeItemCount() {
            return cumulativeItemCount;
        }

        public void setCumulativeItemCount(int cumulativeItemCount) {
            this.cumulativeItemCount = cumulativeItemCount;
        }

        public String getCumulativeItemCountPercentage() {
            return cumulativeItemCountPercentage;
        }

        public void setCumulativeItemCountPercentage(String cumulativeItemCountPercentage) {
            this.cumulativeItemCountPercentage = cumulativeItemCountPercentage;
        }
    }
    static public class Data5Entry{
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
        @Excel(name = "出库日期")
        private Date deliveryDate;

        @Excel(name = "订单编号")
        private long orderNumber;

        @Excel(name = "物料编号")
        private String materialNumber;

        @Excel(name = "物料名称")
        private String materialName;

        @Excel(name = "出货数量")
        private double shippedQuantity;

        @Excel(name = "出货单位")
        private String shipmentUnit;

        @Excel(name = "销售单价（元/件）")
        private double unitPrice;

        @Excel(name = "托盘装件数")
        private double palletizedItemCount;

        // 添加构造函数、getter和setter方法...


        public Date getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(Date deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public long getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(long orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getMaterialNumber() {
            return materialNumber;
        }

        public void setMaterialNumber(String materialNumber) {
            this.materialNumber = materialNumber;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public double getShippedQuantity() {
            return shippedQuantity;
        }

        public void setShippedQuantity(double shippedQuantity) {
            this.shippedQuantity = shippedQuantity;
        }

        public String getShipmentUnit() {
            return shipmentUnit;
        }

        public void setShipmentUnit(String shipmentUnit) {
            this.shipmentUnit = shipmentUnit;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getPalletizedItemCount() {
            return palletizedItemCount;
        }

        public void setPalletizedItemCount(double palletizedItemCount) {
            this.palletizedItemCount = palletizedItemCount;
        }
    }


}
