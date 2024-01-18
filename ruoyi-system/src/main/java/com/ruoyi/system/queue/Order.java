package com.ruoyi.system.queue;

import java.util.Date;

public class Order {
    private String orderCode;//订单编号
    private String goodsCode;//物料编码
    private Date createDate;//订单创立时间
    private Date workDate;//订单创建时间
    private Date completeDate;//订单完成时间
    private double  goodsNum;//物料数量
    private double  volume;//体积
    private double  weight;//重量
    private int status;//完成状态
    private String customerCode;
    private String customerCity;
    public Order(){
        super();
    }
    public Order(String orderCode, String goodsCode, Date createDate, Date workDate, double goodsNum, double volume) {
        this.orderCode = orderCode;
        this.goodsCode = goodsCode;
        this.createDate = createDate;
        this.workDate = workDate;
        this.completeDate = completeDate;
        this.goodsNum = goodsNum;
        this.volume = volume;
        this.weight = weight;
        this.status = status;
    }
    public Order(String orderCode, String goodsCode,  double goodsNum) {
        this.orderCode = orderCode;
        this.goodsCode = goodsCode;
        this.goodsNum = goodsNum;

    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public double getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(double goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
}
