package com.example.spiceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NameMood extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_mood);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        final CardView createMood = (CardView) findViewById(R.id.cardConfirmName);
        final TextView moodName = (TextView) findViewById(R.id.edtMoodName);

        createMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mood = moodName.getText().toString();
                if(!mood.isEmpty()) {
                    database.child("users").child(user.getUid()).child("Moods").child(mood).setValue(mood);
                    database.child("users").child(user.getUid()).child("Moods").child(mood).child("name").setValue(mood);
                    Intent nextScreen = new Intent(v.getContext(), MealTime.class);
                    nextScreen.putExtra("NAME_OF_MOOD", mood);
                    startActivityForResult(nextScreen, 0);
                }
                else Toast.makeText(v.getContext(), "Please enter a name for the mood.", Toast.LENGTH_LONG).show();

            }
        });


    }
}
