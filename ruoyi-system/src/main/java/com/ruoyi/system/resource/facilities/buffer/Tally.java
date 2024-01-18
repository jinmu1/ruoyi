package com.ruoyi.system.resource.facilities.buffer;



import com.ruoyi.system.utils.WarehousingUtil;
import com.ruoyi.system.form.Goods;
import com.ruoyi.system.queue.Order;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.equipment.Tray;
import com.ruoyi.system.utils.AreaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tally {
    private List<Point> points;
    private double torr;//托
    private List<Goods> goodsList;
    private List<Order> orders;
    private List<Tray> trays  = new ArrayList<>();//托盘
    private double pallet;//托盘量
    private double area;//区域面积
    private int tally_transverse;// 纵向数量
    private int tally_longitudinal;//横向数量
    public static double tally_channel = 0.6;//托盘间隙
    public static double forklift_channel=3;//叉车通道
    private int emp;//人员数量
    private double empCost;//人员成本
    private static double x=60;//初始化点的x坐标
    private static double y=40;//初始点的y坐标



    /**
     * 初始化分拣区位置
     * @param tally_transverse 横向托盘数量
     * @param tally_longitudinal 纵向托盘数量
     * @param channel_width 通道宽度
     * @param tray_clearance  托盘间隙
     */
    public void initTally(int tally_transverse,int tally_longitudinal,double channel_width,double tray_clearance,double tally_width,double tally_length){
        points = new ArrayList<>();
        for (int i=0;i<tally_transverse;i++){
            for (int j =0;j<tally_longitudinal;j++){
                Point point =new Point(x+i*(tally_width+channel_width)+tally_width/2,y+j*(tally_length+tray_clearance)+tally_length/2,1,0);
                points.add(point);
            }
        }
    }

    public void addTrays(Tray tray){
        trays = new ArrayList<>();
        for(Point point:points){
            if(point.getStatus()==0){
                point.setStatus(1);
                trays.add(tray);
            }
        }
    }
    public void removeTray(Tray tray){
        trays = new ArrayList<>();
        for(Point point:points){
            if(point.getStatus()==1&& WarehousingUtil.getDistance(point,tray.getPoint())==0){
                point.setStatus(0);
                trays.remove(tray);
            }
        }
    }

    public List<Tray> getTrays() {
        return trays;
    }

    public void setTrays(List<Tray> trays) {
        this.trays = trays;
    }

    public static double getTally_channel() {
        return tally_channel;
    }

    public static void setTally_channel(double tally_channel) {
        Tally.tally_channel = tally_channel;
    }

    public static double getForklift_channel() {
        return forklift_channel;
    }

    public static void setForklift_channel(double forklift_channel) {
        Tally.forklift_channel = forklift_channel;
    }

    public static double getX() {
        return x;
    }

    public static void setX(double x) {
        Tally.x = x;
    }

    public static double getY() {
        return y;
    }

    public static void setY(double y) {
        Tally.y = y;
    }

    public int getEmp() {
        return emp;
    }

    public void setEmp(int emp) {
        this.emp = emp;
    }

    public double getEmpCost() {
        return empCost;
    }

    public void setEmpCost(double empCost) {
        this.empCost = empCost;
    }

    public Tally(double torr) {
        this.torr=torr;
        this.goodsList = new ArrayList<>();
        this.orders = new ArrayList<>();
    }
    public Tally() {
        this.goodsList = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public double getPallet() {
        return pallet;
    }

    public void setPallet(double pallet) {
        this.pallet = pallet;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getTally_transverse() {
        return tally_transverse;
    }

    public void setTally_transverse(int tally_transverse) {
        this.tally_transverse = tally_transverse;
    }

    public int getTally_longitudinal() {
        return tally_longitudinal;
    }

    public void setTally_longitudinal(int tally_longitudinal) {
        this.tally_longitudinal = tally_longitudinal;
    }

    public double getTorr() {
        return torr;
    }

    public void setTorr(double torr) {
        this.torr = torr;
    }

    public void deal(){
        this.torr--;
    }
    public void add(){
        this.torr++;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
    public void add(List<Goods> goods){

        this.goodsList.addAll(goods);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Tally getTallyArea(double transportNum,String carType, double batch, double tally_channel, double tray_clearance) {
        double pallet_num = transportNum/batch;//理货平均托盘量
        int tally_transverse = (int)AreaUtils.getPlatform(transportNum,carType).getPlatform_num();//纵向数量
        if (tally_transverse>pallet_num){
            tally_transverse = 1;
        }
        Tray tray = new Tray();

        int tally_longitudinal= (int)Math.ceil(pallet_num/tally_transverse);//横向数量
        double tally_area = tally_longitudinal*(tray.getLength()+tray_clearance)*(tray.getWidth()+tally_channel)*tally_transverse/0.7;//理货区面积
        Tally tally = new Tally();
        tally.setPallet(pallet_num);
        tally.setArea(tally_area);
        tally.setTally_longitudinal(tally_longitudinal);
        tally.setTally_transverse(tally_transverse);
        return tally;
    }

    public void putInTrays(List<Tray> trays) {
        List<Goods> goodsList = new ArrayList<>();
        List<Tray> trayList = new ArrayList<>();
        for (Tray tray:trays){
            goodsList.addAll(tray.getGoodsList());
        }
        Map<String,List<Goods>> map = goodsList.stream().collect(Collectors.groupingBy(Goods::getGoodsCode));//出库单

        for (int i = 0;i<points.size();i++){
            Tray tray = new Tray();
            tray.setPoint(points.get(points.size()-i-1));
            points.get(i).setStatus(1);
            trayList.add(tray);
        }

        this.trays.addAll(trayList) ;
    }

}
