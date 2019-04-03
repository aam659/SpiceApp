package com.example.spiceapp;
import com.example.spiceapp.FirebaseObjects.Mood;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

/** This class was written as a quality of life for this app.
 * Some methods may be redundant, but to abstract getting commonly
 * needed references to some common aspects of the database will
 * both cleanup code and keep things organized and standard.
 *
 * @author Ryan Simpson
 */


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

    //Returns a reference to current user's last name
    static DatabaseReference getLastNameReference() {
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("lName");
    }

    //Returns a reference to current user's phone number
    static DatabaseReference getPhoneNumberReference() {
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("phoneNumber");
    }

    //Returns a reference to the top level of the moods level of the database
    static DatabaseReference getMoodsReference(){
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("Moods");
    }

    //Returns a reference to the top level of the images level of the database
    static DatabaseReference getImageReference(){
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("Images");
    }

    //Returns a reference to the top level of the moods level of the database
    static DatabaseReference getSpecifcMoodReference(String mood){
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("Moods").child(mood);
    }

    //Save selected preference into the firebase database
    static void setCurrentPreference(Mood pref){
        mDatabase.getReference("users").child(getCurrentUser().getUid()).child("CurrentPreference").setValue(pref);
    }

    //Returns a REFERENCE to the current mood
    static DatabaseReference getCurrentPreference(){
        return mDatabase.getReference("users").child(getCurrentUser().getUid()).child("CurrentPreference");
    }

    static void clearCurrentPreference(){
        mDatabase.getReference("users").child(getCurrentUser().getUid()).child("CurrentPreference").removeValue();
    }

    //Method to delete a node, please use carefully
    static void deleteDatabaseNode(DatabaseReference node){
        node.removeValue();
    }

    //Tells us if the current user is logged in
    static boolean isLoggedIn() {
            return !(FirebaseAuth.getInstance().getCurrentUser() == null);
    }
}
