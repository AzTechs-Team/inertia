package com.example.inertia.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inertia.R;
import com.example.inertia.helpers.RedirectToActivity;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private static int RC_SIGN_IN=100;
    private FirebaseAuth mAuth;
    private static Button signup, viewLogin;
    private static EditText name, email, password, password2;
    private static CircularProgressIndicator spinner;
    private static TextView altTextLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        signup = findViewById(R.id.signup);
        viewLogin = findViewById(R.id.viewLogin);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);

        spinner = findViewById(R.id.spinner);
        altTextLogin = findViewById(R.id.altTextLogin);

        spinner.setIndicatorSize(180);
        spinner.setTrackThickness(15);

        spinner.setVisibility(View.GONE);

        password2.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            signupUser();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loadingSignup(true);
                signupUser();
            }
        });

        viewLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new RedirectToActivity().redirectActivityAfterFinish(SignupActivity.this, LoginActivity.class);
            }
        });
    }

    private void signupUser(){
        if(validateInput()) {
            new FirebaseSignUp(
                    mAuth,
                    name.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    SignupActivity.this
            );
        }else{
            loadingSignup(false);
            Toast.makeText(SignupActivity.this, "Enter valid credentials!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void loadingSignup(boolean loading){
        if(loading){
            spinner.setVisibility(View.VISIBLE);
            signup.setVisibility(View.GONE);
            viewLogin.setVisibility(View.GONE);
            altTextLogin.setVisibility(View.GONE);
            name.setFocusable(false);
            name.setClickable(false);
            email.setFocusable(false);
            email.setClickable(false);
            password.setFocusable(false);
            password.setClickable(false);
            password2.setFocusable(false);
            password2.setClickable(false);
        }else{
            spinner.setVisibility(View.GONE);
            signup.setVisibility(View.VISIBLE);
            viewLogin.setVisibility(View.VISIBLE);
            altTextLogin.setVisibility(View.VISIBLE);
            name.setFocusable(true);
            name.setClickable(true);
            name.setFocusableInTouchMode(true);
            email.setFocusable(true);
            email.setClickable(true);
            email.setFocusableInTouchMode(true);
            password.setFocusable(true);
            password.setClickable(true);
            password.setFocusableInTouchMode(true);
            password2.setFocusable(true);
            password2.setClickable(true);
            password2.setFocusableInTouchMode(true);
        }
    }

    private boolean validateInput() {
        if (name.length() == 0) {
            name.setError("This field is required");
            return false;
        }

        if (email.length() == 0) {
            email.setError("This field is required");
            return false;
        }

        if (password.length() == 0) {
            password.setError("This field is required");
            return false;
        } else if(password2.length() == 0){
            password2.setError("This field is required");
            return false;
        } else if (password.length() < 6) {
            password.setError("Password must be minimum 6 characters");
            return false;
        } else if(!password.getText().toString().equals(password2.getText().toString())){
            password2.setError("Password doesn't match!");
            return false;
        }
        return true;
    }
}