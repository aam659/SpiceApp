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

public class PriceRange extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_range);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        final String nameOfMood = getIntent().getStringExtra("NAME_OF_MOOD");
        String isCurr = getIntent().getStringExtra("ISCURR");
        final Spinner maximum = (Spinner) findViewById(R.id.spinnerMaximum);
        ArrayAdapter<CharSequence> maxAdapt = ArrayAdapter.createFromResource(this, R.array.priceRange, android.R.layout.simple_spinner_dropdown_item);
        maxAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maximum.setAdapter(maxAdapt);

        final Spinner minimum = (Spinner) findViewById(R.id.spinnerMinimum);
        ArrayAdapter<CharSequence> minAdapt = ArrayAdapter.createFromResource(this, R.array.priceRange, android.R.layout.simple_spinner_dropdown_item);
        maxAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minimum.setAdapter(minAdapt);

        final CardView btnConfirm = (CardView) findViewById(R.id.cardConfirmPrice);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxInt = getIntegerRepresentation(maximum.getSelectedItem().toString());
                database.child("users").child(user.getEmail().replace('.','_')).child("Moods").child(nameOfMood).child("price").child("HighPrice").setValue(maxInt);
                int minInt = getIntegerRepresentation(minimum.getSelectedItem().toString());
                database.child("users").child(user.getEmail().replace('.','_')).child("Moods").child(nameOfMood).child("price").child("LowPrice").setValue(minInt);
                if(isCurr.equals("yes")){
                    database.child("users").child(user.getEmail().replace('.','_')).child("CurrentPreference").child("price").child("highPrice").setValue(maxInt);
                    database.child("users").child(user.getEmail().replace('.','_')).child("CurrentPreference").child("price").child("lowPrice").setValue(minInt);
                }
                Intent nextScreen = new Intent(v.getContext(), DistanceRange.class);
                nextScreen.putExtra("ISCURR",isCurr);
                nextScreen.putExtra("NAME_OF_MOOD", nameOfMood);
                startActivityForResult(nextScreen, 0);
            }
        });

    }

    private int getIntegerRepresentation(String price){
        switch (price){
            case "$":
                return 1;
            case "$$" :
                return 2;
            case "$$$":
                return 3;
            case "$$$$" :
                return 4;
            case "$$$$$":
                return 5;
        }
        return 0;
    }

}
