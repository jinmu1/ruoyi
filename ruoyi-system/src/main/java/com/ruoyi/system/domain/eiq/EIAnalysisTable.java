package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;

public     class EIAnalysisTable {
    @JsonProperty("订单编号")
    private String orderNumber;

    @JsonProperty("订单对应物料品种数")
    private int materialVarietiesCount;

    @JsonProperty("订单编号累计品目数")
    private int cumulativeItemNumber;

    // Getter and Setter methods

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getMaterialVarietiesCount() {
        return materialVarietiesCount;
    }

    public void setMaterialVarietiesCount(int materialVarietiesCount) {
        this.materialVarietiesCount = materialVarietiesCount;
    }

    public int getCumulativeItemNumber() {
        return cumulativeItemNumber;
    }

    public void setCumulativeItemNumber(int cumulativeItemNumber) {
        this.cumulativeItemNumber = cumulativeItemNumber;
    }
}
