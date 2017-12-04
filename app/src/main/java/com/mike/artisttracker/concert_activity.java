package com.mike.artisttracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.umass.lastfm.Caller;

import static com.mike.artisttracker.saved_artist.savedArtists;

public class concert_activity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> concertEvents;
    ListView concert_list_view;
    double lati =  37.338208;
    double longi = -121.886329;

    private void setData(StringBuilder data){

        String localTime = "To be announced...";

        try {
            // Gets json object from the stringBuilder built inside async task
            Object mainObject = new JSONObject(data.toString());

            if (mainObject instanceof JSONObject) {

                JSONObject jsonembeded = ((JSONObject) mainObject).getJSONObject("_embedded");
                JSONArray jsonEvents = jsonembeded.getJSONArray("events");

                JSONObject firstJsonArrayItem = jsonEvents.getJSONObject(0);
                String artistname = firstJsonArrayItem.getString("name");

                // Sets ArrayList item to Artist name
                concertEvents.add(artistname);
                concertEvents.add("\n");

                // Loops through each concert in jsonEvents
                for (int i = 0; i < jsonEvents.length(); i++) {

                    try {
                        // Sets childJSONObject to each individual event i
                        JSONObject childJSONObject = jsonEvents.getJSONObject(i);
                        JSONObject dateObject = childJSONObject.getJSONObject("dates");
                        JSONObject dates = dateObject.getJSONObject("start");

                        try {
                            localTime = dates.getString("localTime");
                        } catch (JSONException e) {
                            // Sets localTime if null
                            localTime = "To be announced";
                        }

                        String localDate = dates.getString("localDate");

                        JSONObject embeddedInfo = childJSONObject.getJSONObject("_embedded");
                        JSONArray individualEvents = embeddedInfo.getJSONArray("venues");

                        JSONObject individualVenue = individualEvents.getJSONObject(0);
                        String venueName = individualVenue.getString("name");

                        JSONObject city = individualVenue.getJSONObject("city");
                        JSONObject country  = individualVenue.getJSONObject("country");
                        JSONObject locationData = individualVenue.getJSONObject("location");

                        String cityString = city.getString("name");
                        String countryString = country.getString("name");

                        double latitude = Double.parseDouble(locationData.getString("latitude"));
                        double longitude = Double.parseDouble(locationData.getString("longitude"));



                        double dist = distance(latitude, longitude, lati, longi);

                        if ( dist < 500 ) {
                            concertEvents.add(venueName);
                            concertEvents.add(cityString);
                            concertEvents.add(countryString);
                            concertEvents.add(localDate);
                            concertEvents.add(localTime);
                            concertEvents.add(" ");
                        }

//                        String compile = venueName + " " + cityString + " " +  countryString + " " +  localDate + " " +  localTime + "\n";
//                        concertEvents.add(compile);

//                        Log.d("Async", "city: " + cityString + ", Country: " + countryString + "," + "\n" + latitude + " " + longitude);
//                        Log.d("Async", "Venue: " + venueName + ", Date: " + localDate + ", Time: " + localTime);

                    } catch (JSONException e) {
                        Log.d("Async", e.toString());
                    }
                }

                adapter = new ArrayAdapter<String>(concert_activity.this, R.layout.artist_list_detail, concertEvents);
                concert_list_view.setAdapter(adapter);
            }
        }

        catch (JSONException e) {Log.d("exception", e.toString());}
    }


    // distance function to calculate dinstances in km based of lat/long
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.concert);
        // set background blur
        concertEvents = new ArrayList<>();
        concert_list_view =(ListView)findViewById(R.id.concert_list);
        concertEvents.clear();

        // Loop through each artist in savedArtist and call concertAsync to make API call
        for (saved_artist artist : savedArtists) {
            Caller.getInstance().setCache(null);
            Caller.getInstance().setUserAgent("tst");
            new concertsAsync().execute(artist.getArtistName());
        }
    }

    //TODO pass list of artist to this class

    public class concertsAsync extends AsyncTask<String, Void, Void> {
        StringBuilder jraw;

        @Override
        protected Void doInBackground(String...artist) {

//            String artistName = "Rise+Against";
            List<String> concertDates = new ArrayList<>();
//            List<Concert> concerts = new ArrayList<>();
            String artistName = artist[0];
            Log.d("Async",artistName);




            try {
                URL url = new URL("https://app.ticketmaster.com/discovery/v2/events.json?keyword="+artistName.replaceAll("\\s+","")+"&apikey=rSGJhKsBo8Udeyb9AFolUh2zScl47o38");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    jraw = stringBuilder;
//                    Log.i("Async", stringBuilder.toString());
//                    // return stringBuilder.toString();

                } finally {
                    urlConnection.disconnect();
////                    Log.i("Async", stringBuilder.toString())
                }


            } catch (Exception e) {
                Log.i("ERROR", e.getMessage());
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);
            setData(jraw);
        }
    }
    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, main_activity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();}
}


// PlaceHolder class, may be needed to sort concerts
class Concert {

    protected int year;
    protected int month;
    protected int day;
    protected String artist;
    protected String city;
    protected String country;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getArtist() {
        return artist;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
