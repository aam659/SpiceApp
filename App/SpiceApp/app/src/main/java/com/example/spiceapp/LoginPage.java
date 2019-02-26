package com.example.spiceapp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.transition.Fade;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginPage extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setExitTransition(new Fade());
        mAuth = FirebaseManager.getAuth();

        setContentView(R.layout.activity_login_page);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final CardView btnLogin = (CardView) findViewById(R.id.cardLogin);
        final CardView btnRegister = (CardView) findViewById(R.id.cardRegister);
        final TextView edtEmail = (TextView) findViewById(R.id.edtEmail);
        final TextView edtPassword = (TextView) findViewById(R.id.edtLoginPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                signInUser(email, password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View img = findViewById(R.id.imageView3);
                Intent nextScreen = new Intent(v.getContext(), RegisterPage.class);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginPage.this, img, "regUser");
                startActivityForResult(nextScreen, 0);
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    user = mAuth.getCurrentUser();
                    startActivityForResult(new Intent(LoginPage.this, HomePage.HomePageActivity.class), 0);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginPage.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
