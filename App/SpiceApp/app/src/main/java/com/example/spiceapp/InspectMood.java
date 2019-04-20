package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.spiceapp.FirebaseObjects.Categories;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Class to allow users to view, edit, or delete a mood
public class InspectMood extends AppCompatActivity {

    private FirebaseUser user; //Current User
    private DatabaseReference database; //Database Reference
    private DatabaseReference mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_mood);
        initializeTextViews();

        user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();
        mood = FirebaseManager.getSpecifcMoodReference(getIntent().getStringExtra("NAME")); //Gets reference to current mood

        //On click method for delete button
        final FrameLayout btnDelete = (FrameLayout) findViewById(R.id.cardMoodDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseManager.deleteDatabaseNode(mood); //DELETES THE NODE WITHOUT CONFIRMATION
                //TODO: ADD confirmation to deleting node
                Intent intent = new Intent(v.getContext(), ListMoods.class);
                startActivityForResult(intent, 0);
            }
        });

        final FrameLayout btnEdit = (FrameLayout) findViewById(R.id.cardMoodEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            //TODO: MAKE BREAKFAST SET CATEGORIES TO 0
            public void onClick(View v) {
                checkIfCurrentMood();
            }
        });

    }

    private void checkIfCurrentMood(){
        Query query = FirebaseManager.getCurrentPreference();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String nameOfMood = getIntent().getStringExtra("NAME");
                System.out.println("SELECTED " + nameOfMood);
                String currMood = dataSnapshot.child("name").getValue(String.class);
                Intent intent = new Intent(getApplicationContext(), MealTime.class);
                if(nameOfMood.equals(currMood)){
                    System.out.println("curr " + currMood);
                    intent.putExtra("ISCURR","yes");// is current mood
                    intent.putExtra("NAME_OF_MOOD", getIntent().getStringExtra("NAME"));
                    startActivityForResult(intent, 0);
                }
                else{
                    intent.putExtra("ISCURR","no");// not current mood
                    intent.putExtra("NAME_OF_MOOD", getIntent().getStringExtra("NAME"));
                    startActivityForResult(intent, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    //Method to pull data from previous intent and set them in the textviews
    private void initializeTextViews() {
        //Retrieves mood name from previous intent
        String nameOfMood = getIntent().getStringExtra("NAME");
        //Set Mood Name Text view to mood name passed from previous intent
        final TextView textMoodName = (TextView) findViewById(R.id.textMoodName);
        textMoodName.setText(nameOfMood);

        //Methods below are the same as above
        String mealTime = getIntent().getStringExtra("MEALTIME");
        final TextView textMealTime = (TextView) findViewById(R.id.textMealTime);
        String mealTimeText = "Meal Time: " + mealTime;
        textMealTime.setText(mealTimeText); //Android studio does not like string concat in a settext field

        String minPrice = intToDollarSigns("Min");
        final TextView textMinPrice = (TextView) findViewById(R.id.textMinPrice);
        textMinPrice.setText(minPrice);

        String maxPrice = intToDollarSigns("Max");
        final TextView textMaxPrice = (TextView) findViewById(R.id.textMaxPrice);
        textMaxPrice.setText(maxPrice);

        ArrayList<String> categories = getIntent().getStringArrayListExtra("Categories");
        final TextView categoryTextView = (TextView) findViewById(R.id.textCategories);
        categoryTextView.setText(getCategoryText(categories));
    }


    private String getCategoryText(ArrayList<String> list){
        StringBuilder returnString = new StringBuilder("Categories: ");
        int iter = 0;
        while (list != null && list.size() != 0 && iter != 2){
            returnString.append(list.get(0));
            list.remove(0);
            if (list.size() > 0) {
                returnString.append(", ");
            }
            iter++;
        }
        return returnString.toString();
    }

    private String intToDollarSigns(String price) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(price);
        stringBuilder.append(": ");
        int val = getIntent().getIntExtra(price, 0);
        for(int i = 0; i < val; i++){
            stringBuilder.append("$");
        }
        return  stringBuilder.toString();
    }
}
