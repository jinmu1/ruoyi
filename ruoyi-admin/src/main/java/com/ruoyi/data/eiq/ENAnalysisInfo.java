package com.ruoyi.data.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/****
 * EN分类---对订单-订单行数统计
 */
@Getter
@Setter
public class ENAnalysisInfo {
    @Getter
    @Setter
    @JsonProperty("订单编号")
    private String orderNumber;
    @Getter
    @Setter
    @JsonProperty("订单对应行数")
    private int orderLineCount;
    @Getter
    @Setter
    @JsonProperty("订单编号累计品目数")
    private int cumulativeItemNumber;
}
