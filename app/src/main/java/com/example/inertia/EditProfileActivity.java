package com.example.inertia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {
    ImageView uploadDP;
    EditText updateUsername,updateBio;
    Button editProfile;
    int SELECT_PICTURE = 200;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        uploadDP = findViewById(R.id.uploadDP);
        updateBio = findViewById(R.id.updateBio);
        updateUsername = findViewById(R.id.updateUsername);
        editProfile = findViewById(R.id.editProfile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        uploadDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(selectedImageUri == null) {
                    selectedImageUri = Uri.parse("android.resource://com.example.inertia/" + R.drawable.dpp);
                }
                if (validateInput()) {
                    new StoreUserData().uploadPhotoToFirebase(user, selectedImageUri, updateUsername.getText().toString(), updateBio.getText().toString());
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    EditProfileActivity.this.startActivity(intent);
                    EditProfileActivity.this.finish();
                }
            }
        });
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    uploadDP.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private boolean validateInput() {
        if (updateUsername.length() == 0) {
            updateUsername.setError("This field is required");
            return false;
        }
        if (updateUsername.length() >= 9) {
            updateUsername.setError("This field is TOOOOOOOOOOO LONGGGGGGGGGGGGGG");
            return false;
        }
        if (updateBio.length() == 0) {
            updateUsername.setError("This field is required");
            return false;
        }
        return true;
    }
}