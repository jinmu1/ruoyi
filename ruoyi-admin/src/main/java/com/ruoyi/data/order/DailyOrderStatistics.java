package com.ruoyi.data.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DailyOrderStatistics {
    @NotNull
    @JsonProperty("订单出库日期")
    private String orderOutboundDate;
    @NotNull
    @JsonProperty("食品百货类物料周期内每日订单订购总数量")
    private Double foodDailyOrderTotal;
    @NotNull
    @JsonProperty("食品百货类物料周期内每日订单订购数量平均值")
    private Double foodDailyOrderAverage;
    @NotNull
    @JsonProperty("用品百货类物料周期内每日订单订购总数量")
    private Double suppliesDailyOrderTotal;
    @NotNull
    @JsonProperty("用品百货类物料周期内每日订单订购数量平均值")
    private Double suppliesDailyOrderAverage;
    @NotNull
    @JsonProperty("零售百货类物料周期内每日订单订购总数量")
    private Double retailDailyOrderTotal;
    @NotNull
    @JsonProperty("零售百货类物料周期内每日订单订购数量平均值")
    private Double retailDailyOrderAverage;
}
