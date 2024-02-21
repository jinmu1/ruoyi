package com.ruoyi.data.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ResponseDistribution {
    @NotNull
    @JsonProperty("订单响应周期分布区间")
    private String responseTimeInterval;
    @NotNull
    @JsonProperty("区间内对应订单分布的数量")
    private Integer orderDistributionCount;
}
