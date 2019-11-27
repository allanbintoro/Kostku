package com.allan.kostku.Model;

import java.io.Serializable;

public class User implements Serializable {
    String userId, userName, userKtp, userEmail, userPassword, userType;

    public User() {
    }

    public User(String userId, String userName, String userKtp, String userEmail, String userPassword, String userType) {
        this.userId = userId;
        this.userName = userName;
        this.userKtp = userKtp;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserKtp() {
        return userKtp;
    }

    public void setUserKtp(String userKtp) {
        this.userKtp = userKtp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userFullName='" + userName + '\'' +
                ", userKtp='" + userKtp + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
