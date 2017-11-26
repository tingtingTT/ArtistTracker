/*****************************************************
 * get upcoming albums info for saved artist.
 * Using Google API for searching upcoming albums for artists
 ****************************************************/
package com.mike.artisttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import static com.mike.artisttracker.saved_artist.savedArtists;
import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;

public class album_activity extends AppCompatActivity {
    ArrayList<String> saved_album_names = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        ListView album_list_view = findViewById(R.id.saved_album_list_view);
        for(saved_artist artist: savedArtists) {
            Caller.getInstance().setCache(null);
            Caller.getInstance().setUserAgent("tst");
            if(artist.getArtistTopAlbums() == null) {
                artist.updateArtistTopAlbums(artist.getArtistName());
            }
            if (artist.getArtistTopAlbums() != null) {
                for (Album albums : artist.getArtistTopAlbums()) {
                    if(albums.getName() != null || !(albums.getName().equals("(null)"))) {
                        saved_album_names.add(albums.getName());
                    }
                }
            }
        }

        adapter = new ArrayAdapter<>(album_activity.this,R.layout.artist_list_detail, saved_album_names);
        album_list_view.setAdapter(adapter);
    }
}
