package com.example.inertia.post;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.inertia.R;
import com.example.inertia.helpers.StoreUserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UploadPostActivity extends AppCompatActivity {

    private EditText addCaption, addLocation;
    private ImageView uploadPhoto;
    private Button addPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        uploadPhoto = findViewById(R.id.uploadPhoto);
        addCaption = findViewById(R.id.addCaption);
        addLocation = findViewById(R.id.addLocation);
        addPost = findViewById(R.id.addPost);

        FirebaseAuth mAuth;
        FirebaseUser user;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        addPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri selectedImageUri = Uri.parse("android.resource://com.example.inertia/" + R.drawable.dpstock);
                new StoreUserData().addPostDataToFirebase(
                        UploadPostActivity.this,
                        selectedImageUri,
                        user.getUid(),
                        addCaption.getText().toString(),
                        addLocation.getText().toString()
                );

            }
        });
    }
}