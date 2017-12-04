package com.mike.artisttracker;

/**
 * Created by MilkhaisHaile on 12/4/17.
 */

public class login_state {

    //public static login_state last_user;
    public static login_state last_user = new login_state(null,null,false);


    private String last_logged_in_username;
    private String last_logged_in_password;
    private Boolean is_logged_in;

    public login_state(String last_logged_in_username, String last_logged_in_password, Boolean is_logged_in) {
        this.last_logged_in_username = last_logged_in_username;
        this.last_logged_in_password = last_logged_in_password;
        this.is_logged_in = is_logged_in;
    }

    public String getLast_logged_in_username() {
        return last_logged_in_username;
    }
    public void setLast_logged_in_username(String last_logged_in_username) {
        this.last_logged_in_username = last_logged_in_username;
    }
    public String getLast_logged_in_password() {
        return last_logged_in_password;
    }
    public void setLast_logged_in_password(String last_logged_in_password) {
        this.last_logged_in_password = last_logged_in_password;
    }
    public Boolean getIs_logged_in() {
        return is_logged_in;
    }
    public void setIs_logged_in(Boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    public static void setLast_user(String username, String pass, Boolean logged_in) {
        login_state.last_user.last_logged_in_username = username;
        login_state.last_user.last_logged_in_password = pass;
        login_state.last_user.is_logged_in = logged_in;

    }
}
