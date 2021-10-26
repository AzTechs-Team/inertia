package com.example.inertia.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.RedirectToActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button login, viewSignup;
    private EditText email, password;
    private CircularProgressIndicator spinner;
    private TextView altTextSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.login);
        viewSignup = findViewById(R.id.viewSignup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        spinner = findViewById(R.id.spinner);
        altTextSignUp = findViewById(R.id.altTextSignUp);

        spinner.setIndicatorSize(180);
        spinner.setTrackThickness(15);

        spinner.setVisibility(View.GONE);

        password.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            loginUser();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loadingLoginScreen(true);
                loginUser();
            }
        });

        viewSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new RedirectToActivity().redirectActivityAfterFinish(LoginActivity.this, SignupActivity.class);
            }
        });
    }

    private void loginUser(){
        if(validateInput()){
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                new RedirectToActivity().redirectActivityAfterFinish(LoginActivity.this, MainActivity.class);
                            } else {
                                loadingLoginScreen(false);
                                Toast.makeText(LoginActivity.this, "Login failed! Enter valid credentials and try again!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            loadingLoginScreen(false);
            Toast.makeText(LoginActivity.this, "Enter valid credentials!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loadingLoginScreen(boolean loading){
        if(loading){
            spinner.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            viewSignup.setVisibility(View.GONE);
            altTextSignUp.setVisibility(View.GONE);
            email.setClickable(false);
            email.setFocusable(false);
            password.setClickable(false);
            password.setFocusable(false);
        }else{
            spinner.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            viewSignup.setVisibility(View.VISIBLE);
            altTextSignUp.setVisibility(View.VISIBLE);
            email.setClickable(true);
            email.setFocusable(true);
            email.setFocusableInTouchMode(true);
            password.setClickable(true);
            password.setFocusable(true);
            password.setFocusableInTouchMode(true);
        }
    }

    private boolean validateInput() {
        if (email.length() == 0) {
            email.setError("This field is required");
            return false;
        }
        if (password.length() == 0) {
            password.setError("This field is required");
            return false;
        } else if (password.length() < 5) {
            password.setError("Password must be minimum 6 characters");
            return false;
        }
        return true;
    }
}