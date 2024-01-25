package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;

public  class ENAnalysisTable {
    @JsonProperty("订单编号")
    private String orderNumber;

    @JsonProperty("订单对应行数")
    private int orderLineCount;

    @JsonProperty("订单编号累计品目数")
    private int cumulativeItemNumber;

    // Getter and Setter methods

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderLineCount() {
        return orderLineCount;
    }

    public void setOrderLineCount(int orderLineCount) {
        this.orderLineCount = orderLineCount;
    }

    public int getCumulativeItemNumber() {
        return cumulativeItemNumber;
    }

    public void setCumulativeItemNumber(int cumulativeItemNumber) {
        this.cumulativeItemNumber = cumulativeItemNumber;
    }
}
