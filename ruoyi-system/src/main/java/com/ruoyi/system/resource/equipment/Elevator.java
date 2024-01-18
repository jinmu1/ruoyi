package com.ruoyi.system.resource.equipment;


import com.ruoyi.system.queue.Point;

public class Elevator {
    private String  code;//编码
    private int cvmax;//承载量
    private int floor;//所在楼层
    private double v0;//上升速度
    private double v1;//下降速度
    private int status;//状态 0-无安排,1-上升，2-下降
    private Point curr;//当前位置
    private Point tar;//目标位置

    public Elevator() {
    }

    public Elevator(String code, int cvmax, int floor, double v0, double v1, int status, Point curr) {
        this.code = code;
        this.cvmax = cvmax;
        this.floor = floor;
        this.v0 = v0;
        this.v1 = v1;
        this.status = status;
        this.curr = curr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCvmax() {
        return cvmax;
    }

    public void setCvmax(int cvmax) {
        this.cvmax = cvmax;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getV0() {
        return v0;
    }

    public void setV0(double v0) {
        this.v0 = v0;
    }

    public double getV1() {
        return v1;
    }

    public void setV1(double v1) {
        this.v1 = v1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Point getCurr() {
        return curr;
    }

    public void setCurr(Point curr) {
        this.curr = curr;
    }

    public Point getTar() {
        return tar;
    }

    public void setTar(Point tar) {
        this.tar = tar;
    }
}
