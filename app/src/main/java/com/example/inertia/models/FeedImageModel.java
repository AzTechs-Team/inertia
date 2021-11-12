package com.example.inertia.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class FeedImageModel {

    private String img, caption, location, id, username, userPFP, uid;
    private ArrayList<String> likes;
    private GeoPoint coords;

    public FeedImageModel(
            String img, String caption, String location, String id, String username, String userPFP, ArrayList<String> likes, String uid, GeoPoint coords) {
        this.img = img;
        this.caption = caption;
        this.location = location;
        this.id = id;
        this.username = username;
        this.userPFP = userPFP;
        this.likes = likes;
        this.uid = uid;
        this.coords = coords;
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

    public ArrayList<String> getLikes() {
        return likes;
    }

    public String getUid() {
        return uid;
    }

    public GeoPoint getCoords() { return coords; }
}
