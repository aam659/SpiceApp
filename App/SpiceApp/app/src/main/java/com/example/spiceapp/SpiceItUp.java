package com.example.spiceapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiceapp.FirebaseObjects.Mood;
import com.example.spiceapp.FirebaseObjects.Price;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.search.Address;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ExtendedAttribute;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.PlaceRequest;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;
import com.here.android.mpa.search.SearchRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * SpiceItUp is the class for the SpiceItUp activity,
 * which allows users to generate random dining suggestions.
 * These suggestions can be personalized according to user's
 * preferences, if one is logged in. Otherwise, they are random.
 *
 * @author Logan Dawkins, Alan Manning
 */

// TODO: extend place results displayed
public class SpiceItUp extends AppCompatActivity {
    private boolean firstRun = true;    // flag if activity just started
    private static List<DiscoveryResult> s_ResultList;
    private PlacesClient placesClient;
    private final String TAG = "SpiceItUp";
    private LocationManager locationManager;    // LocationManager for location access
    private LocationListener locationListener;  // Location Listener
    private static double deviceLatitude;
    private static double deviceLongitude;
    private String name;    // name of place
    private String addr;    // address of place
    private Bitmap bitmap;  //pic of place
    private static String preferencesString; // string used for place query
    private static Mood mood;   // users current mood
    private static String moodName = "None"; // name of mood used
    private static int distance = 10; // distance in mood
    private static int lowPrice; // low price in mood
    private static int highPrice; //high price in mood

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_it_up);
        initializeToolbar();    //add toolbar
        initializeNavBar(); //add nav bar
        initMapEngine();    // start places API

        FirebaseManager.initialize(); // start firbase
        if(FirebaseManager.isLoggedIn()) {  //if user a authenticated get current mood
            getUserInfo(new FirebaseCallback() {
                @Override
                public void onCallback(String pref, int dist, int low, int hi) { //got mood
                    preferencesString = pref;
                    distance = dist;
                    lowPrice = low;
                    highPrice = hi;
                    locationSetup(new LocationCallBack() { //get user location
                        @Override
                        public void onCallback(double lat, double lon) { //all data is fetched, so find place
                            deviceLatitude = lat;
                            deviceLongitude = lon;
                            if(firstRun) {
                                findPlace();
                                firstRun = false;
                            }
                        }
                    });
                }
            });
        }
        else
        locationSetup(new LocationCallBack() { //user not logged in
            @Override
            public void onCallback(double lat, double lon) {
                deviceLatitude = lat;
                deviceLongitude = lon;
                findPlace();
            }
        });


        findViewById(R.id.btnSIU).setOnClickListener(view -> findPlace());
        findViewById(R.id.btnAccept).setOnClickListener(view -> launchMap());
        findViewById(R.id.btnChangeCategories).setOnClickListener(view -> chooseMood());
    }


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Synchronized Firebase <<<<<<<<<<<<<<<<<<<<<<< //

    /**
     * FirebaseCallback used to wait for the data to populate
     */
    private interface FirebaseCallback{
        void onCallback(String pref,int dist,int low,int hi);
    }

    /**
     * getUserInfo
     * @param firebaseCallback
     * gets current users mood values to refine search
     */
    private void getUserInfo(FirebaseCallback firebaseCallback){
        Query query = FirebaseManager.getCurrentPreference();
                /*
                Queries database for current user mood
                 */
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Since we need to pull an object from the database which is an implemented generic,
                //we can't just pull the interface class "ArrayList.class", so instead we create and object that
                //is in the format to let the firebase database know that we're going to be expecting a generic implementation
//                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>(){};
                mood = dataSnapshot.getValue(Mood.class);
                moodName = mood.getName();

                ArrayList<String> categories = mood.getCategories();
                int dist = (int) mood.getDistance();
                int low = mood.getPrice().getLowPrice();
                int high = mood.getPrice().getHighPrice();


                preferencesString = "";
                for (int i = categories.size() - 1; i > -1; --i) {
                    if (i != -1)
                        preferencesString += ", ";

                    preferencesString += categories.get(i);
                }

                firebaseCallback.onCallback(preferencesString,dist,low,high);
                // Debugging code
                // System.out.println("Categories: " + categories);
                // updateButton(dataSnapshot.getValue(String.class), btnMainAction);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> User Location <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //
    /**onRequestPermissionsResult for location permission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Permission Granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Have permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, locationListener);
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 10, 5, locationListener);
            }
        }
    }

    /**
     * locationSetup
     * retrieve users current location
     */
    private void locationSetup(LocationCallBack locationCallBack){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());
                // gets device latitude and longitude
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                locationCallBack.onCallback(lat,lon);
                // Log above constants for check
                Log.i("Latitude", String.valueOf(lat));
                Log.i("Longitude", String.valueOf(lon));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // Check for device running SDK < 23
        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, locationListener);
            }
        }

        else {
            // Request for permission if none
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 15);
            } else {
                // Location access already granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 5, locationListener);
            }

        }
    }

    /**
     * LocationCallBack
     * waits for the device location before calling back for findPlace
     */
    private interface LocationCallBack{
        void onCallback(double lat,double lon);
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> HereAPI <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //
    /**
     * initMapEngine
     * initialize hereAPI
     */
    private void initMapEngine(){
        MapEngine mapEngine = MapEngine.getInstance();
        mapEngine.init(this, new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // Post initialization code goes here
                } else {
                    // handle factory initialization failure
                    Toast.makeText(getApplicationContext(),
                            "ERROR:Failed to initialize Map Engine", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * findPlace
     * starts search request by finding a restaurant using hereAPI
     * if signed in it uses current mood else it uses a random search
     */
    private void findPlace(){
        SearchRequest searchRequest;
        if (FirebaseManager.isLoggedIn()) {
            searchRequest = new SearchRequest("Restaurant" + preferencesString);
        } else {
            searchRequest = new SearchRequest("Restaurant");
        }
        searchRequest.setSearchCenter(new GeoCoordinate(deviceLatitude,deviceLongitude));

        searchRequest.execute(discoveryResultPageListener);
    }

    /**
     * result listener for the here searchRequest in findPlace
     */
    private ResultListener<DiscoveryResultPage> discoveryResultPageListener = new ResultListener<DiscoveryResultPage>() {
        @Override
        public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
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
                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                        PlaceLink placeLink = (PlaceLink) item;
                        // Check for distance

                        if ((placeLink.getDistance() * .00062137) <= distance) {
                            PlaceRequest placeRequest = placeLink.getDetailsRequest();
                            System.out.println("Distance: " + (placeLink.getDistance() * .00062137));
                            placeRequest.execute(m_placeResultListener);
                            break;
                        }
                    }
                }
                // TODO: Implement case for no search results found
            } else {
                // Invalid Search
                Toast.makeText(getApplicationContext(), "Invalid Search Paramters", Toast.LENGTH_SHORT);
                System.out.println("Failed Search Request");
            }
        }
    };

    /**
     * result listener for place request in discovery listener
     */
    private ResultListener<Place> m_placeResultListener = new ResultListener<Place>() {
        @Override
        public void onCompleted(Place place, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
                name = place.getName();
                GeoCoordinate geoCoordinate = place.getLocation().getCoordinate();
                getHereAddress(geoCoordinate);
                autoComplete(place.getName());
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "ERROR:Place request returns error: " + errorCode, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    /**
     * getHereAddress
     * @param geoCoordinate
     * retrieves the restaurants address
     */
    private void getHereAddress(GeoCoordinate geoCoordinate){
        ReverseGeocodeRequest revGeo = new ReverseGeocodeRequest(geoCoordinate);
        revGeo.execute((new ResultListener<Address>() {
            @Override
            public void onCompleted(Address address, ErrorCode errorCode) {
                if(errorCode == ErrorCode.NONE){
                    addr = address.getText();
                }
                else{
                    System.out.println("Failed");
                }
            }
        }));
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Google Places <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //
    /**
     * autoComplete
     * @param query
     * takes name of restaurant and uses place id to call findPlaceById
     */
    private void autoComplete(String query){
        Places.initialize(getApplicationContext(),"AIzaSyDRXeL2mFFQmQPz3dpMn-wkIu87tmo_Tg4");
        placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setCountry("US")
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                // Returns Place ID
                findPlaceByID(prediction.getPlaceId());
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
    private void findPlaceByID(String id) {
// Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Field> fields =
                Arrays.asList(Field.PHOTO_METADATAS, Field.PRICE_LEVEL);

// Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(id, fields).build();

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            com.google.android.libraries.places.api.model.Place place = response.getPlace();

            // Check for price level
            if ((FirebaseManager.isLoggedIn()) && ((place.getPriceLevel() == null) || (place.getPriceLevel() > highPrice - 1) || (place.getPriceLevel() < lowPrice - 1))) {
                if (place.getPriceLevel() != null) {
                    System.out.println("Pricing" + place.getPriceLevel().toString());
                }
                findPlace();
            }
            else {
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
                        updateViews();
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

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Intents <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //
    //launch activty in future for nav
    private void launchMap(){
        Intent nextScreen = new Intent(SpiceItUp.this, MapPage.class);
        startActivityForResult(nextScreen, 0);
    }

    /**
     * chooseMood
     * takes user to choose a preference
     */
    private void chooseMood(){
        if (FirebaseManager.isLoggedIn()) {
            Intent nextScreen = new Intent(SpiceItUp.this, SetPreference.class);
            startActivityForResult(nextScreen, 0);
        } else {
            Toast.makeText(getApplicationContext(), "Not logged in!", Toast.LENGTH_SHORT).show();
        }
    }
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> UI <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //
    /**
     * updateViews
     * updates all views with the results place details
     */
    private void updateViews(){
        TextView txtName = findViewById(R.id.txtName);
        TextView txtLocation = findViewById(R.id.txtLocation);
        txtName.setText(name);
        txtLocation.setText(addr);
        ImageView restaurantImage = findViewById(R.id.imgRestuarant);
        restaurantImage.setImageBitmap(bitmap);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mood: " + moodName);
    }

    /**
     * initializeToolbar
     * setups up general purpose top action bar
     */
    private void initializeToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Spice It Up");
    }

    /**
     * initializeNavBar
     * sets up the bottom nav bar
     */
    private void initializeNavBar(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent nextScreen;
                switch (item.getItemId()) {
                    case R.id.tlbLogin:
                        if (FirebaseManager.isLoggedIn()) {
                            Toast.makeText(SpiceItUp.this, "Already logged in!", Toast.LENGTH_LONG).show();
                            return false;
                        } else {
                            nextScreen = new Intent(SpiceItUp.this, LoginPage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        }

                    case R.id.tlbSocial:
                        if (FirebaseManager.isLoggedIn()) {
                            nextScreen = new Intent(SpiceItUp.this, SocialPage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        } else {
                            Toast.makeText(SpiceItUp.this, "Not Logged In", Toast.LENGTH_LONG).show();
                            return false;
                        }

                    case R.id.tlbProfile:
                        if(FirebaseManager.isLoggedIn()) {
                            nextScreen = new Intent(SpiceItUp.this, ProfilePage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        } else {
                            Toast.makeText(SpiceItUp.this, "Not Logged In", Toast.LENGTH_LONG).show();
                            return false;
                        }

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
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< //
}