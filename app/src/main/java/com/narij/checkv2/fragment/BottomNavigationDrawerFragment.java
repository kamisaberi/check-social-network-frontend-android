package com.narij.checkv2.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.narij.checkv2.R;
import com.narij.checkv2.activity.AccountActivity;
import com.narij.checkv2.activity.SettingsPrefActivity;
import com.narij.checkv2.env.Globals;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {


    NavigationView navigation_view;
    ImageView close_imageview;


    TextView txtUser;
    TextView txtEmail;
    CircleImageView imgProfile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_navigation_drawer, container, false);
        navigation_view = view.findViewById(R.id.navigation_view);
        close_imageview = view.findViewById(R.id.close_imageview);

        txtUser = view.findViewById(R.id.txtUser);
        txtEmail = view.findViewById(R.id.txtEmail);
        imgProfile = view.findViewById(R.id.imgProfile);

        txtUser.setText(Globals.loggedInUser.getName());
        txtEmail.setText(Globals.loggedInUser.getEmail());
        String p = Globals.PROFILE_URL + Globals.loggedInUser.getId() + "/" + Globals.loggedInUser.getAvatar();
        Picasso.get().load(p).placeholder(R.drawable.profile_default).into(imgProfile);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AccountActivity.class);
                intent.putExtra("user", Globals.loggedInUser.getId());
                startActivity(intent);

            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        getDialog().getWindow().setGravity(Gravity.BOTTOM);
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels * 0.30));// here i have fragment height 30% of window's height you can set it as per your requirement
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.mnuSettings:
                        Intent intent = new Intent(getActivity(), SettingsPrefActivity.class);
                        startActivity(intent);
                        break;

                }

                return true;
            }
        });


        close_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationDrawerFragment.this.dismiss();
            }
        });

        disableNavigationViewScrollbars(navigation_view);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

                //                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);

                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:
                                dialog.dismiss();
                                break;
//                            default:
//                                close_imageview.setVisibility(View.GONE);
//                                break;
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        if (slideOffset > 0.5f) {
                            close_imageview.setVisibility(View.VISIBLE);
                        } else {
                            close_imageview.setVisibility(View.GONE);
                        }

                    }
                });


            }
        });


        return dialog;
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);
    }


}
