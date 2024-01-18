package com.ruoyi.system.form;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 glc_point
 * 
 * @author ruoyi
 * @date 2021-06-24
 */
public class GlcPoint extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 省份 */
    @Excel(name = "省份")
    private String provinces;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 经度 */
    @Excel(name = "经度")
    private String lat;

    /** 纬度 */
    @Excel(name = "纬度")
    private String lng;

    /** 纬度 */
    @Excel(name = "gdp")
    private String gdp;

    public void setProvinces(String provinces) 
    {
        this.provinces = provinces;
    }

    public String getProvinces() 
    {
        return provinces;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setLat(String lat) 
    {
        this.lat = lat;
    }

    public String getLat() 
    {
        return lat;
    }
    public void setLng(String lng) 
    {
        this.lng = lng;
    }

    public String getLng() 
    {
        return lng;
    }

    public GlcPoint(String provinces) {
        this.provinces = provinces;
    }
    public GlcPoint() {

    }
    public String getGdp() {
        return gdp;
    }

    public void setGdp(String gdp) {
        this.gdp = gdp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("provinces", getProvinces())
            .append("city", getCity())
            .append("lat", getLat())
            .append("lng", getLng())
            .append("gdp",getGdp())
            .toString();
    }
}
