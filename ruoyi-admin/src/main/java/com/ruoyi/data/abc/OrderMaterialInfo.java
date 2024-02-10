package com.ruoyi.data.abc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderMaterialInfo {
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
