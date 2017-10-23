package com.mike.artisttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.SearchView;



public class artist_search_activity extends AppCompatActivity {

    public Button confirm_search_single_artist_button;
    public SearchView searchViewArtists;

    public void init_layout()
    {

        //init searchView
        searchViewArtists = (SearchView) findViewById(R.id.searchView);
        searchViewArtists.setQueryHint("Search artist");
        // init button by id
        /*confirm_search_single_artist_button = (Button)findViewById(R.id.confirm_search_single_artist_button);

        // user clicks search this selected artist
        confirm_search_single_artist_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(artist_search_activity.this, individual_artist_activity.class);
                startActivity(intent);
            }
        });*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_search);
        init_layout();
    }
}
