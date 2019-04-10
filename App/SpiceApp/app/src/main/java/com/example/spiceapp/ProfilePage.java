package com.example.spiceapp;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ProfilePage extends AppCompatActivity {

    private FirebaseStorage storage;
    StorageReference storageReference;
    private static FirebaseUser user;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private static String fullName = null;
    private final String TAG = "ProfilePage";
    private static String phoneNumber;

    /**
     * FirebaseCallback used to wait for the data to populate
     */
    private interface FirebaseCallback{
        void onCallback(String fullName, String phoneNumber);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView name = (TextView) findViewById(R.id.textName);
        TextView number = (TextView) findViewById(R.id.textPhone);
        final TextView btnMoods = (TextView) findViewById(R.id.btnMoods);
        final TextView btnLogout = (TextView) findViewById(R.id.btnLogout);
        final TextView btnEdit = (TextView) findViewById(R.id.btnEditDetails);
        final ImageView imgProfile = (ImageView) findViewById(R.id.imgProfilePic);
        final TextView btnUpload = (TextView) findViewById(R.id.btnUpload);

//        mAuth = FirebaseManager.getAuth();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseManager.initialize();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Task<Uri> task = storageReference.child("images/"+FirebaseManager.getCurrentUser().getEmail().replace('.','_')).getDownloadUrl();
        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to load image",Toast.LENGTH_LONG);
            }
        });

        // Gets Firebase user info
        getUserInfo(new FirebaseCallback() {
            @Override
            public void onCallback(String fullName, String phoneNumber) {
                if (fullName != null) {
                    name.setText(fullName);
                }

                if (phoneNumber != null) {
                    number.setText("Phone Number: " + phoneNumber);
                }
            }
        });

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

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        getPhoto();

                    }
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
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
    }

    public void getPhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 1);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getPhoto();

            }
        }
    }

    /**
     * getUserInfo
     * @param firebaseCallback
     * gets current users mood values to refine search
     */
    private void getUserInfo(FirebaseCallback firebaseCallback){
        if(FirebaseManager.isLoggedIn()) {
            Query query = FirebaseManager.getDatabaseReference().child("users").child(FirebaseManager.getCurrentUser().getEmail().replace('.','_'));
                    /*
                    Queries database for current user mood
                     */
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    fullName = dataSnapshot.child("fName").getValue(String.class) + " " + dataSnapshot.child("lName").getValue(String.class);
                    phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                    //TODO Add username and email fields to be displayed

                    firebaseCallback.onCallback(fullName, phoneNumber);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);

                ImageView imageView = (ImageView) findViewById(R.id.imgProfilePic);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ FirebaseManager.getCurrentUser().getEmail().replace('.','_'));
            System.out.println("File name:" + FirebaseManager.getCurrentUser().getEmail().replace('.','_'));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfilePage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfilePage.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Error message: " + e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
