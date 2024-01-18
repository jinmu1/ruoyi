package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 netsend
 * 
 * @author ruoyi
 * @date 2023-01-10
 */
public class Netsend extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String 订单编号;

    /** 客户代码 */
    @Excel(name = "客户代码")
    private String 客户代码;

    /** 渠道 */
    @Excel(name = "渠道")
    private String 渠道;

    /** 线上线下 */
    @Excel(name = "线上线下")
    private String 线上线下;

    /** 区域 */
    @Excel(name = "区域")
    private String 区域;

    /** RDC城市 */
    @Excel(name = "RDC城市")
    private String RDC城市;

    /** 客户城市 */
    @Excel(name = "客户城市")
    private String 客户城市;

    /** 实际运输时间 */
    @Excel(name = "实际运输时间")
    private String 实际运输时间;

    /** 体积 */
    @Excel(name = "体积")
    private String 体积;

    /** 要求到货时间 */
    @Excel(name = "要求到货时间")
    private String 要求到货时间;

    /** 签收时间 */
    @Excel(name = "签收时间")
    private String 签收时间;

    /** 订单创建时间 */
    @Excel(name = "订单创建时间")
    private String 订单创建时间;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String 物料编码;

    /** 运输方 */
    @Excel(name = "运输方")
    private String 运输方;

    /** 订货量 */
    @Excel(name = "订货量")
    private String 订货量;

    /** 单位(箱) */
    @Excel(name = "单位(箱)")
    private String 单位1;

    /** 发货单号 */
    @Excel(name = "发货单号")
    private String 发货单号;

    /** 出库单创建时间 */
    @Excel(name = "出库单创建时间")
    private String 出库单创建时间;

    /** 发货数量 */
    @Excel(name = "发货数量")
    private String 发货数量;

    /** 单位(箱)1 */
    @Excel(name = "单位(箱)1")
    private String 单位2;

    public void set订单编号(String 订单编号) 
    {
        this.订单编号 = 订单编号;
    }

    public String get订单编号() 
    {
        return 订单编号;
    }
    public void set客户代码(String 客户代码) 
    {
        this.客户代码 = 客户代码;
    }

    public String get客户代码() 
    {
        return 客户代码;
    }
    public void set渠道(String 渠道) 
    {
        this.渠道 = 渠道;
    }

    public String get渠道() 
    {
        return 渠道;
    }
    public void set线上线下(String 线上线下) 
    {
        this.线上线下 = 线上线下;
    }

    public String get线上线下() 
    {
        return 线上线下;
    }
    public void set区域(String 区域) 
    {
        this.区域 = 区域;
    }

    public String get区域() 
    {
        return 区域;
    }
    public void setRDC城市(String RDC城市) 
    {
        this.RDC城市 = RDC城市;
    }

    public String getRDC城市() 
    {
        return RDC城市;
    }
    public void set客户城市(String 客户城市) 
    {
        this.客户城市 = 客户城市;
    }

    public String get客户城市() 
    {
        return 客户城市;
    }
    public void set实际运输时间(String 实际运输时间) 
    {
        this.实际运输时间 = 实际运输时间;
    }

    public String get实际运输时间() 
    {
        return 实际运输时间;
    }
    public void set体积(String 体积) 
    {
        this.体积 = 体积;
    }

    public String get体积() 
    {
        return 体积;
    }
    public void set要求到货时间(String 要求到货时间) 
    {
        this.要求到货时间 = 要求到货时间;
    }

    public String get要求到货时间() 
    {
        return 要求到货时间;
    }
    public void set签收时间(String 签收时间) 
    {
        this.签收时间 = 签收时间;
    }

    public String get签收时间() 
    {
        return 签收时间;
    }
    public void set订单创建时间(String 订单创建时间) 
    {
        this.订单创建时间 = 订单创建时间;
    }

    public String get订单创建时间() 
    {
        return 订单创建时间;
    }
    public void set物料编码(String 物料编码) 
    {
        this.物料编码 = 物料编码;
    }

    public String get物料编码() 
    {
        return 物料编码;
    }
    public void set运输方(String 运输方) 
    {
        this.运输方 = 运输方;
    }

    public String get运输方() 
    {
        return 运输方;
    }
    public void set订货量(String 订货量) 
    {
        this.订货量 = 订货量;
    }

    public String get订货量() 
    {
        return 订货量;
    }
    public void set单位1(String 单位1) 
    {
        this.单位1 = 单位1;
    }

    public String get单位1() 
    {
        return 单位1;
    }
    public void set发货单号(String 发货单号) 
    {
        this.发货单号 = 发货单号;
    }

    public String get发货单号() 
    {
        return 发货单号;
    }
    public void set出库单创建时间(String 出库单创建时间) 
    {
        this.出库单创建时间 = 出库单创建时间;
    }

    public String get出库单创建时间() 
    {
        return 出库单创建时间;
    }
    public void set发货数量(String 发货数量) 
    {
        this.发货数量 = 发货数量;
    }

    public String get发货数量() 
    {
        return 发货数量;
    }
    public void set单位2(String 单位2) 
    {
        this.单位2 = 单位2;
    }

    public String get单位2() 
    {
        return 单位2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("订单编号", get订单编号())
            .append("客户代码", get客户代码())
            .append("渠道", get渠道())
            .append("线上线下", get线上线下())
            .append("区域", get区域())
            .append("RDC城市", getRDC城市())
            .append("客户城市", get客户城市())
            .append("实际运输时间", get实际运输时间())
            .append("体积", get体积())
            .append("要求到货时间", get要求到货时间())
            .append("签收时间", get签收时间())
            .append("订单创建时间", get订单创建时间())
            .append("物料编码", get物料编码())
            .append("运输方", get运输方())
            .append("订货量", get订货量())
            .append("单位1", get单位1())
            .append("发货单号", get发货单号())
            .append("出库单创建时间", get出库单创建时间())
            .append("发货数量", get发货数量())
            .append("单位2", get单位2())
            .toString();
    }
}
