package com.narij.checkv2.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.narij.checkv2.R;
import com.narij.checkv2.adapter.LogAdapter;
import com.narij.checkv2.adapter.MessageAdapter;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.firebase.app.Config;
import com.narij.checkv2.firebase.util.NotificationUtils;
import com.narij.checkv2.library.SegmentedGroup;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.Log;
import com.narij.checkv2.model.Message;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitMessages;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DutyActivity extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ProgressDialog dialog;


    SegmentedGroup segmented2;

    RadioButton rdbInfo;
    RadioButton rdbLogs;
    RadioButton rdbChat;
    RadioButton rdbOperations;

    LinearLayout pnlInfo;
    LinearLayout pnlLogs;
    LinearLayout pnlChat;
    LinearLayout pnlOperations;

    Toolbar toolbar;

    TextView txtGroup;
    TextView txtTitle;
    TextView txtDescription;
    TextView txtStartDate;
    TextView txtEndDate;
    TextView txtUsers;
    Button btnAddLog;
    EditText edtLog;
    RecyclerView rcLogs;

    LogAdapter logAdapter;
    ArrayList<Log> logs = new ArrayList<>();

    APIInterface apiInterface;

    int dutyId = 0;
    Duty duty = new Duty();

    MessageAdapter messageAdapter;

    RecyclerView rcMessages;
    ImageButton btnSend;
    EditText edtMessage;


    Button btnReportSuccess;
    Button btnReportFail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(DutyActivity.this);


        segmented2 = findViewById(R.id.segmented2);
        rdbInfo = findViewById(R.id.rdbInfo);
        rdbLogs = findViewById(R.id.rdbLogs);
        rdbChat = findViewById(R.id.rdbChat);
        rdbOperations = findViewById(R.id.rdbOperations);
        pnlInfo = findViewById(R.id.pnlInfo);
        pnlLogs = findViewById(R.id.pnlLogs);
        pnlChat = findViewById(R.id.pnlChat);
        pnlOperations = findViewById(R.id.pnlOperations);

        ViewTreeObserver vto = segmented2.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    DutyActivity.this.segmented2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    DutyActivity.this.segmented2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = DutyActivity.this.segmented2.getMeasuredWidth();
                int height = DutyActivity.this.segmented2.getMeasuredHeight();
                rdbInfo.setWidth(width / 4);
                rdbLogs.setWidth(width / 4);
                rdbChat.setWidth(width / 4);
                rdbOperations.setWidth(width / 4);
            }
        });

        rdbInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.d(Globals.LOG_TAG, b+"");
                if (b == true) {
                    pnlInfo.setVisibility(View.VISIBLE);
                    pnlLogs.setVisibility(View.GONE);
                    pnlChat.setVisibility(View.GONE);
                    pnlOperations.setVisibility(View.GONE);
                }
            }
        });

        rdbLogs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    pnlInfo.setVisibility(View.GONE);
                    pnlLogs.setVisibility(View.VISIBLE);
                    pnlChat.setVisibility(View.GONE);
                    pnlOperations.setVisibility(View.GONE);
                }
            }
        });

        rdbChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    pnlInfo.setVisibility(View.GONE);
                    pnlLogs.setVisibility(View.GONE);
                    pnlChat.setVisibility(View.VISIBLE);
                    pnlOperations.setVisibility(View.GONE);
                }
            }
        });

        rdbOperations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    pnlInfo.setVisibility(View.GONE);
                    pnlLogs.setVisibility(View.GONE);
                    pnlChat.setVisibility(View.GONE);
                    pnlOperations.setVisibility(View.VISIBLE);
                }
            }
        });

        rdbInfo.setChecked(true);


        txtGroup = findViewById(R.id.btnGroup);
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtUsers = findViewById(R.id.txtUsers);
        btnAddLog = findViewById(R.id.btnAddLog);
        edtLog = findViewById(R.id.edtLog);
        rcLogs = findViewById(R.id.rcLogs);


        rcMessages = findViewById(R.id.rcMessages);
        btnSend = findViewById(R.id.btnSend);
        edtMessage = findViewById(R.id.edtMessage);


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
                            txtGroup.setText(group.getTitle());
                            txtTitle.setText(duty.getTitle());
                            txtDescription.setText(duty.getDescription());
                            txtStartDate.setText(new SimpleDateFormat(Globals.dateTimeFormat).format(new Date(duty.getStartDate())));
                            txtEndDate.setText(new SimpleDateFormat(Globals.dateTimeFormat).format(new Date(duty.getStartDate() + duty.getDuration())));

                            logs = duty.getLogs();
                            logAdapter = new LogAdapter(logs);
                            rcLogs.setAdapter(logAdapter);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(DutyActivity.this, RecyclerView.VERTICAL, false);
                            layoutManager.scrollToPosition(0);
                            rcLogs.setLayoutManager(layoutManager);

                            ArrayList<Message> messages = new ArrayList<>();
                            messageAdapter = new MessageAdapter(messages);
                            rcMessages.setAdapter(messageAdapter);
                            LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                            layout.scrollToPosition(messages.size() - 1);
                            rcMessages.setLayoutManager(layout);


                            ArrayList<String> users = new ArrayList<>();
                            for (User user : duty.getUsers()) {
                                users.add(user.getName());
                            }
                            String u = TextUtils.join(" ", users);
                            txtUsers.setText(u);

