package com.example.inertia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
}