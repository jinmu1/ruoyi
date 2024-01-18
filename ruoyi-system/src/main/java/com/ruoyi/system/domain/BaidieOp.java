package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 操作对象 baidie_op
 * 
 * @author ruoyi
 * @date 2023-03-18
 */
public class BaidieOp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 选项 */
    @Excel(name = "选项")
    private String opId;

    /** 用户 */
    @Excel(name = "用户")
    private String userId;

    /** 操作 */
    @Excel(name = "操作")
    private String drillID;

    /** 值 */
    @Excel(name = "值")
    private String opValue;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOpId(String opId) 
    {
        this.opId = opId;
    }

    public String getOpId() 
    {
        return opId;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setDrillID(String drillID) 
    {
        this.drillID = drillID;
    }

    public String getDrillID() 
    {
        return drillID;
    }
    public void setOpValue(String opValue) 
    {
        this.opValue = opValue;
    }

    public String getOpValue() 
    {
        return opValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("opId", getOpId())
            .append("userId", getUserId())
            .append("drillID", getDrillID())
            .append("opValue", getOpValue())
            .toString();
    }
}
