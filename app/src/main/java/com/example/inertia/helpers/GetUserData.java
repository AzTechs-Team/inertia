package com.example.inertia.helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inertia.MainActivity;
import com.example.inertia.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GetUserData {
    List<Map<String, Object>> posts;
    List <String> uids;
    List <String> allPostsIds;
    FirebaseFirestore db;
    public GetUserData() {
        this.posts =  new ArrayList<>();
        this.uids =  new ArrayList<>();
        this.allPostsIds =  new ArrayList<>();
        this.db = FirebaseFirestore.getInstance();
    }

    public void getHomeFeedData(){
        db.collection("users")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc.get("uid") != null) {
                            uids.add(doc.get("uid").toString());
                        }
                    }
                    for(String uid: uids){
                        getAllPosts(uid);
                    }
                    SplashScreen.homeFeedPosts.setHomeFeedPosts(posts);
                }
            });
    }

    private void getAllPosts(String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .document(uid).collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                        @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc.get("id") != null && allPostsIds != null&& !allPostsIds.contains(doc.get("id").toString())) {
                            posts.add(doc.getData());
                            allPostsIds.add(doc.get("id").toString());
                        }
                    }

                    for (int i = 0; i < posts.size()-1; i++)
                        for (int j = 0; j < posts.size()-i-1; j++)
                            if (Double.parseDouble((String)posts.get(j).get("id")) < Double.parseDouble((String) posts.get(j+1).get("id")))
                            {
                                Map<String,Object> temp = posts.get(j);
                                posts.set(j, posts.get(j+1));
                                posts.set(j+1, temp);
                            }
                }
            });
    }

    public void getPostsData(String uid) {
        //TODO: add onchange listener, so we dont have calls every time profile fragment is loaded
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("posts").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            temp.add(document.getData());
                        }
                        if(temp != null) {
                            Collections.reverse(temp);
                            MainActivity.userProfile.setFeed(temp);
                        }
                    }
                }
            });
    }

}
