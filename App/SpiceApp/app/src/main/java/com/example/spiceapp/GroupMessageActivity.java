package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiceapp.Adapters.GroupMessageAdapter;
import com.example.spiceapp.Adapters.MessageAdapter;
import com.example.spiceapp.FirebaseObjects.Chat;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupMessageActivity extends AppCompatActivity {

    CircleImageView profile_pic;
    TextView userName;
    FirebaseUser mUser;
    DatabaseReference reference;
    Intent intent;
    EditText message_send;
    ImageButton send;

    GroupMessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        initializeToolbar();

        recyclerView = findViewById(R.id.groupMessageRecyler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mUser = FirebaseManager.getCurrentUser();
        profile_pic = findViewById(R.id.profileImg);
        message_send = findViewById(R.id.message_send);
        send = findViewById(R.id.btnSend);
        intent = getIntent();
        String groupName = intent.getStringExtra("groupName");
        reference = FirebaseDatabase.getInstance().getReference("Groups")
                .child(groupName).child("Chats");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(groupName);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(groupName, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(mUser.getEmail().replace('.','_'), groupName, msg);
                }
                else
                    Toast.makeText(GroupMessageActivity.this, "Message empty.", Toast.LENGTH_SHORT).show();
                message_send.setText("");
            }
        });
    }

    private void initializeToolbar(){
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarGroupMessage);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
    }

    private void sendMessage(String sender, String groupName, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("message", message);
        reference.child("Groups").child(groupName).child("Chats").push().setValue(hashMap);
    }

    private void readMessage(String groupName,String imageURL){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    mChat.add(chat);
                }

                messageAdapter = new GroupMessageAdapter(GroupMessageActivity.this, mChat, imageURL);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
