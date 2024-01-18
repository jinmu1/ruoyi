package com.ruoyi.system.domain;

import java.util.List;

public class SumScoreDTO {
    public String Id ;
    public String SumScore ;
    public String UserId ;
    public String UserCode;
    public List<DetailScoreDTO> DetailScore ;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSumScore() {
        return SumScore;
    }

    public void setSumScore(String sumScore) {
        SumScore = sumScore;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public List<DetailScoreDTO> getDetailScore() {
        return DetailScore;
    }

    public void setDetailScore(List<DetailScoreDTO> detailScore) {
        DetailScore = detailScore;
    }
}
