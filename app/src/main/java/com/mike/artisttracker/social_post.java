package com.mike.artisttracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by MilkhaisHaile on 12/1/17.
 */

public class social_post implements Serializable {

    public static Collection<social_post> saved_posts = new ArrayList<social_post>();

    private String message;
    private String author;
    private String post_time;

    public social_post(String message, String author, String post_time) {
        this.message = message;
        this.author = author;
        this.post_time = post_time;
    }

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String post) {
        this.message = post;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getPost_time() {
        return post_time;
    }
    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }


    public static void addPost(String post_message, String post_author,String post_date) {
        social_post addee = new social_post(post_message, post_author,post_date);
        saved_posts.add(addee);
    }
}
