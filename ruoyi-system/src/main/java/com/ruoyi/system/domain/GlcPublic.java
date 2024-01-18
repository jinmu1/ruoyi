package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 百碟项目对象 glc_public
 * 
 * @author ruoyi
 * @date 2023-03-15
 */
public class GlcPublic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String name;

    /** 项目对应页面 */
    @Excel(name = "项目对应页面")
    private String arr;

    /** 账号Token */
    @Excel(name = "账号Token")
    private String Token;

    /** 对应账号信息 */
    @Excel(name = "对应账号信息")
    private String DrillID;

    /** 应用ID */
    @Excel(name = "应用ID")
    private String PatternID;

    /** 应用编码 */
    @Excel(name = "应用编码")
    private String PatternCode;

    /** 对应项目 */
    @Excel(name = "对应项目")
    private String PortIsIM;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setArr(String arr) 
    {
        this.arr = arr;
    }

    public String getArr() 
    {
        return arr;
    }
    public void setToken(String Token) 
    {
        this.Token = Token;
    }

    public String getToken() 
    {
        return Token;
    }
    public void setDrillID(String DrillID) 
    {
        this.DrillID = DrillID;
    }

    public String getDrillID() 
    {
        return DrillID;
    }
    public void setPatternID(String PatternID) 
    {
        this.PatternID = PatternID;
    }

    public String getPatternID() 
    {
        return PatternID;
    }
    public void setPatternCode(String PatternCode) 
    {
        this.PatternCode = PatternCode;
    }

    public String getPatternCode() 
    {
        return PatternCode;
    }
    public void setPortIsIM(String PortIsIM) 
    {
        this.PortIsIM = PortIsIM;
    }

    public String getPortIsIM() 
    {
        return PortIsIM;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("arr", getArr())
            .append("Token", getToken())
            .append("DrillID", getDrillID())
            .append("PatternID", getPatternID())
            .append("PatternCode", getPatternCode())
            .append("PortIsIM", getPortIsIM())
            .toString();
    }
}
