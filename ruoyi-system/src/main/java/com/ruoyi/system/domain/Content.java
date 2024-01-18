package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 内容对象 content
 * 
 * @author ruoyi
 * @date 2021-01-15
 */
public class Content extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 0-视频,1-pdf,2-工具 */
    @Excel(name = "0-视频,1-pdf,2-工具")
    private Long gwType;

    /** $column.columnComment */
    @Excel(name = "0-视频,1-pdf,2-工具")
    private Long sort;

    /** 内容 */
    @Excel(name = "内容")
    private String title;

    /** 图片链接 */
    @Excel(name = "图片链接")
    private String imgUrl;

    /** 链接1 */
    @Excel(name = "链接1")
    private String url1;

    /** 链接2 */
    @Excel(name = "链接2")
    private String url2;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGwType(Long gwType) 
    {
        this.gwType = gwType;
    }

    public Long getGwType() 
    {
        return gwType;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setImgUrl(String imgUrl) 
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() 
    {
        return imgUrl;
    }
    public void setUrl1(String url1) 
    {
        this.url1 = url1;
    }

    public String getUrl1() 
    {
        return url1;
    }
    public void setUrl2(String url2) 
    {
        this.url2 = url2;
    }

    public String getUrl2() 
    {
        return url2;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createBy", getCreateBy())
            .append("gwType", getGwType())
            .append("sort", getSort())
            .append("title", getTitle())
            .append("imgUrl", getImgUrl())
            .append("url1", getUrl1())
            .append("url2", getUrl2())
            .append("createDate", getCreateDate())
            .toString();
    }
}
