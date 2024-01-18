package com.ruoyi.system.result;

import com.ruoyi.system.node.City;

import java.util.List;

public class ResultMsg {
    private double cost;
    private Result result;
    private int rdc;
    private List<Result> resultList;
    private List<City> cities;

    public int getRdc() {
        return rdc;
    }

    public void setRdc(int rdc) {
        this.rdc = rdc;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }
}
