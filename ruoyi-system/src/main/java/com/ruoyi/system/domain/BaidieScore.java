package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 baidie_score
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
public class BaidieScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 任务ID */
    @Excel(name = "任务ID")
    private String headId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 用户代码 */
    @Excel(name = "用户代码")
    private String userCode;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 成绩 */
    @Excel(name = "成绩")
    private String score;

    /** 标记 */
    @Excel(name = "标记")
    private String remarks;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setHeadId(String headId) 
    {
        this.headId = headId;
    }

    public String getHeadId() 
    {
        return headId;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setUserCode(String userCode) 
    {
        this.userCode = userCode;
    }

    public String getUserCode() 
    {
        return userCode;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setScore(String score) 
    {
        this.score = score;
    }

    public String getScore() 
    {
        return score;
    }
    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("headId", getHeadId())
            .append("userId", getUserId())
            .append("userCode", getUserCode())
            .append("name", getName())
            .append("score", getScore())
            .append("remarks", getRemarks())
            .toString();
    }
}
