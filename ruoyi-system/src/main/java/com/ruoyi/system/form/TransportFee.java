package com.ruoyi.system.form;

public class TransportFee {
    private String provice;//省份
    private String city;//城市
    private double transportfee;//运输费率

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public double getTransportfee() {
        return transportfee;
    }

    public void setTransportfee(double transportfee) {
        this.transportfee = transportfee;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
