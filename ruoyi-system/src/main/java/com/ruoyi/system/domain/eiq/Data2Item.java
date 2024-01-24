package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data2Item {
    @JsonProperty("日期")
    private String date;

    @JsonProperty("E分析")
    private int eAnalysis;

    @JsonProperty("N分析")
    private int nAnalysis;

    @JsonProperty("Q分析")
    private double qAnalysis;

    // Getter and Setter methods

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int geteAnalysis() {
        return eAnalysis;
    }

    public void seteAnalysis(int eAnalysis) {
        this.eAnalysis = eAnalysis;
    }

    public int getnAnalysis() {
        return nAnalysis;
    }

    public void setnAnalysis(int nAnalysis) {
        this.nAnalysis = nAnalysis;
    }

    public double getqAnalysis() {
        return qAnalysis;
    }

    public void setqAnalysis(double qAnalysis) {
        this.qAnalysis = qAnalysis;
    }
}