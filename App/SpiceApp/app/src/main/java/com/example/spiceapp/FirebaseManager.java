package com.example.spiceapp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    static FirebaseAuth mAuth;
    static FirebaseDatabase mDatabase;
    static boolean init;
    private static String firstName;


    static void initialize(){
        if(init)
            return;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        init = true;
    }

    static FirebaseAuth getAuth(){
        if(init)
            return mAuth;
        else {
            initialize();
            return mAuth;
        }
    }

    static FirebaseUser getCurrentUser(){
        if(init)
            return mAuth.getCurrentUser();
        else {
            initialize();
            return mAuth.getCurrentUser();
        }
    }

    static DatabaseReference getDatabaseReference(){
        if(init)
            return mDatabase.getReference();
        else {
            initialize();
            return mDatabase.getReference();
        }
    }

    static DatabaseReference getFirstNameReference() {
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("fName");
    }

    static boolean isLoggedIn() {
            return !(FirebaseAuth.getInstance().getCurrentUser() == null);
    }
}
