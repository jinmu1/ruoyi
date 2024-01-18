package com.ruoyi.system.queue;

import java.util.List;

/**
 * 作业清单
 */
public class Work {
    private List<Order> workOrder;//安排的工作计划


    public List<Order> getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(List<Order> workOrder) {
        this.workOrder = workOrder;
    }
}
