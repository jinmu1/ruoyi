package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;

import java.util.Date;


public  class EIQBasicTable {


    @Excel(name = "出库日期")
    @JsonProperty("出库日期")
    private Date deliveryDate;


    @Excel(name = "订单编号")
    @JsonProperty("订单编号")
    private String orderNumber;


    @Excel(name = "物料编号")
    @JsonProperty("物料编号")
    private String materialNumber;


    @Excel(name = "物料名称")
    @JsonProperty("物料名称")
    private String materialName;


    @Excel(name = "出货数量")
    @JsonProperty("出货数量")
    private double deliveryQuantity;


    @Excel(name = "出货单位")
    @JsonProperty("出货单位")
    private String deliveryUnit;


    @Excel(name = "销售单价")
    @JsonProperty("销售单价")
    private double unitPrice;


    @Excel(name = "托盘装件数")
    @JsonProperty("托盘装件数")
    private double palletizedItems;


    @Excel(name = "换算单位")
    @JsonProperty("换算单位")
    private double conversionUnit;


    @Excel(name = "换算单位1")
    @JsonProperty("换算单位1")
    private double conversionUnit1;

    // Getter and Setter methods


    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public double getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(double deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public String getDeliveryUnit() {
        return deliveryUnit;
    }

    public void setDeliveryUnit(String deliveryUnit) {
        this.deliveryUnit = deliveryUnit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getPalletizedItems() {
        return palletizedItems;
    }

    public void setPalletizedItems(double palletizedItems) {
        this.palletizedItems = palletizedItems;
    }

    public double getConversionUnit() {
        return conversionUnit;
    }

    public void setConversionUnit(double conversionUnit) {
        this.conversionUnit = conversionUnit;
    }

    public double getConversionUnit1() {
        return conversionUnit1;
    }

    public void setConversionUnit1(double conversionUnit1) {
        this.conversionUnit1 = conversionUnit1;
    }

}