/*****************************************************
 * get upcoming albums info for saved artist.
 * Using Google API for searching upcoming albums for artists
 ****************************************************/
package com.mike.artisttracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static com.mike.artisttracker.saved_artist.savedArtists;
import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class album_activity extends AppCompatActivity {
    ArrayList<String> saved_album_names = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private String api_key = "44ce572665909f50a88232d35e667812";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        ListView album_list_view =(ListView)findViewById(R.id.saved_album_list_view);

        Document upcoming_albums_list = null;
        try {
            upcoming_albums_list = Jsoup.connect("http://www.metacritic.com/browse/albums/release-date/coming-soon/date").get();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements artists_in_list = null;
        Elements albums_dates = null;
        if(upcoming_albums_list != null) {
            artists_in_list = upcoming_albums_list.select("td.artistName");
            albums_dates = upcoming_albums_list.select("tr.module");

        }
        for(saved_artist artist: savedArtists) {
            Caller.getInstance().setCache(null);
            Caller.getInstance().setUserAgent("tst");
            String artist_name = artist.getArtistName();
            String album_name = " ";
            if(artist.getArtistTopAlbums() == null) {
                //artist.updateArtistTopAlbums(artist_name);  - ADDED SOMEWHERE ELSE TO IMPROVE PERFORMANCE
            }
            //try {

                //this is will be used for any artist, not just specific
//
//                Document upcoming_albums_list = Jsoup.connect("http://www.metacritic.com/browse/albums/release-date/coming-soon/date").get();
//                Elements albums_dates = upcoming_albums_list.select("tr.module");
//                System.out.println(albums_dates.text()); //dates
//
//                for (Element element : albums_dates){
//                    System.out.println("ELEMENTSSSSSSSSSSSSS");
//                    Elements album_names =
//                }


//                Document upcoming_albums_list = Jsoup.connect("http://www.metacritic.com/browse/albums/release-date/coming-soon/date").get();
//                Elements artists_in_list = upcoming_albums_list.select("td.artistName");

                for(Element specific_artist : artists_in_list){
                    if (artist_name.equals(specific_artist.text())) {
                        album_name = specific_artist.nextElementSibling().text(); //NOT SURE IF WORKS
                        //System.out.println(album_name);

                        Document release_doc = null;
                        try {
                            release_doc = Jsoup.connect("http://google.com/search?q=" + artist_name + " " + album_name + " date of release").get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //                //Element hreEles = doc.select("div#_XWk").last();

                        Elements hreEles = release_doc.getElementsByClass("_XWk");
                        if (hreEles != null) {
                            String[] lines;
                            String regex = "\\n";
                            lines = hreEles.toString().split(regex);

                            if (lines.length > 2) {
                                String[] splitDate = lines[1].split("\\s");
                                if (splitDate.length > 2) {
                                    String month = splitDate[1];
                                    String day = splitDate[2].substring(0, splitDate[2].length() - 1);
                                    String year = splitDate[3];
                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(date);
                                    int int_month = cal.get(Calendar.MONTH);
                                    Calendar albumDate = Calendar.getInstance();
                                    albumDate.set(Calendar.YEAR, Integer.parseInt(year));
                                    albumDate.set(Calendar.MONTH, int_month);
                                    albumDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                                    Calendar today = Calendar.getInstance();
                                    if (albumDate.after(today)) {
                                        saved_album_names.add(artist_name + ": " + album_name + " -" + lines[1]);
                                    }

                                } else if(splitDate.length == 2){ //case where just year is shown..we will accept for now
                                    String year = splitDate[1];
                                    Calendar now = Calendar.getInstance();   // Gets the current date and time
                                    int now_year = now.get(Calendar.YEAR); ;

                                    if(Integer.parseInt(year) >= now_year){
                                        saved_album_names.add(artist_name + ": " + album_name + " - " + year);
                                    }
                                }
                            } else { // not full date given - still a new album according to google
                                int year = Calendar.getInstance().get(Calendar.YEAR);
                                if(lines.length > 1) { //a result is shown

                                }
                                saved_album_names.add(artist_name+ ": " + album_name + " - " + year);
                            }
                        }

                    }


                }
        }


//                Document album_doc = Jsoup.connect("http://google.com/search?q=" + "new album" + " " + artist_name).get();
//                Elements album_ele = album_doc.getElementsByClass("_XWk");
//                if(album_ele != null) {
//                    String[] lines;
//                    String regex = "\\n";
//                    lines = album_ele.toString().split(regex);
//                    if(lines.length >1) {
//                        album_name = lines[1];
//                        //System.out.println(album_name); *****************************8
//                    }
//
//
//                }
//
//
//
//
//                Document release_doc = Jsoup.connect("http://google.com/search?q=" + artist_name + " " + album_name + " date of release").get();
//                //Element hreEles = doc.select("div#_XWk").last();
//                Elements hreEles = release_doc.getElementsByClass("_XWk");
//                if (hreEles != null) {
//                    //hreEles.get(0).toString();
//                    String[] lines;
//                    String regex = "\\n";
//                    lines = hreEles.toString().split(regex);
//                    //System.out.println(lines[1].toString() + "hey");
//
//                    if (lines.length > 2) {
//                        //System.out.println(album_name + " " + lines[1]);
//                        String[] splitDate = lines[1].split("\\s");
//                        if (splitDate.length > 2) {
//                            String month = splitDate[1];
//                            String day = splitDate[2].substring(0, splitDate[2].length() - 1);
//                            String year = splitDate[3];
//                            //System.out.println(Integer.parseInt(year));
//                            Date date = null;
//                            try {
//                                date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTime(date);
//                            int int_month = cal.get(Calendar.MONTH);
//                            Calendar albumDate = Calendar.getInstance();
//                            albumDate.set(Calendar.YEAR, Integer.parseInt(year));
//                            albumDate.set(Calendar.MONTH, int_month);
//                            albumDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
//
//                            //Date albumDate = new Date(Integer.parseInt(year), int_month, Integer.parseInt(day));
//                            //Date today = new Date();
//                            Calendar today = Calendar.getInstance();
//                            //System.out.println(album_name);
////                            System.out.println("l"+year+"l");
////                            System.out.println(albumDate);
////                            System.out.println(today);
//                            if (albumDate.after(today)) {
//                                saved_album_names.add(album_name + " -" + lines[1]);
//                                //artist.setUpcomingAlbum(album_name + " -" + lines[1]);
//                                            //System.out.println("hi");
//
//                                //WEIRD ERROR WERE THE YEAR OF ALBUM DATE IS 3917 not 20..
//                                //MAYBE bECAUSE DATE IS DEPRICATED - HOW DO I FIX??
//                                //https://stackoverflow.com/questions/5677470/java-why-is-the-date-constructor-deprecated-and-what-do-i-use-instead
//
//                            }
//
//                        } else if(splitDate.length == 2){ //case where just year is shown..we will accept for now
//                            String year = splitDate[1];
//;
//                            Calendar now = Calendar.getInstance();   // Gets the current date and time
//                            int now_year = now.get(Calendar.YEAR); ;
//
//                            if(Integer.parseInt(year) >= now_year){
//                                saved_album_names.add(album_name + " - " + year);
//                            }
//
//
//                        }
//
//                                    //System.out.println(hreEles.toString());
//                                    ///System.out.println();
//                    } else { // not full date given - still a new album according to google
//                        int year = Calendar.getInstance().get(Calendar.YEAR);
//
//                        if(lines.length > 1) { //a result is shown
////                            String[] splitDate = lines[1].split("\\s");
////                            if(splitDate.length > 1) {
////                                saved_album_names.add(album_name + " - " + year);
////                            }
//                            saved_album_names.add(album_name + " - " + year);
//                        }
//                    }
//
//                }
            //} catch(IOException e){

            //}


        //}


        adapter = new ArrayAdapter<String>(album_activity.this,R.layout.artist_list_detail, saved_album_names);
        album_list_view.setAdapter(adapter);
    }

    public void saveDataToText(){
        try {
            FileOutputStream os = openFileOutput("SavedArtists.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(saved_artist.savedArtists);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            System.out.println("ERROR"); //temporary
        }
    }

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

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, main_activity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();}
}
