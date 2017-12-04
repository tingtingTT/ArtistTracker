/*****************************************
 * Display a list of saved artists for user
 ****************************************/
package com.mike.artisttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
        ListView artist_list_view = findViewById(R.id.saved_artist_list_view);

        for(saved_artist artist: savedArtists){
            saved_artist_names.add(artist.getArtistName());
        }

        adapter = new ArrayAdapter<>(artist_list_activity.this,R.layout.artist_list_detail, saved_artist_names);
        artist_list_view.setAdapter(adapter);

        registerForContextMenu(artist_list_view);

        artist_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getBaseContext(), "Selected: "+ saved_artist_names.get(i), Toast.LENGTH_SHORT).show();

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
                saved_artist.deleteArtist(artist_to_delete);
                Toast.makeText(getBaseContext(), "" + artist_to_delete.getArtistName() +" is deleted", Toast.LENGTH_SHORT).show();
                saved_artist_names.remove(obj.position);
                saveAccountsToFile();
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

    // This method will load all user accounts from file
    public void saveAccountsToFile(){
        try {
            Toast.makeText(getBaseContext(), "Attempting to save updated account list" , Toast.LENGTH_SHORT).show();

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

/*****************************************************************

    //grabs persisting data and updates the savedArtist Data
    public void grabDataFromFile(){
        try{

            String file_name = "SavedArtists.txt";
            FileInputStream inputStream = openFileInput("SavedArtists.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            saved_artist.savedArtists = (ArrayList<saved_artist>) objStream.readObject();

            inputStream.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 *****************************************************************/
    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, main_activity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }
}
