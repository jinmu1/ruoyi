package com.ruoyi.data.eiq;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/****
 * EIQ分类---对订单-物料种类数-出库数量统计
 */
public class EIQAnalysisTable {
    @JsonProperty("日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    @Getter
    @Setter
    private Date date;
    @JsonProperty("E分析")
    @Getter
    @Setter
    private Integer EAnalysis;
    @JsonProperty("N分析")
    @Getter
    @Setter
    private Integer NAnalysis;
    @Getter
    @Setter
    @JsonProperty("Q分析")
    private Double QAnalysis;
}