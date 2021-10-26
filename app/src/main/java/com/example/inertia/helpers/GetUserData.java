package com.example.inertia.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.inertia.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GetUserData {
    public GetUserData() {}

    public void getPostsData(String uid) {
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
