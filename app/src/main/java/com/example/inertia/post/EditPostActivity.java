package com.example.inertia.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.StoreUserData;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.squareup.picasso.Picasso;

public class EditPostActivity extends AppCompatActivity {
    private EditText editCaption, editLocation;
    private Button editPost;
    private ImageView editPhoto;
    private CircularProgressIndicator spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        editCaption = findViewById(R.id.editCaption);
        editLocation = findViewById(R.id.editLocation);
        editPost = findViewById(R.id.editPost);
        editPhoto = findViewById(R.id.editPhoto);
        spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        Intent i = getIntent();
        String photoURI = i.getStringExtra("photoURI");
        String caption = i.getStringExtra("caption");
        String location = i.getStringExtra("destination");
        String id = i.getStringExtra("id");
        editCaption.setText(caption);
        editLocation.setText(location);
        Picasso.get().load(photoURI).into(editPhoto);

        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingEditPostScreen(true);
                new StoreUserData().editPostDetailsToFirestore(
                        EditPostActivity.this,
                        MainActivity.userProfile.user.get("uid").toString(),
                        photoURI,
                        id,
                        editCaption.getText().toString(),
                        editLocation.getText().toString()
                );
            }
        });
    }

    private void loadingEditPostScreen(boolean loading){
        if(loading){
            spinner.setVisibility(View.VISIBLE);
            editPost.setVisibility(View.GONE);
            editCaption.setClickable(false);
            editCaption.setFocusable(false);
            editLocation.setClickable(false);
            editLocation.setFocusable(false);
        }else{
            spinner.setVisibility(View.GONE);
            editPost.setVisibility(View.VISIBLE);
            editCaption.setClickable(true);
            editCaption.setFocusable(true);
            editLocation.setClickable(true);
            editLocation.setFocusable(true);
        }
    }
}