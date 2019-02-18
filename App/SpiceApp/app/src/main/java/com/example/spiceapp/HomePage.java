package com.example.spiceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


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

        private void initializeToolbar(){
            Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);
            ActionBar actionBar = getSupportActionBar();
        }

        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
            return true;
        }
    }



}

/*private void initializePlaces(){
            String apiKey = "";

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
        }*/