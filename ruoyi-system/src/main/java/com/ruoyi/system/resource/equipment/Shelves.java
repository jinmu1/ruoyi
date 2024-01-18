package com.ruoyi.system.resource.equipment;

import java.util.ArrayList;
import java.util.List;

public class Shelves {

    private String type;//货架类型
    private double bearing;//承重
    private double price;//价格


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

    public Shelves(String type, double bearing, double price) {
        this.type = type;
        this.bearing = bearing;
        this.price = price;
    }


    public static List<Shelves> initShelves(){
        List<Shelves> list = new ArrayList<>();
        list.add(new Shelves("轻型货架-2米高",300,900));
        list.add(new Shelves("自动化立体货架-单深（1+1）",2000,450));
        list.add(new Shelves("自动化立体货架-双深（2+2）",2000,450));
        list.add(new Shelves("自动化立体货架-多深（12~20个货位）",2000,540));

        return list;

    }
}
