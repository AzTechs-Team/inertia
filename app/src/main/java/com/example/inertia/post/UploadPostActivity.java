package com.example.inertia.post;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UploadPostActivity extends AppCompatActivity {

    private EditText addCaption, addLocation;
    private ImageView uploadPhoto;
    private Button addPost;
    private CircularProgressIndicator spinner;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        uploadPhoto = findViewById(R.id.uploadPhoto);
        addCaption = findViewById(R.id.addCaption);
        addLocation = findViewById(R.id.addLocation);
        addPost = findViewById(R.id.addPost);
        spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        addPost.setClickable(false);
        addPost.setEnabled(false);
        addPost.setAlpha(0.5f);

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
                if (validateInput())
                    addingPostData(user);
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }



    private void addingPostData(FirebaseUser user){
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
            uploadPhoto.setClickable(false);
        }else{
            spinner.setVisibility(View.GONE);
            addPost.setVisibility(View.VISIBLE);
            addCaption.setClickable(true);
            addCaption.setFocusable(true);
            addLocation.setClickable(true);
            addLocation.setFocusable(true);
            uploadPhoto.setClickable(true);
        }
    }

    void imageChooser() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pickFromGallery();
            }
        });
        builder.create().show();
    }


    private void pickFromGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .setBorderLineColor(Color.parseColor("#38A3A5"))
                .setGuidelinesColor(Color.parseColor("#C7F9CC"))
                .setBorderCornerColor(Color.parseColor("#C7F9CC"))
                .start(UploadPostActivity.this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectedImageUri = result.getUri();
                if (null != selectedImageUri) {
                    uploadPhoto.setImageURI(selectedImageUri);
                    addPost.setEnabled(true);
                    addPost.setClickable(true);
                    addPost.setAlpha(1);
                }
            }
        }
    }

    private boolean validateInput() {
        if (addLocation.length() == 0) {
            addLocation.setError("This field is required");
            return false;
        }
        return true;
    }
}