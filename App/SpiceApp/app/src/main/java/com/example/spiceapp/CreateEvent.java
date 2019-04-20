package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spiceapp.Adapters.NewGroupAdapter;
import com.example.spiceapp.FirebaseObjects.Mood;
import com.example.spiceapp.FirebaseObjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.PlaceRequest;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CreateEvent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewGroupAdapter adapter;
    private List<User> mUsers;
    private EditText editEventName;
    private DatabaseReference reference;
    private FirebaseUser mUser;
    private ArrayList<String> mEmails;
    private ArrayList<Mood> currentPreferences = new ArrayList<>();
    private List<DiscoveryResult> s_ResultList;
    private LocationManager locationManager;    // LocationManager for location access
    private LocationListener locationListener;  // Location Listener
    private double deviceLatitude;
    private double deviceLongitude;
    private Mood mood;
    private int distance;
    private PlaceLink place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initMapEngine();    // start places API

        recyclerView = findViewById(R.id.recyclerNewEvent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = new ArrayList<>();
        adapter = new NewGroupAdapter(this,  mUsers);

        locationSetup(new LocationCallBack() {
            @Override
            public void onCallback(double lat, double lon) {
                deviceLatitude = lat;
                deviceLongitude = lon;
                readUsers();
            }
        });


        editEventName = findViewById(R.id.newEventName);
        findViewById(R.id.btnMakeEvent).setOnClickListener(view -> makeEvent(new EventCallBack() {
            @Override
            public void onCallback() {
                addEventToDB();
            }
        }));


    }

    private void readUsers() {

        //Get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Reference to contacts
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getEmail().replace('.','_')).child("Contacts");

        //Populates recyclerview
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(!(user.getEmail().replace('.','_')).equals(firebaseUser.getEmail().replace('.','_'))){
                        mUsers.add(user);
                    }
                }

                System.out.println(mUsers);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void makeEvent(CreateEvent.EventCallBack eventCallBack){
        reference = FirebaseDatabase.getInstance().getReference("Events");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String eventName = editEventName.getText().toString();
        if(!eventName.isEmpty()) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(eventName).exists()) {
                        mEmails = adapter.getCheckedUsers();
                        if(mEmails.size() > 1){
                            createMoodList(eventName);
                            findPlace();
                            //Intent nextScreen = new Intent(getBaseContext(), eventMessageActivity.class);
                            //nextScreen.putExtra("eventName", eventName);
                            //startActivityForResult(nextScreen, 0);
                        }
                        else
                            Toast.makeText(getBaseContext(), "Please select one or more users.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Event name already exists.", Toast.LENGTH_SHORT).show();
                        editEventName.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
            Toast.makeText(getBaseContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();

        eventCallBack.onCallback();
    }

    private interface EventCallBack{
        void onCallback();
    }

    private HashMap<String, Integer> getHashMap() {
        HashMap <String, Integer> rsvp = new HashMap<>();
        for(String email : mEmails){
            rsvp.put(email, 0);
        }
        return rsvp;
    }

    private void createMoodList(String eventName){
        for(String email : mEmails){
            DatabaseReference currentPreferenceReference = FirebaseDatabase.getInstance().getReference("users").child(email).child("CurrentPreference");
            currentPreferenceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        currentPreferences.add(dataSnapshot.getValue(Mood.class));
                        reference.child(eventName).child("currentPreferences").setValue(currentPreferences);

                    }
                    catch (Exception e){
                        System.out.println("CURRENT PREFERENCE NOT FOUND: " + e);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
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
     * findPlace
     * starts search request by finding a restaurant using hereAPI
     * if signed in it uses current mood else it uses a random search
     */
    private void findPlace(){
        Random rand = new Random();
        mood = currentPreferences.get(rand.nextInt(currentPreferences.size()));
        ArrayList<String> categories = mood.getCategories();


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
        searchRequest = new SearchRequest("Restaurant" + query );

        searchRequest.setSearchCenter(new GeoCoordinate(deviceLatitude,deviceLongitude));

        // Checks device coordinates
        System.out.println("COORDINATES: " + String.valueOf(deviceLatitude) + " " + String.valueOf(deviceLongitude));

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
                            place = placeLink;
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
    private void locationSetup(CreateEvent.LocationCallBack locationCallBack){
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

    private void addEventToDB() {
        String eventName = editEventName.getText().toString();
        reference = FirebaseDatabase.getInstance().getReference("Events");
        reference.child(eventName).child("users").setValue(mEmails);
        reference.child(eventName).child("eventName").setValue(eventName);
        reference.child(eventName).child("rsvp").setValue(getHashMap());
        reference.child(eventName).child("rsvp").setValue(place);
    }
}
