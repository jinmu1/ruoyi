package com.ruoyi.data.pcb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialPackagingInfo {
    @Getter
    @Setter
    @JsonProperty("物料编码")
    private long materialCode;
    @Getter
    @Setter
    @JsonProperty("出库数量（个）：N")
    private int outQuantityN;
    @Getter
    @Setter
    @JsonProperty("单品与箱的对应数量关系：A")
    private int correspondenceA;
    @Getter
    @Setter
    @JsonProperty("箱与托盘的对应数量关系：B")
    private int correspondenceB;
    @Getter
    @Setter
    @JsonProperty("A*B")
    private int productBoxRelation;
    @Getter
    @Setter
    @JsonProperty("N/(A*B)")
    private String outDivision;
    @Getter
    @Setter
    @JsonProperty("需求数量对应托盘数量（P）")
    private int demandPalletQuantity;
    @Getter
    @Setter
    @JsonProperty("N-M*A*B")
    private int remainder;
    @Getter
    @Setter
    @JsonProperty("(N-M*A*B)/A")
    private String divisionByA;
    @Getter
    @Setter
    @JsonProperty("需求数量对应对应箱装数量（C）")
    private int demandBoxQuantity;
    @Getter
    @Setter
    @JsonProperty("需求数量对应对应单品数量（B）")
    private int demandProductQuantity;
}
