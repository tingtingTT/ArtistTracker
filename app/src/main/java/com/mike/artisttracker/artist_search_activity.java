/***********************************************
 * Activity for searching artists use lastfm UI
 ***********************************************/
package com.mike.artisttracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collection;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import static de.umass.lastfm.Artist.getInfo;
import static de.umass.lastfm.Artist.search;


public class artist_search_activity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    public SearchView search_view_artists;
    public static ArrayList<String> name_result = new ArrayList<>();
    public static ArrayList<String> url_result = new ArrayList<>();
    public static ArrayList<String> mbid_result = new ArrayList<>();
    public static ArrayList<String> info_result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_search);

        // TODO: see below:
        //https://stackoverflow.com/questions/6343166/how-do-i-fix-android-os-networkonmainthreadexception   - bad idea??? read
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        search_view_artists = findViewById(R.id.search_view);
        int id = search_view_artists.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = search_view_artists.findViewById(id);
        textView.setTextColor(Color.YELLOW);
        //search_view_artists.setQueryHint("Search artist");
        search_view_artists.setQueryHint(Html.fromHtml("<font color = #FFFF00>" + getResources().getString(R.string.search_hint) + "</font>"));
        search_view_artists.setOnQueryTextListener(this);
    }

    public Collection<Artist> getSearchResults(String query){
        return search(query,"44ce572665909f50a88232d35e667812");
    }

    public Artist getArtistInfoApiCall(String nameOrMBID) {
        Artist a = getInfo(nameOrMBID, null, null, "44ce572665909f50a88232d35e667812");
        return a;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() != 0){
            Collection<Artist> result_artist_list;
            result_artist_list = getSearchResults(query);
            name_result.clear();
            url_result.clear();
            mbid_result.clear();
            info_result.clear();
            for (Artist artists : result_artist_list) {
                name_result.add(artists.getName());
                url_result.add(artists.getUrl());
                mbid_result.add(artists.getMbid());
                info_result.add(artists.getWikiText());
            }
//            System.out.println(result_artist_list.toString());
            final ListView lv = findViewById(R.id.search_results);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.artist_list_detail,name_result);
            lv.setAdapter(adapter);
            lv.setClickable(true);

            // set on click for list view and save entry as a save_artist object
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getBaseContext(), name_result.get(position), Toast.LENGTH_SHORT).show();
                    saved_artist saved_artist = new saved_artist(name_result.get(position), mbid_result.get(position));
                    saved_artist.setArtistURL(url_result.get(position));
                    saved_artist.setArtistInfo(info_result.get(position));
                    // When a specific artist is clicked, additional API call is made to get artist info
                    Artist b = getArtistInfoApiCall(saved_artist.getArtistMBID());
                    saved_artist.setArtistInfo(b.getWikiSummary());
                    saved_artist.setArtist_image(b.getImageURL(ImageSize.LARGE));
                    transferArtist(saved_artist);
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

    // Transfers an artist object to individual_artist_activity
    // using intent and serializable
    public void transferArtist(saved_artist a) {
        // temp artist used as example

        Intent i = new Intent(artist_search_activity.this, individual_artist_activity.class);
        Bundle b = new Bundle();

        int add;
        if(saved_artist.isSaved(a)){
            add = 0;
        }
        else
            add = 1;
        // Puts the saved artist "a" into the bundle using the key "savedKey"
        b.putSerializable("savedKey", a);
        i.putExtras(b);
        i.putExtra("add", add);

        startActivity(i);
    }

    @Override
    public void onBackPressed() {
       Intent it = new Intent(this, main_activity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }
}
