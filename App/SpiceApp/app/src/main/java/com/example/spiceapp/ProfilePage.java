package com.example.spiceapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfilePage extends AppCompatActivity {

    FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference database;
    private String userFName = null;
    private String userLName = null;
    private String userPhone = null;
    private String userEmail = null;
    /*
    private Bitmap userPhoto = null;
    private Uri imgFilePath;
    private static final int GALLERY_INTENT = 2;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseManager.getAuth();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseManager.initialize();
        user = FirebaseManager.getCurrentUser();


        final Button btnMoods = (Button)findViewById(R.id.btnMoods);
        final Button btnLogout = (Button)findViewById(R.id.btnLogout);
        final Button btnEdit = (Button)findViewById(R.id.btnEditDetails);
        final TextView txtUserFName = (TextView)findViewById(R.id.textUserFName);
        final TextView txtUserLName = (TextView)findViewById(R.id.textUserLName);
        final TextView txtUserEmail = (TextView)findViewById(R.id.textUserEmail);
        final TextView txtUserPhone = (TextView)findViewById(R.id.textUserPhone);
        final ImageView imgPicture= (ImageView)findViewById(R.id.imgProfilePic);

        //Functionality to display User Profile information
        if(FirebaseManager.isLoggedIn()) {
            //Display user first name
            Query queryFname = FirebaseManager.getFirstNameReference();
            queryFname.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userFName = dataSnapshot.getValue(String.class);
                    String textFname = userFName;
                    if (textFname != null) {
                        txtUserFName.setText(textFname);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            //Display user Last Name
            Query queryLname = FirebaseManager.getLastNameReference();
            queryLname.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userLName = dataSnapshot.getValue(String.class);
                    String textLname = userLName;
                    if (textLname != null) {
                        txtUserLName.setText(textLname);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Display user Phone Number
            Query queryPhone = FirebaseManager.getPhoneReference();
            queryPhone.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userPhone = dataSnapshot.getValue(String.class);
                    String textPhone = userPhone;
                    if (textPhone != null) {
                        txtUserPhone.setText(textPhone);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Display user Email
            if(user.getEmail()!= null){
                userEmail = user.getEmail();
                txtUserEmail.setText(userEmail);
            }

            /*
            Query queryPhoto = FirebaseManager.getPhotoReference();
            queryPhoto.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userPhoto = dataSnapshot.getValue(Bitmap.class);
                    Bitmap imgPhoto = userPhoto;
                    if (imgPhoto != null){
                        imgPicture.setImageBitmap(imgPhoto);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            */
        }

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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(v.getContext(), AdditionalDetails.class);
                startActivityForResult(nextScreen, 0);
            }
        });


        //TODO: REPLACE WITH BOTTOM NAV BAR FUNCTION
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
        /*
        imgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");
                imgIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(imgIntent, "Choose Photo"), GALLERY_INTENT);
            }
        });
        */
    }
    /**
     *
     * @param requestCode GALLEY_INTENT
     * @param resultCode Should be OK
     * @param image Returns Image selected from Gallery
     * This activity takes the image selected formats it for the database and saves it.
     */
    /*
    public void onActivityResult(int requestCode, int resultCode, Intent image) {
        super.onActivityResult(requestCode, resultCode, image);
        if ((requestCode == GALLERY_INTENT) && (resultCode == RESULT_OK) && (image.getData() != null)) {
            imgFilePath = image.getData();
        }
        database = FirebaseManager.getDatabaseReference();
        //Make imgFile Path URI into URL
        //  database.child("users").child(user.getUid()).child("profilePicture").setValue(imgFilePath);
        System.out.println("Image URI: " + imgFilePath.toString());
        Toast.makeText(ProfilePage.this, "Upload successful", Toast.LENGTH_SHORT).show();
    }
    */
}
