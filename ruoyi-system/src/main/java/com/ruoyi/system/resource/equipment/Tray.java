package com.ruoyi.system.resource.equipment;



import com.ruoyi.system.form.Goods;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.utils.WarehousingUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 托盘
 */
public class Tray {
    private static double width=1.1;//托盘的宽
    private static double length=1.2;//托盘的长
    private static double thickness=0.6;//托盘的厚度
    private static double height=1.6;//托盘的高度---限高
    private List<Goods> goodsList = new ArrayList<>();//存储的物料数量
    private Point point;
    private int status;//托盘的状态 0-空 1-有物料 2-满载了

    public Tray(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
    public Tray() {
        super();
    }
    public Tray(double width, double length, double thickness, double height, int status) {
        this.width = width;
        this.length = length;
        this.thickness = thickness;
        this.height = height;
        this.goodsList = goodsList;
        this.point = point;
        this.status = status;
    }

    public static List<Tray> initTrays(List<Goods> list,double transportNum) {
        List<Tray> list1 =new ArrayList<>();
        for (int i = 0;i<transportNum*90;i++){
            Tray tray = new Tray();
            tray.getGoodsList().add(list.get(i%list.size()));
            tray.setPoint(new Point(0,0,0));
            list1.add(tray);
        }
        return list1;
    }

    public static List<Tray> initOrder(List<Goods> list, double transportNum, int orderLine) {
        List<Tray> list1 =new ArrayList<>();
        for (int i=0;i<transportNum*orderLine/2;i++){
            Tray tray = new Tray();
            tray.getGoodsList().add(list.get((int) WarehousingUtil.random(0,list.size()-1)));
            tray.setPoint(new Point(0,0,0));
            list1.add(tray);
        }

        return list1;
    }

    public static List<Tray> initTrays1(List<Goods> list, double transportNum, int orderLine) {
        List<Tray> list1 =new ArrayList<>();
        for (int i = 0;i<transportNum*orderLine*90;i++){
            Tray tray = new Tray();
            tray.getGoodsList().add(list.get(i%list.size()));
            tray.setPoint(new Point(0,0,0));
            list1.add(tray);
        }
        return list1;
    }

    private void init(){

    }

    public double getWidth() {
        return width;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
