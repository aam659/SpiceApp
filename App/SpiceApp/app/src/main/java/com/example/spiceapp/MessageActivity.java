package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    FirebaseUser mUser;
    DatabaseReference reference;
    Intent intent;
    EditText message_send;
    ImageButton send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initializeToolbar();
        mUser = FirebaseManager.getCurrentUser();
        profile_pic = findViewById(R.id.profileImg);
        message_send = findViewById(R.id.message_send);
        send = findViewById(R.id.btnSend);
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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!message_send.equals("")){
                    sendMessage(mUser.getEmail().replace('.','_'), userEmail, message_send.getText().toString());
                }
                else
                    Toast.makeText(MessageActivity.this, "Message empty.", Toast.LENGTH_SHORT).show();
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
        hashMap.put("message", message);
        reference.child("Chats").setValue(hashMap);

        //FINISH HASHMAP



    }

}
