package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spiceapp.FirebaseObjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddContact extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        FirebaseUser user = FirebaseManager.getCurrentUser();
        database = FirebaseManager.getDatabaseReference();

        final EditText contactEmail = (EditText) findViewById(R.id.edtContactName);
        final CardView confirmName = (CardView) findViewById(R.id.cardConfirmName);

        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = contactEmail.getText().toString();
                if(!email.isEmpty()) {
                        Query query = FirebaseManager.getUserByEmail(email);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if(dataSnapshot.exists()) {
                                    database.child("users").child(FirebaseManager.getCurrentUser().getEmail().replace('.', '_'))
                                            .child("Contacts").child(email.replace('.', '_')).setValue(user);
                                }
                                else Toast.makeText(v.getContext(), "User does not exist.", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        Intent nextScreen = new Intent(v.getContext(), HomePage.HomePageActivity.class);
                        startActivityForResult(nextScreen, 0);

                }
                else Toast.makeText(v.getContext(), "Please enter an email address.", Toast.LENGTH_LONG).show();

            }
        });

    }

}
