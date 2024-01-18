package com.ruoyi.system.resource.equipment;

import java.util.ArrayList;
import java.util.List;

/**
 * 叉车
 */
public class Forklift {
    private String name;//设备名称
    private String type;//设备类型
    private double bearing;//承重
    private double price;//价格
    private double height;//可伸缩高度
    private double speed;//速度




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
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

    public Forklift(String name, String type, double bearing, double price, double speed,double height) {
        this.name = name;
        this.type = type;
        this.bearing = bearing;
        this.price = price;
        this.speed = speed;
        this.height = height;
    }

    public static List<Forklift> initList(){
        List<Forklift> forklifts = new ArrayList<>();
        forklifts.add(new Forklift("AGV-叉取式-搬运","搬运",1000,150000,1.2,9));
        forklifts.add(new Forklift("AGV-叉车式","搬运",1000,150000,1.2,9));
        forklifts.add(new Forklift("AGV-顶升式（地牛式）","搬运",1000,80000,1.2,9));
        forklifts.add(new Forklift("穿梭板-多深货架使用","搬运",1000,200000,1.2,9));
        forklifts.add(new Forklift("AGV-叉取式-搬运","搬运",1000,150000,1.2,9));
        forklifts.add(new Forklift("电动叉车-可提升12m高","搬运",700,500000,2.3,12));
        forklifts.add(new Forklift("电动叉车-可提升9m高","搬运",900,300000,2.3,9));
        return forklifts;
    }
}
