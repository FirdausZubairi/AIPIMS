package com.heroku.java.Model;

public class Project_Item {
    private Integer projid;
    private Integer itid;
    private Integer quantityitemproject;
    
    public Project_Item() {
    }

    public Integer getProjid() {
        return projid;
    }

    public void setProid(Integer projid) {
        this.projid = projid;
    }

    public Integer getItid() {
        return itid;
    }

    public void setItid(Integer itid) {
        this.itid = itid;
    }

    public Integer getQuantityitemproject() {
        return quantityitemproject;
    }

    public void setQuantityitemproject(Integer quantityitemproject) {
        this.quantityitemproject = quantityitemproject;
    }
}
