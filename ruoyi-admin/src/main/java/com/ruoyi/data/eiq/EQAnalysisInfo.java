package com.ruoyi.data.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
/****
 * EQ分类---对订单-物料数量统计
 */
public  class EQAnalysisInfo {
    @Getter
    @Setter
    @JsonProperty("订单编号")
    private String orderNumber;
    @Getter
    @Setter
    @JsonProperty("订单对应出库总数量")
    private Double totalDeliveredQuantity;
    @Getter
    @Setter
    @JsonProperty("订单编号累计品目数")
    private Integer cumulativeItemNumber;
}
