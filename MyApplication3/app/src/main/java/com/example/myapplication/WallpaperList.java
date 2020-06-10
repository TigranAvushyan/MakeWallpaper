package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

public class WallpaperList extends BaseAdapter {

    private ArrayList<DrawWallpaper> wallpaper_list;
    private LayoutInflater layoutInflater;

    private SQLiteDatabase database;
    private Context context;
    private int id;

    WallpaperList(Context context, ArrayList<DrawWallpaper> wallpaper_list) {
        WallpaperBD wallpaperBD = new WallpaperBD(context);
        database = wallpaperBD.getWritableDatabase();
        this.context = context;
        this.wallpaper_list = wallpaper_list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return wallpaper_list.size();
    }

    @Override
    public DrawWallpaper getItem(int position) {
        return wallpaper_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null){
            view = layoutInflater.inflate(R.layout.list_wallpaper, parent, false);
        }
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(getItem(position).getText());
        return view;
    }
}
