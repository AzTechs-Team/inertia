package com.example.inertia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inertia.auth.GoogleSignUp;
import com.example.inertia.auth.LoginActivity;
import com.example.inertia.auth.SignupActivity;
import com.example.inertia.helpers.GetUserData;
import com.example.inertia.helpers.RedirectToActivity;
import com.example.inertia.models.HomeFeedPosts;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Button loginBtn, signupBtn;
    private SignInButton signInWithGoogleBtn;
    private static int RC_SIGN_IN=100;
    private CircularProgressIndicator spinner;
    private TextView altTextSignIn;
    public static HomeFeedPosts homeFeedPosts;
    public static List<Map<String, Object>> allUsers;

    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);
        signInWithGoogleBtn = findViewById(R.id.sign_in_button);
        spinner = findViewById(R.id.spinner);
        altTextSignIn = findViewById(R.id.altTextSignIn);

        spinner.setIndicatorSize(180);
        spinner.setTrackThickness(15);

        loginBtn.setVisibility(View.GONE);
        signupBtn.setVisibility(View.GONE);
        signInWithGoogleBtn.setVisibility(View.GONE);
        altTextSignIn.setVisibility(View.GONE);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new RedirectToActivity().redirectActivityOnly(SplashScreen.this, LoginActivity.class);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new RedirectToActivity().redirectActivityOnly(SplashScreen.this, SignupActivity.class);
            }
        });

        signInWithGoogleBtn.setSize(SignInButton.SIZE_STANDARD);
        signInWithGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(SplashScreen.this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        if (mAuth != null) {
            currentUser = mAuth.getCurrentUser();
            homeFeedPosts = new HomeFeedPosts();
            GetUserData alUserHomeFeed = new GetUserData();
            alUserHomeFeed.getHomeFeedData();
            allUsers = alUserHomeFeed.getUsers();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    new RedirectToActivity().redirectActivityAfterFinish(SplashScreen.this, MainActivity.class);
                }else {
                    loginBtn.setVisibility(View.VISIBLE);
                    signupBtn.setVisibility(View.VISIBLE);
                    signInWithGoogleBtn.setVisibility(View.VISIBLE);
                    altTextSignIn.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = null;
            try {
                account = task.getResult(ApiException.class);
                Log.d("Success: "  , "firebaseAuthWithGoogle: " + account.getIdToken());
                new GoogleSignUp(mAuth, SplashScreen.this, account.getIdToken(), mGoogleSignInClient);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}