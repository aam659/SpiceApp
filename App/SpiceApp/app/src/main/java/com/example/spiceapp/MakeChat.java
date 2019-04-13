package com.example.spiceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.TlsVersion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spiceapp.Adapters.ContactsAdapter;
import com.example.spiceapp.Adapters.NewGroupAdapter;
import com.example.spiceapp.FirebaseObjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MakeChat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewGroupAdapter adapter;
    private List<User> mUsers;
    private EditText editGroupName;
    private DatabaseReference reference;
    private FirebaseUser mUser;
    private ArrayList<String> mEmails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_chat);

        recyclerView = findViewById(R.id.recyclerNewGroup);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers = new ArrayList<>();
        adapter = new NewGroupAdapter(this,  mUsers);

        readUsers();

        editGroupName = findViewById(R.id.newGroupName);
        findViewById(R.id.btnMakeGroup).setOnClickListener(view -> makeGroupChat());


    }

    private void readUsers() {

        //Get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Reference to contacts
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getEmail().replace('.','_')).child("Contacts");

        //Populates recyclerview
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(!(user.getEmail().replace('.','_')).equals(firebaseUser.getEmail().replace('.','_'))){
                        mUsers.add(user);
                    }
                }

                System.out.println(mUsers);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void makeGroupChat() {
        reference = FirebaseDatabase.getInstance().getReference("Groups");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String groupName = editGroupName.getText().toString();
        if(!groupName.isEmpty()) {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(groupName).exists()) {
                        mEmails = adapter.getCheckedUsers();
                        if(mEmails.size() > 1){
                            reference.child(groupName).child("Users").setValue(mEmails);
                            Intent nextScreen = new Intent(getBaseContext(), GroupMessageActivity.class);
                            nextScreen.putExtra("groupName", groupName);
                            System.out.println("USERS ADDED " + adapter.getCheckedUsers());
                            System.out.println("CURRENT USER " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            startActivityForResult(nextScreen, 0);
                        }
                        else
                            Toast.makeText(getBaseContext(), "Please select one or more users.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getBaseContext(), "Group name already exists.", Toast.LENGTH_SHORT).show();
                        editGroupName.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
            Toast.makeText(getBaseContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();



        //TODO: ADD CHECKING TO MAKE SURE THE NAME DOESN'T ALREADY EXIST AND ALSO MAKE SURE THAT THE GROUP NAME EDIT TEXT IS NOT NULL


    }
}
