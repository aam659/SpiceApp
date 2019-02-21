package com.example.spiceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SocialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_page);

//        initializeToolbar();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent nextScreen;
                boolean isLogged = isLoggedIn();

                switch (item.getItemId()) {
                    case R.id.tlbLogin:
                        nextScreen = new Intent(SocialPage.this, LoginPage.class);
                        startActivityForResult(nextScreen, 0);
                        return true;

                    case R.id.tlbSIU:
                        nextScreen = new Intent(SocialPage.this, SpiceItUp.class);
                        startActivityForResult(nextScreen, 0);
                        return true;

                    case R.id.tlbProfile:
                        if (isLogged) {
                            nextScreen = new Intent(SocialPage.this, ProfilePage.class);
                            startActivityForResult(nextScreen, 0);
                        } else
                            Toast.makeText(SocialPage.this, "Not Logged In", Toast.LENGTH_LONG).show();
                        return true;

                    case R.id.tlbHome:
                        nextScreen = new Intent(SocialPage.this, HomePage.HomePageActivity.class);
                        startActivityForResult(nextScreen, 0);
                        return true;
                    case R.id.tlbSocial:
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

}
