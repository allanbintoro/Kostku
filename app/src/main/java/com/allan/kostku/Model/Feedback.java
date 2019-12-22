package com.allan.kostku.Model;

import java.io.Serializable;

public class Feedback implements Serializable {
    private String feedbackId, feedbackDesc, kostId, roomId, userId;
    private float feedbackRating;
    private Long feedbackDate;

    public Feedback() {
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

    public String getKostId() {
        return kostId;
    }

    public void setKostId(String kostId) {
        this.kostId = kostId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(float feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public Long getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Long feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId='" + feedbackId + '\'' +
                ", feedbackDesc='" + feedbackDesc + '\'' +
                ", kostId='" + kostId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                ", feedbackRating=" + feedbackRating +
                ", feedbackDate=" + feedbackDate +
                '}';
    }
}
