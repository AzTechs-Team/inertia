package com.example.inertia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        userName.setText(MainActivity.userProfile.user.get("name").toString());
        bio.setText(MainActivity.userProfile.user.get("bio").toString());
        return rootView;
    }
}