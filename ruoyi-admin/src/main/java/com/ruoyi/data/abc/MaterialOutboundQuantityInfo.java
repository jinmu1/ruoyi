package com.ruoyi.data.abc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MaterialOutboundQuantityInfo {
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
