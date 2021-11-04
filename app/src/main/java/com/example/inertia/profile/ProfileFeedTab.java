package com.example.inertia.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.DeleteUserData;
import com.example.inertia.models.FeedImageModel;
import com.example.inertia.post.EditPostActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


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
                feedPostsList.add(new FeedImageModel(i.get("photoURI").toString(), i.get("caption").toString(), i.get("location").toString(), i.get("id").toString(),i.get("username").toString(),i.get("userPFP").toString()));
            }
        }

        ProfileFeedGridViewAdapter adapter;
        adapter = new ProfileFeedGridViewAdapter(rootView.getContext(), feedPostsList,"profile");
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedImageModel item = (FeedImageModel) parent.getItemAtPosition(position);
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View dialog = factory.inflate(R.layout.post_card_view, null);

                ImageView img = dialog.findViewById(R.id.post_dialog_image);
                ImageView horizontalMenu = dialog.findViewById(R.id.horizontal_menu);
                TextView caption = dialog.findViewById(R.id.post_dialog_caption);
                CircleImageView userPFP = dialog.findViewById(R.id.post_dialog_profilepic);
                TextView username = dialog.findViewById(R.id.post_dialog_username);
                ExtendedFloatingActionButton extendedFAB = dialog.findViewById(R.id.post_dialog_location);
                Picasso.get().load(item.getImg()).into(img);
                username.setText(item.getUsername());
                Picasso.get().load(item.getUserPFP()).into(userPFP);


                caption.setText(item.getCaption());
                extendedFAB.shrink();
                extendedFAB.setText(item.getLocation());

                extendedFAB.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if(extendedFAB.isExtended()) {
                          extendedFAB.shrink();
                      } else{
                          extendedFAB.extend();
                      }
                  }
                });

                horizontalMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(), horizontalMenu);
                        popup.getMenuInflater().inflate(R.menu.edit_post_menu, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem i) {
                                String selectedItem = (String) i.getTitle();
                                switch (selectedItem){
                                    case "Edit Post":
                                        Intent intent = new Intent(getContext(), EditPostActivity.class);
                                        intent.putExtra("id",item.getId());
                                        intent.putExtra("photoURI", item.getImg());
                                        intent.putExtra("caption", item.getCaption());
                                        intent.putExtra("destination", item.getLocation());
                                        startActivity(intent);
                                        break;

                                    case "Delete Post":
                                        new DeleteUserData().deletePost((Activity) getContext(), item.getId());
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                });
                final AlertDialog postDialog = new AlertDialog.Builder(getContext()).create();
                postDialog.setView(dialog);
                postDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                postDialog.show();
            }
        });
        return rootView;
    }

}