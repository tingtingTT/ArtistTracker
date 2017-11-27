package com.mike.artisttracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by MilkhaisHaile on 11/20/17.
 */

public class user_account implements Serializable{

    public static Collection<user_account> saved_Accounts = new ArrayList<user_account>();

    public static String current_username;
    public static String current_password;


    private String user_name;
    private String password;
    private Collection<saved_artist> saved_Artists = new ArrayList<saved_artist>();


    public user_account(String user_name, String password, ArrayList<saved_artist> saved_Artists) {
        this.user_name = user_name;
        this.password = password;
        this.saved_Artists = saved_Artists;
    }


    public String getUser_name() {
        return user_name;
    }
    public String getPassword() {
        return password;
    }
    public static String getCurrent_username() {
        return current_username;
    }
    public Collection<saved_artist> getSaved_Artists() {
        return saved_Artists;
    }


    public static boolean login_user(String user_entered, String pass_entered) {

        boolean alread_saved = false;

        for (user_account user : saved_Accounts) {
            if(user.getUser_name().equals(user_entered) && user.getPassword().equals(pass_entered)) {
                alread_saved = true;

                current_username = user.getUser_name();
                current_password = user.getPassword();
                saved_artist.savedArtists = user.getSaved_Artists();
            }
        }
        return alread_saved;
    }

    public static boolean login_new_user(String user_entered, String pass_entered){

        boolean valid_username = false;

        if(saved_Accounts == null){
            saved_Accounts =  new ArrayList<user_account>();
        }
        else{
            for (user_account user : saved_Accounts) {
                if(user.getUser_name().equals(user_entered) ) {
                    return valid_username;
                }
            }
            valid_username = true;
        }

        user_account new_user = new user_account(user_entered, pass_entered, new ArrayList<saved_artist>());
        saved_Accounts.add(new_user);

        current_username = user_entered;
        current_password = pass_entered;
        saved_artist.savedArtists = new ArrayList<saved_artist>();

        return valid_username;
    }


    // The method updates current user's saved artist list
    public static void update_user_account() {
        for(user_account user : saved_Accounts){
            if (user.getUser_name().equals(current_username)){
                user.saved_Artists = saved_artist.savedArtists;
            }
        }
    }

    // Used for future user account delete option
    public static void  delete_user( user_account user_to_delete){
        for (user_account user : saved_Accounts) {
            if(user.getUser_name().equals(user_to_delete.getUser_name())){
                saved_Accounts.remove(user);
                break;
            }
        }
    }
}
