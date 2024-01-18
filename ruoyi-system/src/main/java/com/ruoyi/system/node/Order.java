package com.ruoyi.system.node;

import java.util.Date;

public class Order {
    private String orderCode;//订单编码
    //客户编码
    private String customerCode;
    //@TableField(value = "客户名称")
    private String customerName;

    private City supplierCity;//供应商城市
   // @TableField(value = "客户城市")
    private City customerCity;
   // @TableField(value = "物料编码")
    private String goodsCode;
   // @TableField(value = "区域")
    private String goodsArea;
    //@TableField(value = "产品数量")
    private double goodsNum;
    //@TableField(value = "产品单价")
    private double volume;//物料体积

    private double goodsPrice;
   // @TableField(value = "下单日期")
    private Date orderDate;
   // @TableField(value = "发货日期")
    private Date deliveryDate;

    private Date storageDate;//仓储作业时间

    private Date replenishmentDate;//仓储作业时间


    public City getSupplierCity() {
        return supplierCity;
    }

    public void setSupplierCity(City supplierCity) {
        this.supplierCity = supplierCity;
    }

    public Order() {
    }
    public Order(String goodsCode,  double goodsNum,double volume) {
        this.goodsCode = goodsCode;
        this.goodsNum = goodsNum;
        this.volume = volume;
    }
    public Order(String goodsCode,  double goodsNum,double volume,double price) {
        this.goodsCode = goodsCode;
        this.goodsNum = goodsNum;
        this.volume = volume;
        this.goodsPrice = price;
    }

    public Order(Date orderDate, String goodsCode, double goodsNum,double volume,double price) {
        this.goodsCode = goodsCode;
        this.goodsNum = goodsNum;
        this.deliveryDate = orderDate;
        this.volume = volume;
        this.goodsPrice = price;
    }
    public Order(String orderCode,Date orderDate, String goodsCode, double goodsNum,double volume,double price) {
        this.orderCode = orderCode;
        this.goodsCode = goodsCode;
        this.goodsNum = goodsNum;
        this.deliveryDate = orderDate;
        this.volume = volume;
        this.goodsPrice = price;
    }
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public City getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(City customerCity) {
        this.customerCity = customerCity;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsArea() {
        return goodsArea;
    }

    public void setGoodsArea(String goodsArea) {
        this.goodsArea = goodsArea;
    }

    public double getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(double goodsNum) {
        this.goodsNum = goodsNum;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }


    public Date getReplenishmentDate() {
        return replenishmentDate;
    }

    public void setReplenishmentDate(Date replenishmentDate) {
        this.replenishmentDate = replenishmentDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "物料名称=" + goodsCode +
                ", 物料数量=" + goodsNum +
                ", 体积=" + volume +
                '}';
    }
}
