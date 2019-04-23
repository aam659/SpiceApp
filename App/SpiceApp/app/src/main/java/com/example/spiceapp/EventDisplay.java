package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiceapp.FirebaseObjects.Mood;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.Random;

/**
 * Event display handles the users voting for a place and getting the new place
 *
 * @author Logan Dawkins
 */
public class EventDisplay extends AppCompatActivity {
    private final String TAG = "EventDisplay";
    private PlacesClient placesClient;
    private static double rating = 0.0; // rating of place - Default in case null
    private Bitmap bitmap;  //pic of place
    private int distance;
    private List<DiscoveryResult> s_ResultList;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        initializeToolbar(); //add toolbar
        initializeNavBar(); //add nav bar
        initMapEngine();
        FirebaseManager.initialize();
        findViewById(R.id.btnNo).setOnClickListener(view -> vote(-1));
        findViewById(R.id.btnYes).setOnClickListener(view -> vote(1));
        findViewById(R.id.btnDetails).setOnClickListener(view -> dropPin());
        pd = new ProgressDialog(this);

        final String name = getIntent().getStringExtra("eventName");
        Query query = FirebaseManager.getEventRefernce(name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int vote = dataSnapshot.child("rsvp").child(FirebaseManager
                        .getCurrentUser().getEmail().replace('.','_')).getValue(Integer.class);
                ActionBar actionBar = getSupportActionBar();
                if(vote == 1)
                    actionBar.setTitle("Current Vote: Yes");
                else if(vote == -1)
                    actionBar.setTitle("Current Vote: No");
                else
                    actionBar.setTitle("You Have Not Voted.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getCurrentPlace();
        Query query1 = FirebaseManager.getEventRefernce(name).child("addr");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getCurrentPlace();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCurrentPlace(){
        pd.setTitle("Fetching Place...");
        pd.show();
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
        pd.dismiss();
        TextView txtName = findViewById(R.id.txtName);
        TextView txtLocation = findViewById(R.id.txtLocation);
        RatingBar ratingBar = findViewById(R.id.rating);

        txtLocation.setText(addr);
        ratingBar.setRating((float)rating);
        txtName.setText(name);

        ImageView restaurantImage = findViewById(R.id.imgRestuarant);
        restaurantImage.setImageBitmap(bitmap);
    }

    private void vote(int x){
        final String name = getIntent().getStringExtra("eventName");
        DatabaseReference db = FirebaseManager.getEventRefernce(name);
        db.child("rsvp").child(FirebaseManager.getCurrentUser().getEmail().replace('.','_')).setValue(x)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        checkVotes();
                    }
                });
    }

    private void checkVotes(){
        final String name = getIntent().getStringExtra("eventName");
        Query query = FirebaseManager.getEventRefernce(name).child("rsvp");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double yes = 0;
                double no = 0;
                GenericTypeIndicator<HashMap<String,Integer>> t = new GenericTypeIndicator<HashMap<String,Integer>>() {};
                HashMap<String,Integer> rsvpMap = dataSnapshot.getValue(t);
                for(Map.Entry<String,Integer> entry : rsvpMap.entrySet()){
                    if(entry.getValue() == 1)   yes++;
                    else if (entry.getValue() == -1)    no++;
                }

                if(yes/rsvpMap.size() >= .5) {
                    Intent next = new Intent(getApplicationContext(), EventResult.class);
                    next.putExtra("eventName",name);
                    startActivityForResult(next, 0);
                }
                else if (no/rsvpMap.size() > .5)
                    updatePlace();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * initializeToolbar
     * setups up general purpose top action bar
     */
    private void initializeToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void updatePlace(){
        final String name = getIntent().getStringExtra("eventName");
        Query query = FirebaseManager.getEventRefernce(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double lat = dataSnapshot.child("lat").getValue(Double.class);
                double lon = dataSnapshot.child("lon").getValue(Double.class);
                GenericTypeIndicator<HashMap<String,Integer>> t = new GenericTypeIndicator<HashMap<String,Integer>>() {};
                HashMap<String,Integer> map = dataSnapshot.child("rsvp").getValue(t);
                for(Map.Entry<String,Integer> entry : map.entrySet()){
                    entry.setValue(0);
                }
                FirebaseManager.getEventRefernce(name).child("rsvp").setValue(map);
                placeSearch(lat,lon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void placeSearch(double lat,double lon){
        final String eventName = getIntent().getStringExtra("eventName");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events").child(eventName);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Mood>> t = new GenericTypeIndicator<ArrayList<Mood>>() {};
                ArrayList<Mood> yourStringArray = dataSnapshot.child("currentPreferences").getValue(t);
                Random rand = new Random();
                Mood mood;
                ArrayList<String> categories;
                if(yourStringArray.size() > 0) {
                    mood = yourStringArray.get(rand.nextInt(yourStringArray.size()));
                    categories = mood.getCategories();


                    distance = (int) mood.getDistance();
                    String query = "";
                    if (categories != null) {
                        for (int i = categories.size() - 1; i > -1; --i) {
                            if (i != -1)
                                query += ", ";

                            query += categories.get(i);
                        }
                    } else {
                        query += ", Breakfast";
                    }
                    SearchRequest searchRequest;
                    searchRequest = new SearchRequest("Restaurant" + query);

                    searchRequest.setSearchCenter(new GeoCoordinate(lat, lon));

                    // Checks device coordinates
                    System.out.println("COORDINATES: " + String.valueOf(lat) + " " + String.valueOf(lon));

                    searchRequest.execute(discoveryResultPageListener);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

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
                            System.out.println("PLACEDETS" + placeRequest.getContent().toString());
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
    private ResultListener<com.here.android.mpa.search.Place> m_placeResultListener = new ResultListener<com.here.android.mpa.search.Place>() {
        @Override
        public void onCompleted(com.here.android.mpa.search.Place place, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
                // Check for place coordinates
                System.out.println("GEOCOORD" + String.valueOf(place.getLocation().getCoordinate()));
                getHereAddress(place.getLocation().getCoordinate(),place.getName());
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
    private void getHereAddress(GeoCoordinate geoCoordinate,String name){
        final String eventName = getIntent().getStringExtra("eventName");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events").child(eventName);
        ReverseGeocodeRequest revGeo = new ReverseGeocodeRequest(geoCoordinate);
        revGeo.execute((new ResultListener<Address>() {
            @Override
            public void onCompleted(Address address, ErrorCode errorCode) {
                if(errorCode == ErrorCode.NONE){
                    ref.child("name").setValue(name);
                    ref.child("addr").setValue(address.getText());
                    ref.child("lat").setValue(geoCoordinate.getLatitude());
                    ref.child("lon").setValue(geoCoordinate.getLongitude());
                }
                else{
                    System.out.println("Failed");
                }
            }
        }));
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
                            Toast.makeText(EventDisplay.this, "Already logged in!", Toast.LENGTH_LONG).show();
                            return false;
                        } else {
                            nextScreen = new Intent(EventDisplay.this, LoginPage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        }

                    case R.id.tlbSocial:
                        if (FirebaseManager.isLoggedIn()) {
                            nextScreen = new Intent(EventDisplay.this, SocialPage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        } else {
                            Toast.makeText(EventDisplay.this, "Not Logged In", Toast.LENGTH_LONG).show();
                            return false;
                        }

                    case R.id.tlbProfile:
                        if(FirebaseManager.isLoggedIn()) {
                            nextScreen = new Intent(EventDisplay.this, ProfilePage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        } else {
                            Toast.makeText(EventDisplay.this, "Not Logged In", Toast.LENGTH_LONG).show();
                            return false;
                        }

                    case R.id.tlbHome:
                        nextScreen = new Intent(EventDisplay.this, HomePage.HomePageActivity.class);
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
}
