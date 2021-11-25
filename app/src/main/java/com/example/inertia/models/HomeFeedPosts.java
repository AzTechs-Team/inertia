package com.example.inertia.models;


import java.util.List;
import java.util.Map;

public class HomeFeedPosts {
    public List<Map<String, Object>> posts;
    boolean isMainActivityLoaded = false;
    public HomeFeedPosts() { }

    public void setHomeFeedPosts(List<Map<String, Object>> posts) {
        this.posts = posts;
    }

    public boolean isMainActivityLoaded() {
        return isMainActivityLoaded;
    }

    public void setMainActivityLoaded(boolean mainActivityLoaded) {
        isMainActivityLoaded = mainActivityLoaded;
    }

    public List<Map<String, Object>> getPosts() {
        return posts;
    }
}
