package com.narij.checkv2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.narij.checkv2.R;
import com.narij.checkv2.adapter.PriorityAdapter;
import com.narij.checkv2.adapter.SelectGroupAdapter;
import com.narij.checkv2.adapter.SelectUserAdapter;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.library.SegmentedGroup;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.Priority;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDutyActivity extends AppCompatActivity {

    int id;
    APIInterface apiInterface;

    Toolbar toolbar;
    ProgressDialog dialog;
    DateFormat formatter;

    SegmentedGroup segmented2;

    RadioButton rdbInfo;
    RadioButton rdbGroup;
    RadioButton rdbUsers;
    RadioButton rdbDate;

    LinearLayout pnlBasicInfo;
    LinearLayout pnlSelectGroups;
    LinearLayout pnlSelectUsers;
    LinearLayout pnlPickDates;


    // Basic Info
    EditText edtTitle;
    EditText edtDescription;
    int selectedPriorityId = 0;
    RecyclerView rcPriorities;
    PriorityAdapter priorityAdapter;


    // Select Groups

    //    EditText edtGroupTitle;
    EditText edtSearchGroup;
    //    Button btnNewGroup;
    RecyclerView rcGroups;
    SelectGroupAdapter selectGroupAdapter;

    // Select Users
    RecyclerView rcUsers;
    SelectUserAdapter selectUserAdapter;
    EditText edtSearchUser;

    // Pick Date
//    Switch swStarts;
//    Switch swEnds;
    Button btnStarts;
    Button btnEnds;
    Date dtStarts;
    Date dtEnds;
    TextView txtStarts;
    TextView txtEnds;
    TextView txtStartsLabel;
    TextView txtEndsLabel;
    SingleDateAndTimePicker dteStartEnd;

    Switch swExactTime;
    Switch swCanContinueAfterTimeout;



    ImageButton imbtnHelpCanContinueAfterTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duty);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        segmented2 = findViewById(R.id.segmented2);
        rdbInfo = findViewById(R.id.rdbInfo);
        rdbGroup = findViewById(R.id.rdbGroup);
        rdbUsers = findViewById(R.id.rdbUsers);
        rdbDate = findViewById(R.id.rdbDate);

        pnlBasicInfo = findViewById(R.id.pnlBasicInfo);
        pnlSelectGroups = findViewById(R.id.pnlSelectGroups);
        pnlSelectUsers = findViewById(R.id.pnlSelectUsers);
        pnlPickDates = findViewById(R.id.pnlPickDates);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dialog = new ProgressDialog(this);
        formatter = new SimpleDateFormat(Globals.dateTimeFormat);

        ViewTreeObserver vto = segmented2.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    AddDutyActivity.this.segmented2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    AddDutyActivity.this.segmented2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = AddDutyActivity.this.segmented2.getMeasuredWidth();
                int height = AddDutyActivity.this.segmented2.getMeasuredHeight();
                rdbInfo.setWidth(width / 4);
                rdbGroup.setWidth(width / 4);
                rdbUsers.setWidth(width / 4);
                rdbDate.setWidth(width / 4);
