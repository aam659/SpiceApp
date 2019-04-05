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

public class Tab3Fragment extends Fragment {
    private static final String TAG= "Tab3Fragment";

    private Button btnTest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab3_fragment,container,false);
        btnTest = (Button) view.findViewById(R.id.testbtn3);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TESTING TAB 3", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
