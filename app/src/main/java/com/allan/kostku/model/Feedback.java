package com.allan.kostku.model;

public class Feedback {
    private String feedbackId,feedbackDesc,user;
    private int feedbackRating;

    public Feedback() {
    }

    public String getUser() {
        return user;
    }

    public Feedback(String feedbackId, String feedbackDesc, int feedbackRating, String user) {
        this.feedbackId = feedbackId;
        this.feedbackDesc = feedbackDesc;
        this.feedbackRating = feedbackRating;
        this.user = user;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public String getFeedbackDesc() {
        return feedbackDesc;
    }

    public int getFeedbackRating() {
        return feedbackRating;
    }
}
