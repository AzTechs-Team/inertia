package com.example.inertia.map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inertia.R;
import com.example.inertia.SplashScreen;
import com.google.firebase.firestore.GeoPoint;
import com.here.android.mpa.cluster.ClusterLayer;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.MapMarker;

import java.io.File;
import java.util.List;

public class MapFragment extends Fragment {
    public MapFragment() { }

    private Map map = null;
    private AndroidXMapFragment mapFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (AndroidXMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);

        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(getActivity().getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps");

        List<java.util.Map<String, Object>> postsList = SplashScreen.homeFeedPosts.posts;

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                try {
                    String scheme = Map.Scheme.NORMAL_NIGHT;
                    map = mapFragment.getMap();
                    map.setMapScheme(scheme);
                    map.setCenter(new GeoCoordinate(22.3236938, 73.2350609, 0.0), Map.Animation.LINEAR);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    map.setProjectionMode(Map.Projection.MERCATOR);
                    ClusterLayer cl = new ClusterLayer();
                    Image x = new Image();
                    x.setImageResource(R.drawable.location_heart_filled);

                    for(java.util.Map<String,Object> post :postsList){
                        GeoPoint tempCoords = (GeoPoint) post.get("coords");
                        MapMarker mm = new MapMarker();
                        mm.setIcon(x);
                        mm.setCoordinate(new GeoCoordinate(tempCoords.getLatitude(), tempCoords.getLongitude()));
                        cl.addMarker(mm);
                    }
                    map.addClusterLayer(cl);
                } catch (Throwable t) {
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapFragment.onDestroy();
    }
}