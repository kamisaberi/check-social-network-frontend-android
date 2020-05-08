package com.narij.checkv2.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.narij.checkv2.R;
import com.narij.checkv2.adapter.ConversationAdapter;
import com.narij.checkv2.env.ContentType;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.env.RecordType;
import com.narij.checkv2.model.Conversation;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Log;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitMessages;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class DutyDetailsBottomSheetNavigationFragment extends BottomSheetDialogFragment {


    private final int OPEN_GALLERY_REQUEST_CODE = 1001;

    //    NavigationView navigation_view;
    ImageView close_imageview;
    RecyclerView rcLogs;
    ConversationAdapter conversationAdapter;
    Duty duty;

    AppCompatImageButton btnAddLog;
    AppCompatImageButton btnAddVoice;
    AppCompatImageButton btnOpenGallery;
    TextInputEditText edtLog;

    ProgressDialog dialog;

    APIInterface apiInterface;


    MediaRecorder mediaRecorder;
    private String outputFile;

    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.RECORD_AUDIO
    };


    private static final int RC_CONTACTS_PERM = 124;

    private boolean hasContactsPermissions() {
        return EasyPermissions.hasPermissions(getContext(), Manifest.permission.RECORD_AUDIO);
    }


    @AfterPermissionGranted(RC_CONTACTS_PERM)
    public void recordVoice() {
        if (hasContactsPermissions()) {

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException ise) {
                // make something ...
            } catch (IOException e) {
                // make something
            }

        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_contacts),
                    RC_CONTACTS_PERM,
                    Manifest.permission.RECORD_AUDIO);
        }


    }


    @SuppressLint("ValidFragment")
    public DutyDetailsBottomSheetNavigationFragment(Duty duty) {
        this.duty = duty;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duty_details_bottom_sheet_navigation, container, false);
//        navigation_view = view.findViewById(R.id.navigation_view);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);


        close_imageview = view.findViewById(R.id.close_imageview);
        rcLogs = view.findViewById(R.id.rcLogs);

        dialog = new ProgressDialog(getActivity(), R.style.ThemeOverlay_MaterialComponents_Dialog);
        dialog.setTitle("Sending...");
        dialog.setIndeterminate(true);

        ArrayList<Conversation> conversations = new ArrayList<>();
        for (Log g : duty.getLogs()) {
            g.setType(Conversation.LOG);
            conversations.add(g);
        }

        conversationAdapter = new ConversationAdapter(conversations);
        rcLogs.setAdapter(conversationAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        rcLogs.setLayoutManager(layoutManager);

        btnAddLog = view.findViewById(R.id.btnAddLog);
        btnAddVoice = view.findViewById(R.id.btnAddVoice);
        btnOpenGallery = view.findViewById(R.id.btnOpenGallery);
        edtLog = view.findViewById(R.id.edtLog);

        apiInterface = APIClient.getClient().create(APIInterface.class);

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

//        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//
//                }
//
//                return true;
//            }
//        });


        close_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DutyDetailsBottomSheetNavigationFragment.this.dismiss();
            }
        });


        btnAddLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                long d = new Date().getTime();







                Call<WebServiceMessage> call1 = apiInterface.addRecord(
                        RecordType.LOG,
                        Globals.loggedInUser.getId(),
                        duty.getId(),
                        edtLog.getText().toString(),
                        ContentType.CONTENT_TYPE_TEXT,
                        d,
                        System.currentTimeMillis()
                );

                call1.enqueue(new Callback<WebServiceMessage>() {
                    @Override
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                        WebServiceMessage webServiceMessage = response.body();
                        Log log = new Log();
                        log.setId(Integer.parseInt(webServiceMessage.getMessage()));
                        log.setLog(edtLog.getText() + "");
                        log.setUser(Globals.loggedInUser);
                        log.setDate(d);

                        duty.getLogs().add(log);
//                            priorityAdapter.getRecords().add(log);
                        conversationAdapter.notifyDataSetChanged();

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                    }
                });






                Call<WebServiceMessage> call = apiInterface.addLog(
                        Globals.loggedInUser.getId(),
                        duty.getId(),
                        edtLog.getText().toString(),
                        d,
                        System.currentTimeMillis());

                call.enqueue(new Callback<WebServiceMessage>() {
                    @Override
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                        WebServiceMessage webServiceMessage = response.body();
                        Log log = new Log();
                        log.setId(Integer.parseInt(webServiceMessage.getMessage()));
                        log.setLog(edtLog.getText() + "");
                        log.setUser(Globals.loggedInUser);
                        log.setDate(d);

                        duty.getLogs().add(log);
//                            priorityAdapter.getRecords().add(log);
                        conversationAdapter.notifyDataSetChanged();

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });










            }
        });


        btnAddVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {

                    recordVoice();
//                    android.util.Log.d(Globals.LOG_TAG, "Touch down");

                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    mediaRecorder.stop();
                    mediaRecorder.release();
//                    mediaRecorder= null;
//                    android.util.Log.d(Globals.LOG_TAG, "Touch up");
                }
                return true;
            }
        });


        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), OPEN_GALLERY_REQUEST_CODE);

            }
        });


//        disableNavigationViewScrollbars(navigation_view);

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
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

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

//                        if (slideOffset > 0.5f) {
//                            close_imageview.setVisibility(View.VISIBLE);
//                        } else {
//                            close_imageview.setVisibility(View.INVISIBLE);
//                        }

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK && requestCode == OPEN_GALLERY_REQUEST_CODE) {


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


//        super.onActivityResult(requestCode, resultCode, data);


    }
}
