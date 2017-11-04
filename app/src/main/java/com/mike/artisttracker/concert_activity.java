package com.mike.artisttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class concert_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concert);
        // set background blur
        ImageView background = (ImageView)findViewById(R.id.concert_background);
        Bitmap backgroundBmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.concert));
        background.setImageBitmap(backgroundBmp);

    }
}
