package com.amzsoft.hostel.Model;

public class ContactItemModel {
    String img_url;
    String phone_number;
    String name;

    public ContactItemModel() {
    }

    public ContactItemModel(String img_url, String phone_number, String name) {
        this.img_url = img_url;
        this.phone_number = phone_number;
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
