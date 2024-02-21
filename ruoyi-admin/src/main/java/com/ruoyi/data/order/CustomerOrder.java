package com.ruoyi.data.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CustomerOrder {
    @NotNull
    @JsonProperty("客户订单编码")
    private String customerOrderCode;
    @NotNull
    @JsonProperty("周期内客户订单对应物料种类总数量")
    private Integer totalMaterialTypes;
    @NotNull
    @JsonProperty("客户订单编码累计品目数")
    private Integer cumulativeItemsCount;
}
