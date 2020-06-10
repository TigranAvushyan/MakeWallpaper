package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WallpaperBD extends SQLiteOpenHelper {

    public static final String DATABASE_NAME            = "mww.db";
    public static final int DATABASE_VERSION            = 5;
    public static final String TABLE_NAME               = "wallpaper";

    public static final String COLUMN_ID                = "_id";
    public static final String COLUMN_TEXT              = "title";
    public static final String COLUMN_FONT_FAMILY       = "fontFamily";
    public static final String COLUMN_FONT_SIZE         = "fontsize";
    public static final String COLUMN_FONT_COLOR        = "fontColor";
    public static final String COLUMN_BACKGROUND_COLOR  = "backgroundColor";
    public static final String COLUMN_ALIGN             = "align";
    public static final String COLUMN_POSITION_X        = "positionX";
    public static final String COLUMN_POSITION_Y        = "positionY";



    WallpaperBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEXT                 + " TEXT, " +
                COLUMN_FONT_FAMILY          + " TEXT, " +
                COLUMN_FONT_SIZE            + " INTEGER, " +
                COLUMN_FONT_COLOR           + " INTEGER, " +
                COLUMN_BACKGROUND_COLOR     + " INTEGER, " +
                COLUMN_ALIGN                + " INTEGER, " +
                COLUMN_POSITION_X           + " INTEGER, " +
                COLUMN_POSITION_Y           + " INTEGER );";
        db.execSQL(query);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
