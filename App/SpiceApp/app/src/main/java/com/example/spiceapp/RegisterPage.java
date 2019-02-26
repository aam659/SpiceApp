package com.example.spiceapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.transition.Fade;
import android.view.Window;
import android.widget.TextView;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        final TextView email = (TextView) findViewById(R.id.edtLogin);
        final TextView password = (TextView) findViewById(R.id.edtPassword);
        final TextView retype = (TextView) findViewById(R.id.edtRetype);




    }

}
