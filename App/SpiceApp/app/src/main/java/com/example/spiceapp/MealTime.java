package com.example.spiceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MealTime extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_time);


        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        String nameOfMood = getIntent().getStringExtra("NAME_OF_MOOD");
        final CardView breakfast = (CardView) findViewById(R.id.cardBreakfast);
        final CardView lunch = (CardView) findViewById(R.id.cardLunch);
        final CardView dinner = (CardView) findViewById(R.id.cardDinner);

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Breakfast Clicked", Toast.LENGTH_LONG).show();
                database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("MealTime").setValue("Breakfast");
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Lunch Clicked", Toast.LENGTH_LONG).show();
                database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("MealTime").setValue("Lunch");
                
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Dinner Clicked", Toast.LENGTH_LONG).show();
                database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("MealTime").setValue("Dinner");
            }
        });



    }
}
