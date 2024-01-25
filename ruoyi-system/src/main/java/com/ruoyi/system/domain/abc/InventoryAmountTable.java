package com.ruoyi.system.domain.abc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

public class InventoryAmountTable {
    @Getter
    @Setter
    @JsonProperty("物料编码")
    @Excel(name = "物料编码")
    private String materialCode;
    @Getter
    @Setter
    @JsonProperty("销售单价")
    @Excel(name = "销售单价")
    private double unitPrice;
    @Getter
    @Setter
    @JsonProperty("平均库存")
    @Excel(name = "平均库存")
    private double averageInventory;
    @Getter
    @Setter
    @JsonProperty("平均资金占用额")
    @Excel(name = "平均资金占用额")
    private double averageFundsOccupied;
    @Getter
    @Setter
    @JsonProperty("平均资金占用额累计")
    @Excel(name = "平均资金占用额累计")
    private double cumulativeAverageFundsOccupied;
    @Getter
    @Setter
    @JsonProperty("平均资金占用额累计百分比")
    @Excel(name = "平均资金占用额累计百分比")
    private String cumulativeAverageFundsOccupiedPercentage;
    @Getter
    @Setter
    @JsonProperty("物料累计品目数")
    @Excel(name = "物料累计品目数")
    private int cumulativeItemNumber;
    @Getter
    @Setter
    @JsonProperty("物料累计品目数百分比")
    @Excel(name = "物料累计品目数百分比")
    private String cumulativeItemNumberPercentage;

}
