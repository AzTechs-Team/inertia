package com.example.inertia.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.inertia.MainActivity;
import com.example.inertia.profile.EditProfileActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
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

    public void uploadProfilePhotoToFirebase(Activity context, FirebaseUser user, Uri selectedImageUri, String userName, String bio, boolean didImageUpdate){
        if(didImageUpdate) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference ref = storageRef.child("images/" + user.getUid() + "/profilePic.jpg");
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
                        storeAuthDetailsToFireStore(context, user, photoURI, userName, bio);
                    }
                }
            });
        }else{
            storeAuthDetailsToFireStore(context, user, selectedImageUri, userName, bio);
        }
    }

    private boolean storeAuthDetailsToFireStore(Activity context, FirebaseUser user_, Uri photoURI , String userName, String bio){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("uid", user_.getUid());
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

    public void addPostDataToFirebase(Activity context, Uri imageUri, String uid, String caption, String location,GeoPoint coordinates){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            String timeInstance = String.valueOf(Timestamp.from(Instant.now()).getTime());
            final StorageReference ref = storageRef.child("images/" + uid + "/posts/"+ timeInstance +".jpg");
            UploadTask uploadTask = ref.putFile(imageUri);
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
                        ArrayList<String> likes = new ArrayList<String>();
                        storePostDetailsToFirestore(context, uid, photoURI, caption, location , timeInstance,likes, coordinates);
                    }
                }
            });
    }

    private void storePostDetailsToFirestore(
            Activity context, String uid, Uri photoURI , String caption, String location, String timeInstance, ArrayList<String> likes, GeoPoint coordinates){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> post = new HashMap<>();
        post.put("id",timeInstance);
        post.put("photoURI", photoURI.toString());
        post.put("caption",caption);
        post.put("location", location);
        post.put("likes", likes);
        post.put("coords",coordinates);

        db.collection("users")
                .document(uid)
                .collection("posts")
                .document(timeInstance)
                .set(post)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error adding document", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        new RedirectToActivity().redirectActivityAfterFinish(context, MainActivity.class);
                    }
                });
    }

    public void editPostDetailsToFirestore(
            Activity context, String uid , String id, String caption, String location){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> post = new HashMap<>();
        post.put("caption",caption);
        post.put("location", location);

        db.collection("users")
            .document(uid)
            .collection("posts")
            .document(id)
            .update(post)
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("", "Error adding document", e);
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    new RedirectToActivity().redirectActivityAfterFinish(context, MainActivity.class);
                }
            });
    }

    public void updateLikesToFirestore(ArrayList<String> likes, String uid, String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> post = new HashMap<>();
        post.put("likes",likes);

        db.collection("users")
                .document(uid)
                .collection("posts")
                .document(id)
                .update(post)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error reading likes", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {}
                });
    }
}
