package com.mike.artisttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

public class artist_list_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list);
        // Get the reference of entries
        ListView entryList=(ListView)findViewById(R.id.saved_artist_list_view);
        ImageView background = (ImageView)findViewById(R.id.artist_background);
        Bitmap backgroundBmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.artist));
        background.setImageBitmap(backgroundBmp);
    }
}
