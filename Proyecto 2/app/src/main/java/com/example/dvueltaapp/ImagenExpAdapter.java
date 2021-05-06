package com.example.dvueltaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ImagenExpAdapter extends ArrayAdapter<ImagenesBase64> {

    public ImagenExpAdapter(Context context, List<ImagenesBase64> imagenesBase64) {
        super(context, 0 , imagenesBase64);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View gridImagenes = convertView;
        if (gridImagenes == null){
            gridImagenes = LayoutInflater.from(getContext()).inflate(R.layout.item_imagen, parent, false);
        }
        return gridImagenes;
    }
}
