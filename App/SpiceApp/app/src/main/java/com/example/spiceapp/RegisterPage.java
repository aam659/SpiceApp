package com.example.spiceapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseManager.getAuth();

        final TextView email = (TextView) findViewById(R.id.edtNewUser);
        final TextView password = (TextView) findViewById(R.id.edtNewUserPassword);
        final TextView retype = (TextView) findViewById(R.id.edtRetype);
        final CardView create = (CardView) findViewById(R.id.cardCreate);



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newUser = email.getText().toString();
                String newUserPassword = password.getText().toString();
                String passwordRetype = retype.getText().toString();
                if(newUserPassword.equals(passwordRetype)) {
                    createAccount(newUser, newUserPassword);
                }
                else{
                    Toast.makeText(v.getContext(), "Passwords do not match.", Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    protected void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
//                            mFirebaseDatabase.getReference("Users").child(userEmail).child("FirstName").setValue(name);
                            moveToAdditionalDetails(email, password);
                        } else {
                            // If sign in fails, display a message
                            // to the user.
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            Toast.makeText(RegisterPage.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    protected void moveToAdditionalDetails(String user, String password){
        Intent details = new Intent(this, AdditionalDetails.class);
        details.putExtra("UserEmail", user);
        details.putExtra("UserPassword", password);
        startActivityForResult(details, 0);

    }



}
