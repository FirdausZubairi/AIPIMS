package com.heroku.java.Model;

public class RequestDetail {
    private Integer requestId;
    private Integer projectId;
    private Integer reqQuantity;
    private String status;
    private Integer itemId;
    private Integer projectQuantity;
    private String itemName;
    private String projectName;
    private Integer itemQuantity;

    public RequestDetail() {
    }

    public RequestDetail(Integer requestId, Integer projectId, Integer reqQuantity, String status, Integer itemId, Integer projectQuantity, String itemName, String projectName, Integer itemQuantity) {
        this.requestId = requestId;
        this.projectId = projectId;
        this.reqQuantity = reqQuantity;
        this.status = status;
        this.itemId = itemId;
        this.projectQuantity = projectQuantity;
        this.itemName = itemName;
        this.projectName = projectName;
        this.itemQuantity = itemQuantity;
    }

    public RequestDetail(Integer requestId, Integer projectId, Integer reqQuantity, String status, Integer itemId, Integer projectQuantity) {
        this.requestId = requestId;
        this.projectId = projectId;
        this.reqQuantity = reqQuantity;
        this.status = status;
        this.itemId = itemId;
        this.projectQuantity = projectQuantity;
    }

    public RequestDetail(Integer requestId, Integer projectId, Integer reqQuantity, String status, Integer itemId, Integer projectQuantity, String itemName, String projectName) {
        this.requestId = requestId;
        this.projectId = projectId;
        this.reqQuantity = reqQuantity;
        this.status = status;
        this.itemId = itemId;
        this.projectQuantity = projectQuantity;
        this.itemName = itemName;
        this.projectName = projectName;
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

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getReqQuantity() {
        return reqQuantity;
    }

    public void setReqQuantity(Integer reqQuantity) {
        this.reqQuantity = reqQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getProjectQuantity() {
        return projectQuantity;
    }

    public void setProjectQuantity(Integer projectQuantity) {
        this.projectQuantity = projectQuantity;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    
}
