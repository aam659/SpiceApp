package com.example.spiceapp.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spiceapp.CreateEvent;
import com.example.spiceapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Testing class with a TabLayout
 * WILL BE REPLACED SHORTLY
 * @author Ryan Simpson
 */

public class EventsFragment extends Fragment {
    private static final String TAG= "EventsFragment";

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.events_fragment,container,false);
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
}
