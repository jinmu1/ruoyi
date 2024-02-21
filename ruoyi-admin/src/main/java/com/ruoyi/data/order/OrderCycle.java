package com.ruoyi.data.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderCycle {
    @NotNull
    @JsonProperty("客户订单编码")
    private String customerOrderCode;
    @NotNull
    @JsonProperty("订单创建日期")
    private String orderCreateDate;
    @NotNull
    @JsonProperty("订单出库日期")
    private String orderOutboundDate;
    @NotNull
    @JsonProperty("订单响应周期（天）")
    private Integer responseTimeDays;
}