//                Log.d(Globals.LOG_TAG, "width :" + width + " height:" + height);
            }
        });


        rdbInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.d(Globals.LOG_TAG, b+"");
                if (b == true) {
                    pnlBasicInfo.setVisibility(View.VISIBLE);
                    pnlSelectGroups.setVisibility(View.GONE);
                    pnlSelectUsers.setVisibility(View.GONE);
                    pnlPickDates.setVisibility(View.GONE);
                }
            }
        });

        rdbGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    pnlBasicInfo.setVisibility(View.GONE);
                    pnlSelectGroups.setVisibility(View.VISIBLE);
                    pnlSelectUsers.setVisibility(View.GONE);
                    pnlPickDates.setVisibility(View.GONE);
                }
            }
        });

        rdbUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    pnlBasicInfo.setVisibility(View.GONE);
                    pnlSelectGroups.setVisibility(View.GONE);
                    pnlSelectUsers.setVisibility(View.VISIBLE);
                    pnlPickDates.setVisibility(View.GONE);
                }
            }
        });

        rdbDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    pnlBasicInfo.setVisibility(View.GONE);
                    pnlSelectGroups.setVisibility(View.GONE);
                    pnlSelectUsers.setVisibility(View.GONE);
                    pnlPickDates.setVisibility(View.VISIBLE);
                }
            }
        });


        //Basic Info
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        rcPriorities = findViewById(R.id.rcPriorities);
        ArrayList<Priority> priorities = new ArrayList<>();
        for (Priority p : Globals.priorities) {
            Priority priority = new Priority();
            priority.setId(p.getId());
            priority.setTitle(p.getTitle());
            priorities.add(priority);
        }
        priorityAdapter = new PriorityAdapter(priorities);
        rcPriorities.setAdapter(priorityAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddDutyActivity.this, RecyclerView.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        rcPriorities.setLayoutManager(layoutManager);


        // Select Groups


//        edtGroupTitle = findViewById(R.id.edtGroupTitle);
        edtSearchGroup = findViewById(R.id.edtSearchGroup);
//        btnNewGroup = findViewById(R.id.btnNewGroup);
        rcGroups = findViewById(R.id.rcGroups);
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group(0, "no_group"));
        for (Group group : Globals.justGroups) {

            groups.add(group);
        }

        selectGroupAdapter = new SelectGroupAdapter(groups);
        rcGroups.setAdapter(selectGroupAdapter);
        LinearLayoutManager grouplayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        grouplayoutManager.scrollToPosition(0);
        rcGroups.setLayoutManager(grouplayoutManager);

//        btnNewGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dialog.show();
//                Call<WebServiceMessage> call = apiInterface.addGroup(Globals.loggedInUser.getId(), edtTitle.getText().toString(), "", "");
//                call.enqueue(new Callback<WebServiceMessage>() {
//                    @Override
//                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
//
//                        if (dialog.isShowing())
//                            dialog.dismiss();
//                        WebServiceMessage webServiceMessage = response.body();
//                        if (webServiceMessage.isError() == false) {
//                            String sid = webServiceMessage.getMessage();
//                            selectGroupAdapter.getGroups().add(new Group(Integer.parseInt(sid), edtTitle.getText() + ""));
//                            selectGroupAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {
//                        if (dialog.isShowing())
//                            dialog.dismiss();
//                    }
//                });
//
//            }
//        });


        // Select Users

        rcUsers = findViewById(R.id.rcUsers);
        edtSearchUser = findViewById(R.id.edtSearchUser);

        ArrayList<User> users = new ArrayList<>();
        users.add(Globals.loggedInUser);
        for (User user : Globals.friends) {

            users.add(user);
        }

        selectUserAdapter = new SelectUserAdapter(users);
        rcUsers.setAdapter(selectUserAdapter);
        LinearLayoutManager userslayoutManager = new LinearLayoutManager(AddDutyActivity.this, RecyclerView.VERTICAL, false);
        userslayoutManager.scrollToPosition(0);
        rcUsers.setLayoutManager(userslayoutManager);


        // Pick Date
//        swStarts = findViewById(R.id.swStarts);
//        swEnds = findViewById(R.id.swEnds);
        txtStarts = findViewById(R.id.txtStarts);
        txtEnds = findViewById(R.id.txtEnds);
        txtStartsLabel = findViewById(R.id.txtStartsLabel);
        txtEndsLabel = findViewById(R.id.txtEndsLabel);
        btnStarts = findViewById(R.id.btnStarts);
        btnEnds = findViewById(R.id.btnEnds);

        dtStarts = Calendar.getInstance().getTime();
        dtEnds = Calendar.getInstance().getTime();
        dteStartEnd = findViewById(R.id.dteStartEnd);

        txtStarts.setText(formatter.format(dtStarts));
        txtEnds.setText(formatter.format(dtEnds));

        swExactTime = findViewById(R.id.swExactTime);
        swCanContinueAfterTimeout = findViewById(R.id.swCanContinueAfterTimeout);

        swExactTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    btnStarts.setEnabled(false);
                    dtStarts = Calendar.getInstance().getTime();
                    txtStarts.setText(formatter.format(dtStarts));
                    txtStarts.setEnabled(false);
                    txtStarts.setTextColor(ContextCompat.getColor(AddDutyActivity.this, R.color.tokyoDividerColor));
                    txtStartsLabel.setEnabled(false);
                    txtEndsLabel.setText("Do");
                } else {
                    btnStarts.setEnabled(true);
                    txtStarts.setEnabled(true);
                    txtStarts.setTextColor(ContextCompat.getColor(AddDutyActivity.this, R.color.tokyoColorAccent));
                    txtStartsLabel.setEnabled(true);
                    txtEndsLabel.setText("Ends");
                }
            }
        });

