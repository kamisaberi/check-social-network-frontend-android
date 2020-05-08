package com.narij.checkv2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.activity.EditProfileActivity;
import com.narij.checkv2.activity.LoginActivity;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.library.CircleTransform;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.User;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }

    CircleImageView imgProfile;
    Button btnEditProfile;
    TextView txtName;
    TextView txtNumberOfTasks;
    TextView txtNumberOfFriends;
    TextView txtNumberOfJobs;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

//        Button btnUploadImage = view.findViewById(R.id.btnUploadImage);
//
        imgProfile = view.findViewById(R.id.imgProfile);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        txtName = view.findViewById(R.id.txtName);
        txtNumberOfTasks = view.findViewById(R.id.txtNumberOfTasks);
        txtNumberOfFriends = view.findViewById(R.id.txtNumberOfFriends);
        txtNumberOfJobs = view.findViewById(R.id.txtNumberOfJobs);

        String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
        Log.d(Globals.LOG_TAG, p);
        Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);


        txtName.setText(Globals.loggedInUser.getName());
        txtNumberOfFriends.setText(Globals.friends.size() + "");
        txtNumberOfJobs.setText(Globals.groups.size() + "");

        int tasks = 0;
        for (Group g : Globals.groups) {
            tasks += g.getDuties().size();
        }

        txtNumberOfTasks.setText(tasks + "");

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            txtName.setText(Globals.loggedInUser.getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Globals.loggedInUser.getAvatar().isEmpty()) {
            Picasso.get().load(R.drawable.profile_default).transform(new CircleTransform()).into(imgProfile);
        } else {
            String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
            Picasso.get().load(p).transform(new CircleTransform()).placeholder(R.drawable.profile_default).into(imgProfile);
        }
        txtName.setText(Globals.loggedInUser.getName());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.mnuExit :
                Globals.loggedInUser = new User();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

                break;


        }


        return super.onOptionsItemSelected(item);
    }
}
