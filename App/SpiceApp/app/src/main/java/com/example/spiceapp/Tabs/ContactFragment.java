package com.example.spiceapp.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spiceapp.Adapters.ContactsAdapter;
import com.example.spiceapp.AddContact;
import com.example.spiceapp.FirebaseManager;
import com.example.spiceapp.FirebaseObjects.User;
import com.example.spiceapp.PriceRange;
import com.example.spiceapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Testing class with a TabLayout
 * WILL BE REPLACED SHORTLY
 * @author Ryan Simpson
 */

public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";

    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private List<User> mUsers;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contacts_fragment, container ,false);

        recyclerView = view.findViewById(R.id.contact_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        mUsers = new ArrayList<>();
        readUsers();

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabContacts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent (v.getContext(), AddContact.class);
                startActivityForResult(nextScreen, 0);
            }
        });

        return view;
    }

    private void readUsers() {

       final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getEmail().replace('.','_')).child("Contacts");



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
                contactsAdapter = new ContactsAdapter(getContext(), mUsers);
                recyclerView.setAdapter(contactsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
