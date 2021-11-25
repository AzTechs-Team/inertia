package com.example.inertia.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.SplashScreen;
import com.example.inertia.auth.LoginActivity;
import com.example.inertia.helpers.RedirectToActivity;
import com.example.inertia.map.MapFragment;
import com.example.inertia.models.FeedImageModel;
import com.example.inertia.helpers.CardGridViewAdapter;
import com.example.inertia.profile.ProfileFragment;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {
    static FragmentManager currentActivityFragment;
    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        currentActivityFragment = getActivity().getSupportFragmentManager();
//        GridView gridView=(GridView) rootView.findViewById(R.id.home_feed_grid_view);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        SwipeRefreshLayout swipeRefresh;
        swipeRefresh = rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                MainActivity.refreshHomeFragment();
            }
        });
        ArrayList<FeedImageModel> feedPostsList = new ArrayList<FeedImageModel>();
        if(SplashScreen.homeFeedPosts.posts != null) {
            for (Map<String, Object> i : SplashScreen.homeFeedPosts.getPosts()) {
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
        }else{
            Button explore = (Button) rootView.findViewById(R.id.explore);
            explore.setVisibility(View.VISIBLE);
            explore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedPostsList.clear();
                    explore.setVisibility(View.GONE);
                    for (Map<String, Object> i : SplashScreen.homeFeedPosts.getPosts()) {
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
                    MainActivity.refreshHomeFragment();
                }
            });
        }

        HomeFeedAdapter adapter = new HomeFeedAdapter(getContext(), feedPostsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    static public void changeFragmentToUserProfile(){
        Fragment newFragment = new ProfileFragment("other");
        FragmentTransaction transaction = currentActivityFragment.beginTransaction();
        transaction.add(R.id.frame_container, newFragment);
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.left_padding);
        if(MainActivity.newUserProfile != null) {
            transaction.commit();
        }
    }

    static public void changeFragmentToMapProfile(double lat, double lng){
        Fragment newFragment = new MapFragment(lat, lng);
        FragmentTransaction transaction = currentActivityFragment.beginTransaction();
        transaction.add(R.id.frame_container, newFragment);
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.action_map);
        transaction.commit();
    }
}