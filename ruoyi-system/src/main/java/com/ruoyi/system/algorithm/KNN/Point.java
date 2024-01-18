package com.ruoyi.system.algorithm.KNN;

import com.ruoyi.system.node.City;

import java.util.List;

public class Point {
    // 点的坐标
    private Double x;
    private Double y;
    private String city;
    private List<City> goodsList;

    public Point() {
       super();
    }

    // 所在类ID
    private int clusterID = -1;
    private double size;

    public Point(Double x, Double y, String city) {
        this.x = x;
        this.y = y;
        this.city = city;

    }
    public Point(Double x, Double y, String city,double size) {
        this.x = x;
        this.y = y;
        this.city = city;
        this.size = size;
    }

    public Point(Double x, Double y, String city, int clusterID, double size) {
        this.x = x;
        this.y = y;
        this.city = city;
        this.clusterID = clusterID;
        this.size = size;
    }

    public Point(Double x, Double y, String city,  double size ,List<City> goodsList) {
        this.x = x;
        this.y = y;
        this.city = city;
        this.goodsList = goodsList;

        this.size = size;
    }

    public List<City> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<City> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString()
    {
        return String.valueOf(getClusterID()) + " " + String.valueOf(this.x) + " " + String.valueOf(this.y);
    }

    public Double getX()
    {
        return x;
    }

    public void setX(Double x)
    {
        this.x = x;
    }

    public Double getY()
    {
        return y;
    }

    public void setY(Double y)
    {
        this.y = y;
    }

    public int getClusterID()
    {
        return clusterID;
    }

    public void setClusterID(int clusterID)
    {
        this.clusterID = clusterID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
