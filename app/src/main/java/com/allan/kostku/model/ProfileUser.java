package com.allan.kostku.model;

public class ProfileUser {
    private String userName, userDOB, userStatusRel;
    private int userKTP;

    public ProfileUser() {
    }

    public ProfileUser(String userName, String userDOB, String userStatusRel, int userKTP) {
        this.userName = userName;
        this.userDOB = userDOB;
        this.userStatusRel = userStatusRel;
        this.userKTP = userKTP;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public String getUserStatusRel() {
        return userStatusRel;
    }

    public int getUserKTP() {
        return userKTP;
    }
}
