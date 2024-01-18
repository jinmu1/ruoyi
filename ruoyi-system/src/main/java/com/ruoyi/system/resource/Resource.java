package com.ruoyi.system.resource;



import java.math.BigDecimal;
import java.util.Date;

/**
 * 资源
 *
 * @author luox
 */

public class Resource {


    private Long id; //主键


    private Long batchId; //批次号


    private BigDecimal amount; //数量（人数，台数，面积）


    private Integer resType; //资源类型


    private int ability; //处理能力


    private int locomotivity; //移动能力


    private BigDecimal price; //单价（人工单价、设备单价、租赁单价）


    private Date createTime;   //上传日期


    private int operatorId;//操作人


    private String batchName; //批次名称


    private String resName; //资源类型

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Integer getResType() {
        return resType;
    }

    public void setResType(Integer resType) {
        this.resType = resType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getAbility() {
        return ability;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }

    public int getLocomotivity() {
        return locomotivity;
    }

    public void setLocomotivity(int locomotivity) {
        this.locomotivity = locomotivity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public Resource(Integer resType,BigDecimal amount,  int ability, int locomotivity, BigDecimal price) {
        this.amount = amount;
        this.resType = resType;
        this.ability = ability;
        this.locomotivity = locomotivity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", batchId=" + batchId +
                ", amount=" + amount +
                ", resType=" + resType +
                ", ability=" + ability +
                ", locomotivity=" + locomotivity +
                ", price=" + price +
                '}';
    }
}
