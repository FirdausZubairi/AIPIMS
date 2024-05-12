package com.heroku.java.Model;

public class Request {
    private Integer rid;
    private Integer sid;
    private Integer iid;
    private String protype;
    private Integer reqQuantity;
    private String rstatus;

    public Request() {
    }

    public Request(Integer rid, String protype, Integer reqQuantity, String rstatus) {
        this.rid = rid;
        this.protype = protype;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
    }

    public Request(Integer rid, Integer sid, Integer iid, String protype, Integer reqQuantity, String rstatus) {
        this.rid = rid;
        this.sid = sid;
        this.iid = iid;
        this.protype = protype;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getProtype() {
        return protype;
    }

    public void setProtype(String protype) {
        this.protype = protype;
    }

    public Integer getReqQuantity() {
        return reqQuantity;
    }

    public void setReqQuantity(Integer reqQuantity) {
        this.reqQuantity = reqQuantity;
    }

    public String getRstatus() {
        return rstatus;
    }

    public void setRstatus(String rstatus) {
        this.rstatus = rstatus;
    }

}
