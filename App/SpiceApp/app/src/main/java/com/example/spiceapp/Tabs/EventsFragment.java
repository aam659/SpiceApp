package com.example.spiceapp.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spiceapp.Adapters.ContactsAdapter;
import com.example.spiceapp.Adapters.EventsAdapter;
import com.example.spiceapp.CreateEvent;
import com.example.spiceapp.FirebaseObjects.Event;
import com.example.spiceapp.FirebaseObjects.User;
import com.example.spiceapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class EventsFragment extends Fragment {
    private static final String TAG= "EventsFragment";

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private List<Event> mEvents;
    FirebaseUser mUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.events_fragment,container,false);

        recyclerView = view.findViewById(R.id.eventRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mEvents = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        readEvents();



        fab = view.findViewById(R.id.fabNewEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(v.getContext(), CreateEvent.class);
                startActivityForResult(next,0);
            }
        });

        return view;
    }

    private void readEvents() {

        //Get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Reference to contacts
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");

        //Populates recyclerview
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEvents.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Event event = snapshot.getValue(Event.class);
                    if(event.getUsers().contains(mUser.getEmail().replace('.','_'))){
                        mEvents.add(event);
                    }
                }

                eventsAdapter = new EventsAdapter(getContext(), mEvents);
                recyclerView.setAdapter(eventsAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
