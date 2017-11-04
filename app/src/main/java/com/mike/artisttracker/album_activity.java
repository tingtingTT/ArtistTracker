package com.mike.artisttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class album_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);
        // set background blur
        ImageView background = (ImageView)findViewById(R.id.album_background);
        Bitmap background_bmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.album));
        background.setImageBitmap(background_bmp);

    }
}
