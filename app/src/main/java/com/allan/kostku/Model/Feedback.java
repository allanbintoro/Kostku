package com.allan.kostku.Model;

public class Feedback {
    private String feedbackId,feedbackDesc,user, feedbackRating;
//    private int feedbackRating;

    public Feedback() {
    }

    public String getUser() {
        return user;
    }

    public Feedback(String feedbackId, String feedbackDesc, String feedbackRating, String user) {
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

    public String getFeedbackRating() {
        return feedbackRating;
    }
}
