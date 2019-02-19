package com.example.spiceapp;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        initializeToolbar();

        final TextView txtUsername = (TextView) findViewById(R.id.etUsername);
        final TextView txtLogin = (TextView) findViewById(R.id.etPassword);
        final Button btnLogin = (Button) findViewById(R.id.bLogin);
        final Button btnRegister = (Button) findViewById(R.id.bRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtLogin.getText().toString();

//              Check for username/password combo and login
                if (username.equals("user")) {
                    if (password.equals("passw0rd")) {
                        Intent nextScreen = new Intent(view.getContext(), HomePage.class);
                        startActivityForResult(nextScreen, 0);
                    }

                    else {
                        Toast.makeText(view.getContext(), "Sorry, wrong username/password!", Toast.LENGTH_SHORT).show();
                    }

                    txtUsername.setText("");
                    txtLogin.setText("");
                }

                else {
                    Toast.makeText(view.getContext(), "Sorry, wrong username/password!", Toast.LENGTH_SHORT).show();
                }

                txtUsername.setText("");
                txtLogin.setText("");
            }


        });

//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String username = txtUsername.getText().toString();
//                String password = txtLogin.getText().toString();
//
////              Register Button Clicked
//                Intent toRegisterScreen = new Intent(view.getContext(), RegisterPage.class);
//                startActivityForResult(toRegisterScreen, 0);
//            }
//        });
    }

    private void initializeToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
    }
}
