package com.example.spiceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class DistanceRange extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_range);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        final String nameOfMood = getIntent().getStringExtra("NAME_OF_MOOD");

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
                Intent nextScreen = new Intent(v.getContext(), HomePage.HomePageActivity.class);
                startActivityForResult(nextScreen, 0);
            }
        });

    }
}
