package com.example.psyyf2.dissertation.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.psyyf2.dissertation.adapter.ChatMessage;
import com.example.psyyf2.dissertation.R;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class ChatPage extends AppCompatActivity {

    Firebase chatmess, branch;
    String stuName, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        stuName = bundle.getString("stuName");

        ListView listOfMessages = (ListView) findViewById(R.id.listchat);

        Firebase.setAndroidContext(this);
        branch = new Firebase("https://chatproject-bb6e6.firebaseio.com/");

        SharedPreferences userSettings= getSharedPreferences("config", 0);
        name = userSettings.getString("name","null");

        chatmess = branch.child("chat").child(name).child(stuName);

        // load the messages on the server
        FirebaseListAdapter<ChatMessage> adapter = new FirebaseListAdapter<ChatMessage>
                (this, ChatMessage.class,R.layout.message, chatmess) {
            @Override
            protected void populateView(View view, ChatMessage chatMessage, int i) {

                TextView name = (TextView) view.findViewById(R.id.message_text);
                TextView user = (TextView) view.findViewById(R.id.message_user);
                TextView time = (TextView) view.findViewById(R.id.message_time);

                name.setText(chatMessage.getMessage());
                user.setText(chatMessage.getName());
                time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", chatMessage.getTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    public void send(View v)
    {
        EditText input = (EditText)findViewById(R.id.input);

        String inputText = input.getText().toString();
        if (!inputText.equals("")) {
            ChatMessage chat = new ChatMessage(name, inputText);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            chatmess.push().setValue(chat);
            input.setText("");
        }
    }
}
