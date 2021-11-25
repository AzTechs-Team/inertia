package com.example.inertia.helpers;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteUserData {

    public void DeleteUserData(){};

    public void deletePost(Activity context, String pid){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(context);

        TextView textView = new TextView(context);
        textView.setText("Are you sure you want to delete this post?");
        textView.setTextColor(Color.WHITE);
        textView.setPadding(24, 36, 20, 34);
        textView.setTextSize(18F);

        deleteAlertBuilder.setCustomTitle(textView);
        deleteAlertBuilder.setCancelable(true);

        deleteAlertBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.collection("users")
                                .document(MainActivity.userProfile.user.get("uid").toString())
                                .collection("posts").document(pid)
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Post has been deleted.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Failed to delete the post.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        StorageReference storageRef = storage.getReference();
                        StorageReference desertRef = storageRef.child("images/" + MainActivity.userProfile.user.get("uid").toString() + "/posts/" + pid + ".jpg");
                        desertRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new RedirectToActivity().redirectActivityOnly((Activity) context, MainActivity.class);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    }
                });

        deleteAlertBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog deleteAlert = deleteAlertBuilder.create();
        deleteAlert.show();
        deleteAlert.getWindow().setBackgroundDrawableResource(R.color.primary_color);

        Button positiveButton = deleteAlert.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#FFFF0400"));
        positiveButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"));
        positiveButton.setPadding(15,0,15,0);

        Button negativeButton = deleteAlert.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#ffffff"));
    }
}
