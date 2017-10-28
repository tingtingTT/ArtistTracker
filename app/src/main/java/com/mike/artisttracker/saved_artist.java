package com.mike.artisttracker;

/**
 * Created by Kyle Batross on 10/25/2017.
 */

import android.widget.Toast;

import java.io.FileOutputStream;
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
    private String apiKey = "44ce572665909f50a88232d35e667812";
    private String artistName;
    private String artistURL;
    private String artistMBID;
    private PaginatedResult<Event> artistEvents; //Concerts
    private PaginatedResult<Image> artistImage;
    private Collection<Album> topAlbums; //for info
    private Album upcomingAlbum;

    public saved_artist(String name, String url, String mbid){
        artistName = name;
        artistURL = url;
        artistMBID = mbid;
        topAlbums = updateArtistTopAlbums(name); // API CALL
        artistEvents = updateArtistEvents(mbid); // API CALL
        //artistImage  = null; TO BE ADDED
    }

    public void setArtistName(String name){
        artistName = name;
    }

    public void setArtistURL(String url){
        artistURL = url;
    }
    public void setArtistMBID(String mbid){
        artistMBID = mbid;
    }

    public String getArtistName() { return artistName; }
    public String getArtistURL() { return artistURL; }
    public String getArtistMBID() { return artistURL;}
    public Collection<Album> getArtistTopAlbums(){ return topAlbums; }
    public PaginatedResult<Event> getArtistsEvents(){ return artistEvents; }

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
    public void addArtist(saved_artist specificArtist){
        savedArtists.add(specificArtist);
    }

    //makes API call for events, unparsed
    public PaginatedResult<Event> updateArtistEvents(String mbid){
        PaginatedResult<Event> events = getEvents(mbid, apiKey);
        return events;
    }

    //makes api call for images, unparesed
    public void updateArtistImage(String mbid){
        PaginatedResult<Image> artistimage = getImages(mbid, apiKey);
        artistImage = artistimage;
    }
    //makes api call for albums ,unparsed
    public Collection<Album> updateArtistTopAlbums(String name){
        Collection<Album> topalbums = getTopAlbums(name,apiKey);
        return topalbums;
    }


    public void saveDataToText(){
        try {
            FileOutputStream os = openFileOutput("SavedArtist.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);

            //Data.arrayList.add(newData);
            output.writeObject(saved_artist.savedArtists);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            System.out.println("ERROR"); //temporary
        }
    }

}
