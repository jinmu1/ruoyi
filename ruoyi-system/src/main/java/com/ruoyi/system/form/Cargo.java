package com.ruoyi.system.form;

import com.ruoyi.system.queue.Point;

public class Cargo {
    private Point point;
    private Goods goods;
    public Cargo() {

    }

    @Override
    public String toString() {
        return "物料:" + goods.getGoodsCode()+"---坐标:" + point;
    }

    public Cargo(Point point, Goods goods) {
        this.point = point;
        this.goods = goods;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
