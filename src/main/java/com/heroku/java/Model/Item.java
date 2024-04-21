package com.heroku.java.Model;

public class Item {
    private Integer iId;
    private String iname;
    private Integer iquantity;
    private String icategory;
    private Integer sid;
    
    public Item() {
    }

    public Item(Integer iId, String iname, Integer iquantity, String icategory, Integer sid) {
        this.iId = iId;
        this.iname = iname;
        this.iquantity = iquantity;
        this.icategory = icategory;
        this.sid = sid;
    }

    public Item(Integer iId, String iname, Integer iquantity, String icategory) {
        this.iId = iId;
        this.iname = iname;
        this.iquantity = iquantity;
        this.icategory = icategory;
    }

    public Integer getiId() {
        return iId;
    }

    public void setiId(Integer iId) {
        this.iId = iId;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public Integer getIquantity() {
        return iquantity;
    }

    public void setIquantity(Integer iquantity) {
        this.iquantity = iquantity;
    }

    public String getIcategory() {
        return icategory;
    }

    public void setIcategory(String icategory) {
        this.icategory = icategory;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

}

