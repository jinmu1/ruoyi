package com.ruoyi.data.abc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

// 物料的平均资金占用额跟百分比信息
@Getter
@Setter
public class MaterialValueInfo {
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
