package com.heroku.java.Model;

public class Users {
    
    private Integer sid;
    private String name;
    private String uname;
    private String pword;
    private String roles;
    private Integer managerID;

    public Users() {

    }

    public Users(Integer sid, String name, String uname, String pword, String roles, Integer managerID)  {
        this.sid = sid;
        this.name = name;
        this.uname = uname;
        this.pword = pword;
        this.roles = roles;
        this.managerID = managerID;
    }

    public Users(Integer sid, String name, String uname, String pword, String roles)  {
        this.sid = sid;
        this.name = name;
        this.uname = uname;
        this.pword = pword;
        this.roles = roles;
    }

    public Users(String name, String uname, String pword, String roles, Integer managerID)  {
        this.name = name;
        this.uname = uname;
        this.pword = pword;
        this.roles = roles;
    }
    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getManagerID() {
        return managerID;
    }

    public void setManagerID(Integer managerID) {
        this.managerID = managerID;
    }
}
