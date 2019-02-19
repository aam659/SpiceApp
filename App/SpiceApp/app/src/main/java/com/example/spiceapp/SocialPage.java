package com.example.spiceapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SocialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_page);

        initializeToolbar();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initializeToolbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Social");
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextScreen;
        switch (item.getItemId()) {
            case R.id.tlbLogin:
                nextScreen = new Intent(SocialPage.this, LoginPage.class);
                startActivityForResult(nextScreen, 0);
                return true;

            case R.id.tlbSIU:
                nextScreen = new Intent(SocialPage.this, SpiceItUp.class);
                startActivityForResult(nextScreen, 0);
                return true;

            case R.id.tlbProfile:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.tlbHome:
                nextScreen = new Intent(SocialPage.this, HomePage.HomePageActivity.class);
                startActivityForResult(nextScreen, 0);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
