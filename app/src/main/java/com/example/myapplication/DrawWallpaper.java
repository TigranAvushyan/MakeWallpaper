package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

public class DrawWallpaper {
    private int id;
    private String text;
    private String fontFamily;
    private int fontSize;
    private int fontColor;
    private int backgroundColor;
    private int align;
    private int positionX;
    private int positionY;
    private Context context;


    Bitmap createBtimapWallpaper(Typeface typeface){

        Bitmap bitmap = Bitmap.createBitmap(MainActivity.WIDHT, MainActivity.HEIGHT, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);


        String[] words = this.text.split("\\r?\\n");

        // draw background
        Paint pBackground = new Paint();
        pBackground.setColor(this.backgroundColor);
        canvas.drawRect(0, 0, MainActivity.WIDHT, MainActivity.HEIGHT, pBackground);

        // draw text
        Paint pText = new Paint();
        pText.setColor(this.fontColor);
        pText.setTypeface(typeface);
        pText.setTextSize(this.fontSize);

        switch (this.align){
            case 0:
                pText.setTextAlign(Paint.Align.LEFT);
                break;
            case 1:
                pText.setTextAlign(Paint.Align.CENTER);
                break;
            case 2:
                pText.setTextAlign(Paint.Align.RIGHT);
                break;
        }


        // position
        int fz = (int) pText.getTextSize();
        int positionDefY = this.positionY;
        for (String i: words) {
            canvas.drawText(i, this.positionX, this.positionY, pText);
            this.positionY = fz + 10 + this.positionY;
        }
        this.positionY = positionDefY;


        canvas.drawBitmap(bitmap, MainActivity.WIDHT, MainActivity.HEIGHT, pText);
        return bitmap;
    }

    static Bitmap createBtimapImage(String text, Typeface fontFamily, int fontSize, int fontColor,int backgroundColor, int align, int positionX, int positionY){

        Bitmap bitmap = Bitmap.createBitmap(MainActivity.WIDHT, MainActivity.HEIGHT, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);


        String[] words = text.split("\\r?\\n");

        // draw background
        Paint pBackground = new Paint();
        pBackground.setColor(backgroundColor);
        canvas.drawRect(0, 0, MainActivity.WIDHT, MainActivity.HEIGHT, pBackground);

        // draw text
        Paint pText = new Paint();
        pText.setColor(fontColor);
        pText.setTypeface(fontFamily);
        pText.setTextSize(fontSize);

        switch (align){
            case 0:
                pText.setTextAlign(Paint.Align.LEFT);
                break;
            case 1:
                pText.setTextAlign(Paint.Align.CENTER);
                break;
            case 2:
                pText.setTextAlign(Paint.Align.RIGHT);
                break;
        }


        // position
        int fz = (int) pText.getTextSize();
        for (String i: words) {
            canvas.drawText(i, positionX, positionY, pText);
            positionY = fz + 10 + positionY;
        }


        canvas.drawBitmap(bitmap, MainActivity.WIDHT, MainActivity.HEIGHT, pText);
        return bitmap;
    }


//    Constructors

    public DrawWallpaper(Context context, int id, String text, String fontFamily, int fontSize, int fontColor,int backgroundColor, int align, int positionX, int positionY) {
        this.context = context;
        this.id = id;
        this.text = text;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.align = align;
        this.positionX = positionX;
        this.positionY = positionY;
    }


    //    Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
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

}
