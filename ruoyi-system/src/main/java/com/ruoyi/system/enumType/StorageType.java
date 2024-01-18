package com.ruoyi.system.enumType;

public enum StorageType {
    地堆区域("1"),
    高位货架存储区("2"),
    立库存储区("3"),
    轻型货架存储区("4");


    private String code;
    private StorageType(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}
