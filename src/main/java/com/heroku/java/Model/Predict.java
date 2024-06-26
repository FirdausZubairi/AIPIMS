package com.heroku.java.Model;

public class Predict {
    private Integer predictID;
    private Integer predictquan;
    private String proID;
    private String itemID;
    private Integer reqID;
    private String year;

    private String itemName;
    private String proName;
    private Integer reqQuan;
    private Integer quanItem;
    
    public Predict() {
    }
    
    public Predict(Integer predictquan, String itemID, Integer reqID, String year, String itemName, String proName) {
        this.predictquan = predictquan;
        this.itemID = itemID;
        this.reqID = reqID;
        this.year = year;
        this.itemName = itemName;
        this.proName = proName;
    }



    public Predict(Integer predictquan, String proName, String itemID, Integer reqID,
            String itemName) {
        this.predictquan = predictquan;
        this.proName = proName;
        this.itemID = itemID;
        this.reqID = reqID;
        this.itemName = itemName;
    }



    public Predict(Integer predictID, Integer predictquan, String proID, String itemID, Integer reqID,
            Integer reqQuan, Integer quanItem) {
        this.predictID = predictID;
        this.predictquan = predictquan;
        this.proID = proID;
        this.itemID = itemID;
        this.reqID = reqID;
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

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
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

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
