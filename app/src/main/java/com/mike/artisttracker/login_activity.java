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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load all user accounts
                grabAccountsFromFile();
                validate(user_Name.getText().toString(), password.getText().toString());
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Create Account clicked" , Toast.LENGTH_SHORT).show();
                grabAccountsFromFile();
                createAccount(user_Name.getText().toString(), password.getText().toString());
            }
        });
    }

    // This method will check if entered log in info matches an existing account
    private void validate(String user, String pass){
        // login_user will return true if user name and pass were a match to an account
        if(user_account.login_user(user,pass)){
            // login was succesfull so launch app
            launchMain();
        }
        else{
            Toast.makeText(getBaseContext(), "Login failed. Please enter valid username and password." , Toast.LENGTH_SHORT).show();
        }
    }

    // This method will check if new username is available and if so create a new account using entered credentials
    private void createAccount(String user, String pass){
        // login_new_user will return true if User name is available and account is created
        if(user_account.login_new_user(user,pass)){
            Toast.makeText(getBaseContext(), "User name is valid, account logged in" , Toast.LENGTH_SHORT).show();
            // login was succesfull so launch app
            saveAccountsToFile();
            launchMain();
        }
        else{
            Toast.makeText(getBaseContext(), "Username isn't available" , Toast.LENGTH_SHORT).show();
        }

    }

    // This method will load all user accounts from file
    private void grabAccountsFromFile() {
        try{

            String file_name = "SavedAccounts.txt";
            FileInputStream inputStream = openFileInput("SavedAccounts.txt");
            ObjectInputStream objStream = new ObjectInputStream(inputStream);
            user_account.saved_Accounts = (ArrayList<user_account>) objStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Grabbing account failed" , Toast.LENGTH_SHORT).show();
        }

    }

    // This method will save user accounts to file
    public void saveAccountsToFile(){
        try {
            FileOutputStream os = openFileOutput("SavedAccounts.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(user_account.saved_Accounts);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            Toast.makeText(getBaseContext(), "Saving account failed" , Toast.LENGTH_SHORT).show();
            System.out.println("ERROR"); //temporary
        }
    }

    private void launchMain() {
        Intent intent = new Intent(login_activity.this, main_activity.class);
        Toast.makeText(getBaseContext(), "Logged in as: "+ user_account.getCurrent_username(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
