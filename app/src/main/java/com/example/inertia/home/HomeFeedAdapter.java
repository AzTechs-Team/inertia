package com.example.inertia.home;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.GetUserData;
import com.example.inertia.helpers.StoreUserData;
import com.example.inertia.models.FeedImageModel;
import com.example.inertia.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>{
    private ArrayList<FeedImageModel> feedData;
    private Context context;

    public HomeFeedAdapter(Context context, ArrayList<FeedImageModel> listdata) {
        this.feedData = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.post_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FeedImageModel post = feedData.get(position);
        holder.username.setText(post.getUsername());
        Picasso.get().load(post.getUserPFP()).into(holder.userPFP);

        holder.location.setText(post.getLocation());
        holder.location.shrink();

        String temp = post.getCaption().substring(0, Math.min(post.getCaption().length(), 20));
        holder.caption.setText(temp.length()>= 20? temp+"...":temp);

        Picasso.get().load(post.getImg()).into(holder.image);

        holder.horizontal_menu.setVisibility(View.INVISIBLE);
        holder.horizontal_menu.setClickable(false);

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.location.isExtended()) {
                    holder.location.shrink();
                } else{
                    holder.location.extend();
                }
            }
        });

        handleLikesUI(holder, post);
    }

    private void handleLikesUI(ViewHolder holder, FeedImageModel post){
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ArrayList<String> likesList= post.getLikes();


        final boolean[] likeStatus = {likesList.contains(currentUserID)};
        if(likeStatus[0]){
            onLike(holder.likeIcon, holder.unlikeIcon);
        }else{
            onUnlike(holder.likeIcon, holder.unlikeIcon);
        }

        holder.imgIconLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeStatus[0] = !likeStatus[0];

                if(likeStatus[0]){
                    holder.imgIconLike.setSpeed(2F);
                    likesList.add(MainActivity.userProfile.user.get("uid").toString());
                }else{
                    holder.imgIconLike.setSpeed(-2F);
                    likesList.remove(MainActivity.userProfile.user.get("uid").toString());
                }

                if(likeStatus[0]){
                    Balloon balloon = new Balloon.Builder(context)
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
                            .setTextColor(ContextCompat.getColor(context, R.color.white))
                            .setTextIsHtml(true)
                            .setIconDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24))
                            .setBackgroundColor(ContextCompat.getColor(context, R.color.blue_900))
                            .setBalloonAnimation(BalloonAnimation.FADE)
                            .setAutoDismissDuration(1000L)
                            .setPaddingHorizontal(12)
                            .build();
                    balloon.showAlignTop(holder.likeIcon);
                }

                new StoreUserData().updateLikesToFirestore(
                        likesList,
                        post.getUid(),
                        post.getId()
                );

                holder.imgIconLike.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        holder.unlikeIcon.setVisibility(View.GONE);
                        holder.likeIcon.setVisibility(View.GONE);
                        holder.unlikeIcon.setClickable(false);
                        holder.likeIcon.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if(likeStatus[0]){
                            onLike(holder.likeIcon, holder.unlikeIcon);
                        }else{
                            onUnlike(holder.likeIcon, holder.unlikeIcon);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
                holder.imgIconLike.playAnimation();
            }
        });

        holder.cardHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getUid().equals(MainActivity.userProfile.user.get("uid"))){
                    Fragment fragment = new ProfileFragment("self");
                    MainActivity.loadFragment(fragment);
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.action_profile);
                }else{
                    MainActivity.getUserProfileDetails(post.getUid());
                }
            }
        });

        holder.cardBody.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LayoutInflater factory = LayoutInflater.from(context);
                final View dialog = factory.inflate(R.layout.post_stats_card_view, null);
                ArrayList<String> UserLikeList = GetUserData.getUserName(likesList);
                try {
                    ListView likeId = dialog.findViewById(R.id.likesId);
                    TextView caption = dialog.findViewById(R.id.post_stats_caption);
                    TextView likedBy = dialog.findViewById(R.id.post_stats_likes);
                    ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.search_list_item, UserLikeList);
                    likeId.setAdapter(adapter);
                    caption.setText(post.getCaption());
                    likedBy.setText("\uD83D\uDC96 Liked by "+UserLikeList.size());
                }
                catch (Throwable err){
                }
                final AlertDialog postDialog = new AlertDialog.Builder(context).create();
                postDialog.setView(dialog);
                postDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                postDialog.show();
                return false;
            }
        });
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

    @Override
    public int getItemCount() {
        return feedData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userPFP, image, horizontal_menu, unlikeIcon, likeIcon;
        public TextView username, caption;
        public ExtendedFloatingActionButton location;
        public LottieAnimationView imgIconLike;
        public View cardHeader, cardBody;

        public ViewHolder(View itemView) {
            super(itemView);
            this.userPFP = (ImageView) itemView.findViewById(R.id.post_dialog_profilepic);
            this.username = (TextView) itemView.findViewById(R.id.post_dialog_username);
            this.image = (ImageView) itemView.findViewById(R.id.post_dialog_image);
            this.caption = (TextView) itemView.findViewById(R.id.post_dialog_caption);
            this.location = (ExtendedFloatingActionButton) itemView.findViewById(R.id.post_dialog_location);
            this.horizontal_menu = (ImageView) itemView.findViewById(R.id.horizontal_menu);

            this.imgIconLike = (LottieAnimationView) itemView.findViewById(R.id.post_dialog_like_animation_view);
            this.unlikeIcon = (ImageView) itemView.findViewById(R.id.post_dialog_unlike);
            this.likeIcon = (ImageView) itemView.findViewById(R.id.post_dialog_like);

            this.cardHeader = (View) itemView.findViewById(R.id.card_header);
            this.cardBody = (View) itemView.findViewById(R.id.card);
        }
    }
}