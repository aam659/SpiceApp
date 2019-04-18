package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spiceapp.Adapters.NewGroupAdapter;
import com.example.spiceapp.FirebaseObjects.Mood;
import com.example.spiceapp.FirebaseObjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateEvent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewGroupAdapter adapter;
    private List<User> mUsers;
    private EditText editEventName;
    private DatabaseReference reference;
    private FirebaseUser mUser;
    private ArrayList<String> mEmails;
    private ArrayList<Mood> moods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        recyclerView = findViewById(R.id.recyclerNewEvent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = new ArrayList<>();
        adapter = new NewGroupAdapter(this,  mUsers);

        readUsers();

        editEventName = findViewById(R.id.newEventName);
        findViewById(R.id.btnMakeEvent).setOnClickListener(view -> makeEvent());


    }

    private void readUsers() {

        //Get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Reference to contacts
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getEmail().replace('.','_')).child("Contacts");

        //Populates recyclerview
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(!(user.getEmail().replace('.','_')).equals(firebaseUser.getEmail().replace('.','_'))){
                        mUsers.add(user);
                    }
                }

                System.out.println(mUsers);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void makeEvent(){
        reference = FirebaseDatabase.getInstance().getReference("Events");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String eventName = editEventName.getText().toString();
        if(!eventName.isEmpty()) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(eventName).exists()) {
                        mEmails = adapter.getCheckedUsers();
                        if(mEmails.size() > 1){
                            createMoodList();
                            System.out.println(moods);
                            reference.child(eventName).child("Users").setValue(mEmails);
                            reference.child(eventName).child("eventName").setValue(eventName);
                            //Intent nextScreen = new Intent(getBaseContext(), eventMessageActivity.class);
                            //nextScreen.putExtra("eventName", eventName);
                            System.out.println("USERS ADDED " + adapter.getCheckedUsers());
                            System.out.println("CURRENT USER " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            //startActivityForResult(nextScreen, 0);
                            System.out.println("list of moods: " + moods);
                        }
                        else
                            Toast.makeText(getBaseContext(), "Please select one or more users.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Event name already exists.", Toast.LENGTH_SHORT).show();
                        editEventName.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
            Toast.makeText(getBaseContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();

    }

    private void createMoodList(){
        for(String e:mEmails){
            reference = FirebaseDatabase.getInstance().getReference("users").child(e).child("CurrentPreference");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    moods.add(dataSnapshot.getValue(Mood.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
