package com.example.inertia.models;

public class FeedImageModel {

    private String img, caption, location;

    public FeedImageModel(String img, String caption, String location) {
        this.img = img;
        this.caption = caption;
        this.location = location;
    }

    public String getImg() {
        return img;
    }

    public String getCaption() {
        return caption;
    }

    public String getLocation() {
        return location;
    }
}
