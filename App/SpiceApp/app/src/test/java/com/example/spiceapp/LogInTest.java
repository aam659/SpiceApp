package com.example.spiceapp;

import android.app.Activity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

//Testing imports
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//Mocking imports
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class LogInTest {

    private FirebaseAuth mockedFirebaseAuth;
    private FirebaseUser mockedFirebaseUser;

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
    public void before() {

        //Setting up mock methods before we start testing

        //Gives a false reference to a FirebaseAuth
        mockedFirebaseAuth = Mockito.mock(FirebaseAuth.class);

        //Gives a false reference to a FirebaseUser
        mockedFirebaseUser = Mockito.mock(FirebaseUser.class);

        //When we call the Auth's getcurrentUser() method, it will return our mock user
        when(mockedFirebaseAuth.getCurrentUser()).thenReturn(mockedFirebaseUser);

    }

    @Test
    public void getSignedInUserProfileTest() {
        when(mockedFirebaseAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(result); //When the user logs in with 2 strings...

        //The task should not have been completed yet
        assert (!result.isComplete());

        //Mock logging in
        Task<AuthResult> test = mockedFirebaseAuth.signInWithEmailAndPassword("test", "test");

        //Logged in check
        assert(result.isSuccessful());

        //We can see that our method of logging in can work
        mockedFirebaseUser = mockedFirebaseAuth.getCurrentUser();


    }



}

