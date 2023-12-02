package com.amzsoft.hostel.Model;

public class ServiceItemsModel {
    String img_url;
    String distance;
    String heading;
    String description;

    public ServiceItemsModel() {
    }

    public ServiceItemsModel(String img_url, String distance, String heading, String description) {
        this.img_url = img_url;
        this.distance = distance;
        this.heading = heading;
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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
