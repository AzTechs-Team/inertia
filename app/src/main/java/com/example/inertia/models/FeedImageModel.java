package com.example.inertia.models;

public class FeedImageModel {
    private int img;
    private String title;

    public FeedImageModel(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

}
