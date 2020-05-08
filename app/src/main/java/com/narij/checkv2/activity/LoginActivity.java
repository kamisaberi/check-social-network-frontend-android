package com.narij.checkv2.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.firebase.app.Config;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.RetrofitFriends;
import com.narij.checkv2.retrofit.model.RetrofitUser;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    APIInterface apiInterface;
    TextInputEditText edtPhone;
    TextInputEditText edtPassword;
    AppCompatButton btnLogin;
    AppCompatTextView btnRegister;


//    AVLoadingIndicatorView prg;

    SharedPreferences pref;
    ProgressDialog dialog;

//    ChipsInputLayout chipsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        chipsInput = (ChipsInputLayout) findViewById(R.id.chips_input);
//
//        List<CoolChip> chips = new ArrayList<>();
//
//        chips.add(new CoolChip(1001,"alireza"));
//        chips.add(new CoolChip(1002,"reza"));
//        chips.add(new CoolChip(1003,"ali"));
//        chips.add(new CoolChip(1004,"ahmad"));
//        chips.add(new CoolChip(1005,"mahmood"));
//        this.chipsInput.setFilterableChipList(chips);

//        Button btn1= findViewById(R.id.btn1);
//        AppCompatEditText txt1= findViewById(R.id.edt1);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        dialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog);
        dialog.setTitle("Authenticating...");
        dialog.setIndeterminate(true);
        pref = getApplicationContext().getSharedPreferences(Globals.SHARED_PREF, 0);
//        try {
//            PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//// listen refresh event
//            layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    // start refresh
//                }
//            });
//// refresh complete
//            layout.setRefreshing(false);
//
//        } catch (Exception e) {
//            Log.d(Globals.LOG_TAG, e.getMessage());
//        }


        apiInterface = APIClient.getClient().create(APIInterface.class);


        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

//        prg = findViewById(R.id.prg);

//        btnLogin.setTypeface(Globals.boldRoboto);

//        if (Globals.DEBUG_MODE == true) {
//            edtPhone.setText("+989365982333");
//            edtPassword.setText("1234");
//        }

        String phone = pref.getString(Globals.PREF_PHONE_KEY, "");
        String password = pref.getString(Globals.PREF_PASSWORD_KEY, "");
        edtPhone.setText(phone);
        edtPassword.setText(password);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dialog.show();
//                prg.setVisibility(View.VISIBLE);
                String phone = edtPhone.getText().toString();
                String password = edtPassword.getText().toString();
                Log.d(Globals.LOG_TAG, "" + phone + " " + password);
                Call<RetrofitUser> call = apiInterface.login(phone, password, System.currentTimeMillis());

                call.enqueue(new Callback<RetrofitUser>() {
                    @Override
                    public void onResponse(Call<RetrofitUser> call, Response<RetrofitUser> response) {

                        RetrofitUser retrofitUser = response.body();
                        Log.d(Globals.LOG_TAG, retrofitUser.isError() + "");
                        if (retrofitUser.isError() == false) {
                            Globals.loggedInUser = retrofitUser.getUser();
                            Globals.experts = retrofitUser.getExperts();
                            Log.d(Globals.LOG_TAG, "EXP SZ :" + Globals.experts.size());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Globals.PREF_PHONE_KEY, edtPhone.getText().toString());
                            editor.putString(Globals.PREF_PASSWORD_KEY, edtPassword.getText().toString());
                            editor.commit();

                            Log.d(Globals.LOG_TAG, "AVATAR : " + Globals.loggedInUser.getAvatar());

                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(Globals.LOG_TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    String token = Objects.requireNonNull(task.getResult()).getToken();
                                    Log.d(Globals.LOG_TAG, token);
                                    if (Globals.loggedInUser.getFcmToken().equals(token) == false) {
                                        Globals.loggedInUser.setFcmToken(token);

                                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("regId", token);
                                        editor.commit();

                                        Call<WebServiceMessage> call1 = apiInterface.changeFcmToken(Globals.loggedInUser.getId(), token, System.currentTimeMillis());
                                        call1.enqueue(new Callback<WebServiceMessage>() {
                                            @Override
                                            public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                                                if (response.body().isError() == false) {
                                                    Log.d(Globals.LOG_TAG, "FCM  Token Changed");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<WebServiceMessage> call, Throwable t) {

                                            }
                                        });
                                    }
                                }
                            });

                            //Log.d(Globals.LOG_TAG, "user id :" + retrofitUser.getUser().getId());
                            Call<RetrofitFriends> call1 = apiInterface.getFriends(retrofitUser.getUser().getId(), System.currentTimeMillis());
                            call1.enqueue(new Callback<RetrofitFriends>() {
                                @Override
                                public void onResponse(Call<RetrofitFriends> call, Response<RetrofitFriends> response) {

                                    RetrofitFriends retrofitFriends = response.body();
                                    if (retrofitFriends.isError() == false) {

                                        Log.d(Globals.LOG_TAG, "friends size :" + retrofitFriends.getFriends().size());

//                                        prg.setVisibility(View.INVISIBLE);
                                        Globals.friends = retrofitFriends.getFriends();
                                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (dialog.isShowing())
                                        dialog.dismiss();
//                                    prg.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<RetrofitFriends> call, Throwable t) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();

//                                    prg.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), retrofitUser.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RetrofitUser> call, Throwable t) {

//                        prg.setVisibility(View.GONE);
//                        Snackbar.make(LoginActivity.this, )
                    }
                });

            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
