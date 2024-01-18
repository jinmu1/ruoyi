package com.ruoyi.system.form;

public class Supplier {
    private String SupplierCode;
    private String goodsCode;//物料编码
    private String distance;//运输距离
    private double leadTime;//运输提前期


    public Supplier() {
       super();
    }

    public Supplier(String supplierCode, String goodsCode, String distance, double leadTime) {
        SupplierCode = supplierCode;
        this.goodsCode = goodsCode;
        this.distance = distance;
        this.leadTime = leadTime;
    }


    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        SupplierCode = supplierCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(double leadTime) {
        this.leadTime = leadTime;
    }
}
