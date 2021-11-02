package com.example.inertia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.inertia.helpers.GetUserData;
import com.example.inertia.helpers.RedirectToActivity;
import com.example.inertia.models.UserProfile;
import com.example.inertia.home.HomeFragment;
import com.example.inertia.map.MapFragment;
import com.example.inertia.post.UploadPostActivity;
import com.example.inertia.profile.ProfileFragment;
import com.example.inertia.search.SearchFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public static UserProfile userProfile;
    private static Context mContext;
    private FloatingActionButton mFab;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserProfile();
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadFragment(new HomeFragment());
        mContext = this;

        mFab = findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new RedirectToActivity().redirectActivityOnly(MainActivity.this, UploadPostActivity.class);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            item.setChecked(true);
                            fragment = new HomeFragment();
                            loadFragment(fragment);
                            break;
                        case R.id.action_search:
                            item.setChecked(true);
                            fragment = new SearchFragment();
                            loadFragment(fragment);
                            break;
                        case R.id.action_map:
                            item.setChecked(true);
                            fragment = new MapFragment();
                            loadFragment(fragment);
                            break;
                        case R.id.action_profile:
                            item.setChecked(true);
                            fragment = new ProfileFragment();
                            loadFragment(fragment);
                            break;
                    }
                    return true;
                }
            });
    }

    @Override
    public void onBackPressed() {
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if (selectedItemId == R.id.action_home) {
            finish();
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setUserProfile(){
        FirebaseAuth mAuth;
        FirebaseUser user;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(user.getUid()).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> docSnap = documentSnapshot.getData();
                        userProfile = new UserProfile(docSnap);
                        new GetUserData().getPostsData(user.getUid());
                    }else{
                        user.delete();
                        logout();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("ERROR", "Error getting data", e);
                }
            });
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
        userProfile = null;
        new RedirectToActivity().redirectActivityOnly((Activity) mContext, SplashScreen.class);
    }
}