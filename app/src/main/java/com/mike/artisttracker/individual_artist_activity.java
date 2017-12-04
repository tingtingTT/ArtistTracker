/*************************************************************
 * Individual artist activity
 * Showing info for the artist and have add, delete functions
 *************************************************************/
package com.mike.artisttracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import de.umass.lastfm.Caller;


public class individual_artist_activity extends AppCompatActivity {
    public TextView artist_name;
    public TextView artist_info;
    public ImageView artistImage;
    public saved_artist sa;
    public int add;

    public void init_layout(saved_artist sa) {

        artistImage = findViewById(R.id.artist_image);
//        ImageView background = findViewById(R.id.individual_artist);
//        Bitmap background_bmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.artist));
//        background.setImageBitmap(background_bmp);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        if(sa.getArtistTopAlbums() == null) {
//            System.out.println(sa.getArtistName());
//            sa.updateArtistTopAlbums(sa.getArtistName());  //- ADDED SOMEWHERE ELSE TO IMPROVE PERFORMANCE
//        }

        //fix caching/reloading fail problem
        Caller.getInstance().setCache(null);
        Caller.getInstance().setUserAgent("tst");

        artist_name = findViewById(R.id.artist_name);
        artist_info = findViewById(R.id.artist_info);
        artist_name.setText(sa.getArtistName());
        if (sa.getUpcomingAlbum() == null) {
            sa.updateArtistTopAlbums(sa.getArtistName());
        }
        //System.out.println(sa.getUpcomingAlbum());
        //System.out.println(sa.getArtistTopAlbums());
        artist_info.setText(sa.getArtistInfo() + "\n \n" + "Top Albums \n"  + sa.getUpcomingAlbum());
        artist_info.setMovementMethod(new ScrollingMovementMethod());
    }

    // Gets artist object from bundle
    // Needs previous activity to pass bundle through intent
    public saved_artist get_artist() {
        Bundle b = this.getIntent().getExtras();
        saved_artist a = new saved_artist("", "");
        if (b != null)
            a = (saved_artist)getIntent().getExtras().getSerializable("savedKey");
        return a;
    }

    public void parse_concert_data() {
        //need concert information
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_artist);
        sa = get_artist();

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();

        final Button add_button = findViewById(R.id.add);
        final Button delete_button = findViewById(R.id.delete);

        // todo perhaps checking if sa is in savedArtist collection instead?
        int isAdd = getIntent().getIntExtra("add", 0);
        add = isAdd;

        // set buttons visibility
        // if add value is 1, show add button, hide delete button
        if (isAdd == 1)
        {
            add_button.setVisibility(View.VISIBLE);
            delete_button.setVisibility(View.GONE);
        }
        // otherwise hide add button, show delete button
        else
        {
            delete_button.setVisibility(View.VISIBLE);
            add_button.setVisibility(View.GONE);
        }
        parse_concert_data();
        init_layout(sa);
        imageLoader.displayImage(sa.getArtist_image(), artistImage);

        // set button on click
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to list
                saved_artist.addArtist(sa);
                Toast.makeText(getBaseContext(), "" + sa.getArtistName() + " is added.", Toast.LENGTH_SHORT).show();
                saveAccountsToFile();
                // hide the add button once user clicks it
                add_button.setVisibility(View.GONE);
                Intent search_intent = new Intent(individual_artist_activity.this, main_activity.class);
                startActivity(search_intent);
            }
        });

        // set button on click
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to list
                saved_artist.deleteArtist(sa);
                Toast.makeText(getBaseContext(), "" + sa.getArtistName() +" is deleted", Toast.LENGTH_SHORT).show();
                // saveDataToText(); old save
                saveAccountsToFile(); // New Save
                // hide the add button once user clicks it
                delete_button.setVisibility(View.GONE);
                Intent search_intent = new Intent(individual_artist_activity.this, artist_list_activity.class);
                startActivity(search_intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(add == 0)
        {
            Intent it = new Intent(this, artist_list_activity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            finish();
        }
        else
        {
            Intent it = new Intent(this, artist_search_activity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            finish();        }
    }

    // This method will save all user accounts to file
    public void saveAccountsToFile(){
        try {
            user_account.update_user_account();
            FileOutputStream os = openFileOutput("SavedAccount.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(user_account.saved_Accounts);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            Toast.makeText(getBaseContext(), "Saving account failed" , Toast.LENGTH_SHORT).show();
            System.out.println("ERROR"); //temporary
        }
    }

}
