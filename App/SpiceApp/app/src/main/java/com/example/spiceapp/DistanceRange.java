package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.spiceapp.FirebaseObjects.Mood;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DistanceRange extends AppCompatActivity {

    DatabaseReference database;
    String currMood;
    Mood curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_range);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        final String nameOfMood = getIntent().getStringExtra("NAME_OF_MOOD");
        String isCurr = getIntent().getStringExtra("ISCURR");
        final Spinner distance = (Spinner) findViewById(R.id.spinnerDistance);
        ArrayAdapter<CharSequence> distanceAdapt = ArrayAdapter.createFromResource(this, R.array.distances, android.R.layout.simple_spinner_dropdown_item);
        distanceAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance.setAdapter(distanceAdapt);

        final CardView btnConfirm = (CardView) findViewById(R.id.cardConfirmDistance);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double distanceNum = Double.valueOf(distance.getSelectedItem().toString().split(" ")[0]);
                database.child("users").child(user.getEmail().replace('.','_')).child("Moods").child(nameOfMood).child("distance").setValue(distanceNum);
                if(isCurr.equals("yes"))
                    database.child("users").child(user.getEmail().replace('.','_')).child("CurrentPreference").child("distance").setValue(distanceNum);
                Intent nextScreen = new Intent(v.getContext(), HomePage.HomePageActivity.class);
                startActivityForResult(nextScreen, 0);
            }
        });

    }
}