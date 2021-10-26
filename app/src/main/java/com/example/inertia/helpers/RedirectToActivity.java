package com.example.inertia.helpers;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inertia.profile.EditProfileActivity;

public class RedirectToActivity {
    public RedirectToActivity(){}

    public void redirectActivityOnly(Activity fromContext, Class toContext){
        Intent intent = new Intent(fromContext, toContext);
        fromContext.startActivity(intent);
    }

    public void redirectActivityAfterFinish(Activity fromContext, Class toContext){
        Intent intent = new Intent(fromContext, toContext);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        fromContext.startActivity(intent);
        fromContext.finish();
    }
}
