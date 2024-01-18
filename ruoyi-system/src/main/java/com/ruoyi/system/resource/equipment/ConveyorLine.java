package com.ruoyi.system.resource.equipment;

/**
 * 输送线
 */
public class ConveyorLine {
    private double length;//管道长度
    private double speed;//输送线速度
    private double price;//单米价格


    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
