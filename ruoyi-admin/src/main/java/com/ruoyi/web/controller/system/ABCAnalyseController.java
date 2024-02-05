package com.ruoyi.web.controller.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.baidie.BaidieProcessor;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.web.controller.utils.BaidieUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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
    public AjaxResult importData(MultipartFile file, boolean updateSupport, HttpServletRequest request)
    {
        try {
            final Map<String, List<?>> resultKeyToDataArrays = BaidieProcessor.importFileForGroupOne(file);
            final JSONObject json = BaidieUtils.generateResponseJson(resultKeyToDataArrays);

            return AjaxResult.success(json);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * ABC频次和数量分析的导入数据
     */
    @PostMapping("/importData1")
    @ResponseBody
    public AjaxResult importData1(MultipartFile file, boolean updateSupport, HttpServletRequest request)
    {
        try {
            final Map<String, List<?>> resultKeyToDataArrays = BaidieProcessor.importABCGroupTwo(file);
            final JSONObject json = BaidieUtils.generateResponseJson(resultKeyToDataArrays);
            return AjaxResult.success(json);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * ABC频次和数量分析的导入数据
     */
    @PostMapping("/importData2")
    @ResponseBody
    public AjaxResult importData2(MultipartFile file, boolean updateSupport, HttpServletRequest request) {
        try {
            // 将导入的EXCEL文件转换为List对象，可能会throw exception.
            List<Data5Entry> data5Entries = BaidieUtils.parseFromExcelFile(file, Data5Entry.class);

            // 使用流式操作和Collectors按照物料编码属性分类
            Map<String, List<Data5Entry>> categorizedMap = data5Entries.stream()
                    .collect(Collectors.groupingBy(Data5Entry::getMaterialNumber));

            // 输出分类结果

            List<Data4Entry> data4Entries = new ArrayList<>();
            int tatol = categorizedMap.size();//总物料数量
            int AllOutboundFrequency = 0;//总出库频次
            double AllShippedQuantity = 0.0;//总出库数量
            for (String key : categorizedMap.keySet()) {
                List<Data5Entry> data5EntryList1 = categorizedMap.get(key);
                Data3Entry data3Entry = new Data3Entry();
                Data4Entry data4Entry = new Data4Entry();
                data3Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
                data4Entry.setMaterialCode(data5EntryList1.get(0).getMaterialNumber());
                data4Entry.setMaterialDescription(data5EntryList1.get(0).getMaterialName());
                int count = 0;//出库频次统计
                double num = 0.0;//出库数量统计
                for (Data5Entry data5Entry : data5EntryList1) {
                    count++;
                    num = num + data5Entry.getShippedQuantity();
                }
                AllOutboundFrequency += count;
                AllShippedQuantity += num;
                data4Entry.setOutboundQuantity(num);
                data4Entries.add(data4Entry);
            }

            Collections.sort(data4Entries, (m1, m2) -> Double.compare(m2.getOutboundQuantity(), m1.getOutboundQuantity()));//按照出库数量降序排序


            int num2 = 0;//排行
            double quantity = 0.0;//累计数量
            for (Data4Entry data4Entry : data4Entries) {
                num2++;
                quantity += data4Entry.getOutboundQuantity();
                data4Entry.setCumulativeOutboundQuantity(quantity);
                data4Entry.setCumulativeOutboundQuantityPercentage(new BigDecimal(quantity * 100 / AllShippedQuantity).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
                data4Entry.setCumulativeItemCount(num2);
                data4Entry.setCumulativeItemCountPercentage(new BigDecimal(num2 * 100 / tatol).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
            }
            JSONObject json = BaidieUtils.generateResponseJson(new HashMap<>(){{
                put("data4", data4Entries);
                put("data5", data5Entries);
            }});
            return AjaxResult.success(json);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    static public class Data1Entry {
        @Excel(name = "物料编码")
        @JsonProperty("物料编码")
        private String materialCode;
        @JsonProperty("平均库存")
        @Excel(name = "平均库存（件/月）")
        private int averageInventory;
        @JsonProperty("当月出库总量")
        @Excel(name = "当月出库总量（件）")
        private double monthlyTotalOutbound;
        @JsonProperty("库存周转次数")
        @Excel(name = "库存周转次数")
        private int inventoryTurnover;
        @JsonProperty("库存周转天数")
        @Excel(name = "库存周转天数")
        private int inventoryTurnoverDays;
        @JsonProperty("销售单价")
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
    @Getter
    @Setter
    static public class Data2Entry {
        @JsonProperty("物料编码")
        @NotNull
        private String materialCode;
        @JsonProperty("销售单价")
        @NotNull
        private Double unitPrice;
        @JsonProperty("平均库存")
        @NotNull
        private Integer averageInventory;
        @JsonProperty("平均资金占用额")
        @NotNull
        private Double averageFundsOccupied;
        @JsonProperty("平均资金占用额累计")
        @NotNull
        private Double cumulativeAverageFundsOccupied;
        @JsonProperty("平均资金占用额累计百分比")
        @NotNull
        private String cumulativeAverageFundsOccupiedPercentage;
        @JsonProperty("物料累计品目数")
        @NotNull
        private Integer cumulativeItemNumber;
        @JsonProperty("物料累计品目数百分比")
        @NotNull
        private String cumulativeItemNumberPercentage;
    }
    @Getter
    @Setter
    static public class Data3Entry{
        @JsonProperty("物料编码")
        @NotNull
        private String materialCode;
        @JsonProperty("出库频次")
        @NotNull
        private int outboundFrequency;
        @JsonProperty("累计出库频次")
        @NotNull
        private Integer cumulativeOutboundFrequency;
        @JsonProperty("累计出库频次百分比")
        @NotNull
        private String cumulativeOutboundFrequencyPercentage;
        @JsonProperty("物料累计品目数")
        @NotNull
        private Integer cumulativeItemCount;
        @JsonProperty("物料累计品目数百分比")
        @NotNull
        private String cumulativeItemCountPercentage;
    }
    static public class Data4Entry{
        @JsonProperty("物料编码")
        @Excel(name = "物料编码")
        private String materialCode;
        @JsonProperty("物料描述")
        @Excel(name = "物料描述")
        private String materialDescription;
        @JsonProperty("出库量")
        @Excel(name = "出库量")
        private double outboundQuantity;
        @JsonProperty("累计出库量")
        @Excel(name = "累计出库量")
        private double cumulativeOutboundQuantity;
        @JsonProperty("累计出库量百分比")
        @Excel(name = "累计出库量百分比")
        private String cumulativeOutboundQuantityPercentage;
        @JsonProperty("物料累计品目数")
        @Excel(name = "物料累计品目数")
        private int cumulativeItemCount;
        @JsonProperty("物料累计品目数百分比")
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
        @JsonProperty("出库日期")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
        @Excel(name = "出库日期")
        private Date deliveryDate;
        @JsonProperty("订单编号")
        @Excel(name = "订单编号")
        private long orderNumber;
        @JsonProperty("物料编号")
        @Excel(name = "物料编号")
        private String materialNumber;
        @JsonProperty("物料名称")
        @Excel(name = "物料名称")
        private String materialName;
        @JsonProperty("出货数量")
        @Excel(name = "出货数量")
        private double shippedQuantity;
        @JsonProperty("出货单位")
        @Excel(name = "出货单位")
        private String shipmentUnit;
        @JsonProperty("销售单价")
        @Excel(name = "销售单价（元/件）")
        private double unitPrice;
        @JsonProperty("托盘装件数")
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
