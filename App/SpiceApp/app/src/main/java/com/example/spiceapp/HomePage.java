package com.example.spiceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class HomePage extends AppCompatActivity {

    public static class HomePageActivity extends AppCompatActivity {

        private FirebaseUser user;
        private DatabaseReference database;
        private String userName = null;

        private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
        private static final String[] RUNTIME_PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseManager.initialize();
            user = FirebaseManager.getCurrentUser();

            // Initialize top toolbar
            initializeToolbar();

            if (hasPermissions(this, RUNTIME_PERMISSIONS)) {
                //setupMapFragmentView();
                if (!(FirebaseManager.isLoggedIn()))
                    Toast.makeText(this, "Got permission", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat
                        .requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
            }


            final TextView btnMainAction = (TextView) findViewById(R.id.btnMainAct);

            if(FirebaseManager.isLoggedIn()) {
                Query query = FirebaseManager.getFirstNameReference();
                /*
                This is a simple example of how to "query" the database
                 */
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userName = dataSnapshot.getValue(String.class);
                        System.out.println("userName in listener: " + userName);
                        updateButton(dataSnapshot.getValue(String.class), btnMainAction);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            btnMainAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FirebaseManager.isLoggedIn()){
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
                            // System.out.println("userName when login: " + userName);
                            if (FirebaseManager.isLoggedIn()) {
                                Toast.makeText(HomePageActivity.this, "Already logged in!", Toast.LENGTH_LONG).show();
                                return false;
                            } else {
                                nextScreen = new Intent(HomePageActivity.this, LoginPage.class);
                                startActivityForResult(nextScreen, 0);
                                return true;
                            }

                        case R.id.tlbSIU:
                            nextScreen = new Intent(HomePageActivity.this, SpiceItUp.class);
                            startActivityForResult(nextScreen, 0);
                            return true;

                        case R.id.tlbProfile:
                            if(FirebaseManager.isLoggedIn()) {
                                nextScreen = new Intent(HomePageActivity.this, ProfilePage.class);
                                startActivityForResult(nextScreen, 0);
                                return true;
                            } else {
                                Toast.makeText(HomePageActivity.this, "Not Logged In!", Toast.LENGTH_LONG).show();
                                return false;
                            }

                        case R.id.tlbSocial:
                            if (FirebaseManager.isLoggedIn()) {
                                nextScreen = new Intent(HomePageActivity.this, SocialPage.class);
                                startActivityForResult(nextScreen, 0);
                                return true;
                            } else {
                                Toast.makeText(HomePageActivity.this, "Not Logged In!", Toast.LENGTH_LONG).show();
                                return false;
                            }
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

        private void initializeToolbar(){
            Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Home");
        }

        private void updateButton(String value, TextView btnMainAction) {
            String btnText;
            if(FirebaseManager.isLoggedIn()){
                System.out.println("CURRENT USER NAME " + userName);
                if (userName != null) {
                    btnText = "Hi " + userName + "! Feeling spicy?";
                }

                else {
                    btnText = "Hi default user! Feeling spicy?";
                }
                btnMainAction.setText(btnText);
            }
            else{
                btnText = "Looks like you're not signed in. Login?";
                btnMainAction.setText(btnText);
            }
        }

        private static boolean hasPermissions(Context context, String... permissions) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission)
                            != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            switch (requestCode) {
                case REQUEST_CODE_ASK_PERMISSIONS: {
                    for (int index = 0; index < permissions.length; index++) {
                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {

                            /*
                             * If the user turned down the permission request in the past and chose the
                             * Don't ask again option in the permission request system dialog.
                             */
                            if (!ActivityCompat
                                    .shouldShowRequestPermissionRationale(this, permissions[index])) {
                                Toast.makeText(this, "Required permission " + permissions[index]
                                                + " not granted. "
                                                + "Please go to settings and turn on for sample app",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Required permission " + permissions[index]
                                        + " not granted", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    //setupMapFragmentView();
                    break;
                }
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}