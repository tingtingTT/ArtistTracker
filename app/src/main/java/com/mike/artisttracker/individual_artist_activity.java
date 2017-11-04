package com.mike.artisttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.*;
import static com.mike.artisttracker.saved_artist.savedArtists;


public class individual_artist_activity extends AppCompatActivity {

    public TextView artist_name;
    public TextView artist_info;
    public TextView concert_info;
    public TextView album_info;
    public saved_artist sa;



    public void init_layout() {

        // blur image
        ImageView background = (ImageView)findViewById(R.id.individual_artist);
        Bitmap background_bmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.artist));
        background.setImageBitmap(background_bmp);

        artist_name = (TextView) findViewById(R.id.artist_name);
        artist_info = (TextView) findViewById(R.id.artist_info);
        concert_info = (TextView) findViewById(R.id.concert_info);
        album_info = (TextView) findViewById(R.id.album_info);
//        sa = get_artist();


        // call setText Before setContentView ?
        artist_name.setText(sa.getArtistName());
        artist_info.setText(sa.getArtistInfo());
        concert_info.setText(sa.getArtistMBID());
        album_info.setText("");

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
        //need artist object
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_artist);
        sa = get_artist();

        final Button add_button = (Button)findViewById(R.id.add);
        final Button delete_button = (Button)findViewById(R.id.delete);
        int isAdd = getIntent().getIntExtra("add", 0);

        // set buttons visibility
        // if add value is 1, show add button, hide delete button
        if (isAdd == 1)
        {
            add_button.setVisibility(View.VISIBLE);
            delete_button.setVisibility(View.GONE);
            Log.d("add", "Using add button");


        }
        // otherwise hide add button, show delete button
        else
        {
            delete_button.setVisibility(View.VISIBLE);
            add_button.setVisibility(View.GONE);
            Log.d("add", "Using delete button");
            // delete from the list
            savedArtists.remove(sa);
        }

        // call before setContentView?
        // get_artist();
        parse_concert_data();
        init_layout();

        // set button on click
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to list
                savedArtists.add(sa);
                Toast.makeText(getBaseContext(), "added", Toast.LENGTH_SHORT).show();
                // hide the add button once user clicks it
                add_button.setVisibility(View.GONE);
            }
        });

        // set button on click
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to list
                savedArtists.remove(sa);
                Toast.makeText(getBaseContext(), "deleted", Toast.LENGTH_SHORT).show();
                delete_button.setVisibility(View.GONE);
            }
        });

    }
}