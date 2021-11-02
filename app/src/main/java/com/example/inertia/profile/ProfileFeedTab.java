package com.example.inertia.profile;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.models.FeedImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

        ArrayList<FeedImageModel> feedPostsList = new ArrayList<FeedImageModel>();
        List<Map<String, Object>> feedInfo = null;
        if(MainActivity.userProfile.feed != null) {
            feedInfo = MainActivity.userProfile.feed;
            for (Map<String, Object> i : feedInfo) {
                feedPostsList.add(new FeedImageModel(i.get("photoURI").toString(), i.get("caption").toString(), i.get("location").toString()));
            }
        }

        ProfileFeedGridViewAdapter adapter;
        adapter = new ProfileFeedGridViewAdapter(rootView.getContext(), feedPostsList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedImageModel item = (FeedImageModel) parent.getItemAtPosition(position);
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View dialog = factory.inflate(R.layout.post_card_view, null);

                ImageView img = dialog.findViewById(R.id.post_dialog_image);
                TextView caption = dialog.findViewById(R.id.post_dialog_caption);
                TextView location = dialog.findViewById(R.id.post_dialog_location);
                Picasso.get().load(item.getImg()).into(img);

                caption.setText(item.getCaption());
                location.setText(item.getLocation());

                final AlertDialog postDialog = new AlertDialog.Builder(getContext()).create();
                postDialog.setView(dialog);
                postDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                postDialog.show();
            }
        });

        return rootView;
    }
}