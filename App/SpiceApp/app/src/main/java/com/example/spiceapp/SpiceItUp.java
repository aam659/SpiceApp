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
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.Media;
import com.here.android.mpa.search.MediaCollectionPage;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.PlaceRequest;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.util.Collections;
import java.util.List;

public class SpiceItUp extends AppCompatActivity {
    private TextView txtName;
    private TextView txtLocation;
    private TextView txtUrl;
    private ImageView imgRestuarant;
    public static List<DiscoveryResult> s_ResultList;
    public static DiscoveryResult s_ResultListItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_it_up);

       boolean isLogged = isLoggedIn();

        initializeToolbar();

        initializeViews();

        initMapEngine();
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

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("loginKey", false);
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
        txtLocation = findViewById(R.id.txtLocation);
        txtUrl = findViewById(R.id.txtUrl);
    }

    private void initMapEngine(){
        MapEngine mapEngine = MapEngine.getInstance();
        mapEngine.init(this, new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // Post initialization code goes here
                } else {
                    // handle factory initialization failure
                    System.out.println("ERROR: Cannot initialize Map Engine");
                }}
        });
    }

    private void findPlace(){
        //https://developer.here.com/documentation/android-starter/dev_guide/topics/places.html
        SearchRequest searchRequest = new SearchRequest("Restaurant");
        searchRequest.setSearchCenter(new GeoCoordinate(33.2140,-87.5391));
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
                        PlaceRequest placeRequest = placeLink.getDetailsRequest();
                        placeRequest.execute(m_placeResultListener);
                        txtName.setText(item.getTitle());
                        return;
                    }
                }


            } else {
                System.out.println("Failed Search Request");
            }
        }
    };

    private ResultListener<Place> m_placeResultListener = new ResultListener<Place>() {
        @Override
        public void onCompleted(Place place, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
                /*
                 * No error returned,let's show the name and location of the place that just being
                 * selected.Additional place details info can be retrieved at this moment as well,
                 * please refer to the HERE Android SDK API doc for details.
                 */
                txtName.setText(place.getName());
                GeoCoordinate geoCoordinate = place.getLocation().getCoordinate();
                txtLocation.setText(geoCoordinate.toString());
                txtUrl.setText(place.getIconUrl());
            } else {
                Toast.makeText(getApplicationContext(),
                        "ERROR:Place request returns error: " + errorCode, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };
}

