package com.narij.checkv2.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.fragment.DutyDetailsBottomSheetNavigationFragment;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.Log;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

public class Duty2Activity extends AppCompatActivity {

    int dutyId = 0;
    Duty duty = new Duty();
    ProgressDialog dialog;
    APIInterface apiInterface;

    AppCompatTextView txtTitle;
    AppCompatTextView txtCreator;
    AppCompatTextView txtDescription;
    AppCompatTextView txtStartDate;
    AppCompatTextView txtEndDate;

    Chip chpGroup;
    Chip chpPriority;
    ChipGroup chgUsers;

    AppCompatButton btnShowChats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty2);


        txtTitle = findViewById(R.id.txtTitle);
        txtCreator = findViewById(R.id.txtCreator);
        txtDescription = findViewById(R.id.txtDescription);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        chpGroup = findViewById(R.id.chpGroup);
        chpPriority = findViewById(R.id.chpPriority);
        chgUsers = findViewById(R.id.chgUsers);


        if (getIntent() != null) {
            int id = getIntent().getIntExtra("id", 0);
            dutyId = id;

            try {
                for (Group group : Globals.groups) {
                    for (Duty duty : group.getDuties()) {
                        if (duty.getId() == id) {

                            android.util.Log.e(Globals.LOG_TAG, "D ID :" + id);
                            this.duty = duty;

                            Globals.selectedDutyId = id;
                            txtTitle.setText(duty.getTitle());
                            txtCreator.setText(duty.getCreator().getName());
                            chpGroup.setText(group.getTitle());
                            chpPriority.setText(duty.getPriority().getTitle());

                            if (duty.getDescription().trim().isEmpty()) {
                                txtDescription.setVisibility(View.GONE);
                            } else {
                                txtDescription.setVisibility(View.VISIBLE);
                                txtDescription.setText(duty.getDescription());
                            }

                            txtStartDate.setText(new SimpleDateFormat(Globals.dateFormat).format(new Date(duty.getStartDate())));
                            txtEndDate.setText(new SimpleDateFormat(Globals.dateFormat).format(new Date(duty.getStartDate() + duty.getDuration())));


//                            logs = duty.getRecords();
//                            logAdapter = new LogAdapter(logs);
//                            rcLogs.setAdapter(logAdapter);
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(Duty2Activity.this, RecyclerView.VERTICAL, false);
//                            layoutManager.scrollToPosition(0);
//                            rcLogs.setLayoutManager(layoutManager);

//                            ArrayList<Message> messages = new ArrayList<>();
//                            messageAdapter = new MessageAdapter(messages);
//                            rcMessages.setAdapter(messageAdapter);
//                            LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//                            layout.scrollToPosition(messages.size() - 1);
//                            rcMessages.setLayoutManager(layout);


                            ArrayList<String> users = new ArrayList<>();
                            for (User user : duty.getUsers()) {

                                users.add(user.getName());

                            }

//                            Call<RetrofitMessages> call = apiInterface.getMessages(id, 0L, System.currentTimeMillis());
//                            call.enqueue(new Callback<RetrofitMessages>() {
//                                @Override
//                                public void onResponse(Call<RetrofitMessages> call, Response<RetrofitMessages> response) {
//
//                                    RetrofitMessages retrofitMessages = response.body();
//                                    if (retrofitMessages.isError() == false) {
//                                        messageAdapter.getMessages().addAll(retrofitMessages.getMessages());
//                                        messageAdapter.notifyDataSetChanged();
//                                        rcMessages.getLayoutManager().scrollToPosition(messageAdapter.getMessages().size() - 1);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<RetrofitMessages> call, Throwable t) {
//
//                                }
//                            });
                            throw new Exception();
                        }
                    }
                }
            } catch (Exception e) {
            }


//            btnAddLog.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    dialog.show();
//                    long d = new Date().getTime();
//
//                    Call<WebServiceMessage> call = apiInterface.addLog(
//                            Globals.loggedInUser.getId(),
//                            dutyId,
//                            edtLog.getText().toString(),
//                            d,
//                            System.currentTimeMillis());
//
//                    call.enqueue(new Callback<WebServiceMessage>() {
//                        @Override
//                        public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
//
//                            WebServiceMessage webServiceMessage = response.body();
//
//                            Log log = new Log();
//                            log.setId(Integer.parseInt(webServiceMessage.getMessage()));
//                            log.setContent(edtLog.getText() + "");
//                            log.setUser(Globals.loggedInUser);
//                            log.setDate(d);
//
//                            duty.getRecords().add(log);
////                            priorityAdapter.getRecords().add(log);
//                            logAdapter.notifyDataSetChanged();
//
//                            if (dialog.isShowing()) {
//                                dialog.dismiss();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<WebServiceMessage> call, Throwable t) {
//
//                            if (dialog.isShowing()) {
//                                dialog.dismiss();
//                            }
//                        }
//                    });
//
//                }
//            });
        }

        btnShowChats = findViewById(R.id.btnShowChats);
        btnShowChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DutyDetailsBottomSheetNavigationFragment dutyDetailsBottomSheetNavigationFragment = new DutyDetailsBottomSheetNavigationFragment(duty);
                dutyDetailsBottomSheetNavigationFragment.show(getSupportFragmentManager(), dutyDetailsBottomSheetNavigationFragment.getTag());

            }
        });

    }
}
