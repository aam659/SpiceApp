package com.example.spiceapp.Tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spiceapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Testing class with a TabLayout
 * WILL BE REPLACED SHORTLY
 * @author Ryan Simpson
 */

public class Tab1Fragment extends Fragment {
    private static final String TAG= "Tab1Fragment";

    private Button btnTest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab1_fragment,container,false);
        btnTest = (Button) view.findViewById(R.id.testbtn1);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TESTING TAB 1", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
