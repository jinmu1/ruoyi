package com.ruoyi.web.controller.system;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/system/eiq")
public class EIQAnalyseController {

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
        ExcelUtil<Data1Item> util = new ExcelUtil<>(Data1Item.class);

        List<Data1Item> data1Entries = util.importExcel(file.getInputStream());
        // 将 "data1" 转换为数组

        List<Data1Item> list = new ArrayList<>();



        return AjaxResult.success(null);
    }
    class Data1Item {
        @Excel(name = "出库日期")
        private String deliveryDate;

        @Excel(name = "订单编号")
        private int orderNumber;

        @Excel(name = "物料编号")
        private int materialNumber;

        @Excel(name = "物料名称")
        private String materialName;

        @Excel(name = "出货数量")
        private int deliveryQuantity;

        @Excel(name = "出货单位")
        private String deliveryUnit;

        @Excel(name = "销售单价")
        private int unitPrice;

        @Excel(name = "托盘装件数")
        private int palletizedItems;

        @Excel(name = "换算单位")
        private int conversionUnit;

        @Excel(name = "换算单位1")
        private int conversionUnit1;

        // Getter and Setter methods

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getMaterialNumber() {
            return materialNumber;
        }

        public void setMaterialNumber(int materialNumber) {
            this.materialNumber = materialNumber;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public int getDeliveryQuantity() {
            return deliveryQuantity;
        }

        public void setDeliveryQuantity(int deliveryQuantity) {
            this.deliveryQuantity = deliveryQuantity;
        }

        public String getDeliveryUnit() {
            return deliveryUnit;
        }

        public void setDeliveryUnit(String deliveryUnit) {
            this.deliveryUnit = deliveryUnit;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getPalletizedItems() {
            return palletizedItems;
        }

        public void setPalletizedItems(int palletizedItems) {
            this.palletizedItems = palletizedItems;
        }

        public int getConversionUnit() {
            return conversionUnit;
        }

        public void setConversionUnit(int conversionUnit) {
            this.conversionUnit = conversionUnit;
        }

        public int getConversionUnit1() {
            return conversionUnit1;
        }

        public void setConversionUnit1(int conversionUnit1) {
            this.conversionUnit1 = conversionUnit1;
        }
    }
    class Data2Item {
        @JsonProperty("日期")
        private String date;

        @JsonProperty("E分析")
        private int eAnalysis;

        @JsonProperty("N分析")
        private int nAnalysis;

        @JsonProperty("Q分析")
        private int qAnalysis;

        // Getter and Setter methods
    }
    class Data3Item {
        @JsonProperty("订单编号")
        private int orderNumber;

        @JsonProperty("订单对应行数")
        private int orderLineCount;

        @JsonProperty("订单编号累计品目数")
        private int cumulativeItemNumber;

        // Getter and Setter methods
    }
    class Data4Item {
        @JsonProperty("订单编号")
        private int orderNumber;

        @JsonProperty("订单对应出库总数量")
        private double totalDeliveredQuantity;

        @JsonProperty("订单编号累计品目数")
        private int cumulativeItemNumber;

        // Getter and Setter methods
    }
    class Data5Item {
        @JsonProperty("订单编号")
        private int orderNumber;

        @JsonProperty("订单对应物料品种数")
        private int materialVarietiesCount;

        @JsonProperty("订单编号累计品目数")
        private int cumulativeItemNumber;

        // Getter and Setter methods
    }
    // Class for data6
    class Data6Item {
        @JsonProperty("物料名称")
        private String materialName;

        @JsonProperty("物料编码")
        private int materialCode;

        @JsonProperty("出现次数")
        private int occurrenceCount;

        @JsonProperty("物料编号累计品目数")
        private int cumulativeItemNumber;

        // Getter and Setter methods
    }
}
