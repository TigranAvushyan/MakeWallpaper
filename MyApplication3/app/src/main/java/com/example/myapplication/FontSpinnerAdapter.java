package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class FontSpinnerAdapter extends ArrayAdapter {

    List<String> fontArray;
    Context context;

    public FontSpinnerAdapter(Context context, List<String> fontArray) {
        super(context, 0, fontArray);
        this.fontArray = fontArray;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_dr_down, parent, false
            );
        }

        TextView textView = convertView.findViewById(R.id.spinnerdropdown);
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(am, fontArray.get(position));

        textView.setTypeface(typeface);
        textView.setText("Font Example");
        return convertView;
    }
}
