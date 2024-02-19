package com.ruoyi.data.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * IK基础导入的类-对物料--出库频次统计
 */
public class IKAnalysisInfo {
    @Getter
    @Setter
    @JsonProperty("物料名称")
    private String materialName;
    @Getter
    @Setter
    @JsonProperty("物料编码")
    private String materialCode;
    @Getter
    @Setter
    @JsonProperty("出现次数")
    private int occurrenceCount;
    @Getter
    @Setter
    @JsonProperty("物料编号累计品目数")
    private int cumulativeItemNumber;
}
