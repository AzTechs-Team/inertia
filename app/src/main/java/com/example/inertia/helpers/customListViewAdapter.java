package com.example.inertia.helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inertia.R;
import com.example.inertia.models.UserSearchModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class customListViewAdapter extends ArrayAdapter<UserSearchModel> {
    private ArrayList<UserSearchModel> searchUsers;
    private Activity context;
    public customListViewAdapter(Activity context, ArrayList<UserSearchModel> searchUsers){
        super(context, R.layout.row_item, searchUsers);
        this.context = context;
        this.searchUsers = searchUsers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        UserSearchModel listItem_ = getItem(position);
        row = inflater.inflate(R.layout.row_item,parent, false);
        TextView textViewUserNames = (TextView) row.findViewById(R.id.userName);
        TextView textViewNames = (TextView) row.findViewById(R.id.name);
        ImageView imageViewPhoto = (ImageView) row.findViewById(R.id.photoId);
        textViewUserNames.setText(searchUsers.get(position).getUserName());
        textViewNames.setText(searchUsers.get(position).getName());
        Picasso.get().load(listItem_.getPhotoURI()).into(imageViewPhoto);
        return row;
    }

    public ArrayList<UserSearchModel> filter_(String s) {
        ArrayList<UserSearchModel> temp = new ArrayList<UserSearchModel>();
        int length = this.searchUsers.size();
        int i = 0;
        while (i<length) {
            if(searchUsers.get(i).getUserName().startsWith(s)){
                temp.add(searchUsers.get(i));
            }
        }
        return temp;
    }
}
