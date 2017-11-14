/**************************************************************************************************
 *  Main activity page for Artist Tracker App. // TODO: change name?  a-track-tive music??
 *  This activity mainly does the navigation to different activities for the app.
 *  Three main buttons will direct to concert_activity, album_activity and SavedArtistsActivity.
 *  One plus button will navigate to artist_search_activity.
 *  UI(Backgroud): TODO: UI TBD
 * ************************************************************************************************
 * Coding standard:
 * 1. Add comments at the beginning of each java file and all functions.
 * 2. Add comments at important variables.
 * 3. All names that contain more than one word should use this_is_name instead of thisIsName.
 * 4. All buttons' texts are stored in string.xml file, first letter cap.
 * 5. All IDs for buttons or any contents should be declared as "this_is_a_button" or "this_is_text_view" format
 * TODO: more coding and name standards
 * ************************************************************************************************/

package com.mike.artisttracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
//import static com.mike.artisttracker.R.string.API_KEY;


// IMPORTS ALL OF THE LASTFM WRAPPER API
// TODO: modify this to only import necessary parts


public class main_activity extends AppCompatActivity implements View.OnClickListener {

    public Button concert_button;
    public Button album_button;
    public Button saved_artist_button;
    public FloatingActionButton search_artist_float_button;
//    public Context context = getApplicationContext(); // to be used for file save /update


    public void init_layout()
    {


        // set all buttons by ids
        concert_button = (Button)findViewById(R.id.concert_button);
        album_button = (Button)findViewById(R.id.album_button);
        saved_artist_button = (Button)findViewById(R.id.saved_artist_button);
        search_artist_float_button = (FloatingActionButton)findViewById(R.id.search_artist_float_button);

        // making these buttons listening/ready to be clicked
        concert_button.setOnClickListener(this);
        album_button.setOnClickListener(this);
        saved_artist_button.setOnClickListener(this);
        search_artist_float_button.setOnClickListener(this);

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        grabDataFromFile();
        // set background blur
        ImageView background = (ImageView)findViewById(R.id.backgroud_image_view);
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
            // TODO: change design of the button later
            case R.id.search_artist_float_button:
                Intent search_intent = new Intent(main_activity.this, artist_search_activity.class);
                startActivity(search_intent);
                break;
        }
    }



    public void saveDataToText(){
        try {
            FileOutputStream os = openFileOutput("SavedArtist.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(saved_artist.savedArtists);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            System.out.println("ERROR"); //temporary
        }
    }

    //grabs persisting data and updates the savedArtist Data
    public void grabDataFromFile(){
        try{

            String file_name = "SavedArtist.txt";
            FileInputStream inputStream = openFileInput("SavedArtist.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            saved_artist.savedArtists = (ArrayList<saved_artist>) objStream.readObject();

            inputStream.close();



        } catch (Exception e) {
            e.printStackTrace();
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
