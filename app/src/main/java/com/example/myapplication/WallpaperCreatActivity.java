package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.example.myapplication.DrawWallpaper.createBtimapImage;

public class WallpaperCreatActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {



    // Widgets
    ImageView imageView;
    TextView textView;
    EditText editText;
    Spinner spinner;
    RadioGroup radioGroup;
    SeekBar sbfontSize, sbpositionX,sbpositionY;
    Button add, backgroundColorButton, fontColorButton;

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

    WallpaperBD wallpaperBD;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_creat);

        wallpaperBD = new WallpaperBD(this);
        database = wallpaperBD.getWritableDatabase();


        imageView = findViewById(R.id.imageView);


        List<String> fontArray = Arrays.asList(getResources().getStringArray(R.array.FontsArray));
        // Spinner
        spinner = findViewById(R.id.spinner);
        FontSpinnerAdapter adapter = new FontSpinnerAdapter(this, fontArray);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        // ADD Button
        add = findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
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

                database.insert(WallpaperBD.TABLE_NAME, null, contentValues);

                Intent i = new Intent(WallpaperCreatActivity.this, MainActivity.class);
                startActivity(i);


            }
        });

        // set Background Color

        backgroundColorButton = findViewById(R.id.backgroundColor);
        backgroundColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(WallpaperCreatActivity.this, Color.rgb(41, 128, 185), new AmbilWarnaDialog.OnAmbilWarnaListener() {
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

        // set Font Color

        fontColorButton = findViewById(R.id.fontColor);
        fontColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(WallpaperCreatActivity.this, Color.WHITE, new AmbilWarnaDialog.OnAmbilWarnaListener() {
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




        // EditText settings (TEXT)
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);

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



        // Radio settings (align settings)
        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioLeft:    // when checked LEFT
                        setAlign(0);
                        break;
                    case R.id.radioCenter:  // when checked CENTER
                        setAlign(1);
                        break;
                    case R.id.radioRight:   // when checked RIGHT
                        setAlign(2);
                        break;
                }
                changeImageView();
            }
        });


        // SeekBars Settings
        sbfontSize  = findViewById(R.id.seekBar_fontsize);      // font size
        sbpositionX = findViewById(R.id.seekBar_position_x);    // Position X
        sbpositionY = findViewById(R.id.seekBar_position_y);    // Position Y

        sbfontSize.setMax((int) Math.round(MainActivity.HEIGHT / 3.5));           // max font size
        sbpositionX.setMax(2000);       // max Position X
        sbpositionY.setMax(2000);       // max Position Y

        sbfontSize.setOnSeekBarChangeListener(this);
        sbpositionX.setOnSeekBarChangeListener(this);
        sbpositionY.setOnSeekBarChangeListener(this);

        setText("Hello");
        editText.setText(getText());
        setFontFamilyStr("Allan-Regular.ttf");
        setFontFamily(Typeface.createFromAsset(this.getAssets(), getFontFamilyStr()));
        spinner.setSelection(0);
        setFontSize(300);
        sbfontSize.setProgress(getFontSize());
        setBackgroundColor(Color.rgb(41, 128, 185));
        setFontColor(Color.WHITE);
        setAlign(1);
        radioGroup.check(R.id.radioCenter);
        setPositionX(500);
        sbpositionX.setProgress(getPositionX());
        setPositionY(500);
        sbpositionY.setProgress(getPositionY());

        changeImageView();

    }


    public void changeImageView(){
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
        setFontFamily(Typeface.createFromAsset(this.getAssets(), getFontFamilyStr()));
        changeImageView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setFontSize(sbfontSize.getProgress());
        setPositionX(sbpositionX.getProgress());
        setPositionY(sbpositionY.getProgress());
        changeImageView();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}



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
        database.close();
    }


}
