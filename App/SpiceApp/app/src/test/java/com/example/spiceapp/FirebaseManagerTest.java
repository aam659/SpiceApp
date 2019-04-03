package com.example.spiceapp;

import android.app.Activity;

import com.example.spiceapp.FirebaseObjects.Mood;
import com.example.spiceapp.FirebaseObjects.Price;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @Author Alan Manning
 * Tests various aspects of the app using the Firebase
 * realtime database
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class FirebaseManagerTest {
    private Mood spicy;
    private String firstName;
    private String lastName;
    private String mealTime;
    private double distance;
    private String currentPreference;
    private Price price;
    private ArrayList<String> keywords;

    private FirebaseAuth mockedFirebaseAuth;
    private FirebaseUser mockedFirebaseUser;
    private FirebaseManager mockedFirebaseManager;
    private DatabaseReference mockedDatabaseReference;

    //Safely ignore this, set up a mock result for testing
    private Task<AuthResult> result = new Task<AuthResult>() {
        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Nullable
        @Override
        public AuthResult getResult() {
            return null;
        }

        @Nullable
        @Override
        public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    @Before
    public void setUp() throws Exception {
        firstName = "Michael";
        lastName = "Scott";
        mealTime = "Dinner";
        distance = 10;
        price = new Price();
        price.setHighPrice(4);
        price.setLowPrice(1);
        keywords = new ArrayList<>();
        keywords.add("Japanese");
        spicy = new Mood(firstName,mealTime,distance,price,keywords);
        Query query;

        //Setting up mock methods before we start testing

        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);

        //Gives a false reference to a FirebaseAuth
        mockedFirebaseAuth = Mockito.mock(FirebaseAuth.class);

        //Gives a false reference to a FirebaseUser
        mockedFirebaseUser = Mockito.mock(FirebaseUser.class);

        //When we call the Auth's getcurrentUser() method, it will return our mock user
        when(mockedFirebaseAuth.getCurrentUser()).thenReturn(mockedFirebaseUser);
    }

    @Test
    public void getFirstNameReference() {
        when(mockedFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(result); //When the user logs in with 2 strings...

        // Returns reference to mockedDatabaseReference
        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);

        //The auth task should not have been completed yet
        assert (!result.isComplete());

//        Mock logging in
        Task<AuthResult> test = mockedFirebaseAuth.signInWithEmailAndPassword("test", "test");

        //Logged in check
        assert (result.isSuccessful());

        //We can see that our method of logging in can work
        mockedFirebaseUser = mockedFirebaseAuth.getCurrentUser();

        // Set mocked user's name to firstName
        mockedDatabaseReference.child("users").child(mockedFirebaseUser.getUid()).child("fName").setValue(firstName);

        // Check if user's name is firstName
        assert(FirebaseManager.getFirstNameReference().equals(firstName));
    }

//    @Test
//    public void getMoodsReference() {
//    }
//
//    @Test
//    public void getCurrentPreference() {
//    }
//    @Test
//    public void getLastNameReference() {
//    }
//    @Test
//    public void getPhoneNumberReference() {
//    }
}