package com.allan.kostku.Model;

import java.io.Serializable;

public class Feedback implements Serializable {
    private String feedbackId, feedbackDesc, feedbackScore;
    private User user;

    public Feedback() {
    }

    public Feedback(String feedbackId, String feedbackDesc, String feedbackScore, User user) {
        this.feedbackId = feedbackId;
        this.feedbackDesc = feedbackDesc;
        this.feedbackScore = feedbackScore;
        this.user = user;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackDesc() {
        return feedbackDesc;
    }

    public void setFeedbackDesc(String feedbackDesc) {
        this.feedbackDesc = feedbackDesc;
    }

    public String getFeedbackScore() {
        return feedbackScore;
    }

    public void setFeedbackScore(String feedbackScore) {
        this.feedbackScore = feedbackScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
