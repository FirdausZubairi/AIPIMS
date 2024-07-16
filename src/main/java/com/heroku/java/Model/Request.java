package com.heroku.java.Model;

import java.sql.Date;

public class Request {
    private Integer rid;
    private Integer sid;
    private Integer iid;
    private Date dateReq;
    private String dateNext;
    private String proid;
    private Integer reqQuantity;
    private String rstatus;

    private String sname;
    private String proname;
    private String iname;
    private Integer iquantity;
    private Integer piid;

    public Request() {
    }

    public Request(Integer rid, String proid, Integer reqQuantity, String rstatus, String proname, String iname,
            Integer iquantity, Integer piid) {
        this.rid = rid;
        this.proid = proid;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
        this.proname = proname;
        this.iname = iname;
        this.iquantity = iquantity;
        this.piid = piid;
    }



    public Request(Integer rid, String proid, Integer reqQuantity, String rstatus, String proname, Integer piid) {
        this.rid = rid;
        this.proid = proid;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
        this.proname = proname;
        this.piid = piid;
    }

    public Request(Integer rid, String sname, String proname, Integer reqQuantity, String rstatus) {
        this.rid = rid;
        this.sname = sname;
        this.proname = proname;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
    }


    public Request (String proid, String proname, Integer reqQuantity) {
        this.proid = proid;
        this.proname = proname;
        this.reqQuantity = reqQuantity;
    }

    public Request(Integer rid, String proid, Integer reqQuantity, String rstatus) {
        this.rid = rid;
        this.proid = proid;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
    }

    public Request(Integer rid, Integer sid, Integer iid, String proid, Integer reqQuantity, String rstatus) {
        this.rid = rid;
        this.sid = sid;
        this.iid = iid;
        this.proid = proid;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
    }

    public Request(Integer rid, Integer sid, String proid, String proname, Integer reqQuantity, String rstatus) {
        this.rid = rid;
        this.sid = sid;
        this.proid = proid;
        this.proname = proname;
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

    public Date getDateReq() {
        return dateReq;
    }

    public void setDateReq(Date dateReq) {
        this.dateReq = dateReq;
    }

    public String getDateNext() {
        return dateNext;
    }

    public void setDateNext(String dateNext) {
        this.dateNext = dateNext;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }
    
    public String getProid() {
        return proid;
    }

    public void setProid(String proid) {
        this.proid = proid;
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

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public Integer getPiid() {
        return piid;
    }

    public void setPiid(Integer piid) {
        this.piid = piid;
    }

    public Integer getIquantity() {
        return iquantity;
    }

    public void setIquantity(Integer iquantity) {
        this.iquantity = iquantity;
    }
    
}
