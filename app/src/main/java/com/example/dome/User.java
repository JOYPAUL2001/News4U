package com.example.dome;

public class User {
    private String fName, eMail;

    public User() {
    }

    public User(String fName, String eMail) {
        this.fName = fName;
        this.eMail = eMail;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
