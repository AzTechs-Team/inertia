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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.StoreUserData;
import com.example.inertia.models.FeedImageModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
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
            Picasso.get().load(imageModel.getImg()).into(img);
            caption.setText(imageModel.getCaption());
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
            Picasso.get().load(imageModel.getImg()).into(img);
            caption.setText(imageModel.getCaption());

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

            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ArrayList<String> likesList = imageModel.getLikes();
            LottieAnimationView imgIconLike = listitemView.findViewById(R.id.post_dialog_like_animation_view);
            ImageView unlikeIcon = listitemView.findViewById(R.id.post_dialog_unlike);
            ImageView likeIcon = listitemView.findViewById(R.id.post_dialog_like);

            final boolean[] likeStatus = {likesList.contains(currentUserID)};

            if(likeStatus[0]){
                onLike(likeIcon, unlikeIcon);
            }else{
                onUnlike(likeIcon, unlikeIcon);
            }

            imgIconLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeStatus[0] = !likeStatus[0];

                    if(likeStatus[0]){
                        imgIconLike.setSpeed(2F);
                        likesList.add(MainActivity.userProfile.user.get("uid").toString());
                    }else{
                        imgIconLike.setSpeed(-2F);
                        likesList.remove(MainActivity.userProfile.user.get("uid").toString());
                    }

                    if(likeStatus[0]){
                        Balloon balloon = new Balloon.Builder(getContext())
                            .setArrowSize(10)
                            .setArrowOrientation(ArrowOrientation.TOP)
                            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                            .setArrowPosition(0.5f)
                            .setWidth(BalloonSizeSpec.WRAP)
                            .setHeight(65)
                            .setTextSize(18f)
                            .setCornerRadius(6f)
                            .setAlpha(0.9f)
                            .setText(String.valueOf(likesList.size()))
                            .setTextColor(ContextCompat.getColor(getContext(), R.color.white))
                            .setTextIsHtml(true)
                            .setIconDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_favorite_24))
                            .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_900))
                            .setBalloonAnimation(BalloonAnimation.FADE)
                            .setAutoDismissDuration(1000L)
                            .setPaddingHorizontal(12)
                            .build();
                        balloon.showAlignTop(likeIcon);
                    }

                    new StoreUserData().updateLikesToFirestore(
                            likesList,
                            imageModel.getUid(),
                            imageModel.getId()
                    );

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
                                onLike(likeIcon, unlikeIcon);
                            }else{
                                onUnlike(likeIcon, unlikeIcon);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) { }

                        @Override
                        public void onAnimationRepeat(Animator animator) { }
                    });
                    imgIconLike.playAnimation();
                }
            });

            View cardHeader = listitemView.findViewById(R.id.card_header);

            cardHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageModel.getUid().equals(MainActivity.userProfile.user.get("uid"))){
                        Fragment fragment = new ProfileFragment("self");
                        MainActivity.loadFragment(fragment);
                        MainActivity.bottomNavigationView.setSelectedItemId(R.id.action_profile);
                    }else{
                        MainActivity.getUserProfileDetails(imageModel.getUid());
                    }
                }
            });
        }
        return listitemView;
    }

    private void onUnlike(ImageView likeIcon, ImageView unlikeIcon){
        likeIcon.setVisibility(View.GONE);
        likeIcon.setClickable(false);
        unlikeIcon.setVisibility(View.VISIBLE);
        unlikeIcon.setClickable(true);
    }

    private void onLike(ImageView likeIcon, ImageView unlikeIcon){
        unlikeIcon.setVisibility(View.GONE);
        unlikeIcon.setClickable(false);
        likeIcon.setVisibility(View.VISIBLE);
        likeIcon.setClickable(true);
    }
}
