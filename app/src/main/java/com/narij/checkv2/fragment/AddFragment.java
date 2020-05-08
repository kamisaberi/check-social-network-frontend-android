package com.narij.checkv2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.narij.checkv2.R;
import com.narij.checkv2.activity.AddDuty2Activity;
import com.narij.checkv2.activity.AddDutyActivity;
import com.narij.checkv2.activity.AddGroupActivity;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }


    Button btnAddDuty;
    Button btnAddGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        btnAddDuty = view.findViewById(R.id.btnAddDuty);
        btnAddGroup = view.findViewById(R.id.btnAddGroup);


        btnAddDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddDuty2Activity.class);
                startActivity(intent);
            }
        });




        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddGroupActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
