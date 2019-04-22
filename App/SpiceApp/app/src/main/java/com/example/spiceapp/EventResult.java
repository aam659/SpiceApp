package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.here.android.mpa.search.DiscoveryResult;

import java.util.Arrays;
import java.util.List;

/**
 * Event result displays the place the group decided on and offers directions and more place details
 *
 * @author Logan Dawkins
 */
public class EventResult extends AppCompatActivity {
    private final String TAG = "EventResult";
    private PlacesClient placesClient;
    private static double rating = 0.0; // rating of place - Default in case null
    private Bitmap bitmap;  //pic of place


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_result);
        initializeToolbar();
        FirebaseManager.initialize();
        ActionBar bar = getSupportActionBar();
        final String name = getIntent().getStringExtra("eventName");
        bar.setTitle("Event Complete");

        getCurrentPlace();
        findViewById(R.id.btnAccept).setOnClickListener(view -> launchMap());
        findViewById(R.id.btnDetails).setOnClickListener(view -> dropPin());
    }

    private void getCurrentPlace(){
        final String name = getIntent().getStringExtra("eventName");
        Query query = FirebaseManager.getEventRefernce(name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("name").getValue(String.class);
                double lat = dataSnapshot.child("lat").getValue(Double.class);
                double lon = dataSnapshot.child("lat").getValue(Double.class);
                String addr = dataSnapshot.child("addr").getValue(String.class);
                autoComplete(title,lat,lon,addr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Google Places <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //
    /**
     * autoComplete
     * @param query
     * takes name of restaurant and uses place id to call findPlaceById
     */
    private void autoComplete(String query,double lat,double lon,String addr){
        Places.initialize(getApplicationContext(),"AIzaSyDRXeL2mFFQmQPz3dpMn-wkIu87tmo_Tg4");
        placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        RectangularBounds box = RectangularBounds.newInstance(
                new LatLng(lat-1,lon-1),new LatLng(lat+1,lon+1));

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setCountry("US")
                .setLocationBias(box)
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                // Returns Place ID
                findPlaceByID(prediction.getPlaceId(),query,addr);
                return;
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                System.out.println("Autocomplete prediction failed with code: " + apiException.getStatusCode());
                System.out.println("***"+apiException.getMessage()+"***");
            }
        });
    }

    /**
     * findPlaceById
     * @param id
     * takes place id, retrieves relevant place details from google Places
     */
    private void findPlaceByID(String id,String name,String addr) {
// Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields =
                Arrays.asList(Place.Field.PHOTO_METADATAS, Place.Field.PRICE_LEVEL, Place.Field.RATING);

// Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(id, fields).build();

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            com.google.android.libraries.places.api.model.Place place = response.getPlace();

            //Get the restaurant phone number and rating.
            if(place.getRating() != null) {
                rating = place.getRating();
            }
            else if(place.getRating() == null)
                System.out.println("rating was null");

            // Get the photo metadata.
            if (place.getPhotoMetadatas() != null) {
                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);

                // Get the attribution text.
                String attributions = photoMetadata.getAttributions();

                // Create a FetchPhotoRequest.
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    bitmap = fetchPhotoResponse.getBitmap();
                    updateViews(addr,name);
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        // Handle error with given status code.
                        Log.e(TAG, "Place not found: " + exception.getMessage());
                    }
                });
            } else {
                ImageView imgRestaurant = (ImageView) findViewById(R.id.imgRestuarant);
                imgRestaurant.setImageResource(R.drawable.chilli_logo);
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.getMessage());
            }
        });
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //

    /**
     * updateViews
     * updates all views with the results place details
     */
    private void updateViews(String addr,String name){
        TextView txtName = findViewById(R.id.txtName);
        TextView txtLocation = findViewById(R.id.txtLocation);
        RatingBar ratingBar = findViewById(R.id.rating);

        txtLocation.setText(addr);
        ratingBar.setRating((float)rating);
        txtName.setText(name);

        ImageView restaurantImage = findViewById(R.id.imgRestuarant);
        restaurantImage.setImageBitmap(bitmap);
    }

    /**
     * initializeToolbar
     * setups up general purpose top action bar
     */
    private void initializeToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    //launch activty in future for nav
    private void launchMap(){
        final String name = getIntent().getStringExtra("eventName");
        Query query = FirebaseManager.getEventRefernce(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String addr = dataSnapshot.child("addr").getValue(String.class);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + addr);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void dropPin(){
        final String name = getIntent().getStringExtra("eventName");
        Query query = FirebaseManager.getEventRefernce(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String addr = dataSnapshot.child("addr").getValue(String.class);
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + addr);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
