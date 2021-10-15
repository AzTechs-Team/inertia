package com.example.inertia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView userName = (TextView) rootView.findViewById(R.id.userName);
        TextView bio = (TextView) rootView.findViewById(R.id.bio);
        ImageView dp = (ImageView) rootView.findViewById(R.id.dp);
        try {
            userName.setText(MainActivity.userProfile.user.get("username").toString());
            bio.setText(MainActivity.userProfile.user.get("bio").toString());
            Picasso.get().load(MainActivity.userProfile.user.get("photoURI").toString()).into(dp);
        }catch (Throwable t){
            Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1","nai chal raha :_)");
        }
        return rootView;
    }
}