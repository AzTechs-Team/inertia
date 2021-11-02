package com.example.inertia.auth;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.inertia.helpers.StoreUserData;
import com.example.inertia.helpers.Toaster;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseSignUp {
    public FirebaseSignUp(FirebaseAuth mAuth, String name, String email, String password, Activity context){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) { }
                                        }
                                    });
                           new StoreUserData().redirectToEditProfile(context);

                        } else if(!task.isSuccessful()) {
                            Log.e("ERROOORRRRR hai bsdk: ", task.getException().toString());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                new Toaster().toaster_("Email Badly Formatted!", context);
                            } catch(FirebaseAuthUserCollisionException e) {
                                new Toaster().toaster_("The user already exists!", context);
                            } catch(Exception e) {
                                new Toaster().toaster_("Some Error Occurred!", context);
                                Log.e("ERROR: ", e.getMessage());
                            }
                            SignupActivity.loadingSignup(false);
                        }
                    }
                });
    }
}
