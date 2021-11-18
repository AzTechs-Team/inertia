package com.example.inertia.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.CardGridViewAdapter;
import com.example.inertia.home.HomeFragment;
import com.example.inertia.models.FeedImageModel;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProfileTravelledTab extends Fragment {
    String id;
    public ProfileTravelledTab(String id) {
        this.id = id;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_travelled_tab, container, false);

        GridView gridView=(GridView) rootView.findViewById(R.id.profile_destination_grid_view);

        ArrayList<FeedImageModel> feedPostsList = new ArrayList<FeedImageModel>();
        List<Map<String, Object>> feedInfo = null;
        if(id=="self" && MainActivity.userProfile.feed != null) {
            feedInfo = MainActivity.userProfile.feed;
            for (Map<String, Object> i : feedInfo) {
                feedPostsList.add(
                    new FeedImageModel(
                        i.get("photoURI").toString(),
                        i.get("caption").toString(),
                        i.get("location").toString(),
                        i.get("id").toString(),
                        i.get("username").toString(),
                        i.get("userPFP").toString(),
                        (ArrayList<String>) i.get("likes"),
                        i.get("uid").toString(),
                        (GeoPoint) i.get("coords")
                    )
                );
            }
        }

        if(id=="other" && MainActivity.newUserProfile.feed != null) {
            feedInfo = MainActivity.newUserProfile.feed;
            for (Map<String, Object> i : feedInfo) {
                feedPostsList.add(
                    new FeedImageModel(
                        i.get("photoURI").toString(),
                        i.get("caption").toString(),
                        i.get("location").toString(),
                        i.get("id").toString(),
                        i.get("username").toString(),
                        i.get("userPFP").toString(),
                        (ArrayList<String>) i.get("likes"),
                        i.get("uid").toString(),
                        (GeoPoint) i.get("coords")
                    )
                );
            }
        }

        CardGridViewAdapter adapter;
        adapter = new CardGridViewAdapter(rootView.getContext(), feedPostsList,"destination");
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedImageModel item = (FeedImageModel) parent.getItemAtPosition(position);
                HomeFragment.changeFragmentToMapProfile(item.getCoords().getLatitude(), item.getCoords().getLongitude());
            }
        });
        return rootView;
    }
}