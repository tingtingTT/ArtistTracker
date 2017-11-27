/*******************************************
 * Activity for saved artist object
 *****************************************/
package com.mike.artisttracker;

import android.content.Context;
import android.os.StrictMode;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import de.umass.lastfm.Album;
import de.umass.lastfm.Event;
import de.umass.lastfm.Image;
import de.umass.lastfm.PaginatedResult;

import static android.content.Context.MODE_PRIVATE;
import static de.umass.lastfm.Artist.getEvents;
import static de.umass.lastfm.Artist.getImages;
import static de.umass.lastfm.Artist.getTopAlbums;

public class saved_artist implements Serializable{

    public static Collection<saved_artist> savedArtists = new ArrayList<saved_artist>();
    private String api_key = "44ce572665909f50a88232d35e667812";
    private String artist_name;
    private String artist_url;
    private String artist_mbid;
    private String artist_info;
    private String artist_image;
    private PaginatedResult<Event> artist_events; //Concerts
    private Collection<Album> top_albums; //for info
    private String upcomingAlbum;

    public saved_artist(String name, String mbid){
        artist_name = name;
        artist_mbid = mbid;
        top_albums = null;
    }

    public void setArtistName(String name){
        artist_name = name;
    }

    public void setArtistURL(String url){
        artist_url = url;
    }
    public void setArtistMBID(String mbid){
        artist_mbid = mbid;
    }
    public void setArtistInfo(String info){ artist_info = info; }
    public void setArtist_image(String image) {artist_image = image; }
    public void setArtistEventInfo(PaginatedResult e) {artist_events = e;}

    public String getArtistName() { return artist_name; }
    public String getArtistURL() { return artist_url; }
    public String getArtistMBID() { return artist_mbid; }
    public String getArtistInfo() { return artist_info; }
    public String getArtist_image() { return artist_image; }
    public PaginatedResult<Event> getArtistEventInfo() {return artist_events; }
    public Collection<Album> getArtistTopAlbums(){ return top_albums; }
    public PaginatedResult<Event> getArtistsEvents(){ return artist_events; }

    public String getUpcomingAlbum(){
        //to be figured out
        //this is for Albums that haven't came out yet
        return upcomingAlbum;
    }

    public void setUpcomingAlbum(String upcoming_album){
        upcomingAlbum += upcoming_album + "\n";
    }
    //check for artist with Name and MBID, if found - remove from arrayList
    public static void deleteArtist(saved_artist specificArtist){
        for (saved_artist artists : savedArtists) {
            if(artists.getArtistMBID().equals(specificArtist.getArtistMBID())){
                artists.top_albums = null; // to fix - java.util.ConcurrentModificationException
                savedArtists.remove(artists);
                break;
            }
        }
    }

    public static void addArtist(saved_artist specific_artist){
        boolean alread_saved = isSaved(specific_artist);
        savedArtists.add(specific_artist);
    }

    public static boolean isSaved(saved_artist specific_artist){
        boolean alread_saved = false;

        for (saved_artist artists : savedArtists) {
            if(artists.getArtistMBID().equals(specific_artist.getArtistMBID())){
                alread_saved = true;
            }
        }
        return alread_saved;

    }

    //makes API call for events, unparsed
    public PaginatedResult<Event> updateArtistEvents(String mbid){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        PaginatedResult<Event> events = getEvents(mbid, api_key);
        return events;
    }

    // makes api call for images, unparesed
    public void updateArtistImage(String mbid){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        PaginatedResult<Image> artist_image = getImages(mbid, api_key);
        artist_image = artist_image;
    }
    // makes api call for albums ,unparsed
    public Collection<Album> updateArtistTopAlbums(String name){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        top_albums = getTopAlbums(name,api_key);
        return top_albums;
    }

    // saves savedArtists to textfile for persisting data
    // need to pass a context to use openFileOutput - use getApplicationContext() I think? will test
    // just a template, PLEASE DO NOT USE THIS FUNCTION
    public static void saveDataToText(Context context){
        try {
            FileOutputStream os = context.openFileOutput("SavedArtists.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(savedArtists);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            System.out.println("ERROR"); //temporary
        }
    }

    //grabs persisting data and updates the savedArtist Data
    //just template, PLEASE DO NOT USE THIS FUNCTION
    public static void grabDataFromFile(Context context){
        try{

            String file_name = "SavedArtists.txt";
            FileInputStream inputStream = context.openFileInput("SavedArtists.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            savedArtists = (ArrayList<saved_artist>) objStream.readObject();

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
