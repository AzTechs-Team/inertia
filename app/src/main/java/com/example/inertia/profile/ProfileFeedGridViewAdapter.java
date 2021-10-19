package com.example.inertia.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inertia.R;
import com.example.inertia.models.FeedImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFeedGridViewAdapter extends ArrayAdapter<FeedImageModel> {
    public ProfileFeedGridViewAdapter(@NonNull Context context, ArrayList<FeedImageModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.feed_card_view, parent, false);
        }
        FeedImageModel imageModel = getItem(position);
        ImageView img = listitemView.findViewById(R.id.feed_image);
        TextView title = listitemView.findViewById(R.id.feed_image_title);
        Picasso.get().load(imageModel.getImg()).into(img);

        title.setText(imageModel.getTitle());
        return listitemView;
    }
}
