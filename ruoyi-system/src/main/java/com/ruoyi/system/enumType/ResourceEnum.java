package com.ruoyi.system.enumType;

/**
 * 资源枚举
 *
 * @author luox
 */
public enum ResourceEnum {
    /**
     * 人员资源
     */
    OPERATOR_UNLOADER(101, "卸货员", 1),
    OPERATOR_INSPECT(102, "检验员", 2),
    OPERATOR_STORE(103, "上架员", 3),
    OPERATOR_PICKUP(111, "分拣员", 4),
    OPERATOR_PACKAGE(112, "包装员", 5),
    OPERATOR_RECHECK(113, "复核员", 6),
    OPERATOR_OUT(114, "出库员", 7),
    OPERATOR_DRIVER(121, "叉车司机", 8),
    OPERATOR_DISPATCH(122, "配送员", 9),
    OPERATOR_ADMIN(123, "管理人员", 10),
    /**
     * 设备资源
     */
    EQUIPMENT_SALVER(201, "木质托盘", 1),
    EQUIPMENT_PICKUP_TRUCK(202, "拣货车", 2),
    EQUIPMENT_HAND_TRUCK(203, "手叉车", 3),
    EQUIPMENT_ELECTRIC_TRUCK(204, "电叉车", 4),
    EQUIPMENT_STORE(205, "存储设备", 5),
    EQUIPMENT_OTHER_CASE(206, "其它物流容器", 6),

    /**
     * 设施资源
     */
    WORKSPACE_UNLOAD(301, "卸货区", 1),
    WORKSPACE_INSPECT(302, "检验区", 2),
    WORKSPACE_STORE(303, "存储区", 3),
    WORKSPACE_PICKUP(304, "分拣区", 4),
    WORKSPACE_PACKAGE(304, "包装区", 5),
    WORKSPACE_RECHECK(305, "复核区", 6),
    WORKSPACE_OUT(306, "出库区", 7),
    WORKSPACE_OFFICE(307, "办公区", 8);

    private final int type;
    private final String desc;
    private final int cellIndex;

    ResourceEnum(int type, String desc, int cellIndex) {
        this.type = type;
        this.desc = desc;
        this.cellIndex = cellIndex;
    }



    /**
     * 根据value值获取枚举对象
     *
     * @param type
     * @return
     */
    public static ResourceEnum valueOf(int type) {
        ResourceEnum[] instances = ResourceEnum.values();
        for (ResourceEnum instance : instances) {
            if (instance.type == type) {
                return instance;
            }
        }
        throw new RuntimeException("value=" + type + "transform to ResourceEnum failed!");
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public int getCellIndex() {
        return cellIndex;
    }
}

