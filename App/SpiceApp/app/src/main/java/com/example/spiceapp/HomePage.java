package com.example.spiceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public static class HomePageActivity extends AppCompatActivity{


        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);

            final Button btnLogin =(Button) findViewById(R.id.btnLogin);
            final Button btnSpiceItUp =(Button) findViewById(R.id.btnSpice);
            final Button btnSocial = (Button) findViewById(R.id.btnSocial);
            final Button btnProfile = (Button) findViewById(R.id.btnProfile);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "LOGIN BUTTON CLICKED", Toast.LENGTH_LONG).show();
                }
            });

            btnSpiceItUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("siu clicked");
                    Toast.makeText(v.getContext(), "SIU BUTTON CLICKED", Toast.LENGTH_LONG).show();
                    Intent nextScreen = new Intent(v.getContext(), SpiceItUp.class);
                    startActivityForResult(nextScreen, 0);
                }
            });

            btnSocial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "SOCIAL BUTTON CLICKED", Toast.LENGTH_LONG).show();
                }
            });

            btnProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "PROFILE BUTTON CLICKED", Toast.LENGTH_LONG).show();
                }
            });



        }


    }



}
