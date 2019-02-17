package com.example.spiceapp;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
//import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class SpiceItUp extends AppCompatActivity {
    private PlacesClient placesClient;
    private TextView txtName;
    private ImageView imgRestuarant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_it_up);

        initializeToolbar();

        initializeViews();

        // Create client object
        //placesClient = Places.createClient(this); //causes crash for some reason

        //find a random restuarant and update values
        //spiceItUp();

        // Set listeners for programmatic spiceItUp()
        findViewById(R.id.btnSIU).setOnClickListener(view -> spiceItUp());
    }

    private void initializeToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Spice It Up");
    }

    private void initializeViews(){
        txtName = findViewById(R.id.txtName);
        imgRestuarant = findViewById(R.id.imgRestuarant);
    }

    private void spiceItUp(){
        // Define a Place ID.
        String placeId = "ChIJN1t_tDeuEmsRUsoyG83frY4";

        // Specify the fields to return (in this example all fields are returned).
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);

        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            txtName.setText(place.getName());
            updateImage();
            // In future add directions to the restaurant?
            Toast.makeText(this, "It's Spicy", Toast.LENGTH_LONG).show();
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                txtName.setText("Failed to get the Place object");
                Toast.makeText(this, "Not Spicy", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateImage(/*I think parameter will be PhotoMetadata*/){
        //update image view with new restaurant result
    }

}
