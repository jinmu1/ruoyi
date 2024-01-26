package com.ruoyi.data.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


public  class EIQBasicTable {
    @Getter
    @Setter
    @Excel(name = "出库日期")
    @JsonProperty("出库日期")
    private Date deliveryDate;
    @Getter
    @Setter
    @Excel(name = "订单编号")
    @JsonProperty("订单编号")
    private String orderNumber;
    @Getter
    @Setter
    @Excel(name = "物料编号")
    @JsonProperty("物料编号")
    private String materialNumber;
    @Getter
    @Setter
    @Excel(name = "物料名称")
    @JsonProperty("物料名称")
    private String materialName;
    @Getter
    @Setter
    @Excel(name = "出货数量")
    @JsonProperty("出货数量")
    private double deliveryQuantity;
    @Getter
    @Setter
    @Excel(name = "出货单位")
    @JsonProperty("出货单位")
    private String deliveryUnit;
    @Getter
    @Setter
    @Excel(name = "销售单价")
    @JsonProperty("销售单价")
    private double unitPrice;
    @Getter
    @Setter
    @Excel(name = "托盘装件数")
    @JsonProperty("托盘装件数")
    private double palletizedItems;
    @Getter
    @Setter
    @Excel(name = "换算单位")
    @JsonProperty("换算单位")
    private double conversionUnit;
    @Getter
    @Setter
    @Excel(name = "换算单位1")
    @JsonProperty("换算单位1")
    private double conversionUnit1;
}