//        swStarts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                swEnds.setChecked(!b);
//                dteStartEnd.setDefaultDate(dtStarts);
//            }
//        });

//        swEnds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                swStarts.setChecked(!b);
//                dteStartEnd.setDefaultDate(dtEnds);
//            }
//        });


        btnStarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dtStarts = dteStartEnd.getDate();
                txtStarts.setText(formatter.format(dtStarts));

            }
        });

        btnEnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dtEnds = dteStartEnd.getDate();
                txtEnds.setText(formatter.format(dtEnds));

            }
        });

        txtStarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dteStartEnd.setDefaultDate(dtStarts);
            }
        });

        txtEnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dteStartEnd.setDefaultDate(dtEnds);
            }
        });


//        dteStartEnd.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(String displayed, Date date) {
//                if (swStarts.isChecked() == true) {
//                    dtStarts = date;
//                    txtStarts.setText(formatter.format(date));
//
//                } else {
//                    dtEnds = date;
//                    txtEnds.setText(formatter.format(date));
//                }
//            }
//        });

        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            try {
                for (Group group : Globals.groups) {
                    for (Duty duty : group.getDuties()) {
                        if (duty.getId() == id) {
//                            android.util.Log.e(Globals.LOG_TAG, "D ID :" + id);
//                            this.duty = duty;
                            Globals.selectedDutyId = id;
//                            btnGroup.setText(group.getTitle());
                            edtTitle.setText(duty.getTitle());
                            edtDescription.setText(duty.getDescription());
                            txtStarts.setText(new SimpleDateFormat(Globals.dateTimeFormat).format(new Date(duty.getStartDate())));
                            txtEnds.setText(new SimpleDateFormat(Globals.dateTimeFormat).format(new Date(duty.getStartDate() + duty.getDuration())));
                            swCanContinueAfterTimeout.setChecked((duty.isCanContinueAfterTimeout()));
                            swExactTime.setChecked(duty.isExactTime());
//                            logs = duty.getRecords();
//                            logAdapter = new LogAdapter(logs);
//                            rcLogs.setAdapter(logAdapter);
//                            LinearLayoutManager layoutManager = new LinearLayoutManager(DutyActivity.this, LinearLayoutManager.VERTICAL, false);
//                            layoutManager.scrollToPosition(0);
//                            rcLogs.setLayoutManager(layoutManager);

//                            ArrayList<Message> messages = new ArrayList<>();
//                            messageAdapter = new MessageAdapter(messages);
//                            rcMessages.setAdapter(messageAdapter);
//                            LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                            layout.scrollToPosition(messages.size() - 1);
//                            rcMessages.setLayoutManager(layout);

//                            if (Globals.DEBUG_MODE == true) {
//                                Globals.loggedInUser.setId(1);
//                            }
                            throw new Exception();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }



        imbtnHelpCanContinueAfterTimeout = findViewById(R.id.imbtnHelpCanContinueAfterTimeout);
        imbtnHelpCanContinueAfterTimeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @ColorInt int FORE = 0xFFFF7242;
                @ColorInt int BACK = 0xFF255766;

//                ViewTooltip
//                        .on(AddDutyActivity.this, imbtnHelpCanContinueAfterTimeout)
//                        .autoHide(true, 5000)
//                        .corner(30)
//                        .clickToHide(true)
//                        .textColor(FORE)
//                        .color(BACK)
//                        .position(ViewTooltip.Position.TOP)
//                        .text("if timeout happened , task doest finish you can continue task . time will get negative timespan ")
//                        .show();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        rdbInfo.setChecked(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_add_duty_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mnuSend:


                // Bssic Info
                if (edtTitle.getText().toString().isEmpty() == true) {
                    rdbInfo.setChecked(true);
                    Toast.makeText(getBaseContext(), "Fill Title", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (priorityAdapter.selectedPriorityId == 0) {
                    rdbInfo.setChecked(true);
                    Toast.makeText(getBaseContext(), "Select Priority", Toast.LENGTH_LONG).show();
                    return false;
                }

                Globals.newDutyToRegister.setTitle(edtTitle.getText().toString());
                Globals.newDutyToRegister.setDescription(edtDescription.getText().toString());
                Globals.newDutyToRegister.setPriority(new Priority(priorityAdapter.selectedPriorityId, ""));

                // Select Groups
                if (selectGroupAdapter.selectedGroupId == -1) {
                    rdbGroup.setChecked(true);
                    Toast.makeText(getBaseContext(), "select group", Toast.LENGTH_LONG).show();
                    return false;
                }
                Globals.newDutyToRegister.setGroupids(selectGroupAdapter.selectedGroupId + "");

                // Select Users
                if (selectUserAdapter.getSelectedUsers().size() == 0) {
                    rdbUsers.setChecked(true);
                    Toast.makeText(getBaseContext(), "select at least one user", Toast.LENGTH_LONG).show();
                    return false;
                }
                for (int a : selectUserAdapter.getSelectedUsers()) {
                    Globals.newDutyToRegister.getUsers().add(new User(a));
                }


                // Pick Date
                long duration;
                int parent = 0;
                int group;
                String users;
                int priority = 0;

                ArrayList<String> us = new ArrayList<>();
                for (User u : Globals.newDutyToRegister.getUsers()) {
                    us.add(u.getId() + "");
                }
                users = TextUtils.join(",", us);

                priority = Globals.newDutyToRegister.getPriority().getId();
                group = Integer.parseInt(Globals.newDutyToRegister.getGroupids());
                duration = Math.abs(dtEnds.getTime() - dtStarts.getTime());
                //Date d1 = new Date(btnDate.getText().toString());
                long sd = dtStarts.getTime();

                int et = swExactTime.isChecked() == true ? 1 : 0;
                int cc = swCanContinueAfterTimeout.isChecked() == true ? 1 : 0;


                dialog.show();

                if (this.id == 0) {

                    Call<WebServiceMessage> call = apiInterface.addDuty(
                            edtTitle.getText() + "",
                            edtDescription.getText() + "",
                            sd,
                            duration,
                            0,
                            group,
                            priority,
                            Globals.loggedInUser.getId(),
                            users,
                            "",
                            et,
                            cc,
                            System.currentTimeMillis()
                    );
                    call.enqueue(new Callback<WebServiceMessage>() {
                        @Override
                        public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                            if (dialog.isShowing())
                                dialog.dismiss();

                            WebServiceMessage webServiceMessage = response.body();
                            if (webServiceMessage.isError() == false) {

                                Intent intent = new Intent();
                                intent.putExtra("id", webServiceMessage.getMessage().toString());
                                intent.putExtra("title", Globals.newDutyToRegister.getTitle());
                                intent.putExtra("description", Globals.newDutyToRegister.getDescription());
                                intent.putExtra("start_date", sd);
                                intent.putExtra("parent", parent + "");
                                intent.putExtra("duration", duration);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    });
                } else {

                    Call<WebServiceMessage> call = apiInterface.editDuty(
                            id,
                            edtTitle.getText() + "",
                            edtDescription.getText() + "",
                            sd,
                            duration,
                            0,
                            group,
                            priority,
                            Globals.loggedInUser.getId(),
                            users,
                            "",
                            et,
                            cc,
                            System.currentTimeMillis()
                    );
                    call.enqueue(new Callback<WebServiceMessage>() {
                        @Override
                        public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            WebServiceMessage webServiceMessage = response.body();
                            if (webServiceMessage.isError() == false) {
                                Intent intent = new Intent();
                                intent.putExtra("id", webServiceMessage.getMessage().toString());
                                intent.putExtra("title", Globals.newDutyToRegister.getTitle());
                                intent.putExtra("description", Globals.newDutyToRegister.getDescription());
                                intent.putExtra("start_date", sd);
                                intent.putExtra("parent", parent + "");
                                intent.putExtra("duration", duration);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    });

                }

                break;

            case android.R.id.home:

                finish();

                break;
        }

        return true;
    }
}
