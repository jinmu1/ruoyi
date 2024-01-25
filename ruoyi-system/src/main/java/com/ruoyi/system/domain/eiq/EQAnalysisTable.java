package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;

public     class EQAnalysisTable {
    @JsonProperty("订单编号")
    private String orderNumber;

    @JsonProperty("订单对应出库总数量")
    private double totalDeliveredQuantity;

    @JsonProperty("订单编号累计品目数")
    private int cumulativeItemNumber;

    // Getter and Setter methods

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getTotalDeliveredQuantity() {
        return totalDeliveredQuantity;
    }

    public void setTotalDeliveredQuantity(double totalDeliveredQuantity) {
        this.totalDeliveredQuantity = totalDeliveredQuantity;
    }

    public int getCumulativeItemNumber() {
        return cumulativeItemNumber;
    }

    public void setCumulativeItemNumber(int cumulativeItemNumber) {
        this.cumulativeItemNumber = cumulativeItemNumber;
    }
}
