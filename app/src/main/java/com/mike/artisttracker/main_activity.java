/**************************************************************************************************
 *  Main activity page for Artist Tracker App.
 *  This activity mainly does the navigation to different activities for the app.
 *  Three main buttons will direct to concert_activity, album_activity and SavedArtistsActivity.
 *  One plus button will navigate to artist_search_activity.
 * ************************************************************************************************
 * Coding standard:
 * 1. Add comments at the beginning of each java file and all functions.
 * 2. Add comments at important variables.
 * 3. All names that contain more than one word should use this_is_name instead of thisIsName.
 * 4. All buttons' texts are stored in string.xml file, first letter cap.
 * 5. All IDs for buttons or any contents should be declared as "this_is_a_button" or "this_is_text_view" format
 * ************************************************************************************************/

package com.mike.artisttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class main_activity extends AppCompatActivity implements View.OnClickListener {

    public Button concert_button;
    public Button album_button;
    public Button saved_artist_button;
    public FloatingActionButton search_artist_float_button;
    public FloatingActionButton logout_button;


    public void init_layout()
    {


        // set all buttons by ids
        concert_button = findViewById(R.id.concert_button);
        album_button = findViewById(R.id.album_button);
        saved_artist_button = findViewById(R.id.saved_artist_button);
        search_artist_float_button = findViewById(R.id.search_artist_float_button);
        logout_button = findViewById(R.id.logout_button);

        // making these buttons listening/ready to be clicked
        concert_button.setOnClickListener(this);
        album_button.setOnClickListener(this);
        saved_artist_button.setOnClickListener(this);
        search_artist_float_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // set background blur
        ImageView background = findViewById(R.id.backgroud_image_view);
        Bitmap background_bmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.main));
        background.setImageBitmap(background_bmp);

        init_layout();


        if (getIntent().getBooleanExtra("CLOSEAPP", false)) {
            // Now close the MainActivity
            finish();
        }

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            // user clicks Concert
            case R.id.concert_button:
                Intent concert_intent = new Intent(main_activity.this, concert_activity.class);
                startActivity(concert_intent);
                break;

            // user clicks album
            case R.id.album_button:
                Intent album_intent = new Intent(main_activity.this, album_activity.class);
                startActivity(album_intent);
                break;

            // user clicks saved artist
            case R.id.saved_artist_button:
                Intent saved_intent = new Intent(main_activity.this, artist_list_activity.class);
                startActivity(saved_intent);
                break;

            // user clicks floating button
            case R.id.search_artist_float_button:
                Intent search_intent = new Intent(main_activity.this, artist_search_activity.class);
                startActivity(search_intent);
                break;

            // user clicks logout button
            case R.id.logout_button:
                user_account.reset();
                Intent logout_intent = new Intent(main_activity.this, login_activity.class);
                startActivity(logout_intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(main_activity.this, main_activity.class);
        // Clear other activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("CLOSEAPP", true);
        startActivity(intent);
        finish();
    }
}
