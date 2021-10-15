package com.example.inertia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class StoreUserData {
    public StoreUserData(){}

    public void redirectToEditProfile(Activity context){
        Intent intent = new Intent(context, EditProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        context.startActivity(intent);
        context.finish();
    }

    public void uploadPhotoToFirebase(Activity context, FirebaseUser user, Uri selectedImageUri, String userName, String bio){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child("images/" + user.getUid() + ".jpg");
        UploadTask uploadTask = ref.putFile(selectedImageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri photoURI = task.getResult();
                    storeFireStore(context, user, photoURI ,userName,bio);
                }
            }
        });
    }

    private boolean storeFireStore(Activity context, FirebaseUser user_, Uri photoURI , String userName, String bio){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("name", user_.getDisplayName());
        user.put("username",userName);
        user.put("email", user_.getEmail());
        user.put("bio", bio);
        user.put("photoURI",photoURI.toString());

        db.collection("users")
                .document(user_.getUid())
                .set(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error adding document", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.w("RUNNING", "Firebase user stored, yey!");
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        context.startActivity(intent);
                        context.finish();
                    }
                });
        return true;
    }
}
