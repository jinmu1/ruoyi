package com.ruoyi.system.resource;


import com.ruoyi.system.enumType.CarType;

/**
 * 月台
 */
public class Platform {
    private double platform_num;//月台数量
    private double platform_area;//月台面积

    protected static double platform_width = 3.5;//月台的宽度
    protected static double platform_length = 3;//单个月台的宽度 车辆宽度一般在1.8-2米之间
    protected static double platform_length1 = 2.4;//单个月台的宽度 车辆宽度一般在1.8-2米之间
    protected static double unloading_time = 0.5;//卸货时间
    protected static double everyDay_unloading_time = 8;//日收货作业时间
    protected static double peak_rate = 1.5;//高峰概率
    protected static double volumetric_coefficient=0.8;//容积系数

    public double getPlatform_num() {
        return platform_num;
    }

    public void setPlatform_num(double platform_num) {
        this.platform_num = platform_num;
    }

    public double getPlatform_area() {
        return platform_area;
    }

    public void setPlatform_area(double platform_area) {
        this.platform_area = platform_area;
    }

    public static Platform getPlatform(double  total, String CarType){
        double car_volumetric = Double.parseDouble(com.ruoyi.system.enumType.CarType.valueOf(CarType).getCode());//计算车辆容积 通过车辆类型
        platform_width = 3 * 1.25;

        double car_num = Math.ceil(total/volumetric_coefficient/car_volumetric);//计算车辆数量 通过车辆容积
        double platform_num =  Math.ceil(car_num*peak_rate*unloading_time/everyDay_unloading_time);//月台数量
        double platform_area = platform_num*platform_width*platform_length;//月台面积
        Platform platform = new Platform();
        platform.setPlatform_num(platform_num);
        platform.setPlatform_area(platform_area);
        return platform;
    }
}
