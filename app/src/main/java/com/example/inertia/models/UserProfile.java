package com.example.inertia.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserProfile {
    public Map<String, Object> user;
    public List<Map<String, Object>> feed;

    public UserProfile(Map user) {
        this.user = user;
        this.feed = new ArrayList<Map<String, Object>>();
    }

    public void setFeed(List<Map<String, Object>> feed) {
        this.feed = feed;
    }
}

