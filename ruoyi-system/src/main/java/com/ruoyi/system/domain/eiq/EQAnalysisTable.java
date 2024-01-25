package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public     class EQAnalysisTable {
    @Getter
    @Setter
    @JsonProperty("订单编号")
    private String orderNumber;
    @Getter
    @Setter
    @JsonProperty("订单对应出库总数量")
    private double totalDeliveredQuantity;
    @Getter
    @Setter
    @JsonProperty("订单编号累计品目数")
    private int cumulativeItemNumber;

}
