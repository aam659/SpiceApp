package com.example.spiceapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Adapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ListMoods extends AppCompatActivity {

    private RecyclerView recyclerView; //The container that holds the items of a list
    private ListMoodAdapter listMoodAdapter; //The class that manages the objects that are in the list
    private List<Mood> moodList; //Class that binds the recycler view to our activity

    private FirebaseUser user; //Current User
    private DatabaseReference database; //Database Reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_moods);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        //Button to initiate adding a new Mood
        //Moves the activity to the NameMood activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(view.getContext(), NameMood.class);
                startActivityForResult(nextScreen, 0);
            }
        });

        //Below is the procedure that this activity follows to create the list
        //Fetches recycler view, just like finding a button
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMood);

        //Use this to increase performance, good for lists with objects of fixed size
        recyclerView.setHasFixedSize(true);

        //Sets up communication between the layout and the list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //List to pass to our adapter to populate the list
        moodList = new ArrayList<>();

        //Allows us to use our adapter to populate the list
        listMoodAdapter = new ListMoodAdapter(this, moodList);

        //The foundations of our lists are initialized, now we're going to populate everything
        //Get a reference to all of the user's moods
        // NOTE We can get creative with this, like return moods with BBQ or return moods that are for bfast, and should be easy, this is base case
        database = FirebaseManager.getMoodsReference();

        //Finally, we define a listener to make this list update in real time
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                moodList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Mood mood = snapshot.getValue(Mood.class);
                        moodList.add(mood);
                    }
                    listMoodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //  ¯\_(ツ)_/¯
            }
        };



    }

}
