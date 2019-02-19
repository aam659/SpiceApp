package com.example.spiceapp;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

<<<<<<< HEAD
import android.widget.ImageView;
import android.widget.TextView;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.util.List;
=======
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
>>>>>>> a26a6d3... Recovered previous changes.

public class SpiceItUp extends AppCompatActivity {
    private TextView txtName;
    private ImageView imgRestuarant;
    public static List<DiscoveryResult> s_ResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_it_up);


        initializeViews();

        findPlace();

        // Set listeners for programmatic spiceItUp()
<<<<<<< HEAD
        // findViewById(R.id.btnSIU).setOnClickListener(view -> findPlace());
    }
=======
        // findViewById(R.id.btnSIU).setOnClickListener(view -> hereAPI());
>>>>>>> a26a6d3... Recovered previous changes.

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent nextScreen;
                switch (item.getItemId()) {
                    case R.id.tlbLogin:
                        nextScreen = new Intent(SpiceItUp.this, LoginPage.class);
                        startActivityForResult(nextScreen, 0);
                        return true;

                    case R.id.tlbSocial:
                        nextScreen = new Intent(SpiceItUp.this, SocialPage.class);
                        startActivityForResult(nextScreen, 0);
                        return true;

                    case R.id.tlbProfile:
                        // User chose the "Favorite" action, mark the current item
                        // as a favorite...
                        return true;

                    case R.id.tlbHome:
                        nextScreen = new Intent(SpiceItUp.this, HomePage.HomePageActivity.class);
                        startActivityForResult(nextScreen, 0);
                        return true;
                    case R.id.tlbSIU:
                        return true;
                    default:
                        // If we got here, the user's action was not recognized.
                        //Do nothing
                        return false;
                }
            }
        });
    }


    private void initializeViews(){
        txtName = findViewById(R.id.txtName);
        imgRestuarant = findViewById(R.id.imgRestuarant);
    }

    private void findPlace(){
        //https://developer.here.com/documentation/android-starter/dev_guide/topics/places.html
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Request");
        SearchRequest searchRequest = new SearchRequest("Hotel");
        searchRequest.setSearchCenter(new GeoCoordinate(33.2140,87.5391));
        searchRequest.execute(discoveryResultPageListener);
    }

    private ResultListener<DiscoveryResultPage> discoveryResultPageListener = new ResultListener<DiscoveryResultPage>() {
        @Override
        public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
                /* No error returned,let's handle the results */

                /*
                 * The result is a DiscoveryResultPage object which represents a paginated
                 * collection of items.The items can be either a PlaceLink or DiscoveryLink.The
                 * PlaceLink can be used to retrieve place details by firing another
                 * PlaceRequest,while the DiscoveryLink is designed to be used to fire another
                 * DiscoveryRequest to obtain more refined results.
                 */
                s_ResultList = discoveryResultPage.getItems();
                for (DiscoveryResult item : s_ResultList) {
                    /*
                     * Add a marker for each result of PlaceLink type.For best usability, map can be
                     * also adjusted to display all markers.This can be done by merging the bounding
                     * box of each result and then zoom the map to the merged one.
                     */
                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle("SPICY");
                    }
                }
            } else {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("Not spicy");
            }
        }
    };

    private void updateImage(/*I think parameter will be PhotoMetadata*/){
        //update image view with new restaurant result
    }

}


//    private void spiceItUp(){
//        // Define a Place ID.
//        String placeId = "ChIJN1t_tDeuEmsRUsoyG83frY4";
//
//        // Specify the fields to return (in this example all fields are returned).
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);
//
//
//        // Construct a request object, passing the place ID and fields array.
//        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
//
//        Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);
//
//        placeTask.addOnSuccessListener(
//                (response) -> {
//                    Place place = response.getPlace();
//                    txtName.setText(place.getName());
//                });
//
//        placeTask.addOnFailureListener(
//                (exception) -> {
//                    exception.printStackTrace();
//                    txtName.setText("Failed to get the Place object");
//                    Toast.makeText(this, "Not Spicy", Toast.LENGTH_LONG).show();
//                });
//    }

//    private void spiceItUp1(){
//        // Define a Place ID.
//        String placeId = "ChIJN1t_tDeuEmsRUsoyG83frY4";
//
//        // Specify the fields to return (in this example all fields are returned).
//        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);
//
//        // Construct a request object, passing the place ID and fields array.
//        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
//
//        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
//            Place place = response.getPlace();
//            txtName.setText(place.getName());
//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                ApiException apiException = (ApiException) exception;
//                int statusCode = apiException.getStatusCode();
//                // Handle error with given status code.
//                txtName.setText("Failed to get the Place object");
//                Toast.makeText(this, "Not Spicy", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
