package com.example.spiceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    public static class HomePageActivity extends AppCompatActivity {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);
            String btnText;
            boolean isLogged = isLoggedIn();
            final Button btnMainAction = (Button)findViewById(R.id.btnMainAct);
            final Button btnLogOut = (Button)findViewById(R.id.btnLogO);

            if(isLogged){

                btnText = getResources().getString(R.string.strMainIsLogged);
                btnMainAction.setText(btnText);
            }
            else{
                btnText = getResources().getString(R.string.strMainNotLogged);
                btnMainAction.setText(btnText);
            }

            btnLogOut.setOnClickListener(new View.OnClickListener() {
                String Logged = "loginKey";
                @Override
                public void onClick(View v) {
                    if(isLogged){
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Logged, false);
                        editor.apply();
                        Intent nextScreen = new Intent(v.getContext(), HomePageActivity.class);
                        startActivityForResult(nextScreen, 0);
                    }
                }
            });


            btnMainAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isLogged){
                        Intent nextScreen = new Intent(v.getContext(), SpiceItUp.class);
                        startActivityForResult(nextScreen, 0);
                    }
                    else{
                        Intent nextScreen = new Intent(v.getContext(), LoginPage.class);
                        startActivityForResult(nextScreen, 0);
                    }
                }
            });


            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
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
                            nextScreen = new Intent(HomePageActivity.this, ProfilePage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;

                        case R.id.tlbSocial:
                            nextScreen = new Intent(HomePageActivity.this, SocialPage.class);
                            startActivityForResult(nextScreen, 0);
                            return true;
                        case R.id.tlbHome:
                            return true;
                        default:
                            // If we got here, the user's action was not recognized.
                            //Do nothing
                            return false;
                    }
                }
            });


        }

        boolean isLoggedIn(){
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            return sharedPreferences.getBoolean("loginKey", false);
        }


    }
}