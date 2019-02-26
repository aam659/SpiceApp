package com.example.spiceapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class LoginPage extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setExitTransition(new Fade());

        setContentView(R.layout.activity_login_page);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final CardView btnLogin = (CardView) findViewById(R.id.cardLogin);
        final CardView btnRegister = (CardView) findViewById(R.id.cardRegister);
        final

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Login Button Pressed", Toast.LENGTH_LONG).show();

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
}
