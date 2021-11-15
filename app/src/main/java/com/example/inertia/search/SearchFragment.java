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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.inertia.MainActivity;
import com.example.inertia.R;
import com.example.inertia.SplashScreen;
import com.example.inertia.profile.ProfileFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    public SearchFragment() { }

    SearchView searchView;
    ListView listView;
    ArrayAdapter<String> adapter;

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
        List<String> usernames = new ArrayList<String>();
        Map<String, Object> zizu_ = new HashMap<String, Object>();

        for (Map<String, Object> zizu: SplashScreen.allUsers) {
            zizu_.put(zizu.get("username").toString(), zizu.get("uid"));
            usernames.add(zizu.get("username").toString());
        }

        adapter = new ArrayAdapter<String>(view.getContext(), R.layout.search_list_item, list);
        listView.setAdapter(adapter);
        if(list.isEmpty()){
            recent.setVisibility(View.GONE);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter = new ArrayAdapter<String>(view.getContext(), R.layout.search_list_item, usernames);
                listView.setAdapter(adapter);
                adapter.getFilter().filter(s);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String userProfile = parent.getItemAtPosition(i).toString();
                if(!list.contains(userProfile))
                    list.add(0, userProfile);
                else if(list.contains(userProfile)){
                    list.remove(userProfile);
                    list.add(0, userProfile);
                }
                String uid = zizu_.get(userProfile).toString();
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                adapter = new ArrayAdapter<String>(view.getContext(), R.layout.search_list_item, list);
                listView.setAdapter(adapter);
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
}
