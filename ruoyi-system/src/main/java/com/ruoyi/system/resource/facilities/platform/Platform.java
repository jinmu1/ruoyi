package com.ruoyi.system.resource.facilities.platform;

import com.ruoyi.system.utils.WarehousingUtil;
import com.ruoyi.system.enumType.CarType;
import com.ruoyi.system.form.Car;
import com.ruoyi.system.form.Goods;
import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.personnel.Emp;

import java.util.ArrayList;
import java.util.List;

/**
 * 月台
 * @author jinmu
 */
public class Platform {
    private Car carLine;//车辆队列
    private String code;//编号
    private Point position;//位置

    private int sign;//签单
    private int status;//状态 0-无车辆等待 1-有车辆等待 -1车辆签单状态
    private List<Goods> goodsList;
    private double platform_num;//月台数量
    private double platform_area;//月台面积
    private double forklift;//卸货叉车
    private double empCost;//员工成本
    private double forkliftCost;//叉车成本
    private List<Car> cars;//停靠车辆编号
    private List<Point> points;
    public static double peak_rate = 1.5;//高峰概率
    public static double volumetric_coefficient=0.8;//容积系数
    public  double platform_width;//月台的宽度
    public  double platform_length;//单个月台的宽度 车辆宽度一般在1.8-2米之间
    private double platform_clearance;//月台间隙 3
    private static double x=0;//初始化点的x坐标
    private static double y=0;//初始点的y坐标
    private List<Emp> emps;
    /**
     * 按车位数和列数
     * @param parkNum
     * @param
     */
    public void initPoints(int parkNum){
        points = new ArrayList<>();
        for (int i=0;i<parkNum;i++){
            Point point = new Point(x+i*(platform_width+platform_clearance)+platform_width/2,y+platform_length/2,1,0);
            points.add(point);
        }
    }
    /**
     * 计算泊车位数量的方法
      */
    public int getParking_num(String  carType,double total,double unloading_time,double everyDay_unloading_time,double batch){
        double car_volumetric = Double.parseDouble(CarType.valueOf(carType).getCode());//计算车辆容积 通过车辆类型
        platform_width = 3 * 1.25;

        double car_num = Math.ceil(total/volumetric_coefficient/car_volumetric);//计算车辆数量 通过车辆容积
        int platform_num =  (int) Math.ceil(car_num*peak_rate*unloading_time/everyDay_unloading_time/batch);//月台数量
        return platform_num;
    }

    /**
     * 计算月台面积的方法
     * @param parking_num
     * @param platform_width
     * @param platform_length
     * @return
     */
    public double getPlatformArea(int parking_num,double platform_width,double platform_length){
        double platform_area = parking_num*(3+platform_width)*platform_length;//月台面积
        return platform_area;
    }

    /**
     * 增加车辆
     * @param car
     */
    private void addCar(Car car){
        for (Point point:points ){
            if (point.getStatus()==0){
                point.setStatus(1);
                cars.add(car);
            }
        }
    }
    private void removeCar(Car car){
        for (Point point:points ){
            if (point.getStatus()==1&& WarehousingUtil.getDistance(car.getPoint(),point)==0){
                point.setStatus(1);
                cars.remove(car);
            }
        }
    }

    public Platform() {
    }

    public Platform(String code,int status,Point point ) {
        this.code = code;
        this.status = status;
        this.position = point;
    }

    public List<Emp> getEmps() {
        return emps;
    }

    public void setEmps(List<Emp> emps) {
        this.emps = emps;
    }



    public double getPlatform_num() {
        return platform_num;
    }

    public void setPlatform_num(double platform_num) {
        this.platform_num = platform_num;
    }

    public double getPlatform_area() {
        return platform_area;
    }

    public double getForklift() {
        return forklift;
    }

    public void setForklift(double forklift) {
        this.forklift = forklift;
    }

    public void setPlatform_area(double platform_area) {
        this.platform_area = platform_area;
    }

    public Car getCarLine() {
        return carLine;
    }

    public void setCarLine(Car carLine) {
        this.carLine = carLine;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }



    public void setStatus(int status) {
        this.status = status;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public void inSign() {
        this.sign = this.sign-1;
    }

    public double getEmpCost() {
        return empCost;
    }

    public void setEmpCost(double empCost) {
        this.empCost = empCost;
    }

    public double getForkliftCost() {
        return forkliftCost;
    }

    public void setForkliftCost(double forkliftCost) {
        this.forkliftCost = forkliftCost;
    }

    public void addEmp(Emp emp) {
        if (emps==null){
            emps = new ArrayList<>();
        }
        emps.add(emp);
    }

    public void addCarLine(Car car) {
        this.carLine = car;
    }
}
