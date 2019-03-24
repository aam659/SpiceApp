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
        final CardView btnConfirm = (CardView) findViewById(R.id.cardConfirmCat);


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
                if(checkMex.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Mexican").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Mexican").setValue(0);
                if(checkSeafood.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Seafood").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Seafood").setValue(0);
                if(checkItalian.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Italian").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Italian").setValue(0);
                if(checkFastFood.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("FastFood").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("FastFood").setValue(0);
                if(checkBBQ.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("BBQ").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("BBQ").setValue(0);
                if(checkSteakhouse.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Steakhouse").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Steakhouse").setValue(0);
                if(checkIndian.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Indian").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Indian").setValue(0);
                if(checkChinese.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Chinese").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Chinese").setValue(0);
                if(checkBar.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Bar").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Bar").setValue(0);
                if(checkCafe.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Cafe").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Cafe").setValue(0);
                if(checkPizza.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Pizza").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Pizza").setValue(0);
                if(checkBurgers.isChecked())
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Burgers").setValue(1);
                else
                    database.child("users").child(user.getUid()).child("Moods").child(nameOfMood).child("Categories").child("Burgers").setValue(0);
                Intent nextScreen = new Intent (v.getContext(), PriceRange.class);
                nextScreen.putExtra("NAME_OF_MOOD", nameOfMood);
                startActivityForResult(nextScreen, 0);
            }
        });



    }
}
