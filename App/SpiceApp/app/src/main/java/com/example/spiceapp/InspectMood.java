package com.example.spiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

//Class to allow users to view, edit, or delete a mood
public class InspectMood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_mood);
        initializeTextViews();


    }

    //Method to pull data from previous intent and set them in the textviews
    private void initializeTextViews() {
        //Retrieves mood name from previous intent
        String nameOfMood = getIntent().getStringExtra("NAME");
        //Set Mood Name Text view to mood name passed from previous intent
        final TextView textMoodName = (TextView) findViewById(R.id.textMoodName);
        textMoodName.setText(nameOfMood);

        //Methods below are the same as above
        String mealTime = getIntent().getStringExtra("MEALTIME");
        final TextView textMealTime = (TextView) findViewById(R.id.textMealTime);
        textMealTime.setText(mealTime);
    }
}
