package com.example.spiceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.spiceapp.Tabs.SocialPageAdapter;
import com.example.spiceapp.Tabs.Tab1Fragment;
import com.example.spiceapp.Tabs.Tab2Fragment;
import com.example.spiceapp.Tabs.Tab3Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

/**
 * Social Page which handles the tasks of the TabLayout within, currently plan to
 * have a chatroom tab, an event tab, and a poll tab
 * WILL BE REPLACED SHORTLY
 * @author Ryan Simpson
 */

public class SocialPage extends AppCompatActivity {

    private SocialPageAdapter socialPageAdapter;
    private static final String TAG = "SocialPage";
    private ViewPager socialViewPager;


    //This thing is the tells the TabLayout what to do whenever a tab gets clicked
    private void setupViewPager(ViewPager viewPager){
        SocialPageAdapter adapter = new SocialPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "TAB1");
        adapter.addFragment(new Tab2Fragment(), "TAB2");
        adapter.addFragment(new Tab3Fragment(), "TAB3");
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_page);
        Log.d(TAG, "onCreate: Starting.");

        //Call the adapter that will inflate our tabs
        socialPageAdapter = new SocialPageAdapter(getSupportFragmentManager());
        socialViewPager = (ViewPager) findViewById(R.id.socialViewPager);
        setupViewPager(socialViewPager);

        //Return the TabLayout that we will inflate our tabs with
        TabLayout tabLayout = (TabLayout) findViewById(R.id.socialTab);
        tabLayout.setupWithViewPager(socialViewPager);








//        initializeToolbar();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent nextScreen;
                boolean isLogged = isLoggedIn();

                switch (item.getItemId()) {
                    case R.id.tlbLogin:
                        if (FirebaseManager.isLoggedIn()) {
                            Toast.makeText(SocialPage.this, "Already logged in!", Toast.LENGTH_LONG).show();
                        } else {
                            nextScreen = new Intent(SocialPage.this, LoginPage.class);
                            startActivityForResult(nextScreen, 0);
                        }
                        return true;

                    case R.id.tlbSIU:
                        nextScreen = new Intent(SocialPage.this, SpiceItUp.class);
                        startActivityForResult(nextScreen, 0);
                        return true;

                    case R.id.tlbProfile:
                        if (FirebaseManager.isLoggedIn()) {
                            nextScreen = new Intent(SocialPage.this, ProfilePage.class);
                            startActivityForResult(nextScreen, 0);
                        } else
                            Toast.makeText(SocialPage.this, "Not Logged In!", Toast.LENGTH_LONG).show();
                        return true;

                    case R.id.tlbHome:
                        nextScreen = new Intent(SocialPage.this, HomePage.HomePageActivity.class);
                        startActivityForResult(nextScreen, 0);
                        return true;
                    case R.id.tlbSocial:
                        if (FirebaseManager.isLoggedIn()) {
                            return true;
                        } else {
                            Toast.makeText(SocialPage.this, "Not Logged In!", Toast.LENGTH_LONG).show();
                        }

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
