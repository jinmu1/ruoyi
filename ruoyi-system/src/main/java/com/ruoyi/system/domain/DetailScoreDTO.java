package com.ruoyi.system.domain;

public class DetailScoreDTO {
    public int Id;
    public String headId;
    public String name ;
    public String score ;
    public String remarks;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHeadId() {
        return headId;
    }

    public void setHeadId(String headId) {
        this.headId = headId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
