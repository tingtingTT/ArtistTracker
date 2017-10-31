package com.mike.artisttracker;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collection;

import de.umass.lastfm.Artist;

import static com.mike.artisttracker.R.string.API_KEY;
import static com.mike.artisttracker.R.string.search_artist_hint;
import static de.umass.lastfm.Artist.search;


public class artist_search_activity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    public Button confirm_search_single_artist_button;
    public SearchView searchViewArtists;
    public ArrayList<String> results = new ArrayList<String>();
    public static ArrayList<Artist> artist_list;


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

        final ListView lv = (ListView)findViewById(R.id.search_results);
        artist_list = new ArrayList<Artist>();
        ArrayAdapter<Artist> arrayAdapter =
                new ArrayAdapter<Artist>(this,android.R.layout.simple_list_item_1, artist_list);
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
            {
                Artist selected_artist= artist_list.get(position);

                // a toast for debugging purpose
                Toast.makeText(getApplicationContext(), "Entry selected : "+selected_artist, Toast.LENGTH_SHORT).show();
            }
        });


    }


    public Collection<Artist> getSearchResults(String query){
        return search(query,"44ce572665909f50a88232d35e667812");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() != 0){
            Collection<Artist> tempresults = new ArrayList<Artist>();
            tempresults = getSearchResults(query);
            for (Artist artists : tempresults) {
                results.add(artists.getName());
            }

            System.out.println(tempresults.toString());
            ListView lv = (ListView) findViewById(R.id.search_results);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.searchresults,results);
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
