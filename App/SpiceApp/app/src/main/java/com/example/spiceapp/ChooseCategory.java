package com.example.spiceapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class ChooseCategory extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();


        //Retrieves mood name from previous intent
        String nameOfMood = getIntent().getStringExtra("NAME_OF_MOOD");

        //Confirm button
        final FrameLayout btnConfirm = (FrameLayout) findViewById(R.id.cardConfirmCat);


        //An enumerated list of the checkboxes
        final CheckBox checkMex = (CheckBox) findViewById(R.id.checkMex);
        final CheckBox checkSeafood = (CheckBox) findViewById(R.id.checkSeafood);
        final CheckBox checkItalian = (CheckBox) findViewById(R.id.checkItalian);
        final CheckBox checkFastFood = (CheckBox) findViewById(R.id.checkFastFood);
        final CheckBox checkBBQ = (CheckBox) findViewById(R.id.checkBBQ);
        final CheckBox checkSteakhouse = (CheckBox) findViewById(R.id.checkSteakhouse);
        final CheckBox checkIndian = (CheckBox) findViewById(R.id.checkIndian);
        final CheckBox checkChinese = (CheckBox) findViewById(R.id.checkChinese);
        final CheckBox checkBar = (CheckBox) findViewById(R.id.checkBar);
        final CheckBox checkCafe = (CheckBox) findViewById(R.id.checkCafe);
        final CheckBox checkPizza = (CheckBox) findViewById(R.id.checkPizza);
        final CheckBox checkBurgers = (CheckBox) findViewById(R.id.checkBurgers);


        //Essentially, whenever confirm is clicked, any boxes that are checked will be added to the firebase database
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            //TODO: MAKE CHECKBOXES PRESELECTED IF 1
            @Override
            public void onClick(View v) {
                ArrayList<String> categories = new ArrayList<>();
                if(checkMex.isChecked())
                    categories.add("Mexican");
                if(checkSeafood.isChecked())
                    categories.add("Seafood");
                if(checkItalian.isChecked())
                    categories.add("Italian");
                if(checkFastFood.isChecked())
                    categories.add("FastFood");
                if(checkBBQ.isChecked())
                    categories.add("BBQ");
                if(checkSteakhouse.isChecked())
                    categories.add("Steakhouse");
                if(checkIndian.isChecked())
                    categories.add("Indian");
                if(checkChinese.isChecked())
                    categories.add("Chinese");
                if(checkBar.isChecked())
                    categories.add("Bar");
                if(checkCafe.isChecked())
                    categories.add("Cafe");
                if(checkPizza.isChecked())
                    categories.add("Pizza");
                if(checkBurgers.isChecked())
                    categories.add("Burgers");
                database.child("users").child(user.getEmail().replace('.','_')).child("Moods").child(nameOfMood).child("categories").setValue(categories);
                Intent nextScreen = new Intent (v.getContext(), PriceRange.class);
                nextScreen.putExtra("NAME_OF_MOOD", nameOfMood);
                startActivityForResult(nextScreen, 0);
            }
        });



    }
}
