package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public     class EIAnalysisTable {
    @JsonProperty("订单编号")
    @Getter
    @Setter
    private String orderNumber;
    @Getter
    @Setter
    @JsonProperty("订单对应物料品种数")
    private int materialVarietiesCount;
    @Getter
    @Setter
    @JsonProperty("订单编号累计品目数")
    private int cumulativeItemNumber;



}
