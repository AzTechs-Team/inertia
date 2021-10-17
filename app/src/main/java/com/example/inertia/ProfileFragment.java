package com.example.inertia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

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
        CircleImageView dp = (CircleImageView) rootView.findViewById(R.id.dp);
        try {
            userName.setText(MainActivity.userProfile.user.get("username").toString());
            bio.setText(MainActivity.userProfile.user.get("bio").toString());
            Picasso.get().load(MainActivity.userProfile.user.get("photoURI").toString()).into(dp);
        }catch (Throwable t){
            Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1","nai chal raha :_)");
        }


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(MainActivity.userProfile.user.get("name").toString());
        toolbar.inflateMenu(R.menu.profile_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.starred) {
                    Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!","chal raha hai!!!!!!!!!!!!!!!!");
                }
                return false;
            }
        });

        return rootView;
    }
}