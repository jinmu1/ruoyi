package com.ruoyi.system.domain.eiq;

import com.fasterxml.jackson.annotation.JsonProperty;

public  class IKAnalysisTable {
    @JsonProperty("物料名称")
    private String materialName;

    @JsonProperty("物料编码")
    private String materialCode;

    @JsonProperty("出现次数")
    private int occurrenceCount;

    @JsonProperty("物料编号累计品目数")
    private int cumulativeItemNumber;

    // Getter and Setter methods

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public void setOccurrenceCount(int occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }

    public int getCumulativeItemNumber() {
        return cumulativeItemNumber;
    }

    public void setCumulativeItemNumber(int cumulativeItemNumber) {
        this.cumulativeItemNumber = cumulativeItemNumber;
    }
}
