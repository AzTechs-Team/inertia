package com.example.inertia;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignUp {
    private FirebaseAuth mAuth;
    private Activity context;

    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    // TODO:
    // 1. Add toast to remind please login
    // 2. Login with Google on Google Login Page
//        updateUI(account);

    public GoogleSignUp(FirebaseAuth mAuth, Activity context, String idToken) {
        this.mAuth = mAuth;
        this.context = context;
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Success: ", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            new StoreUserData().uploadPhotoToFirebase(user);
                            new StoreUserData().redirectToDashboard(context);
                        } else {
                            Log.w("Error: ", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}