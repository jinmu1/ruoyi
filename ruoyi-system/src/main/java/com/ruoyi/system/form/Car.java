package com.ruoyi.system.form;



import com.ruoyi.system.queue.Point;
import com.ruoyi.system.resource.equipment.Tray;

import java.util.Date;
import java.util.List;

/**
 *
 * car 车辆信息
 * @Auth: jinmu
 */
public class Car {
    private String carNo;//车辆编号
    private int  type;//车辆类型
    private List<Goods> goodsList;
    private int tora; //托数
    private Date arrinveTime;//到达时间
    private Date leaveTime;//离开时间
    private Point point;
    private List<Tray> trays;
    public Car() {
        super();
    }

    public Car(String carNo, int type, List<Goods> goodsList) {
        this.carNo = carNo;
        this.type = type;
        this.goodsList = goodsList;
    }

    public Car(String carNo, int type, List<Goods> goodsList, Date arrinveTime, int tora) {
        this.carNo = carNo;
        this.type = type;
        this.goodsList = goodsList;
        this.arrinveTime = arrinveTime;
        this.tora = tora;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public List<Tray> getTrays() {
        return trays;
    }

    public void setTrays(List<Tray> trays) {
        this.trays = trays;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getArrinveTime() {
        return arrinveTime;
    }

    public void setArrinveTime(Date arrinveTime) {
        this.arrinveTime = arrinveTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getTora() {
        return tora;
    }

    public void setTora(int tora) {
        this.tora = tora;
    }

    public void removeCar() {
        this.trays.remove(trays.get(0));
        this.tora=this.tora-1;
    }
}
