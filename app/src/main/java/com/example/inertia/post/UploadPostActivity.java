package com.example.inertia.post;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.inertia.R;
import com.example.inertia.helpers.StoreUserData;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UploadPostActivity extends AppCompatActivity {

    private EditText addCaption, addLocation;
    private ImageView uploadPhoto;
    private Button addPost;
    private CircularProgressIndicator spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        uploadPhoto = findViewById(R.id.uploadPhoto);
        addCaption = findViewById(R.id.addCaption);
        addLocation = findViewById(R.id.addLocation);
        addPost = findViewById(R.id.addPost);
        spinner = findViewById(R.id.spinner);

        FirebaseAuth mAuth;
        FirebaseUser user;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        addLocation.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            addingPostData(user);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        addPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addingPostData(user);
            }
        });
    }

    private void addingPostData(FirebaseUser user){
        Uri selectedImageUri = Uri.parse("android.resource://com.example.inertia/" + R.drawable.dpstock);
        try {
            loadingUploadPostScreen(true);
            new StoreUserData().addPostDataToFirebase(
                    UploadPostActivity.this,
                    selectedImageUri,
                    user.getUid(),
                    addCaption.getText().toString(),
                    addLocation.getText().toString()
            );
        } catch (Throwable t){
            loadingUploadPostScreen(false);
        }
    }

    private void loadingUploadPostScreen(boolean loading){
        if(loading){
            spinner.setVisibility(View.VISIBLE);
            addPost.setVisibility(View.GONE);
            addCaption.setClickable(false);
            addCaption.setFocusable(false);
            addLocation.setClickable(false);
            addLocation.setFocusable(false);
        }else{
            spinner.setVisibility(View.GONE);
            addPost.setVisibility(View.VISIBLE);
            addCaption.setClickable(true);
            addCaption.setFocusable(true);
            addLocation.setClickable(true);
            addLocation.setFocusable(true);
        }
    }
}