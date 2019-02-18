package com.example.spiceapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places; //Added by Ryan, Google Places API


public class HomePage extends AppCompatActivity {

    @Override   //this constructor never gets called. I tested by placing a toast in it     - logan
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public static class HomePageActivity extends AppCompatActivity{

        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);

            initializeToolbar();

            //If you put the api key in here, make sure you delete it before you push
            //If you accidentally push the api key, tell Ryan so he can regenerate the key
            //and redistribute it
//            initializePlaces();


            final Button btnLogin =(Button) findViewById(R.id.btnLogin);
            final Button btnSpiceItUp =(Button) findViewById(R.id.btnSpice);
            final Button btnSocial = (Button) findViewById(R.id.btnSocial);
            final Button btnProfile = (Button) findViewById(R.id.btnProfile);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "LOGIN BUTTON CLICKED", Toast.LENGTH_LONG).show();
                    Intent nextScreen = new Intent(v.getContext(), LoginPage.class);
                    startActivityForResult(nextScreen, 0);
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

        private void initializeToolbar(){
            Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);
            ActionBar actionBar = getSupportActionBar();
        }

        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
            return true;
        }

        private void initializePlaces(){
            String apiKey = "AIzaSyA1msmRX58N7mqgmJlPXiU1EKK_pa2X4gQ";

            if (apiKey.equals("")) {
                Toast.makeText(this, "error with api key", Toast.LENGTH_LONG).show();
                return;
            }

            // Setup Places Client
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), apiKey);
            }
            if (!Places.isInitialized()) {
                Toast.makeText(this, "Places not initialized", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Intent nextScreen;
            switch (item.getItemId()) {
                case R.id.tlbLogin:
                    nextScreen = new Intent(HomePageActivity.this, LoginPage.class);
                    startActivityForResult(nextScreen, 0);
                    return true;

                case R.id.tlbSIU:
                    nextScreen = new Intent(HomePageActivity.this, SpiceItUp.class);
                    startActivityForResult(nextScreen, 0);
                    return true;

                case R.id.tlbProfile:
                    // User chose the "Favorite" action, mark the current item
                    // as a favorite...
                    return true;

                case R.id.tlbSocial:
                    nextScreen = new Intent(HomePageActivity.this, SocialPage.class);
                    startActivityForResult(nextScreen, 0);
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }


    }



}
