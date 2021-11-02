package com.example.inertia.models;

public class FeedImageModel {

    private String img, caption, location, id;

    public FeedImageModel(String img, String caption, String location, String id) {
        this.img = img;
        this.caption = caption;
        this.location = location;
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public String getCaption() {
        return caption;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }
}