//                            if (Globals.DEBUG_MODE == true) {
//                                Globals.loggedInUser.setId(1);
//                            }

                            Call<RetrofitMessages> call = apiInterface.getMessages(id, 0L, System.currentTimeMillis());
                            call.enqueue(new Callback<RetrofitMessages>() {
                                @Override
                                public void onResponse(Call<RetrofitMessages> call, Response<RetrofitMessages> response) {

                                    RetrofitMessages retrofitMessages = response.body();
                                    if (retrofitMessages.isError() == false) {
                                        messageAdapter.getMessages().addAll(retrofitMessages.getMessages());
                                        messageAdapter.notifyDataSetChanged();
                                        rcMessages.getLayoutManager().scrollToPosition(messageAdapter.getMessages().size() - 1);
                                    }
                                }

                                @Override
                                public void onFailure(Call<RetrofitMessages> call, Throwable t) {

                                }
                            });
                            throw new Exception();
                        }
                    }
                }
            } catch (Exception e) {
            }


            btnAddLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.show();
                    long d = new Date().getTime();

                    Call<WebServiceMessage> call = apiInterface.addLog(
                            Globals.loggedInUser.getId(),
                            dutyId,
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
                            logAdapter.notifyDataSetChanged();

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
        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

//                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String payload = intent.getStringExtra("payload");

                    int id = 0;
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        id = jsonObject.getInt("duty");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (id == Globals.selectedDutyId) {

                        Call<RetrofitMessages> call = apiInterface.getMessages(Globals.selectedDutyId, messageAdapter.getMessages().get(messageAdapter.getMessages().size() - 1).getDate(), System.currentTimeMillis());
                        call.enqueue(new Callback<RetrofitMessages>() {
                            @Override
                            public void onResponse(Call<RetrofitMessages> call, Response<RetrofitMessages> response) {

                                RetrofitMessages retrofitMessages = response.body();
                                if (retrofitMessages.isError() == false) {
                                    messageAdapter.getMessages().addAll(retrofitMessages.getMessages());
                                    messageAdapter.notifyDataSetChanged();
                                    rcMessages.getLayoutManager().scrollToPosition(messageAdapter.getMessages().size() - 1);
                                }
                            }

                            @Override
                            public void onFailure(Call<RetrofitMessages> call, Throwable t) {

                            }
                        });

                    }


//                    String payload= intent.getStringExtra("payload");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message + "\n" + payload);
//                    Log.d(Globals.LOG_TAG, message);

//                    FileOutputStream fos = null;
//                    try {
//                        fos = new FileOutputStream(file);
//                        Log.d(Globals.LOG_TAG, "file size :" + message.getBytes().length);
//                        fos.write(message.getBytes());
//                        fos.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                }
            }
        };


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<WebServiceMessage> call = apiInterface.addMessage(
                        Globals.loggedInUser.getId(),
                        edtMessage.getText().toString(),
                        Globals.selectedDutyId,
                        System.currentTimeMillis());
                call.enqueue(new Callback<WebServiceMessage>() {
                    @Override
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
                        WebServiceMessage webServiceMessage = response.body();
                        if (webServiceMessage.isError() == false) {


//                            Call<RetrofitMessages> call1 = apiInterface.getMessages(Globals.selectedDutyId, messageAdapter.getMessages().get(messageAdapter.getMessages().size()-1).getDate());
//                            call1.enqueue(new Callback<RetrofitMessages>() {
//                                @Override
//                                public void onResponse(Call<RetrofitMessages> call, Response<RetrofitMessages> response) {
//
//                                    RetrofitMessages retrofitMessages = response.body();
//                                    if (retrofitMessages.isError() == false) {
//                                        messageAdapter.getMessages().addAll(retrofitMessages.getMessages());
//                                        messageAdapter.notifyDataSetChanged();
//                                        rcMessages.getLayoutManager().scrollToPosition(messageAdapter.getMessages().size()-1);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<RetrofitMessages> call, Throwable t) {
//
//                                }
//                            });


                            Message message = new Message();
                            message.setId(Integer.parseInt(webServiceMessage.getMessage()));
                            message.getDuty().setId(1);
                            message.setContent(edtMessage.getText() + "");
                            message.setUser(Globals.loggedInUser);
                            message.setDate(new Date().getTime());
                            messageAdapter.getMessages().add(message);
                            messageAdapter.notifyDataSetChanged();
                            rcMessages.getLayoutManager().scrollToPosition(messageAdapter.getMessages().size() - 1);
                            edtMessage.setText("");
                        }

                    }

                    @Override
                    public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                    }
                });


            }
        });


        btnReportSuccess = findViewById(R.id.btnReportSuccess);
        btnReportFail = findViewById(R.id.btnReportFail);

        if (this.duty.getCreator().getId() != Globals.loggedInUser.getId()) {
            btnReportSuccess.setEnabled(false);
            btnReportFail.setEnabled(false);
        } else {

            if (this.duty.getFinishType() == 0) {
                btnReportSuccess.setEnabled(true);
                btnReportFail.setEnabled(true);
            } else if (this.duty.getFinishType() == 1) {
                btnReportSuccess.setEnabled(false);
                btnReportFail.setEnabled(true);
            } else if (this.duty.getFinishType() == 2) {
                btnReportSuccess.setEnabled(true);
                btnReportFail.setEnabled(false);
            }
        }


        btnReportSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportDutyFinish(1);
                btnReportSuccess.setEnabled(false);
            }
        });

        btnReportFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportDutyFinish(2);
                btnReportFail.setEnabled(false);
            }
        });


    }


    public void reportDutyFinish(int type) {

        dialog.show();
        Call<WebServiceMessage> call = apiInterface.finishDuty(Globals.loggedInUser.getId(), dutyId, type, System.currentTimeMillis());
        call.enqueue(new Callback<WebServiceMessage>() {
            @Override
            public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {
                WebServiceMessage message = response.body();
                if (message.isError() == false) {

                    DutyActivity.this.duty.setFinishType(type);
                    if (type == 1) {

                        btnReportSuccess.setEnabled(false);
                        btnReportFail.setEnabled(true);

                    } else if (type == 2) {

                        btnReportSuccess.setEnabled(true);
                        btnReportFail.setEnabled(false);
                    }
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Call<WebServiceMessage> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }


        return true;
    }
}
