package com.ruoyi.system.result;

/**
 * 人员使用类 记录员工工作状态-
 */
public class EmpLog {
    private String time;//日期
    private String empNo;//员工编号
    private String goods_code;//物料号
    private int empStatus;//1-忙，0-闲
    private String order;//所属订单
    private String position;//位置
    private double comPlut;//完成量
    private double nowPlut;//需要完成量
    private double AllPlut;//总量
    private double distance;//行走距离

    public EmpLog() {
        super();
    }

    public EmpLog(String time, String empNo, String goods_code, int empStatus) {
        this.time = time;
        this.empNo = empNo;
        this.goods_code = goods_code;
        this.empStatus = empStatus;
    }

    public EmpLog(String time, int empStatus) {
        this.time = time;
        this.empStatus = empStatus;
    }
    public EmpLog(String time, String empNo) {
        this.time = time;
        this.empNo = empNo;
    }
    public EmpLog(String time, String empNo, String goods_code, int empStatus, String order, String position, double comPlut, double nowPlut, double AllPlut) {
        this.time = time;
        this.empNo = empNo;
        this.goods_code = goods_code;
        this.empStatus = empStatus;
        this.order = order;
        this.position = position;
        this.comPlut = comPlut;
        this.nowPlut = nowPlut;
        this.AllPlut = AllPlut;
    }
    public EmpLog(String time, String empNo, String goods_code, int empStatus, String order, String position) {
        this.time = time;
        this.empNo = empNo;
        this.goods_code = goods_code;
        this.empStatus = empStatus;
        this.order = order;
        this.position = position;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getGoods_code() {
        return goods_code;
    }

    public void setGoods_code(String goods_code) {
        this.goods_code = goods_code;
    }

    public int getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(int empStatus) {
        this.empStatus = empStatus;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getComPlut() {
        return comPlut;
    }

    public void setComPlut(double comPlut) {
        this.comPlut = comPlut;
    }

    public double getNowPlut() {
        return nowPlut;
    }

    public void setNowPlut(double nowPlut) {
        this.nowPlut = nowPlut;
    }

    public double getAllPlut() {
        return AllPlut;
    }

    public void setAllPlut(double allPlut) {
        AllPlut = allPlut;
    }

    @Override
    public String toString() {
        return "EmpLog{" +
                "time='" + time + '\'' +
                ", empNo='" + empNo + '\'' +
                ", goods_code='" + goods_code + '\'' +
                ", empStatus=" + empStatus +
                ", order='" + order + '\'' +
                ", position='" + position + '\'' +
                ", distance=" + distance +
                '}';
    }
}
