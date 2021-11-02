package com.example.inertia.helpers;

import android.app.Activity;

public class Toaster {
    public void Toaster(){};
    public void toaster_(String ToastMessage, Activity context){
        android.widget.Toast.makeText(context, ToastMessage, android.widget.Toast.LENGTH_SHORT).show();
    }
}
