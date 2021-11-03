package com.example.inertia.auth;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.inertia.MainActivity;
import com.example.inertia.helpers.RedirectToActivity;
import com.example.inertia.helpers.StoreUserData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GoogleSignUp {
    private FirebaseAuth mAuth;
    private Activity context;
    private GoogleSignInClient mGoogleSignInClient;
    public GoogleSignUp(FirebaseAuth mAuth, Activity context, String idToken, GoogleSignInClient mGoogleSignInClient) {
        this.mAuth = mAuth;
        this.context = context;
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mGoogleSignInClient.signOut();
                            Log.d("Success: ", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference doc = db.collection("users").document(user.getUid());
                            doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            new RedirectToActivity().redirectActivityAfterFinish(context, MainActivity.class);
                                        }
                                        else{
                                            new StoreUserData().redirectToEditProfile(context);
                                        }
                                    }
                                }
                            });
                        } else {
                            Log.w("Error: ", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
