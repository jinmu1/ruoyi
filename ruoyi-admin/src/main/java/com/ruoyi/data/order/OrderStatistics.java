package com.ruoyi.data.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderStatistics {
    @NotNull
    @JsonProperty("订单创建日期")
    private String orderCreateDate;
    @NotNull
    @JsonProperty("周期内每日下达订单数量")
    private Integer dailyOrderCount;
    @NotNull
    @JsonProperty("平均数")
    private Integer averageCount;
    @NotNull
    @JsonProperty("中位数")
    private Integer medianCount;
}
