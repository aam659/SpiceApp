package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.Address;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.PlaceRequest;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;
import com.here.android.mpa.search.SearchRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EventDisplay extends AppCompatActivity {
    private final String TAG = "EventDisplay";
    private PlacesClient placesClient;
    private static double rating = 0.0; // rating of place - Default in case null
    private Bitmap bitmap;  //pic of place

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        FirebaseManager.initialize();
        
        getCurrentPlace();
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
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Mood: " + moodName);
    }
}
