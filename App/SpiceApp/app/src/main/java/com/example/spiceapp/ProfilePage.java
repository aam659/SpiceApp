package com.example.spiceapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ProfilePage extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseManager.getAuth();

        final Button btnMoods = (Button)findViewById(R.id.btnMoods);
        final Button btnLogout = (Button)findViewById(R.id.btnLogout);

        btnMoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(v.getContext(), ListMoods.class);
                startActivityForResult(nextScreen, 0);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent nextScreen = new Intent(v.getContext(), HomePage.HomePageActivity.class);
                startActivityForResult(nextScreen, 0);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent nextScreen;
                switch (item.getItemId()) {
                    case R.id.tlbLogin:
                        if (FirebaseManager.isLoggedIn()) {
                            Toast.makeText(ProfilePage.this, "Already logged in!", Toast.LENGTH_LONG).show();
                            return false;
                        } else {
                            nextScreen = new Intent(ProfilePage.this, LoginPage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        }

                    case R.id.tlbSIU:
                        nextScreen = new Intent(ProfilePage.this, SpiceItUp.class);
                        startActivityForResult(nextScreen, 0);
                        return true;

                    case R.id.tlbHome:
                        nextScreen = new Intent(ProfilePage.this, HomePage.HomePageActivity.class);
                        startActivityForResult(nextScreen, 0);
                        return true;

                    case R.id.tlbSocial:
                        nextScreen = new Intent(ProfilePage.this, SocialPage.class);
                        startActivityForResult(nextScreen, 0);
                        return true;
                    case R.id.tlbProfile:
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
