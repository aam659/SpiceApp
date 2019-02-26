package com.example.spiceapp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

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

    static private void pullFirstName(){
        DatabaseReference ref = getDatabaseReference();
        DatabaseReference valueRef = ref.child("users").child(getCurrentUser().getUid()).child("fName");
        valueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        });
    }


    static DatabaseReference getDatabaseReference(){
        if(init)
            return mDatabase.getReference();
        else {
            initialize();
            return mDatabase.getReference();
        }
    }

    public static DatabaseReference getFirstNameReference() {
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("fName");
    }
}
