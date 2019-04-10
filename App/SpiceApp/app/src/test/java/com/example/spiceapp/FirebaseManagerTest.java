package com.example.spiceapp;

import android.app.Activity;

import com.example.spiceapp.FirebaseObjects.Mood;
import com.example.spiceapp.FirebaseObjects.Price;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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
import static org.mockito.Mockito.mock;
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
    private String phone;
    private double distance;
    private String currentPreference;
    private String nextPreference;
    private Price price;
    private ArrayList<String> moods;
    private Query query;

    private FirebaseAuth mockedFirebaseAuth;
    private FirebaseUser mockedFirebaseUser;
    private FirebaseManager mockedFirebaseManager;
    private FirebaseDatabase mockedFirebaseDatabase;
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
        phone = "2709783204";
        moods = new ArrayList<>();
        moods.add("Japanese");
        moods.add("Bar");
        currentPreference = "Japanese";
        nextPreference = "Steakhouse";

        //Setting up mock methods before we start testing
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        mockedFirebaseManager = Mockito.mock(FirebaseManager.class);

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

        // Set mocked user's first name
        mockedDatabaseReference.child("users").child(mockedFirebaseuser.getEmail().replace('.','_')()).child("fName").setValue(firstName);

        // Mock DataSnapshot
        DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);

        // Mock return of DatabaseSnapshot
        when(mockedDataSnapshot.getValue(String.class)).thenReturn("Michael");

        assert(mockedDataSnapshot.getValue(String.class)).equals(firstName);
    }

    @Test
    public void getMoodsReference() {
        // Mock ArrayList for moods
        ArrayList<String> moodsTest = new ArrayList<String>();
        moodsTest.add("Japanese");
        moodsTest.add("Bar");

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
        mockedDatabaseReference.child("users").child(mockedFirebaseuser.getEmail().replace('.','_')()).child("Moods").setValue(moods);

        // Mock DataSnapshot
        DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);

        for (int i = 0; i < moodsTest.size(); ++i) {
            // Mock return of DatabaseSnapshot
            when(mockedDataSnapshot.getValue(String.class)).thenReturn(moodsTest.get(i));
            assert (mockedDataSnapshot.getValue(String.class)).equals(moods.get(i));
        }
    }

    @Test
    public void getCurrentPreference() {
        when(mockedFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(result); //When the user logs in with 2 strings...

        // Returns reference to mockedDatabaseReference
        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);

        // The auth task should not have been completed yet
        assert (!result.isComplete());

        // Mock logging in
        Task<AuthResult> test = mockedFirebaseAuth.signInWithEmailAndPassword("test", "test");

        // Logged in check
        assert (result.isSuccessful());

        // We can see that our method of logging in can work
        mockedFirebaseUser = mockedFirebaseAuth.getCurrentUser();

        // Set mocked user's name to firstName
        mockedDatabaseReference.child("users").child(mockedFirebaseuser.getEmail().replace('.','_')()).child("CurrentPreference").child("name").setValue(currentPreference);

        // Mock DataSnapshot
        DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);

        // Mock return of DatabaseSnapshot
        when(mockedDataSnapshot.getValue(String.class)).thenReturn("Japanese");

        assert(mockedDataSnapshot.getValue(String.class)).equals(currentPreference);
    }

    @Test
    public void getSpecificMoodReference() {
        // Mock ArrayList for moods
        ArrayList<String> moodsTest = new ArrayList<String>();
        moodsTest.add("Japanese");
        moodsTest.add("Bar");

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
        mockedDatabaseReference.child("users").child(mockedFirebaseuser.getEmail().replace('.','_')()).child("Moods").setValue(moods);

        // Mock DataSnapshot
        DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);

        when(mockedDataSnapshot.getValue(String.class)).thenReturn(moodsTest.get(0));
        assert (mockedDataSnapshot.getValue(String.class)).equals(moods.get(0));
    }

    @Test
    public void getLastNameReference() {
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
        mockedDatabaseReference.child("users").child(mockedFirebaseuser.getEmail().replace('.','_')()).child("lName").setValue(lastName);

        // Mock DataSnapshot
        DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);

        // Mock return of DatabaseSnapshot
        when(mockedDataSnapshot.getValue(String.class)).thenReturn("Scott");

        assert(mockedDataSnapshot.getValue(String.class)).equals(lastName);
    }

    @Test
    public void getPhoneNumberReference() {
        when(mockedFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(result); //When the user logs in with 2 strings...

        // Returns reference to mockedDatabaseReference
        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);

        //The auth task should not have been completed yet
        assert (!result.isComplete());

        // Mock logging in
        Task<AuthResult> test = mockedFirebaseAuth.signInWithEmailAndPassword("test", "test");

        // Logged in check
        assert (result.isSuccessful());

        //We can see that our method of logging in can work
        mockedFirebaseUser = mockedFirebaseAuth.getCurrentUser();

        // Set mocked user's name to firstName
        mockedDatabaseReference.child("users").child(mockedFirebaseuser.getEmail().replace('.','_')()).child("phoneNumber").setValue(phone);

        // Mock DataSnapshot
        DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);

        // Mock return of DatabaseSnapshot
        when(mockedDataSnapshot.getValue(String.class)).thenReturn("2709783204");

        assert(mockedDataSnapshot.getValue(String.class)).equals(phone);
    }
}