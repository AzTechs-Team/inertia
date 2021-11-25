package com.example.inertia.post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.AutoSuggestionQuery;
import com.example.inertia.helpers.StoreUserData;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.GeoPoint;
import com.here.android.mpa.common.GeoCoordinate;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EditPostActivity extends AppCompatActivity {
    private EditText editCaption;
    static private AutoCompleteTextView editLocation;
    private Button editPost;
    private ImageView editPhoto;
    private CircularProgressIndicator spinner;
    public static AutoSuggestionQuery editLocationQuery;
    static public GeoPoint selectedCoordinates = null;

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

        editLocation.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int end, int count) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int end, int count) {
                if (count % 2 == 0) {
                    if (editLocation.getText().toString() != null && editLocation.getText().toString().length() != 0) {
                        editLocationQuery = new AutoSuggestionQuery("edit");
                        editLocationQuery.search(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingEditPostScreen(true);
                if(selectedCoordinates != null) {
                    new StoreUserData().editPostDetailsToFirestore(
                            EditPostActivity.this,
                            MainActivity.userProfile.user.get("uid").toString(),
                            id,
                            editCaption.getText().toString(),
                            editLocation.getText().toString()
                    );
                }else{
                    loadingEditPostScreen(false);
                    Toast.makeText(getApplicationContext(), "Enter valid location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadAutoSuggestionItemsInEditPost(List<String> location, List<List<GeoCoordinate>> coordinates) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (EditPostActivity.editLocation.getContext(), android.R.layout.select_dialog_item, location);
        editLocation.setThreshold(3);
        editLocation.setAdapter(adapter);
        editLocation.setTextColor(Color.WHITE);

        editLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedCoordinates = new GeoPoint(
                        coordinates.get(position).get(0).getLatitude(),
                        coordinates.get(position).get(0).getLongitude()
                );
            }
        });
    }

    private void loadingEditPostScreen(boolean loading){
        if(loading){
            spinner.setVisibility(View.VISIBLE);
            editPost.setVisibility(View.GONE);
            editCaption.setFocusable(false);
            editLocation.setFocusable(false);
        }else{
            spinner.setVisibility(View.GONE);
            editPost.setVisibility(View.VISIBLE);
            editCaption.setClickable(true);
            editCaption.setFocusable(true);
            editLocation.setClickable(true);
            editLocation.setFocusable(true);
            editLocation.setFocusableInTouchMode(true);
            editCaption.setFocusableInTouchMode(true);
        }
    }
}