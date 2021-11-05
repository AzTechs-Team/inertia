package com.example.inertia.profile;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.example.inertia.R;
import com.example.inertia.models.FeedImageModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        CircleImageView userPFP;
        TextView caption, username;
        if(id == "profile") {
            img = listitemView.findViewById(R.id.feed_image);
            caption = listitemView.findViewById(R.id.feed_image_title);

        }else {
            img = listitemView.findViewById(R.id.post_dialog_image);
            caption = listitemView.findViewById(R.id.post_dialog_caption);
            username = listitemView.findViewById(R.id.post_dialog_username);
            userPFP = listitemView.findViewById(R.id.post_dialog_profilepic);
            ExtendedFloatingActionButton location = listitemView.findViewById(R.id.post_dialog_location);
            ImageView horizontal_menu = listitemView.findViewById(R.id.horizontal_menu);

            horizontal_menu.setVisibility(View.INVISIBLE);
            horizontal_menu.setClickable(false);

            username.setText(imageModel.getUsername());
            Picasso.get().load(imageModel.getUserPFP()).into(userPFP);

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

            LottieAnimationView imgIconLike = listitemView.findViewById(R.id.post_dialog_like_animation_view);
            ImageView unlikeIcon = listitemView.findViewById(R.id.post_dialog_unlike);
            ImageView likeIcon = listitemView.findViewById(R.id.post_dialog_like);

            likeIcon.setVisibility(View.GONE);
            likeIcon.setClickable(false);

            final boolean[] likeStatus = {false};
            imgIconLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeStatus[0] = !likeStatus[0];
                    if(likeStatus[0]){
                        imgIconLike.setSpeed(2F);
                    }else{
                        imgIconLike.setSpeed(-2F);
                    }

                    imgIconLike.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            unlikeIcon.setVisibility(View.GONE);
                            likeIcon.setVisibility(View.GONE);
                            unlikeIcon.setClickable(false);
                            likeIcon.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if(likeStatus[0]){
                                unlikeIcon.setVisibility(View.GONE);
                                unlikeIcon.setClickable(false);
                                likeIcon.setVisibility(View.VISIBLE);
                                likeIcon.setClickable(true);
                            }else{
                                likeIcon.setVisibility(View.GONE);
                                likeIcon.setClickable(false);
                                unlikeIcon.setVisibility(View.VISIBLE);
                                unlikeIcon.setClickable(true);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                    imgIconLike.playAnimation();
                }
            });

        }
        Picasso.get().load(imageModel.getImg()).into(img);
        caption.setText(imageModel.getCaption());
        return listitemView;
    }
}
