package com.example.inertia.models;

public class FeedImageModel {

    private String img, caption, location, id, username, userPFP;

    public FeedImageModel(String img, String caption, String location, String id, String username, String userPFP) {
        this.img = img;
        this.caption = caption;
        this.location = location;
        this.id = id;
        this.username = username;
        this.userPFP = userPFP;
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

    public String getUsername() {
        return username;
    }

    public String getUserPFP() {
        return userPFP;
    }
}
