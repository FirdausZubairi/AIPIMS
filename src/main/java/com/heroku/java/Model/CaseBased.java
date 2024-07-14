package com.heroku.java.Model;

public class CaseBased {
    private Integer cbrID;
    private Integer predictedQuan;
    private String years;
    private Integer reqID;
    private Integer piid;

    private String itemName;
    private String projectName;
    
    public CaseBased() {
    }

    

    public CaseBased(Integer cbrID, Integer predictedQuan, String years, String itemName, String projectName) {
        this.cbrID = cbrID;
        this.predictedQuan = predictedQuan;
        this.years = years;
        this.itemName = itemName;
        this.projectName = projectName;
    }



    public CaseBased(Integer cbrID, Integer predictedQuan, String years, Integer reqID, Integer piid, String itemName,
            String projectName) {
        this.cbrID = cbrID;
        this.predictedQuan = predictedQuan;
        this.years = years;
        this.reqID = reqID;
        this.piid = piid;
        this.itemName = itemName;
        this.projectName = projectName;
    }



    public Integer getCbrID() {
        return cbrID;
    }

    public void setCbrID(Integer cbrID) {
        this.cbrID = cbrID;
    }

    public Integer getPredictedQuan() {
        return predictedQuan;
    }

    public void setPredictedQuan(Integer predictedQuan) {
        this.predictedQuan = predictedQuan;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public Integer getReqID() {
        return reqID;
    }

    public void setReqID(Integer reqID) {
        this.reqID = reqID;
    }

    public Integer getPiid() {
        return piid;
    }

    public void setPiid(Integer piid) {
        this.piid = piid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    
}
