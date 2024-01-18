package com.ruoyi.system.resource.equipment;


import com.ruoyi.system.form.Goods;

import java.util.List;

/**
 * 单拖数量
 */
public class Plat {
    private List<Goods> goodsList;
    private double volume;//体积
    private double weight;//重量
    private static double maxVolume = 1.1*1.2*1.5;




    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public static double getMaxVolume() {
        return maxVolume;
    }

    public static void setMaxVolume(double maxVolume) {
        Plat.maxVolume = maxVolume;
    }
}
