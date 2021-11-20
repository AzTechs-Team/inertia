package com.example.inertia.post;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inertia.R;
import com.example.inertia.helpers.AutoSuggestionQuery;
import com.example.inertia.helpers.StoreUserData;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;
import com.here.android.mpa.common.GeoCoordinate;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

public class UploadPostActivity extends AppCompatActivity {

    private EditText addCaption;
    static private AutoCompleteTextView addLocation;
    private ImageView uploadPhoto;
    private Button addPost;
    private CircularProgressIndicator spinner;
    private Uri selectedImageUri;
    static public GeoPoint selectedCoordinates = null;
    public static AutoSuggestionQuery uploadLocationQuery;

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

        addLocation.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int end, int count) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int end, int count) {
                if (count % 2 == 0) {
                    if (addLocation.getText().toString() != null && addLocation.getText().toString().length() != 0) {
                        uploadLocationQuery = new AutoSuggestionQuery("upload");
                        uploadLocationQuery.search(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
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

    public static void loadAutoSuggestionItemsInUploadPost(List<String> location, List<List<GeoCoordinate>> coordinates) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (UploadPostActivity.addLocation.getContext(), android.R.layout.select_dialog_item, location);
        addLocation.setThreshold(3);
        addLocation.setAdapter(adapter);
        addLocation.setTextColor(Color.WHITE);

        addLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedCoordinates = new GeoPoint(
                        coordinates.get(position).get(0).getLatitude(),
                        coordinates.get(position).get(0).getLongitude()
                );
            }
        });
    }

    private void addingPostData(FirebaseUser user){
        try {
            loadingUploadPostScreen(true);
            if(selectedCoordinates != null) {
                new StoreUserData().addPostDataToFirebase(
                        UploadPostActivity.this,
                        selectedImageUri,
                        user.getUid(),
                        addCaption.getText().toString().trim(),
                        addLocation.getText().toString(),
                        selectedCoordinates
                );
            }else{
                loadingUploadPostScreen(false);
                Toast.makeText(getApplicationContext(), "Enter valid location", Toast.LENGTH_SHORT).show();
                addLocation.setText("");
            }
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
                    uploadPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
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