package com.mike.artisttracker;

/**
 * Created by Kyle Batross on 10/25/2017.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Event;
import de.umass.lastfm.Image;
import de.umass.lastfm.PaginatedResult;
import static com.mike.artisttracker.R.string.API_KEY;
import static de.umass.lastfm.Artist.getEvents;
import static de.umass.lastfm.Artist.getImages;
import static de.umass.lastfm.Artist.getTopAlbums;

public class SavedArtists implements Serializable{

    public Collection<SavedArtists> savedArtists = new ArrayList<SavedArtists>();
    private String apiKey = "44ce572665909f50a88232d35e667812";
    private String artistName;
    private String artistURL;
    private String artistMBID;
    private PaginatedResult<Event> artistEvents; //Concerts
    private PaginatedResult<Image> artistImage;
    private Collection<Album> topAlbums; //for info
    private Album upcomingAlbum;

    public SavedArtists(String name, String url, String mbid){
        artistName = name;
        artistURL = url;
        artistMBID = mbid;
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

    public void updateArtistEvents(){
        PaginatedResult<Event> events = getEvents(artistMBID, apiKey);
        artistEvents = events;
    }

    public void updateArtistImage(){
        PaginatedResult<Image> artistimage = getImages(artistMBID, apiKey);
        artistImage = artistimage;
    }

    public void updateArtistTopAlbums(){
        Collection<Album> topalbums = getTopAlbums(artistName,apiKey);
        topAlbums = topalbums;
    }

    public Album getUpcomingAlbum(){
        //to be figured out
        return null;
    }





}
