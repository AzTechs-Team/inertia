package com.example.inertia.profile;

import android.app.Activity;
import android.os.Bundle;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.RedirectToActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileBottomSheetFragment extends BottomSheetDialogFragment {
    public ProfileBottomSheetFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_bottom_sheet_dialog, container, false);

        TextView editProfileOption = (TextView) rootView.findViewById(R.id.profile_bottom_menu_one);
        TextView logoutOption = (TextView) rootView.findViewById(R.id.profile_bottom_menu_three);

        editProfileOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.bottomSheetFragment.dismiss();
                new RedirectToActivity().redirectActivityOnly((Activity) getContext(), EditProfileActivity.class);
            }
        });

        logoutOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.bottomSheetFragment.dismiss();
                MainActivity.logout();
            }
        });

        return rootView;
    }
}