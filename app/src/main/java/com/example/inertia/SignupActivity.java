package com.example.inertia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
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
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Authentication successful.",
                                        Toast.LENGTH_SHORT).show();
//                                    FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                startActivity(intent);
                                SignupActivity.this.finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "User already exists!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
        } else if (password.length() < 5) {
            password.setError("Password must be minimum 6 characters");
            return false;
        } else if(!password.getText().toString().equals(password2.getText().toString())){
            password2.setError("Password doesn't match!");
            return false;
        }
        return true;
    }
}