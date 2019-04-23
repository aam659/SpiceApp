package com.example.spiceapp;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdditionalDetails extends AppCompatActivity {

    private DatabaseReference database;
    private String fname;
    private String lname;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtional_details);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        CardView finish = (CardView) findViewById(R.id.cardFinish);
        TextView name = (TextView) findViewById(R.id.edtName);
        TextView phoneNumber = (TextView) findViewById(R.id.edtPhone);
        TextView lName = (TextView) findViewById(R.id.edtLName);

        /*Functionality to display the user info that is currently stored in the database
            The text boxes used to edit will be populated with
            First Name, Last Name, and Phone Number
         */
                Query query = FirebaseManager.getDatabaseReference().child("users").child(FirebaseManager.getCurrentUser().getEmail().replace('.','_'));
                    /*
                    Queries database for current user mood
                     */
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        fname = dataSnapshot.child("fName").getValue(String.class);
                        lname = dataSnapshot.child("lName").getValue(String.class);
                        phoneNum = dataSnapshot.child("phoneNumber").getValue(String.class);
                        name.setText(fname);
                        lName.setText(lname);
                        phoneNumber.setText(phoneNum);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                /*When Finish button is clicked save all the text views data
                    This is used to update the user information
                 */
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserName = name.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();
                String newUserLName = lName.getText().toString();
                if(!userPhoneNumber.isEmpty())
                addDetails(newUserName, userPhoneNumber, newUserLName, user);
                startActivityForResult(new Intent(AdditionalDetails.this, HomePage.HomePageActivity.class), 0);
            }
        });

    }
            //After the information is saved it is uploaded to firebase

    protected void addDetails(String fName, String phone, String lName, FirebaseUser user) {
        String email = getIntent().getStringExtra("UserEmail");

        database.child("users").child(user.getEmail().replace('.','_')).child("uid").setValue(user.getUid());
        if(email != null)
            database.child("users").child(user.getEmail().replace('.','_')).child("email").setValue(email.replace('.','_'));
        if(!fName.isEmpty())
            database.child("users").child(user.getEmail().replace('.','_')).child("fName").setValue(fName);
        if(!lName.isEmpty())
            database.child("users").child(user.getEmail().replace('.','_')).child("lName").setValue(lName);
        if(!phone.isEmpty())
            database.child("users").child(user.getEmail().replace('.','_')).child("phoneNumber").setValue(phone);

        database.child("users").child(user.getEmail().replace('.','_'))
                .child("Moods/Default/name").setValue("Default");
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("Moods/Default/mealTime").setValue("Dinner");
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("Moods/Default/distance").setValue(25);
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("Moods/Default/price/HighPrice").setValue(5);
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("Moods/Default/price/LowPrice").setValue(1);

        database.child("users").child(user.getEmail().replace('.','_'))
                .child("CurrentPreference/name").setValue("Default");
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("CurrentPreference/mealTime").setValue("Dinner");
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("CurrentPreference/distance").setValue(25);
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("CurrentPreference/price/highPrice").setValue(5);
        database.child("users").child(user.getEmail().replace('.','_'))
                .child("CurrentPreference/price/lowPrice").setValue(1);
    }
}
