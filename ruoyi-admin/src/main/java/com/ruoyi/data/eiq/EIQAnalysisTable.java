package com.ruoyi.data.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
/****
 * EIQ分类---对订单-物料种类数-出库数量统计
 */
public class EIQAnalysisTable {
    @JsonProperty("日期")
    @Getter
    @Setter
    private String date;
    @JsonProperty("E分析")
    @Getter
    @Setter
    private int EAnalysis;
    @JsonProperty("N分析")
    @Getter
    @Setter
    private int NAnalysis;
    @Getter
    @Setter
    @JsonProperty("Q分析")
    private double QAnalysis;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getEAnalysis() {
        return EAnalysis;
    }

    public void setEAnalysis(int EAnalysis) {
        this.EAnalysis = EAnalysis;
    }

    public int getNAnalysis() {
        return NAnalysis;
    }

    public void setNAnalysis(int NAnalysis) {
        this.NAnalysis = NAnalysis;
    }

    public double getQAnalysis() {
        return QAnalysis;
    }

    public void setQAnalysis(double QAnalysis) {
        this.QAnalysis = QAnalysis;
    }
}