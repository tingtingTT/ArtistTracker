package com.mike.artisttracker;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collection;
import de.umass.lastfm.Artist;

import static com.mike.artisttracker.saved_artist.savedArtists;
import static de.umass.lastfm.Artist.search;


public class artist_search_activity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    public Button confirm_search_single_artist_button;
    public SearchView search_view_artists;
    public static ArrayList<String> name_result = new ArrayList<>();
    public static ArrayList<String> url_result = new ArrayList<>();
    public static ArrayList<String> mbid_result = new ArrayList<>();
    public static ArrayList<String> album_result = new ArrayList<>();
    public static ArrayList<String> event_result = new ArrayList<>();


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

        search_view_artists = (SearchView) findViewById(R.id.search_view);
        search_view_artists.setQueryHint("Search artist");
        search_view_artists.setOnQueryTextListener(this);
    }


    public Collection<Artist> getSearchResults(String query){
        return search(query,"44ce572665909f50a88232d35e667812");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() != 0){
            Collection<Artist> result_artist_list = new ArrayList<>();
            result_artist_list = getSearchResults(query);
            for (Artist artists : result_artist_list) {
                name_result.add(artists.getName());
                url_result.add(artists.getUrl());
                mbid_result.add(artists.getMbid());
                // TODO: make API call to get album and concert info of the artist
            }

//            System.out.println(result_artist_list.toString());
            final ListView lv = (ListView) findViewById(R.id.search_results);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.searchresults,name_result);
            lv.setAdapter(adapter);
            lv.setClickable(true);
            // set on click for list view and save entry as a save_artist object
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getBaseContext(), url_result.get(position), Toast.LENGTH_SHORT).show();
//                    saved_artist saved_artist = new saved_artist(name_result.get(position), url_result.get(position), mbid_result.get(position));
                    saved_artist sr = new saved_artist("1", "2", "3");

                    savedArtists.add(sr);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //search_view_artists.setQuery(s,false);
        return false;
    }





}
