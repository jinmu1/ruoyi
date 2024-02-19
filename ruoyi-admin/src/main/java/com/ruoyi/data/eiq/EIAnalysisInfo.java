package com.ruoyi.data.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/****
 * EI分类---对订单-物料种类数统计
 */
public  class EIAnalysisInfo {
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
