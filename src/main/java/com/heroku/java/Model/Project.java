package com.heroku.java.Model;

public class Project {
    private Integer prid;
    private String prname;
    private String prtype;

    public Project() {
    }

    public Project(Integer prid, String prname, String prtype) {
        this.prid = prid;
        this.prname = prname;
        this.prtype = prtype;
    }

    public Integer getPrid() {
        return prid;
    }

    public void setPrid(Integer prid) {
        this.prid = prid;
    }

    public String getPrname() {
        return prname;
    }

    public void setPrname(String prname) {
        this.prname = prname;
    }

    public String getPrtype() {
        return prtype;
    }

    public void setPrtype(String prtype) {
        this.prtype = prtype;
    }
    
}


