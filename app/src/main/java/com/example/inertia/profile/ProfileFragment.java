package com.example.inertia.profile;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.helpers.GetUserData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetUserData().getPostsData(MainActivity.userProfile.user.get("uid").toString());
    }

    private  ViewGroup mainContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainContainer = container;
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView userName = (TextView) rootView.findViewById(R.id.userName);
        TextView bio = (TextView) rootView.findViewById(R.id.bio);
        CircleImageView dp = (CircleImageView) rootView.findViewById(R.id.dp);
        userName.setText(MainActivity.userProfile.user.get("username").toString());
        bio.setText("\" " +MainActivity.userProfile.user.get("bio").toString() + " \"");
        Picasso.get().load(MainActivity.userProfile.user.get("photoURI").toString()).into(dp);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(MainActivity.userProfile.user.get("name").toString());
        toolbar.setPadding(60,0,0,0);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.inflateMenu(R.menu.profile_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.burgerMenu) {
                    showBottomSheetDialog();
                }
                return false;
            }
        });

        ViewPager2 viewPager =(ViewPager2) rootView.findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.parseColor("#163950"));
        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position){
                            case 1:
                                tab.setText("Destination");
                                break;
                            case 0:
                            default:
                                tab.setText("Feed");
                        }
                    }
                }).attach();

        return rootView;
    }

    private ProfileTabStateAdapter createCardAdapter() {
        ProfileTabStateAdapter adapter = new ProfileTabStateAdapter(getActivity());
        return adapter;
    }

    public void showBottomSheetDialog() {
        ProfileBottomSheetFragment bottomSheetFragment = new ProfileBottomSheetFragment();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }
}