package com.ruoyi.data.pcb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MaterialOutboundTypeInfo {
    @Getter
    @Setter
    @JsonProperty("物料编码")
    private long materialCode;
    @Getter
    @Setter
    @JsonProperty("出库数量对应托盘数量")
    private int palletQuantity;
    @Getter
    @Setter
    @JsonProperty("出库数量对应对应箱装数量")
    private int boxQuantity;
    @Getter
    @Setter
    @JsonProperty("出库数量对应对应单品数量")
    private int productQuantity;
    @Getter
    @Setter
    @JsonProperty("物料对应出库环节物流包装单元模式判断")
    private String packagingMode;
}
