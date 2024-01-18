package com.ruoyi.system.domain;

import java.util.List;

public class SupplyScoreResultDataDto {

    public Boolean success;
    public List<SumScoreDTO> data ;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<SumScoreDTO> getData() {
        return data;
    }

    public void setData(List<SumScoreDTO> data) {
        this.data = data;
    }
}
