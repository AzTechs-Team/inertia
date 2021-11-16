package com.example.inertia.map;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.SplashScreen;
import com.example.inertia.helpers.AutoSuggestionQuery;
import com.example.inertia.profile.ProfileFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.GeoPoint;
import com.here.android.mpa.cluster.ClusterLayer;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment {
    private double initalLat, initalLng;

    public MapFragment(double initalLat, double initalLng) {
        this.initalLat = initalLat;
        this.initalLng = initalLng;
        if(this.initalLat ==0 && this.initalLng == 0){
            this.initalLat = 22.3236938;
            this.initalLng = 73.2350609;
        }
    }

    private static Map map = null;
    private AndroidXMapFragment mapFragment = null;
    private MaterialCardView mapCard;
    private TextView mapCardUsername, mapCardCaption, mapCardLocation;
    private ImageView mapCardPostPic;
    private HashMap<String,String> post;
    public static AutoSuggestionQuery enterDestinationQuery;
    static private AutoCompleteTextView searchDestination;
    public static View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapCard = rootView.findViewById(R.id.map_card);
        mapCardUsername = rootView.findViewById(R.id.map_dialog_username);
        mapCardCaption = rootView.findViewById(R.id.map_dialog_caption);
        mapCardLocation = rootView.findViewById(R.id.map_dialog_location);
        mapCardPostPic = rootView.findViewById(R.id.map_dialog_postpic);
        searchDestination = rootView.findViewById(R.id.searchDestination);

        searchDestination.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int end, int count) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int end, int count) {
                if (count % 2 == 0) {
                    if (searchDestination.getText().toString() != null && searchDestination.getText().toString().length() != 0) {
                        enterDestinationQuery = new AutoSuggestionQuery("map");
                        enterDestinationQuery.search(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });



        mapFragment = (AndroidXMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(getActivity().getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps");
        List<java.util.Map<String, Object>> postsList = SplashScreen.homeFeedPosts.posts;
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                try {
                    MapGesture.OnGestureListener onGestureListenernew = createGestureListener();
                    String scheme = Map.Scheme.NORMAL_NIGHT;
                    map = mapFragment.getMap();
                    mapFragment.getMapGesture().addOnGestureListener(onGestureListenernew, 0, false);
                    map.setMapScheme(scheme);
                    map.setCenter(new GeoCoordinate(initalLat, initalLng, 0.0), Map.Animation.LINEAR);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    map.setProjectionMode(Map.Projection.MERCATOR);
                    ClusterLayer cl = new ClusterLayer();
                    Image x = new Image();


                    for(java.util.Map<String,Object> post :postsList){
                        GeoPoint tempCoords = (GeoPoint) post.get("coords");
                        MapMarker mm = new MapMarker();
                        HashMap<String, String> temp = new HashMap<String,String>();
                        temp.put("uid",post.get("uid").toString());
                        temp.put("username",post.get("username").toString());
                        temp.put("caption",post.get("caption").toString());
                        temp.put("location",post.get("location").toString());
                        temp.put("postPic",post.get("photoURI").toString());

                        if(post.get("uid").equals(MainActivity.userProfile.user.get("uid"))) {
                            x.setImageResource(R.drawable.self_location_heart_filled);
                        }else{
                            x.setImageResource(R.drawable.location_heart_filled);
                        }
                        mm.setIcon(x);
                        mm.setDescription(temp.toString());
                        mm.setCoordinate(new GeoCoordinate(tempCoords.getLatitude(), tempCoords.getLongitude()));
                        cl.addMarker(mm);
                    }

                    map.addClusterLayer(cl);
                } catch (Throwable t) { }
            }
        });

        mapCard.setClickable(false);

        mapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.get("uid").equals(MainActivity.userProfile.user.get("uid"))){
                    Fragment fragment = new ProfileFragment("self");
                    MainActivity.loadFragment(fragment);
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.action_profile);
                }else{
                    MainActivity.getUserProfileDetails(post.get("uid"));
                }
            }
        });
        return rootView;
    }


    public static void loadAutoSuggestionItemsInMapFragment(List<String> location, List<List<GeoCoordinate>> coordinates) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (rootView.getContext(), android.R.layout.select_dialog_item, location);
        searchDestination.setThreshold(3);
        searchDestination.setAdapter(adapter);
        searchDestination.setTextColor(Color.WHITE);

        searchDestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                map.setCenter(coordinates.get(position).get(0), Map.Animation.LINEAR);
                InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }

    public MapGesture.OnGestureListener createGestureListener(){
        return new MapGesture.OnGestureListener() {
            @Override
            public void onPanStart() { }

            @Override
            public void onPanEnd() { }

            @Override
            public void onMultiFingerManipulationStart() { }

            @Override
            public void onMultiFingerManipulationEnd() { }

            @Override
            public boolean onMapObjectsSelected(@NonNull List<ViewObject> list) {
                for (ViewObject viewObject : list) {
                    if (viewObject.getBaseType() == ViewObject.Type.USER_OBJECT) {
                        MapObject mapObject = (MapObject) viewObject;

                        if (mapObject.getType() == MapObject.Type.MARKER) {
                            MapMarker window_marker = ((MapMarker)mapObject);
                            post = convertToHashmap(window_marker.getDescription());

                            Picasso.get().load(post.get("postPic")).into(mapCardPostPic);
                            String temp = post.get("username").substring(0, Math.min(post.get("username").length(), 10));
                            mapCardUsername.setText(temp.length()>= 10? temp+"...":temp);
                            temp = post.get("caption").substring(0, Math.min(post.get("caption").length(), 10));
                            mapCardCaption.setText(temp.length()>= 10? temp+"...":temp);
                            temp = post.get("location").substring(0, Math.min(post.get("location").length(), 10));
                            mapCardLocation.setText(temp.length()>= 10? temp+"...":temp);

                            mapCard.setClickable(true);
                            mapCard.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onTapEvent(PointF pointF) {
                mapCard.setVisibility(View.GONE);
                mapCard.setClickable(false);
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(PointF pointF) { return false; }

            @Override
            public void onPinchLocked() { }

            @Override
            public boolean onPinchZoomEvent(float v, PointF pointF) { return false; }

            @Override
            public void onRotateLocked() { }

            @Override
            public boolean onRotateEvent(float v) { return false; }

            @Override
            public boolean onTiltEvent(float v) { return false; }

            @Override
            public boolean onLongPressEvent(PointF pointF) { return false; }

            @Override
            public void onLongPressRelease() {}

            @Override
            public boolean onTwoFingerTapEvent(PointF pointF) { return false; }
        };
    }

    public HashMap<String,String> convertToHashmap (String value){
        value = value.substring(1, value.length()-1);
        String[] keyValuePairs = value.split(",");
        HashMap<String,String> map = new HashMap<>();

        for(String pair : keyValuePairs)
        {
            String[] entry = pair.split("=");
            if (entry[0].trim().equals("postPic")){
                final String[] temp = {""};
                Arrays.stream(entry).skip(1).forEach((i)-> temp[0] += "="+i);
                map.put(entry[0].trim(), temp[0].substring(1));
            }else{
                map.put(entry[0].trim(), entry[1].trim());
            }
        }
        return map;
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