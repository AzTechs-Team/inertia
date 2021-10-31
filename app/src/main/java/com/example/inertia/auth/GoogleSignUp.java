package com.example.inertia.auth;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.inertia.MainActivity;
import com.example.inertia.helpers.RedirectToActivity;
import com.example.inertia.helpers.StoreUserData;
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
    // TODO:
    // 1. Google Oauth not working accordingly (not important in Shreya's opinion)
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
                            if(user.getDisplayName() != null)
                                new RedirectToActivity().redirectActivityAfterFinish(context, MainActivity.class);
                            else
                                new StoreUserData().redirectToEditProfile(context);
                        } else {
                            Log.w("Error: ", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
