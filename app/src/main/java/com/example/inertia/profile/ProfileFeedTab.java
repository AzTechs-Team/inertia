package com.example.inertia.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.inertia.R;


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
        gridView.setAdapter(new ProfileFeedGridViewAdapter(rootView.getContext()));
        return rootView;
    }
}