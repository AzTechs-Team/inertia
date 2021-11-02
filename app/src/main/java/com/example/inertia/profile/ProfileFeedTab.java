package com.example.inertia.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.models.FeedImageModel;
import com.example.inertia.post.EditPostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
                feedPostsList.add(new FeedImageModel(i.get("photoURI").toString(), i.get("caption").toString(), i.get("location").toString(), i.get("id").toString()));
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
                ImageView horizontalMenu = dialog.findViewById(R.id.horizontal_menu);
                TextView caption = dialog.findViewById(R.id.post_dialog_caption);
                TextView location = dialog.findViewById(R.id.post_dialog_location);
                Picasso.get().load(item.getImg()).into(img);

                caption.setText(item.getCaption());
                location.setText(item.getLocation());

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
                                        deletePost(item.getId());
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

    private void deletePost(String pid){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(getContext());
        deleteAlertBuilder.setMessage("Are you sure you want to delete this post?");
        deleteAlertBuilder.setCancelable(true);

        deleteAlertBuilder.setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    db.collection("users")
                        .document(MainActivity.userProfile.user.get("uid").toString())
                        .collection("posts").document(pid)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Post has been deleted.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Failed to delete the post.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    StorageReference storageRef = storage.getReference();
                    StorageReference desertRef = storageRef.child("images/" + MainActivity.userProfile.user.get("uid").toString() + "/posts/" + pid + ".jpg");
                    desertRef.delete();
    //                          .addOnSuccessListener(new OnSuccessListener<Void>() {
    //                            @Override
    //                            public void onSuccess(Void aVoid) { }
    //                        }).addOnFailureListener(new OnFailureListener() {
    //                            @Override
    //                            public void onFailure(@NonNull Exception exception) {
    //                            }
    //                        });
                }
            });

        deleteAlertBuilder.setNegativeButton("No",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

        AlertDialog deleteAlert = deleteAlertBuilder.create();
        deleteAlert.show();
    }
}