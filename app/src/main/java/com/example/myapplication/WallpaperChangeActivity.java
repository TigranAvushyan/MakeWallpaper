package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.myapplication.DrawWallpaper.createBtimapImage;

public class WallpaperChangeActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {


    private ImageView imageView;
    private TextView textView;
    private EditText editText;
    private RadioGroup radioGroup;
    private SeekBar sbfontSize;
    private SeekBar sbpositionX;
    private SeekBar sbpositionY;
    private Button change;
    private Button delete;
    private Button backgroundColorButton;
    private Button fontColorButton;

    // Settings

    private String text;
    private Typeface fontFamily;
    private String fontFamilyStr;
    private int fontSize;
    private int fontColor;
    private int backgroundColor;
    private int align;
    private int positionX;
    private int positionY;


    // Data Base

    SQLiteDatabase database;
    Cursor cursor;

    AssetManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_change);

        am = this.getAssets();
        Intent intent = getIntent();
        final String itemId = intent.getStringExtra("itemId");

        // Widgets
        imageView = findViewById(R.id.ChangeimageView);
        textView = findViewById(R.id.ChangetextView);
        editText = findViewById(R.id.ChangeeditText);
        radioGroup = findViewById(R.id.ChangeradioGroup);
        sbfontSize = findViewById(R.id.ChangeseekBar_fontsize);
        sbpositionX = findViewById(R.id.ChangeseekBar_position_x);
        sbpositionY = findViewById(R.id.ChangeseekBar_position_y);
        change = findViewById(R.id.Change);
        delete = findViewById(R.id.delete);
        backgroundColorButton = findViewById(R.id.ChangebackgroundColor);
        fontColorButton = findViewById(R.id.ChangefontColor);

        WallpaperBD wallpaperBD = new WallpaperBD(this);
        database = wallpaperBD.getWritableDatabase();
        cursor = database.query(WallpaperBD.TABLE_NAME, null, WallpaperBD.COLUMN_ID + " = ?", new String[]{itemId}, null, null, null, null);

        if (cursor.moveToFirst()) {
            int title = cursor.getColumnIndex(WallpaperBD.COLUMN_TEXT);
            int fontFamily = cursor.getColumnIndex(WallpaperBD.COLUMN_FONT_FAMILY);
            int fontsize = cursor.getColumnIndex(WallpaperBD.COLUMN_FONT_SIZE);
            int fontColor = cursor.getColumnIndex(WallpaperBD.COLUMN_FONT_COLOR);
            int backgroundColor = cursor.getColumnIndex(WallpaperBD.COLUMN_BACKGROUND_COLOR);
            int align = cursor.getColumnIndex(WallpaperBD.COLUMN_ALIGN);
            int positionX = cursor.getColumnIndex(WallpaperBD.COLUMN_POSITION_X);
            int positionY = cursor.getColumnIndex(WallpaperBD.COLUMN_POSITION_Y);
            do {
                setText(cursor.getString(title));
                setFontFamilyStr(cursor.getString(fontFamily));
                setFontFamily(Typeface.createFromAsset(am, getFontFamilyStr()));
                setFontSize(cursor.getInt(fontsize));
                setFontColor(cursor.getInt(fontColor));
                setBackgroundColor(cursor.getInt(backgroundColor));
                setAlign(cursor.getInt(align));
                setPositionX(cursor.getInt(positionX));
                setPositionY(cursor.getInt(positionY));
            } while (cursor.moveToNext());
        }


        switch (getAlign()){
            case 0:
                radioGroup.check(R.id.ChangeradioLeft);
                break;
            case 1:
                radioGroup.check(R.id.ChangeradioCenter);
                break;
            case 2:
                radioGroup.check(R.id.ChangeradioRight);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ChangeradioLeft:    // when checked LEFT
                        setAlign(0);
                        break;
                    case R.id.ChangeradioCenter:  // when checked CENTER
                        setAlign(1);
                        break;
                    case R.id.ChangeradioRight:   // when checked RIGHT
                        setAlign(2);
                        break;
                }
                changeImageView();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setText(editText.getText().toString());
                changeImageView();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        sbfontSize.setMax((int) Math.round(MainActivity.HEIGHT / 3.5));
        sbpositionX.setMax(2000);
        sbpositionY.setMax(2000);


        sbfontSize.setProgress(getFontSize());
        sbpositionX.setProgress(getPositionX());
        sbpositionY.setProgress(getPositionY());

        sbfontSize.setOnSeekBarChangeListener(this);
        sbpositionX.setOnSeekBarChangeListener(this);
        sbpositionY.setOnSeekBarChangeListener(this);

        editText.setText(getText());
        setFontFamily(Typeface.createFromAsset(this.getAssets(), getFontFamilyStr()));

        backgroundColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(WallpaperChangeActivity.this, getBackgroundColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        setBackgroundColor(color);
                        changeImageView();
                    }
                });
                colorPicker.show();
            }
        });

        fontColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(WallpaperChangeActivity.this, getFontColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        setFontColor(color);
                        changeImageView();
                    }
                });
                colorPicker.show();
            }

        });

        Log.d("__PROGRESS", String.valueOf(getPositionX()));


        List<String> fontArray = Arrays.asList(getResources().getStringArray(R.array.FontsArray));
        // Spinner
        Spinner spinner = findViewById(R.id.Changespinner);
        FontSpinnerAdapter adapter = new FontSpinnerAdapter(this, fontArray);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        int spinnerPosition = adapter.getPosition(getFontFamilyStr());
        spinner.setSelection(spinnerPosition);


        // Change Button

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(WallpaperBD.COLUMN_TEXT, getText());
                contentValues.put(WallpaperBD.COLUMN_FONT_FAMILY, getFontFamilyStr());
                contentValues.put(WallpaperBD.COLUMN_FONT_SIZE, getFontSize());
                contentValues.put(WallpaperBD.COLUMN_FONT_COLOR, getFontColor());
                contentValues.put(WallpaperBD.COLUMN_BACKGROUND_COLOR, getBackgroundColor());
                contentValues.put(WallpaperBD.COLUMN_ALIGN, getAlign());
                contentValues.put(WallpaperBD.COLUMN_POSITION_X, getPositionX());
                contentValues.put(WallpaperBD.COLUMN_POSITION_Y, getPositionY());

                database.update(WallpaperBD.TABLE_NAME, contentValues, WallpaperBD.COLUMN_ID + "= " + itemId, null);

                Intent i = new Intent(WallpaperChangeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

//        Delete Button
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(WallpaperBD.TABLE_NAME, WallpaperBD.COLUMN_ID + " = " + itemId, null);
                Intent i = new Intent(WallpaperChangeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });



    }


    public void changeImageView() {
        imageView.setImageBitmap(createBtimapImage(
                getText(),
                getFontFamily(),
                getFontSize(),
                getFontColor(),
                getBackgroundColor(),
                getAlign(),
                getPositionX(),
                getPositionY()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setFontFamilyStr(parent.getItemAtPosition(position).toString());
        setFontFamily(Typeface.createFromAsset(am, getFontFamilyStr()));
        changeImageView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setFontSize(sbfontSize.getProgress());
        setPositionX(sbpositionX.getProgress());
        setPositionY(sbpositionY.getProgress());
        changeImageView();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    // Getters and Setters


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Typeface getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(Typeface fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontFamilyStr() {
        return fontFamilyStr;
    }

    public void setFontFamilyStr(String fontFamilyStr) {
        this.fontFamilyStr = fontFamilyStr;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }
}
