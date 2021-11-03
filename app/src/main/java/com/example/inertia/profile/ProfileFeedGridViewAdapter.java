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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFeedGridViewAdapter extends ArrayAdapter<FeedImageModel> {
    String id;
    public ProfileFeedGridViewAdapter(@NonNull Context context, ArrayList<FeedImageModel> courseModelArrayList, String id) {
        super(context, 0, courseModelArrayList);
        this.id = id;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            if(id == "profile")
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.feed_card_view, parent, false);
            else
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.post_card_view, parent, false);

        }
        FeedImageModel imageModel = getItem(position);
        ImageView img;
        TextView caption;
        if(id == "profile") {
            img = listitemView.findViewById(R.id.feed_image);
            caption = listitemView.findViewById(R.id.feed_image_title);
        }else {
            img = listitemView.findViewById(R.id.post_dialog_image);
            caption = listitemView.findViewById(R.id.post_dialog_caption);
            ExtendedFloatingActionButton location = listitemView.findViewById(R.id.post_dialog_location);
            ImageView horizontal_menu = listitemView.findViewById(R.id.horizontal_menu);

            horizontal_menu.setVisibility(View.INVISIBLE);
            horizontal_menu.setClickable(false);

            location.setText(imageModel.getLocation());
            location.shrink();

            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(location.isExtended()) {
                        location.shrink();
                    } else{
                        location.extend();
                    }
                }
            });


        }
        Picasso.get().load(imageModel.getImg()).into(img);
        caption.setText(imageModel.getCaption());
        return listitemView;
    }
}
