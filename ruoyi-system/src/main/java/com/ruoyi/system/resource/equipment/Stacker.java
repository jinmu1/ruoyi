package com.ruoyi.system.resource.equipment;



import com.ruoyi.system.form.Cargo;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆垛机使用类
 */
public class Stacker {
    private String eqmNo;;//堆垛机编号
    private int status ;//忙闲状态
    private double xspeed;//水平位移速度
    private double yspped;//垂直位移速度
    private Cargo target;//堆垛机目标货位

    private String type;//类型
    private double price;//价格
    private double speed;//260m/min


    public String getEqmNo() {
        return eqmNo;
    }

    public void setEqmNo(String eqmNo) {
        this.eqmNo = eqmNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getXspeed() {
        return xspeed;
    }

    public void setXspeed(double xspeed) {
        this.xspeed = xspeed;
    }

    public double getYspped() {
        return yspped;
    }

    public void setYspped(double yspped) {
        this.yspped = yspped;
    }

    public Cargo getCargo() {
        return target;
    }

    public void setCargo(Cargo cargo) {
        this.target = cargo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Stacker() {

    }

    public Stacker(String type, double price, double speed) {
        this.type = type;
        this.price = price;
        this.speed = speed;
    }

    public static List<Stacker> initStacker(){
        List<Stacker> list = new ArrayList<>();
        list.add(new Stacker("堆垛机-单深/双深",1100000,4 ));
        list.add(new Stacker("堆垛机-多深",1100000,4 ));
        return list;
    }
}
