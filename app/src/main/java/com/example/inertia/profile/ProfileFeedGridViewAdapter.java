package com.example.inertia.profile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.inertia.R;

public class ProfileFeedGridViewAdapter extends BaseAdapter {
    private android.content.Context Context;
    Integer[] imageIDs={ R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,R.drawable.dp,};

    public ProfileFeedGridViewAdapter(Context c) {
        Context = c;
    }

    @Override
    public int getCount() {
        return imageIDs.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imgg;
        if (view == null) {
            imgg = new ImageView(Context);
            imgg.setLayoutParams(new GridView.LayoutParams(400, 400));
            imgg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgg.setPadding(30, 30, 30, 30); }
        else {
            imgg = (ImageView) view;
        }
        imgg.setImageResource(imageIDs[i]);
        return imgg;
    }
}
