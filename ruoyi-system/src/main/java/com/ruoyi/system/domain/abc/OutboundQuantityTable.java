package com.ruoyi.system.domain.abc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

public class OutboundQuantityTable {
    @Getter
    @Setter
    @JsonProperty("物料编码")
    @Excel(name = "物料编码")
    private String materialCode;
    @Getter
    @Setter
    @JsonProperty("物料描述")
    @Excel(name = "物料描述")
    private String materialDescription;
    @Getter
    @Setter
    @JsonProperty("出库量")
    @Excel(name = "出库量")
    private double outboundQuantity;
    @Getter
    @Setter
    @JsonProperty("累计出库量")
    @Excel(name = "累计出库量")
    private double cumulativeOutboundQuantity;
    @Getter
    @Setter
    @JsonProperty("累计出库量百分比")
    @Excel(name = "累计出库量百分比")
    private String cumulativeOutboundQuantityPercentage;
    @Getter
    @Setter
    @JsonProperty("物料累计品目数")
    @Excel(name = "物料累计品目数")
    private int cumulativeItemCount;
    @Getter
    @Setter
    @JsonProperty("物料累计品目数百分比")
    @Excel(name = "物料累计品目数百分比")
    private String cumulativeItemCountPercentage;




}
