package com.example.inertia.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.StoreUserData;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    CircleImageView uploadDP;
    EditText updateUsername,updateBio;
    Button editProfile;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private Uri selectedImageUri;
    private CircularProgressIndicator spinner;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String cameraPermission[];
    String storagePermission[];
    boolean imageUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        uploadDP = findViewById(R.id.uploadDP);
        updateBio = findViewById(R.id.updateBio);
        updateUsername = findViewById(R.id.updateUsername);
        editProfile = findViewById(R.id.editProfile);
        spinner = findViewById(R.id.spinner);

        spinner.setIndicatorSize(180);
        spinner.setTrackThickness(15);
        spinner.setVisibility(View.GONE);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        checkIfExistingProfile();

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
                    if(MainActivity.userProfile == null) {
                        imageUploaded = true;
                        selectedImageUri = Uri.parse("android.resource://com.example.inertia/" + R.drawable.dpp);
                    }else {
                        selectedImageUri = Uri.parse(MainActivity.userProfile.user.get("photoURI").toString());
                    }
                }
                if (validateInput()) {
                   loadingEditProfile(true);
                    new StoreUserData().uploadProfilePhotoToFirebase(
                            EditProfileActivity.this, user,
                            selectedImageUri,
                            updateUsername.getText().toString(),
                            updateBio.getText().toString(),
                            imageUploaded
                    );
                }else{
                   loadingEditProfile(false);
                }
            }
        });
    }

    private void loadingEditProfile(boolean loading){
        if(loading){
            spinner.setVisibility(View.VISIBLE);
            editProfile.setVisibility(View.GONE);
            updateUsername.setFocusable(false);
            updateUsername.setClickable(false);
            updateBio.setFocusable(false);
            updateBio.setClickable(false);
            uploadDP.setClickable(false);
        }else{
            spinner.setVisibility(View.GONE);
            editProfile.setVisibility(View.VISIBLE);
            updateUsername.setFocusable(true);
            updateUsername.setClickable(true);
            updateUsername.setFocusableInTouchMode(true);
            updateBio.setFocusable(true);
            updateBio.setClickable(true);
            updateBio.setFocusableInTouchMode(true);
            uploadDP.setClickable(true);
        }
    }

    private void checkIfExistingProfile(){
        if(MainActivity.userProfile != null) {
            TextView editProfileTitle = (TextView) findViewById(R.id.editProfileTitle);
            editProfileTitle.setText("Edit profile");
            Picasso.get().load(MainActivity.userProfile.user.get("photoURI").toString()).into(uploadDP);
            updateBio.setText(MainActivity.userProfile.user.get("bio").toString());
            updateUsername.setText(MainActivity.userProfile.user.get("username").toString());

        }
    }

    void imageChooser() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    private void pickFromGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setFixAspectRatio(true)
                .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                .setBorderLineColor(Color.parseColor("#38A3A5"))
                .setGuidelinesColor(Color.parseColor("#C7F9CC"))
                .setBorderCornerColor(Color.parseColor("#C7F9CC"))
                .start(EditProfileActivity.this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectedImageUri = result.getUri();
                if (null != selectedImageUri) {
                    imageUploaded = true;
                    uploadDP.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private boolean validateInput() {
        if (updateUsername.length() == 0) {
            updateUsername.setError("This field is required");
            return false;
        }else if (updateUsername.length() > 19) {
            updateUsername.setError("This field is too long");
            return false;
        }
        if (updateBio.length() == 0) {
            updateUsername.setError("This field is required");
            return false;
        }else if (updateBio.length() >= 40){
            updateBio.setError("This field is too long");
            return false;
        }
        return true;
    }
}