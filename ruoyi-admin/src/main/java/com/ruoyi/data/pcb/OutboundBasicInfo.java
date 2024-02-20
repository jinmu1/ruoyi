package com.ruoyi.data.pcb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OutboundBasicInfo {
    @Getter
    @Setter
    @Excel(name = "出库日期")
    @JsonProperty("出库日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private String outDate;
    @Getter
    @Setter
    @Excel(name = "订单编号")
    @JsonProperty("订单编号")
    private String orderNumber;
    @Getter
    @Setter
    @Excel(name = "物料编码")
    @JsonProperty("物料编码")
    private long materialCode;
    @Getter
    @Setter
    @Excel(name = "物料名称")
    @JsonProperty("物料名称")
    private String materialName;
    @Getter
    @Setter
    @Excel(name = "出库数量（个）")
    @JsonProperty("出库数量")
    private int outQuantity;
    @Getter
    @Setter
    @Excel(name = "出货单位")
    @JsonProperty("出货单位")
    private String outUnit;
}
