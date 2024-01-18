package com.ruoyi.system.node;

/**
 * 备选点城市
 */
public class City {

    private String city;

    private String lat;

    private String lng;

    private String city1;

    private String code;

    private String lat1;

    private String lng1;

    private String distance;

    private double transportfee;

    private String gdp;

    private double price;


    public City(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public City(String city, String lat, String lng, String gdp) {
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.gdp = gdp;
    }
    public City() {

    }
    public City(String city) {
        this.city = city;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLat1() {
        return lat1;
    }

    public void setLat1(String lat1) {
        this.lat1 = lat1;
    }

    public String getLng1() {
        return lng1;
    }

    public void setLng1(String lng1) {
        this.lng1 = lng1;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGdp() {
        return gdp;
    }

    public void setGdp(String gdp) {
        this.gdp = gdp;
    }

    public double getTransportfee() {
        return transportfee;
    }

    public void setTransportfee(double transportfee) {
        this.transportfee = transportfee;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
