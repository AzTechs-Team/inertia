package com.example.inertia.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.inertia.R;
import com.example.inertia.models.FeedImageModel;

import java.util.ArrayList;


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
        ArrayList<FeedImageModel> courseModelArrayList = new ArrayList<FeedImageModel>();
        courseModelArrayList.add(new FeedImageModel(R.drawable.dpstock,"123"));
        courseModelArrayList.add(new FeedImageModel(R.drawable.dpstock,"143"));
        courseModelArrayList.add(new FeedImageModel(R.drawable.dpstock,"12343"));
        courseModelArrayList.add(new FeedImageModel(R.drawable.dpstock,"1232e2"));
        courseModelArrayList.add(new FeedImageModel(R.drawable.dpstock,"12323421432"));
        courseModelArrayList.add(new FeedImageModel(R.drawable.dpstock,"123aghmhjerr"));

        ProfileFeedGridViewAdapter adapter = new ProfileFeedGridViewAdapter(rootView.getContext(), courseModelArrayList);
        gridView.setAdapter(adapter);
        return rootView;
    }
}