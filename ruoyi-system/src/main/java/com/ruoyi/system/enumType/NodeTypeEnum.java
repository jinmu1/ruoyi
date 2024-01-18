package com.ruoyi.system.enumType;

/**
 * 网络节点类型枚举
 *
 * @author luox
 */
public enum NodeTypeEnum {
    /**
     * 网络资源
     */
    FACTORY(1, "生产工厂"),
    DISTRIBUTE_CENTER(2, "配送中心"),
    CUSTOM(3, "客户"),;
    public final int type;
    public final String desc;

    NodeTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据value值获取枚举对象
     *
     * @param type
     * @return
     */
    public static NodeTypeEnum valueOf(int type) {
        NodeTypeEnum[] instances = NodeTypeEnum.values();
        for (NodeTypeEnum instance : instances) {
            if (instance.type == type) {
                return instance;
            }
        }
        throw new RuntimeException("value=" + type + "transform to NodeTypeEnum failed!");
    }
}

