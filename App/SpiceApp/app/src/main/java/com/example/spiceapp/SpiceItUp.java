package com.example.spiceapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpiceItUp extends AppCompatActivity {
    private TextView txtName;
    private ImageView imgRestuarant;
    public static List<DiscoveryResult> s_ResultList;
    public static DiscoveryResult s_ResultListItem;
    // map embedded in the map fragment
    private Map map = null;
    // map fragment embedded in this activity
    private SupportMapFragment mapFragment= null;
    private List<MapObject> m_mapObjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_it_up);

       boolean isLogged = isLoggedIn();

        initializeToolbar();

        initializeViews();

        initializeMap();

        findPlace();

        // Set listeners for programmatic spiceItUp()
        findViewById(R.id.btnSIU).setOnClickListener(view -> findPlace());

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
                        if(isLogged) {
                            nextScreen = new Intent(SpiceItUp.this, ProfilePage.class);
                            startActivityForResult(nextScreen, 0);
                        }
                        else
                            Toast.makeText(SpiceItUp.this, "Not Logged In", Toast.LENGTH_LONG).show();
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

    private void initializeMap() {
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the map center to the Vancouver region (no animation)
                    map.setCenter(new GeoCoordinate(33.2098, -87.56592, 0.0),
                            Map.Animation.NONE);
                    // Set the zoom level to the average between min and max
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                } else {
                    System.out.println("ERROR: Cannot initialize Map Fragment");
                }
            }
        });
    }

    private void findPlace(){
        //https://developer.here.com/documentation/android-starter/dev_guide/topics/places.html
        cleanMap();
        SearchRequest searchRequest = new SearchRequest("Restaurant");
        searchRequest.setSearchCenter(new GeoCoordinate(33.2140,-87.5391));
        searchRequest.execute(discoveryResultPageListener);
    }

    private ResultListener<DiscoveryResultPage> discoveryResultPageListener = new ResultListener<DiscoveryResultPage>() {
        @Override
        public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
                /* No error returned,let's handle the results */
                //m_placeDetailButton.setVisibility(View.VISIBLE);

                /*
                 * The result is a DiscoveryResultPage object which represents a paginated
                 * collection of items.The items can be either a PlaceLink or DiscoveryLink.The
                 * PlaceLink can be used to retrieve place details by firing another
                 * PlaceRequest,while the DiscoveryLink is designed to be used to fire another
                 * DiscoveryRequest to obtain more refined results.
                 */
                s_ResultList = discoveryResultPage.getItems();
                Collections.shuffle(s_ResultList);
                for (DiscoveryResult item : s_ResultList) {
                    /*
                     * Add a marker for each result of PlaceLink type.For best usability, map can be
                     * also adjusted to display all markers.This can be done by merging the bounding
                     * box of each result and then zoom the map to the merged one.
                     */
                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                        PlaceLink placeLink = (PlaceLink) item;
                        s_ResultListItem = item;
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle(item.getTitle());
                        addMarkerAtPlace(placeLink);
                        return;
                    }
                }


            } else {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("Ooof");
            }
        }
    };

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("loginKey", false);
    }


    private void addMarkerAtPlace(PlaceLink placeLink) {
        Image img = new Image();
        try {
            img.setImageResource(R.drawable.marker);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapMarker mapMarker = new MapMarker();
        mapMarker.setIcon(img);
        mapMarker.setCoordinate(new GeoCoordinate(placeLink.getPosition()));
        map.addMapObject(mapMarker);
        m_mapObjectList.add(mapMarker);
    }

    private void cleanMap() {
        if (!m_mapObjectList.isEmpty()) {
            map.removeMapObjects(m_mapObjectList);
            m_mapObjectList.clear();
        }
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
