package com.mike.artisttracker;

/**
 * Created by Kyle Batross on 10/25/2017.
 */

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
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
    private PaginatedResult<Event> artist_events; //Concerts
    private PaginatedResult<Image> artist_image;
    private Collection<Album> top_albums; //for info
    private Album upcomingAlbum;

    public saved_artist(String name, String url, String mbid){
        artist_name = name;
        artist_url = url;
        artist_mbid = mbid;
        top_albums = updateArtistTopAlbums(name); // API CALL
        artist_events = updateArtistEvents(mbid); // API CALL
        //artistImage  = null; TO BE ADDED
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

    public String getArtistName() { return artist_name; }
    public String getArtistURL() { return artist_url; }
    public String getArtistMBID() { return artist_mbid;}
    public Collection<Album> getArtistTopAlbums(){ return top_albums; }
    public PaginatedResult<Event> getArtistsEvents(){ return artist_events; }

    public Album getUpcomingAlbum(){
        //to be figured out
        //this is for Albums that haven't came out yet
        return null;
    }

    //check for artist with Name and MBID, if found - remove from arrayList
    public void deleteArtist(saved_artist specificArtist){
        for (saved_artist artists : savedArtists) {
            if(artists.equals(specificArtist)){
                savedArtists.remove(artists);
            }
       }
    }

    //adds artist to Collection
    public void addArtist(saved_artist specific_artist){
        savedArtists.add(specific_artist);
    }

    //makes API call for events, unparsed
    public PaginatedResult<Event> updateArtistEvents(String mbid){
        PaginatedResult<Event> events = getEvents(mbid, api_key);
        return events;
    }

    //makes api call for images, unparesed
    public void updateArtistImage(String mbid){
        PaginatedResult<Image> artist_image = getImages(mbid, api_key);
        artist_image = artist_image;
    }
    //makes api call for albums ,unparsed
    public Collection<Album> updateArtistTopAlbums(String name){
        Collection<Album> top_albums = getTopAlbums(name,api_key);
        return top_albums;
    }

    //saves savedArtists to textfile for persisting data
    //need to pass a context to use openFileOutput - use getApplicationContext() I think? will test
    public void saveDataToText(Context context){
        try {
            FileOutputStream os = context.openFileOutput("SavedArtist.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);-a
            output.writeObject(savedArtists);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            System.out.println("ERROR"); //temporary
        }
    }

    //grabs persisting data and updates the savedArtist Data
    public void grabDataFromFile(Context context){
        try{

            String file_name = "SavedArtist.txt";
            FileInputStream inputStream = context.openFileInput("SavedArtist.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            savedArtists = (ArrayList<saved_artist>) objStream.readObject();

            inputStream.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
