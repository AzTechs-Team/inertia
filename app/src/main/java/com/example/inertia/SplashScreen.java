package com.example.inertia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private Button button, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SplashScreen.this);
            if(account != null){
                Log.w("WAS ALREADY LOGGED IN: ", account.toString());
                Toast.makeText(SplashScreen.this, "You were logged in, sucker!", Toast.LENGTH_SHORT).show();
                redirectToDashboard();
            }
            else {
                Toast.makeText(SplashScreen.this, "You were logged in with google", Toast.LENGTH_SHORT).show();
                Log.w("WAS NOT LOGGED IN: ", account.toString());
            }
        }
        catch (Throwable t){
            Log.w("THROW KIYA: ", t.toString());
        }
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            currentUser = mAuth.getCurrentUser();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
//                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                    button.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(mainIntent);
                        }
                    });

                    button2.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Intent mainIntent = new Intent(SplashScreen.this, SignupActivity.class);
                            startActivity(mainIntent);
                        }
                    });

                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, 1000);
    }

    private void redirectToDashboard(){
        Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}