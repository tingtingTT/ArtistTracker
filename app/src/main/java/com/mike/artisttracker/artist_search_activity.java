package com.mike.artisttracker;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;


import java.util.ArrayList;
import java.util.Collection;

import de.umass.lastfm.Artist;

import static com.mike.artisttracker.R.string.API_KEY;
import static de.umass.lastfm.Artist.search;


public class artist_search_activity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public Button confirm_search_single_artist_button;
    public SearchView searchViewArtists;
    public ArrayList<Collection<Artist>> results = new ArrayList<Collection<Artist>>();




    public void init_layout()
    {

        //init searchView


        // init button by id
        /*confirm_search_single_artist_button = (Button)findViewById(R.id.confirm_search_single_artist_button);

        // user clicks search this selected artist
        confirm_search_single_artist_button.setOnClickListener(new View.OnClickListener()
        {A
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

        //https://stackoverflow.com/questions/6343166/how-do-i-fix-android-os-networkonmainthreadexception   - bad idea??? read
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        searchViewArtists = (SearchView) findViewById(R.id.search_view);
        searchViewArtists.setQueryHint("Search artist");
        searchViewArtists.setOnQueryTextListener(this);
        //ListView lv = (ListView) findViewById(R.id.search_results);
        //ArrayAdapter<Collection<Artist>> adapter = new ArrayAdapter<Collection<Artist>>(this,R.layout.searchresults,results);
        //lv.setAdapter(adapter);


    }


    public Collection<Artist> getSearchResults(String query){
        return search(query,"44ce572665909f50a88232d35e667812");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() != 0){
            results.add(getSearchResults(query)); //problem
            System.out.println(getSearchResults(query).toString());
            ListView lv = (ListView) findViewById(R.id.search_results);
            ArrayAdapter<Collection<Artist>> adapter = new ArrayAdapter<Collection<Artist>>(this,R.layout.searchresults,results);
            lv.setAdapter(adapter);
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //searchViewArtists.setQuery(s,false);
        return false;
    }
}
