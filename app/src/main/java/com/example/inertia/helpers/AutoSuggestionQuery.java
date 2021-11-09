package com.example.inertia.helpers;

import android.util.Log;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

public class AutoSuggestionQuery {
    public void AutoSuggestQuery(){ }

    public void search(String query) {
        if (query != null && query!= "") {
            TextAutoSuggestionRequest request = null;
            request = new TextAutoSuggestionRequest(query).setSearchCenter(new GeoCoordinate(22.3236938, 73.2350609, 0.0));
            request.execute(new AutoSuggestionQueryListener());
        }
    }
}