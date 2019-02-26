package com.example.spiceapp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    static FirebaseAuth mAuth;
    static FirebaseDatabase mDatabase;
    static boolean authInit;
    static boolean dataInit;

    static void initializeAuthentication(){
        if(authInit)
            return;
        mAuth = FirebaseAuth.getInstance();
        authInit = true;
    }

    static void initializeDatabase(){
        if(dataInit)
            return;
        mDatabase = FirebaseDatabase.getInstance();
        authInit = true;
    }

    static FirebaseUser getCurrentUser(){
        if(authInit)
            return mAuth.getCurrentUser();
        else {
            System.out.println("ERROR: Use FirebaseManger.initalizeAuthentication() before getting user.");
            System.exit(1);
            return null;
        }
    }

    static DatabaseReference getDatabaseReference(){
        if(authInit)
            return mDatabase.getReference();
        else {
            System.out.println("ERROR: Use FirebaseManger.initializeDatabase() before getting reference.");
            System.exit(1);
            return null;
        }
    }
}
