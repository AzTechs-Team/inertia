package com.example.inertia.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.inertia.R;
import com.example.inertia.SplashScreen;
import com.example.inertia.helpers.GetUserData;
import com.example.inertia.models.FeedImageModel;
import com.example.inertia.profile.ProfileFeedGridViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetUserData().getHomeFeedData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        GridView gridView=(GridView) rootView.findViewById(R.id.home_feed_grid_view);
        ArrayList<FeedImageModel> feedPostsList = new ArrayList<FeedImageModel>();
        List<Map<String, Object>> feedInfo = null;
        if(SplashScreen.homeFeedPosts.posts != null) {
            feedInfo = SplashScreen.homeFeedPosts.posts;
            for (Map<String, Object> i : feedInfo) {
                feedPostsList.add(
                        new FeedImageModel(
                                i.get("photoURI").toString(),
                                i.get("caption").toString(),
                                i.get("location").toString(),
                                i.get("id").toString(),
                                i.get("username").toString(),
                                i.get("userPFP").toString()
                        )
                );
            }
        }

        ProfileFeedGridViewAdapter adapter;
        adapter = new ProfileFeedGridViewAdapter(rootView.getContext(), feedPostsList, "home");
        gridView.setAdapter(adapter);

        return rootView;
    }
}