/*******************************************
 * Activity for concert for saved artists
 ******************************************/
package com.mike.artisttracker;

import android.content.Intent;
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
        ImageView background = findViewById(R.id.concert_background);
        Bitmap background_bmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.concert));
        background.setImageBitmap(background_bmp);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, main_activity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }
}
