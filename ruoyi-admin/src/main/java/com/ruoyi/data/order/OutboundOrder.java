package com.ruoyi.data.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OutboundOrder {
    @JsonProperty("订单创建日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    @Excel(name = "订单创建日期")
    private Date orderCreateDate;
    @Excel(name = "客户代码")
    @JsonProperty("客户代码")
    private String customerCode;
    @Excel(name = "客户指定送达方门店代码")
    @JsonProperty("客户指定送达方门店代码")
    private String specifiedDeliveryStoreCode;
    @Excel(name = "客户订单编码")
    @JsonProperty("客户订单编码")
    private String customerOrderCode;
    @Excel(name = "物料编码")
    @JsonProperty("物料编码")
    private String materialCode;
    @Excel(name = "物料描述")
    @JsonProperty("物料描述")
    private String materialDescription;
    @Excel(name = "销售单价")
    @JsonProperty("销售单价")
    private Double unitPrice;
    @Excel(name = "订购数量")
    @JsonProperty("订购数量")
    private Integer orderQuantity;
    @Excel(name = "单位名称")
    @JsonProperty("单位名称")
    private String unitName;
    @Excel(name = "单位代码")
    @JsonProperty("单位代码")
    private String unitCode;
    @Excel(name = "工厂代码")
    @JsonProperty("工厂代码")
    private String factoryCode;
    @JsonProperty("订单出库日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    @Excel(name = "订单出库日期")
    private Date orderOutboundDate;
    @Excel(name = "销售组织")
    @JsonProperty("销售组织")
    private String salesOrganization;
    @Excel(name = "一级分类")
    @JsonProperty("一级分类")
    private String primaryCategory;
    @Excel(name = "二级分类")
    @JsonProperty("二级分类")
    private String secondaryCategory;
    @Excel(name = "三级级分类")
    @JsonProperty("三级级分类")
    private String tertiaryCategory;
    @Excel(name = "四级分类")
    @JsonProperty("四级分类")
    private String quaternaryCategory;
}
