package com.heroku.java.Model;

import java.sql.Date;

public class Request {
    private Integer rid;
    private Integer sid;
    private Integer iid;
    private Date dateReq;
    private String dateNext;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private String proid;
    private Integer reqQuantity;
    private String rstatus;

    private String sname;
    private String proname;

    public Request() {
    }

    public Request(Integer rid, Integer sid, Date dateReq, String dateNext, Integer item1, Integer item2, Integer item3,
            Integer item4, Integer item5, String proid, Integer reqQuantity, String rstatus) {
        this.rid = rid;
        this.sid = sid;
        this.dateReq = dateReq;
        this.dateNext = dateNext;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.proid = proid;
        this.reqQuantity = reqQuantity;
        this.rstatus = rstatus;
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

    public Integer getItem1() {
        return item1;
    }

    public void setItem1(Integer item1) {
        this.item1 = item1;
    }

    public Integer getItem2() {
        return item2;
    }

    public void setItem2(Integer item2) {
        this.item2 = item2;
    }

    public Integer getItem3() {
        return item3;
    }

    public void setItem3(Integer item3) {
        this.item3 = item3;
    }

    public Integer getItem4() {
        return item4;
    }

    public void setItem4(Integer item4) {
        this.item4 = item4;
    }

    public Integer getItem5() {
        return item5;
    }

    public void setItem5(Integer item5) {
        this.item5 = item5;
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
}
