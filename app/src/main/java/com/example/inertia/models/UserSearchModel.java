package com.example.inertia.models;

import java.util.Map;

public class UserSearchModel {
    public String uid;
    public String userName;
    public String name;
    public String photoURI;

    public UserSearchModel(String uid, String userName, String name, String photoURI){
        this.uid = uid;
        this.userName = userName;
        this.name = name;
        this.photoURI = photoURI;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public String getUserName() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }
}
