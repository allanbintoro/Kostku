package com.allan.kostku.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.HashMap;

public class Report implements Serializable {
    private String reportId, reportTitle, reportDesc, user;
    private HashMap<String, Object> timestampCreated;

    public Report() {
    }

    public Report(String reportId, String reportTitle, String reportDesc,String user) {
        this.reportId = reportId;
        this.reportTitle = reportTitle;
        this.reportDesc = reportDesc;
        this.user = user;
        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampNow;
    }

    public String getReportId() {
        return reportId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public String getReportDesc() {
        return reportDesc;
    }

    public String getUser() {
        return user;
    }

    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }
    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }
}
