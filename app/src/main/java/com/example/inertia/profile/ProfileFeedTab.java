package com.example.inertia.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.models.FeedImageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProfileFeedTab extends Fragment {
    public ProfileFeedTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_feed_tab, container, false);
        GridView gridView=(GridView) rootView.findViewById(R.id.profile_feed_grid_view);

        ArrayList<FeedImageModel> feedPostsList = new ArrayList<FeedImageModel>();
        List<Map<String, Object>> feedInfo = null;
        if(MainActivity.userProfile.feed != null) {
            feedInfo = MainActivity.userProfile.feed;
            for (Map<String, Object> i : feedInfo) {
                feedPostsList.add(new FeedImageModel(i.get("photoURI").toString(), i.get("caption").toString(), i.get("location").toString()));
            }
        }

        ProfileFeedGridViewAdapter adapter;
        adapter = new ProfileFeedGridViewAdapter(rootView.getContext(), feedPostsList);
        gridView.setAdapter(adapter);
        return rootView;
    }
}