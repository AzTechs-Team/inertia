package com.example.inertia.helpers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inertia.MainActivity;
import com.example.inertia.SplashScreen;
import com.example.inertia.home.HomeFragment;
import com.example.inertia.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
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
    public static List <Map<String, Object>> users;
    List <String> allPostsIds;
    FirebaseFirestore db;

    public GetUserData() {
        this.posts =  new ArrayList<>();
        this.users =  new ArrayList<>();
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
                            users.add(doc.getData());
                        }
                    }
                    for(Map<String, Object> user: users){
                        getAllPosts(user);
                    }
                }
            });
    }

    private void getAllPosts(Map<String,Object> user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .document(user.get("uid").toString()).collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                        @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }

                    List<DocumentChange> x = value.getDocumentChanges();
                    for(DocumentChange y: x){
                        QueryDocumentSnapshot doc = y.getDocument();
                        Map<String, Object> meta = doc.getData();
                        meta.put("username", user.get("username").toString());
                        meta.put("userPFP", user.get("photoURI").toString());
                        meta.put("uid", user.get("uid").toString());

                        if (y.getType().toString().equals("ADDED")) {
                            posts.add(meta);
                            allPostsIds.add(doc.get("id").toString());
                            sortAndUpdatePosts("ADDED");
                        } else if (y.getType().toString().equals("MODIFIED")) {
                            findAndDeletePost(doc);
                            posts.add(meta);
                            sortAndUpdatePosts("MODIFIED");
                        } else if (y.getType().toString().equals("REMOVED")) {//removed
                            findAndDeletePost(doc);
                            allPostsIds.remove(doc.getData().get("id"));
                            sortAndUpdatePosts("REMOVED");
                        }

                    }
                }
            });
    }

    public void findAndDeletePost(QueryDocumentSnapshot doc){
        String id = (String) doc.getData().get("id");
        Map<String, Object> temp= null;
        for(Map<String, Object>post: posts){
            if(id.equals(post.get("id").toString())){
                temp = post;
            }
        }
        posts.remove(temp);
    }

    public void sortAndUpdatePosts(String id){
        for (int i = 0; i < posts.size()-1; i++)
            for (int j = 0; j < posts.size()-i-1; j++)
                if (Double.parseDouble((String)posts.get(j).get("id")) < Double.parseDouble((String) posts.get(j+1).get("id")))
                {
                    Map<String,Object> temp = posts.get(j);
                    posts.set(j, posts.get(j+1));
                    posts.set(j+1, temp);
                }
        SplashScreen.homeFeedPosts.setHomeFeedPosts(posts);
        if(SplashScreen.homeFeedPosts.isMainActivityLoaded() && id.equals("MODIFIED")) {
            MainActivity.refreshHomeFragment();
        }

    }

    public void getPostsData(String uid, String username, String photoURI, String profileId) {
        //TODO: add onchange listener, so we dont have calls every time profile fragment is loaded
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("posts").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String,Object> meta = document.getData();
                            meta.put("username",username);
                            meta.put("userPFP",photoURI);
                            meta.put("uid",uid);
                            temp.add(meta);
                        }
                        if(temp != null) {
                            Collections.reverse(temp);
                            if(profileId == "self") {
                                MainActivity.userProfile.setFeed(temp);
                                Fragment fragment = new ProfileFragment("self");
                                MainActivity.loadFragment(fragment);
                            }
                            if(profileId == "other") {
                                MainActivity.newUserProfile.setFeed(temp);
                                HomeFragment.changeFragmentToUserProfile();
                            }
                        }
                    }
                }
            });
    }

    public static ArrayList<String> getUserName(ArrayList<String> uid) {
        ArrayList<String> userNames = new ArrayList<String>();
        users = SplashScreen.allUsers;
        for(int i =0; i < uid.size(); i++) {
            for (int j = 0; j < users.size(); j++) {
                if(users.get(j).get("uid").equals(uid.get(i))){
                    userNames.add(users.get(j).get("username").toString());
                }
            }
        }
        return userNames;
    }

    public List<Map<String, Object>> getUsers() {
        return users;
    }
}
