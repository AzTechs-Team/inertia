package com.example.inertia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private static int RC_SIGN_IN=100;
    private static FirebaseAuth mAuth;
    private static FirebaseUser user;
    private static Uri photoURI;
    private GoogleSignInClient mGoogleSignInClient;
    private Button signup, viewLogin;
    private EditText name, email, password, password2;

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
                signupUser();
            }
        });

        viewLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                SignupActivity.this.finish();
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
            Toast.makeText(SignupActivity.this, "Enter valid credentials!",
                    Toast.LENGTH_SHORT).show();
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