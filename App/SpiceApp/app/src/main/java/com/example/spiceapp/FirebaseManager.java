package com.example.spiceapp;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//This class was written as a quality of life for this app
//Some methods may be redundant, but to abstract getting commonly needed references to
//     some common aspects of the database will both cleanup code and keep things organized and standard


public class FirebaseManager {

    private static FirebaseAuth mAuth;
    private static FirebaseDatabase mDatabase;
    private static boolean init;
    private static String firstName;


    //Creates an initial database object if one has not already been made
    static void initialize(){
        if(init)
            return;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        init = true;
    }

    //Gets auth session
    static FirebaseAuth getAuth(){
        if(init)
            return mAuth;
        else {
            initialize();
            return mAuth;
        }
    }

    //Returns the current user
    static FirebaseUser getCurrentUser(){
        if(init)
            return mAuth.getCurrentUser();
        else {
            initialize();
            return mAuth.getCurrentUser();
        }
    }

    //Gets a reference to mDatabase, top level
    static DatabaseReference getDatabaseReference(){
        if(init)
            return mDatabase.getReference();
        else {
            initialize();
            return mDatabase.getReference();
        }
    }

    //Returns a reference to current user's first name
    static DatabaseReference getFirstNameReference() {
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("fName");
    }

    //Returns a reference to the top level of the moods level of the database
    static DatabaseReference getMoodsReference(){
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("Moods");
    }

    //Tells us if the current user is logged in
    static boolean isLoggedIn() {
            return !(FirebaseAuth.getInstance().getCurrentUser() == null);
    }
}
