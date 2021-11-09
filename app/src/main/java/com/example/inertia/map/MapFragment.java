package com.example.inertia.map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inertia.R;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.AndroidXMapFragment;

import java.io.File;

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

        mapFragment =  (AndroidXMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(getActivity().getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps");

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                try{
                    String scheme= Map.Scheme.NORMAL_NIGHT;
                    map = mapFragment.getMap();
                    map.setMapScheme(scheme);
                    map.setCenter(new GeoCoordinate(22.3236938,73.2350609, 0.0), Map.Animation.LINEAR);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    map.setProjectionMode(Map.Projection.MERCATOR);
                } catch (Throwable t){
                    Log.e("ERROR: Cannot initialize Map Fragment",t.toString());
                }
            }
        });
        return rootView;
    }
}