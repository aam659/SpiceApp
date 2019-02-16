package com.example.spiceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places; //Added by Ryan, Google Places API
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient; //Added by Ryan, Google Places API

import java.util.Arrays;
import java.util.List;

public class HomePage extends AppCompatActivity {
//    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        /*//If you put the api key in here, make sure you delete it before you push
        //If you accidentally push the api key, tell Ryan so he can regenerate the key
        //and redistribute it
        String apiKey = "AIzaSyDcNwFNunrrjSZQ5HMUhdJKHAVk9CdW59I";
        Toast.makeText(this, "in create", Toast.LENGTH_LONG).show();


        if (apiKey.equals("")) {
            Toast.makeText(this, "error with api key", Toast.LENGTH_LONG).show();
            return;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
            placesClient = Places.createClient(this);
        }*/
    }

    public static class HomePageActivity extends AppCompatActivity{
        private PlacesClient placesClient;


        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);

            //If you put the api key in here, make sure you delete it before you push
            //If you accidentally push the api key, tell Ryan so he can regenerate the key
            //and redistribute it
            String apiKey = "";

            if (apiKey.equals("")) {
                Toast.makeText(this, "error with api key", Toast.LENGTH_LONG).show();
                return;
            }

            // Setup Places Client
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), apiKey);
                placesClient = Places.createClient(this);
            }

            // Define a Place ID.
            String placeId = "ChIJN1t_tDeuEmsRUsoyG83frY4";

            // Specify the fields to return (in this example all fields are returned).
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);

            // Construct a request object, passing the place ID and fields array.
            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                Toast.makeText(this, place.getName(), Toast.LENGTH_LONG).show();
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
                }
            });

            final Button btnLogin =(Button) findViewById(R.id.btnLogin);
            final Button btnSpiceItUp =(Button) findViewById(R.id.btnSpice);
            final Button btnSocial = (Button) findViewById(R.id.btnSocial);
            final Button btnProfile = (Button) findViewById(R.id.btnProfile);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "LOGIN BUTTON CLICKED", Toast.LENGTH_LONG).show();
                }
            });

            btnSpiceItUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    System.out.println("siu clicked");
                    Toast.makeText(v.getContext(), "SIU BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                    Intent nextScreen = new Intent(v.getContext(), SpiceItUp.class);
                    startActivityForResult(nextScreen, 0);
                }
            });

            btnSocial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "SOCIAL BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                    Intent nextScreen = new Intent(v.getContext(), SocialPage.class);
                    startActivityForResult(nextScreen, 0);                }
            });

            btnProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "PROFILE BUTTON CLICKED", Toast.LENGTH_LONG).show();
                }
            });



        }


    }



}
