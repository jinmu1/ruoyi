package com.ruoyi.data.abc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MaterialOutboundFrequencyInfo {
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
