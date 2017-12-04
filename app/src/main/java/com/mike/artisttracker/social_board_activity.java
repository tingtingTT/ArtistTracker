package com.mike.artisttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.mike.artisttracker.social_post.saved_posts;

public class social_board_activity extends AppCompatActivity {

    ListView post_list_view;
    ArrayAdapter<String> adapter;
    ArrayList<String> posts_list = new ArrayList<>();
    EditText post_edit_text;
    Button post_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_board_activity);

        post_list_view = (ListView)findViewById(R.id.posted_messages_listView);

        for(social_post post: saved_posts){
            posts_list.add(post.getMessage() +'\n' + "~" + post.getAuthor() + "  " + post.getPost_time() );
        }


        adapter = new ArrayAdapter<String>(social_board_activity.this,R.layout.artist_list_detail,posts_list);
        post_list_view.setAdapter(adapter);

        post_edit_text = (EditText)findViewById(R.id.message_edit_text);
        post_button = (Button)findViewById(R.id.post_button);

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: fix time format
//                TimeZone.setDefault(TimeZone.getTimeZone("PST"));
                Date date = Calendar.getInstance().getTime();

                String post_date = date.toString();

                String new_post = post_edit_text.getText().toString();
                String author = user_account.getCurrent_username();

                social_post.addPost(new_post,author,post_date);
                saveAccountsToFile();

                posts_list.add(0, new_post +'\n'+ "~" + author +"  "+ post_date);

                post_edit_text.setText("");
                // hide heyboard
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, main_activity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }

    // This method will save posts to file
    public void saveAccountsToFile(){
        try {
            FileOutputStream os = openFileOutput("SocialPosts.txt", MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(social_post.saved_posts);
            output.close();
        }
        catch (java.io.IOException e) {
            //do something if an IOException occurs.
            //Toast.makeText(getBaseContext(), "Saving social board failed" , Toast.LENGTH_SHORT).show();
            System.out.println("ERROR"); //temporary
        }
    }
}
