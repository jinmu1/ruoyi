package com.ruoyi.system.result;

import com.ruoyi.system.node.City;

import java.util.List;

public class Message {
    private double cost;
    private List<Result> resultList;
    private List<City> cityList;

    public double getCost() {
        return cost;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
