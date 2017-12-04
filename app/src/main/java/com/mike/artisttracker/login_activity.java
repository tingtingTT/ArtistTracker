package com.mike.artisttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;

public class login_activity extends AppCompatActivity {

    private EditText user_Name;
    private EditText password;
    private Button login;
    private Button create_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        user_Name = (EditText)findViewById(R.id.username_edit_text);
        password = (EditText)findViewById(R.id.password_edit_text);
        login = (Button)findViewById(R.id.login_button);
        create_account = (Button)findViewById(R.id.create_account_button);

        LoadLoginStateToFile();

        if (login_state.last_user!= null && login_state.last_user.getIs_logged_in() == true){
            validate(login_state.last_user.getLast_logged_in_username(), login_state.last_user.getLast_logged_in_password());
            Toast.makeText(getBaseContext(), "Welcome back: "+ user_account.getCurrent_username(), Toast.LENGTH_LONG).show();
            // login was successful so launch app
            launchMain();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(( user_Name.getText().toString().length() != 0) && ( password.getText().toString().length() != 0) ){
                    // Load all user accounts
                    grabAccountsFromFile();
                    validate(user_Name.getText().toString(), password.getText().toString());
                }
                else{
                    Toast.makeText(getBaseContext(), "Please enter a valid username and password." , Toast.LENGTH_LONG).show();
                }
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(( user_Name.getText().toString().length() != 0) && ( password.getText().toString().length() != 0) ){
                    grabAccountsFromFile();
                    createAccount(user_Name.getText().toString(), password.getText().toString());
                }
                else{
                    Toast.makeText(getBaseContext(), "Please enter a valid username and password." , Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    // This method will check if entered log in info matches an existing account
    private void validate(String user, String pass){
        // login_user will return true if user name and pass were a match to an account
        if(user_account.login_user(user,pass)){
            loadSocialBoard();
             updateLoginState(user,pass);
            //Toast.makeText(getBaseContext(), "Logged in as: '"+ user_account.getCurrent_username() + "'", Toast.LENGTH_LONG).show();
            // login was succesfull so launch app
            launchMain();
        }
        else{
            Toast.makeText(getBaseContext(), "Login failed. Username and password don't match an existing account." , Toast.LENGTH_LONG).show();
        }
    }

    // This method will check if new username is available and if so create a new account using entered credentials
    private void createAccount(String user, String pass){
        // login_new_user will return true if User name is available and account is created
        if(user_account.login_new_user(user,pass)){
            loadSocialBoard();
            // login was succesfull so launch app
            saveAccountsToFile();
            updateLoginState(user,pass);
            //Toast.makeText(getBaseContext(), "Logged in as: '"+ user_account.getCurrent_username() + "'", Toast.LENGTH_LONG).show();
            launchMain();
        }
        else{
            Toast.makeText(getBaseContext(), "Username isn't available" , Toast.LENGTH_LONG).show();
        }
    }

    private void updateLoginState(String user, String pass) {
        //System.out.println(">>>>>> In update <<<<<<<<");
        //System.out.println("Before setLast_user: " + login_state.last_user.getIs_logged_in().booleanValue());

        login_state.setLast_user(user,pass,true);
        //System.out.println("After setLast_user: " + login_state.last_user.getIs_logged_in().booleanValue());
        saveLoginStateToFile();

    }

    private void loadSocialBoard() {
        try{

            String file_name = "SocialPosts.txt";
            FileInputStream inputStream = openFileInput("SocialPosts.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            social_post.saved_posts = (ArrayList<social_post>) objStream.readObject();
            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getBaseContext(), "Grabbing social board failed" , Toast.LENGTH_LONG).show();
        }

    }

    // This method will load all user accounts from file
    private void grabAccountsFromFile() {
        try{

            String file_name = "SavedAccount.txt";
            FileInputStream inputStream = openFileInput("SavedAccount.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            user_account.saved_Accounts = (ArrayList<user_account>) objStream.readObject();
            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getBaseContext(), "Grabbing account failed" , Toast.LENGTH_LONG).show();
        }

    }

    // This method will save user accounts to file
    public void saveAccountsToFile(){
        try {
            FileOutputStream os = openFileOutput("SavedAccount.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(user_account.saved_Accounts);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            //Toast.makeText(getBaseContext(), "Saving account failed" , Toast.LENGTH_LONG).show();
            System.out.println("ERROR"); //temporary
        }
    }

    // This method will load last login state from file
    private void LoadLoginStateToFile() {
        try{

            String file_name = "LoginState.txt";
            FileInputStream inputStream = openFileInput("LoginState.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            login_state.last_user = (login_state) objStream.readObject();
            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getBaseContext(), "Grabbing login state failed" , Toast.LENGTH_LONG).show();
        }

    }

    // This method will save last login state from file
    private void saveLoginStateToFile() {
        try {
            FileOutputStream os = openFileOutput("LoginState.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(login_state.last_user);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            //Toast.makeText(getBaseContext(), "Saving login state failed" , Toast.LENGTH_LONG).show();
            System.out.println("ERROR"); //temporary
        }
    }



    private void launchMain() {
        Intent intent = new Intent(login_activity.this, main_activity.class);
        startActivity(intent);
    }
}
