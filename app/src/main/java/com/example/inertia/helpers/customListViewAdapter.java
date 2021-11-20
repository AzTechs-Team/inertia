package com.example.inertia.helpers;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inertia.R;
import com.example.inertia.models.userSearchModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class customListViewAdapter extends ArrayAdapter<userSearchModel> {
    private ArrayList<userSearchModel> searchUsers;
    private Activity context;
    public customListViewAdapter(Activity context, ArrayList<userSearchModel> searchUsers){
        super(context, R.layout.row_item, searchUsers);
        this.context = context;
        this.searchUsers = searchUsers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        userSearchModel listItem_ = getItem(position);
        row = inflater.inflate(R.layout.row_item,parent, false);
        TextView textViewUserNames = (TextView) row.findViewById(R.id.userName);
        TextView textViewNames = (TextView) row.findViewById(R.id.name);
        ImageView imageViewPhoto = (ImageView) row.findViewById(R.id.photoId);
        textViewUserNames.setText(searchUsers.get(position).getUserName());
        textViewNames.setText(searchUsers.get(position).getName());
        Picasso.get().load(listItem_.getPhotoURI()).into(imageViewPhoto);
        return row;
    }

    public ArrayList<userSearchModel> filter_(String s) {
        ArrayList<userSearchModel> temp = new ArrayList<userSearchModel>();
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
