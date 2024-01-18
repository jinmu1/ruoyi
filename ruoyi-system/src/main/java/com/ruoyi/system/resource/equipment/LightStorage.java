package com.ruoyi.system.resource.equipment;


import com.ruoyi.system.resource.facilities.storage.Storage;

public class LightStorage  extends Storage {
    private double area;//面积
    private int  line;//排
    private int  row;//列
    private int layer;//层
    private int cargo;//货位数
    private double price;//价格
    private int putawayemp;//作业人数
    private int sortingemp;//作业人数
    private int emp;



    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPutawayemp() {
        return putawayemp;
    }

    public void setPutawayemp(int putawayemp) {
        this.putawayemp = putawayemp;
    }

    public int getSortingemp() {
        return sortingemp;
    }

    public void setSortingemp(int sortingemp) {
        this.sortingemp = sortingemp;
    }

    public int getEmp() {
        return emp;
    }

    public void setEmp(int emp) {
        this.emp = emp;
    }
}
