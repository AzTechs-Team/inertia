package com.example.inertia.search;

import static com.example.inertia.MainActivity.list;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.SplashScreen;
import com.example.inertia.helpers.customListViewAdapter;
import com.example.inertia.models.UserSearchModel;
import com.example.inertia.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {


    public SearchFragment() {}

    SearchView searchView;
    ListView listView;
    customListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = (SearchView) view.findViewById(R.id.search);
        listView = (ListView) view.findViewById(R.id.search_items);

        TextView recent = (TextView) view.findViewById(R.id.recent);
        Map<String, Object> zizu_ = new HashMap<String, Object>();
        ArrayList<UserSearchModel> users = new ArrayList<UserSearchModel>();
        int i = 0;
        for (Map<String, Object> zizu: SplashScreen.allUsers) {
            zizu_.put(zizu.get("username").toString(), zizu.get("uid"));
            users.add(new UserSearchModel(zizu.get("uid").toString() ,zizu.get("username").toString(), zizu.get("name").toString(), zizu.get("photoURI").toString()));
            i++;
        }

        adapter = new customListViewAdapter((Activity) view.getContext(), list);
        listView.setAdapter(adapter);
        if(list.isEmpty()){
            recent.setVisibility(View.GONE);
        }
        listView.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s == null || s.isEmpty()){
                    listView.setVisibility(View.INVISIBLE);
                    recent.setVisibility(View.VISIBLE);
                    adapter = new customListViewAdapter((Activity) view.getContext(), list);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                    return false;
                }else {
                    recent.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    ArrayList<UserSearchModel> temp = filter_(s, users);
                    adapter = new customListViewAdapter((Activity) view.getContext(),temp);
                    listView.setAdapter(adapter);
                    return false;
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                UserSearchModel userProfile = (UserSearchModel) parent.getItemAtPosition(i);
                if(!list.contains(userProfile))
                    list.add(0, userProfile);
                else if(list.contains(userProfile)){
                    list.remove(userProfile);
                    list.add(0, userProfile);
                }
                String uid = userProfile.getUid();
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                if (uid.equals(MainActivity.userProfile.user.get("uid"))){
                    Fragment fragment = new ProfileFragment("self");
                    MainActivity.loadFragment(fragment);
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.action_profile);
                }else{
                    MainActivity.getUserProfileDetails(uid);
                }
            }
        });
        return view;
    }

    public ArrayList<UserSearchModel> filter_(String s, ArrayList<UserSearchModel> users) {
        ArrayList<UserSearchModel> temp = new ArrayList<UserSearchModel>();
        int length = users.size();
        for (int i =0; i<length; i++){
            if(users.get(i).getUserName().startsWith(s)){
                temp.add(users.get(i));
            }
        }
        return temp;
    }
}
