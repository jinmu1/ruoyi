package com.ruoyi.system.resource.personnel;

import com.ruoyi.system.form.Goods;
import com.ruoyi.system.queue.Order;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.equipment.Elevator;
import com.ruoyi.system.resource.equipment.Tray;

import java.util.List;

public class Emp {
    private String name;//员工名称
    private String code;// 编码
    private  double v0;//空托盘行走速度
    private  double v1;//满托行走速度
    private  double t0;//装满一托盘的速度
    private  double t1;//卸载一托盘的速度
    private  double t2;//理货依托的时间
    private  double e_t0;//进入电梯的时间
    private int status;//工作状态 0-没工作,1-到达月台,2-装,3-达到门,4-到达理货区,5-卸货,6-理货区到门,7-门到月台
    private Elevator elevator;
    private Tray tary;
    private Point curr;//当前位置
    private Point tar;//目标位置
    private List<Goods> goods;
    private List<Order> orders;
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Emp(String name, int status, Point curr) {
        this.name = name;
        this.status = status;
        this.curr = curr;

    }
    public Emp(String name, int status, Point curr, List<Goods> goods) {
        this.name = name;
        this.status = status;
        this.curr = curr;
        this.goods = goods;
    }
    public Emp(String name, int status, Point curr, double v0, double v1, double t0, double t1) {
        this.name = name;
        this.status = status;
        this.curr = curr;
        this.v0 = v0;
        this.v1 = v1;
        this.t0 = t0;
        this.t1 = t1;
    }
    public Emp(String name, int status, Point curr, double v0, double v1, double t0, double t1, double t2) {
        this.name = name;
        this.status = status;
        this.curr = curr;
        this.v0 = v0;
        this.v1 = v1;
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
    }
    public boolean arrive(){
        if (Math.abs(curr.getX()-tar.getX())+Math.abs(curr.getY()-tar.getY())<2){
            return true;
        }else {
            return false;
        }
    }
    public boolean eleArrive(){
        if (Math.abs(elevator.getCurr().getZ()-elevator.getTar().getZ())<1){
            return true;
        }else {
            return false;
        }
    }

    public Tray getTary() {
        return tary;
    }

    public void setTary(Tray tary) {
        this.tary = tary;
    }

    public double getE_t0() {
        return e_t0;
    }

    public void setE_t0(double e_t0) {
        this.e_t0 = e_t0;
    }

    @Override
    public String toString() {
        if (this.tar!=null) {
            return "Emp{" + "name='" + name + '\'' + ", status=" + status + ", curr=(" + curr.getX() + "," + curr.getY() + "," + curr.getZ() + "), tar=(" + tar.getX() + "," + tar.getY() + "," + tar.getZ() + ")" + '}';
        }else {
            return "Emp{" + "name='" + name + '\'' + ", status=" + status + ", curr=(" + curr.getX() + "," + curr.getY() + "," + curr.getZ() + ")";
        }
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public double getT1() {
        return t1;
    }

    public void setT1(double t1) {
        this.t1 = t1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getT0() {
        return t0;
    }

    public void setT0(double t0) {
        this.t0 = t0;
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

    public double getT2() {
        return t2;
    }

    public void setT2(double t2) {
        this.t2 = t2;
    }

    public void fix() {
        this.t0=t0-1;
    }


    public void fix1() {
        this.t1 = t1-1;
    }

    public void dispose() {
        this.t2 = this.t2 - 1;
    }


    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addStatus() {
        this.status ++;
    }
}
