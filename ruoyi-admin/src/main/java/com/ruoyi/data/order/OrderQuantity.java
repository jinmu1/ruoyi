package com.ruoyi.data.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderQuantity {
    @NotNull
    @JsonProperty("零食百货类物料对应客户订单编码")
    private Integer snackCustomerOrderCode;
    @NotNull
    @JsonProperty("周期内客户订单订购总数量")
    private Integer totalOrderQuantity;
    @NotNull
    @JsonProperty("订单累计品目数")
    private Integer cumulativeItemCount;
}
