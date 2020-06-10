package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    static ArrayList<DrawWallpaper> Wallpaper_ARRAY;
    static int intervalTime;


    ListView listView;
    WallpaperList adpater;


    static int WIDHT;
    static int HEIGHT;



    WallpaperBD wallpaperBD;
    SQLiteDatabase database;
    Cursor cursor;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wallpaperBD = new WallpaperBD(this);
        database    = wallpaperBD.getWritableDatabase();


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WIDHT   = size.x;
        HEIGHT  = size.y;

        Button addnew = findViewById(R.id.addnew);
        Button cancel = findViewById(R.id.cancel);
        Button run = findViewById(R.id.run);

        final EditText editTime = findViewById(R.id.editTime);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editTime.getText().toString())){
                    intervalTime = 3600000;
                }else {
                    intervalTime = Integer.parseInt(editTime.getText().toString()) * 60000;
                }
                if(Wallpaper_ARRAY.size() == 1){
                    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        Typeface typeface = Typeface.createFromAsset(getAssets(), Wallpaper_ARRAY.get(0).getFontFamily());
                        myWallpaperManager.setBitmap(Wallpaper_ARRAY.get(0).createBtimapWallpaper(typeface));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Intent i = new Intent(getApplicationContext(), WallpaperService.class);
                    startService(new Intent(getApplicationContext(), WallpaperService.class));
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(), WallpaperService.class));
            }
        });

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WallpaperCreatActivity.class);
                startActivity(i);
            }
        });

        listView =  findViewById(R.id.listView);

        Wallpaper_ARRAY = new ArrayList<>();

        adpater = new WallpaperList(this, Wallpaper_ARRAY);
        listView.setAdapter(adpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemId = String.valueOf(((DrawWallpaper) parent.getItemAtPosition(position)).getId());
                Intent i = new Intent(MainActivity.this, WallpaperChangeActivity.class);
                i.putExtra("itemId", itemId);
                startActivity(i);
            }
        });

        cursor = database.query(WallpaperBD.TABLE_NAME, null,null,null,null,null,null,null);

        if(cursor.moveToFirst()){
            int id              = cursor.getColumnIndex(WallpaperBD.COLUMN_ID);
            int text            = cursor.getColumnIndex(WallpaperBD.COLUMN_TEXT);
            int fontFamily      = cursor.getColumnIndex(WallpaperBD.COLUMN_FONT_FAMILY);
            int fontsize        = cursor.getColumnIndex(WallpaperBD.COLUMN_FONT_SIZE);
            int fontColor       = cursor.getColumnIndex(WallpaperBD.COLUMN_FONT_COLOR);
            int backgroundColor = cursor.getColumnIndex(WallpaperBD.COLUMN_BACKGROUND_COLOR);
            int align           = cursor.getColumnIndex(WallpaperBD.COLUMN_ALIGN);
            int positionX       = cursor.getColumnIndex(WallpaperBD.COLUMN_POSITION_X);
            int positionY       = cursor.getColumnIndex(WallpaperBD.COLUMN_POSITION_Y);
            do {
                Wallpaper_ARRAY.add(new DrawWallpaper(
                        MainActivity.this,
                        cursor.getInt(id),
                        cursor.getString(text),
                        cursor.getString(fontFamily),
                        cursor.getInt(fontsize),
                        cursor.getInt(fontColor),
                        cursor.getInt(backgroundColor),
                        cursor.getInt(align),
                        cursor.getInt(positionX),
                        cursor.getInt(positionY)));
            } while (cursor.moveToNext());
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }

}
