package com.heroku.java.Model;

public class Predict {
    private Integer predictID;
    private Integer predictquan;
    private Integer proID;
    private Integer itemID;
    private Integer reqID;
    
    private String itemName;
    private Integer reqQuan;
    private Integer quanItem;
    
    public Predict() {
    }

    public Predict(Integer predictID, Integer predictquan, Integer proID, Integer itemID, Integer reqID,
            Integer reqQuan, Integer quanItem) {
        this.predictID = predictID;
        this.predictquan = predictquan;
        this.proID = proID;
        this.itemID = itemID;
        this.reqID = reqID;
        this.reqQuan = reqQuan;
        this.quanItem = quanItem;
    }

    public Predict(Integer predictquan, Integer proID, Integer itemID, Integer reqID, Integer reqQuan,
            Integer quanItem) {
        this.predictquan = predictquan;
        this.proID = proID;
        this.itemID = itemID;
        this.reqID = reqID;
        this.reqQuan = reqQuan;
        this.quanItem = quanItem;
    }

    public Predict(Integer predictquan, Integer proID, Integer itemID, Integer reqID, String itemName, Integer reqQuan,
            Integer quanItem) {
        this.predictquan = predictquan;
        this.proID = proID;
        this.itemID = itemID;
        this.reqID = reqID;
        this.itemName = itemName;
        this.reqQuan = reqQuan;
        this.quanItem = quanItem;
    }

    public Integer getPredictID() {
        return predictID;
    }

    public void setPredictID(Integer predictID) {
        this.predictID = predictID;
    }

    public Integer getPredictquan() {
        return predictquan;
    }

    public void setPredictquan(Integer predictquan) {
        this.predictquan = predictquan;
    }

    public Integer getProID() {
        return proID;
    }

    public void setProID(Integer proID) {
        this.proID = proID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Integer getReqID() {
        return reqID;
    }

    public void setReqID(Integer reqID) {
        this.reqID = reqID;
    }

    public Integer getReqQuan() {
        return reqQuan;
    }

    public void setReqQuan(Integer reqQuan) {
        this.reqQuan = reqQuan;
    }

    public Integer getQuanItem() {
        return quanItem;
    }

    public void setQuanItem(Integer quanItem) {
        this.quanItem = quanItem;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
