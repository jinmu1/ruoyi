package com.ruoyi.data.abc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialBasicInfo {
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
}
