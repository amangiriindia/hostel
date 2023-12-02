package com.amzsoft.hostel.Model;

public class DailyItemModel {
    String heading;
    String text;
    String img_url;

    public DailyItemModel() {
    }

    public DailyItemModel(String heading, String text, String img_url) {
        this.heading = heading;
        this.text = text;
        this.img_url = img_url;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}

