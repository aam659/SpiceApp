package com.example.spiceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MakeChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_chat);

        findViewById(R.id.btnMakeGroup).setOnClickListener(view -> makeGroupChat());




    }

    private void makeGroupChat() {
        Intent nextScreen = new Intent(this, GroupMessageActivity.class);
        nextScreen.putExtra("groupName", "MyTest");
        startActivityForResult(nextScreen, 0);
    }
}
