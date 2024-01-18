package com.ruoyi.system.enumType;

/**
 * 车辆类型枚举类
 */
public enum CarType {
    小车4米6("12"),
    中车7米2("24"),
    大车9米6("32"),
    超大车17米5("64");


    private String code;
    private CarType(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}
