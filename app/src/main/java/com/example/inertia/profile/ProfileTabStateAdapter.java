package com.example.inertia.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ProfileTabStateAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 2;
    public ProfileTabStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull @Override public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ProfileTravelledTab();
            case 0:
            default:
                return new ProfileFeedTab();
        }
    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}