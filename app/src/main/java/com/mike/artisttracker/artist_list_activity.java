package com.mike.artisttracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.mike.artisttracker.saved_artist.savedArtists;

public class artist_list_activity extends AppCompatActivity {

    ArrayList<String> saved_artist_names = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list);
        // Get the reference of entries
        ListView artist_list_view =(ListView)findViewById(R.id.saved_artist_list_view);

//        ImageView background = (ImageView)findViewById(R.id.artist_background);
//        Bitmap background_bmp = blur_bitmap.blur_image(this, BitmapFactory.decodeResource(getResources(), R.drawable.list));
//        background.setImageBitmap(background_bmp);

        for(saved_artist artist: savedArtists){
            saved_artist_names.add(artist.getArtistName());
        }

        adapter = new ArrayAdapter<String>(artist_list_activity.this,R.layout.artist_list_detail, saved_artist_names);
        artist_list_view.setAdapter(adapter);

        registerForContextMenu(artist_list_view);

        artist_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), "Selected: "+ saved_artist_names.get(i), Toast.LENGTH_SHORT).show();

                // todo make saved_artist grabbing more efficient
                saved_artist selected_artist = (saved_artist) savedArtists.toArray()[i];
                transferArtist(selected_artist);

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_file, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo obj = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.delete_artist:
                saved_artist artist_to_delete = (saved_artist) savedArtists.toArray()[obj.position];
                savedArtists.remove(artist_to_delete);
                saved_artist_names.remove(obj.position);
                adapter.notifyDataSetChanged();
            break;
        }
        return super.onContextItemSelected(item);
    }

    // Transfers an artist object to individual_artist_activity
    // using intent and serializable
    public void transferArtist(saved_artist a) {
        Intent i = new Intent(artist_list_activity.this, individual_artist_activity.class);
        Bundle b = new Bundle();
        int smart_button_flag = 0;

        // Puts the saved artist "a" into the bundle using the key "savedKey"
        b.putSerializable("savedKey", a);
        i.putExtras(b);
        i.putExtra("add", smart_button_flag);

        startActivity(i);
    }
}
