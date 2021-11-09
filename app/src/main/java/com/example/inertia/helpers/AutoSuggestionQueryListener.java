package com.example.inertia.helpers;

import android.util.Log;

import com.example.inertia.post.UploadPostActivity;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AutoSuggestionQueryListener implements ResultListener<List<AutoSuggest>> {
    @Override
    public void onCompleted(List<AutoSuggest> data, ErrorCode error) {
        List<String> location = new ArrayList<String>();
        List<List<GeoCoordinate>> coordinates = new ArrayList<>();
        for (AutoSuggest r : data) {
            try {
                if (r instanceof AutoSuggestPlace) {
                    List<GeoCoordinate> tempLocationCoordinates = Collections.singletonList(((AutoSuggestPlace) r).getPosition());
                    location.add(r.getTitle());
                    coordinates.add(tempLocationCoordinates);
                }
            } catch (IllegalArgumentException ex) {
                Log.e("Auto Suggest not working", "lmao indeed");
            }
        }
        UploadPostActivity.initSpinner(location,coordinates);
    }
}
