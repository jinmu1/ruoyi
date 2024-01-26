package com.ruoyi.system.domain.abc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

public class OutboundFrequencyTable {
    @Getter
    @Setter
    @JsonProperty("物料编码")
    @Excel(name = "物料编码")
    private String materialCode;

    @Getter
    @Setter
    @JsonProperty("出库频次")
    @Excel(name = "出库频次")
    private int outboundFrequency;

    @Getter
    @Setter
    @JsonProperty("累计出库频次")
    @Excel(name = "累计出库频次")
    private int cumulativeOutboundFrequency;

    @Getter
    @Setter
    @JsonProperty("累计出库频次百分比")
    @Excel(name = "累计出库频次百分比")
    private String cumulativeOutboundFrequencyPercentage;

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
