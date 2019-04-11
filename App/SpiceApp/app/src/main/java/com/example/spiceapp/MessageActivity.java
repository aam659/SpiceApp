package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.spiceapp.FirebaseObjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_pic;
    TextView userName;

    DatabaseReference reference;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initializeToolbar();
        profile_pic = findViewById(R.id.profileImg);
        userName = findViewById(R.id.contactName);
        intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        reference = FirebaseDatabase.getInstance().getReference("users")
                    .child(userEmail);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String setName = user.getfName() + " " + user.getlName();
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle(setName);
                //Todo: Change user profile picture too
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializeToolbar(){
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarMessage);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Spice It Up");
    }

    private void sendMessage(String sender, String reciever, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciever", reciever);

        //FINISH HASHMAP



    }

}
