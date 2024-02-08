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
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            final Map<String, List<?>> resultKeyToDataArrays = BaidieProcessor.importABCGroupOne(file);
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
            final Map<String, List<?>> resultKeyToDataArrays = BaidieProcessor.importABCGroupThree(file);
            final JSONObject json = BaidieUtils.generateResponseJson(resultKeyToDataArrays);
            return AjaxResult.success(json);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
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
    @Getter
    @Setter
    static public class Data4Entry{
        @JsonProperty("物料编码")
        @NotNull
        private String materialCode;
        @JsonProperty("物料描述")
        @NotNull
        private String materialDescription;
        @JsonProperty("出库量")
        @NotNull
        private Double outboundQuantity;
        @JsonProperty("累计出库量")
        @NotNull
        private Double cumulativeOutboundQuantity;
        @JsonProperty("累计出库量百分比")
        @NotNull
        private String cumulativeOutboundQuantityPercentage;
        @JsonProperty("物料累计品目数")
        @NotNull
        private Integer cumulativeItemCount;
        @JsonProperty("物料累计品目数百分比")
        @NotNull
        private String cumulativeItemCountPercentage;
    }
    @Getter
    @Setter
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
    }
}
