package com.amzsoft.hostel.Model;

public class FeedbackModel {
    String feedbackType;
    String heading;
    String description;

    public FeedbackModel(String feedbackType, String heading, String description) {
        this.feedbackType = feedbackType;
        this.heading = heading;
        this.description = description;
    }

    public FeedbackModel() {
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
