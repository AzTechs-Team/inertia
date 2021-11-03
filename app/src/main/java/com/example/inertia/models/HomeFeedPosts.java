package com.example.inertia.models;


import java.util.List;
import java.util.Map;

public class HomeFeedPosts {
    public List<Map<String, Object>> posts;

    public HomeFeedPosts() { }

    public void setHomeFeedPosts(List<Map<String, Object>> posts) {
        this.posts = posts;
    }
}
