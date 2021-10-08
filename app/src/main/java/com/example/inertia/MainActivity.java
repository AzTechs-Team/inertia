package com.example.inertia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mAuth = FirebaseAuth.getInstance();
//
//        button = findViewById(R.id.button);
//        textView = findViewById(R.id.textView);
//
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                String email = "test1@test.com";
//                String password = "123456";
//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(MainActivity.this, "Authentication successful.",
//                                            Toast.LENGTH_SHORT).show();
////                                    Log.d("", "createUserWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    textView.setText(user.getUid()+" " + user.getEmail());
//                                } else {
//                                    Toast.makeText(MainActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
    }
}