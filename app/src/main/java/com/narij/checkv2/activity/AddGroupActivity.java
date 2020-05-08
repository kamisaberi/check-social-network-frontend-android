package com.narij.checkv2.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.narij.checkv2.R;
import com.narij.checkv2.adapter.SelectUserAdapter;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.library.SegmentedGroup;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroupActivity extends AppCompatActivity {

    ProgressDialog dialog;
    Toolbar toolbar;

    SegmentedGroup segmented2;

    RadioButton rdbInfo;
    RadioButton rdbUsers;

    LinearLayout pnlBasicInfo;
    LinearLayout pnlSelectUsers;


    EditText edtTitle;
    EditText edtDescription;
    APIInterface apiInterface;


    RecyclerView rcUsers;
    SelectUserAdapter selectUserAdapter;
    EditText edtSearchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        apiInterface = APIClient.getClient().create(APIInterface.class);
        dialog = new ProgressDialog(this);


        segmented2 = findViewById(R.id.segmented2);
        rdbInfo = findViewById(R.id.rdbInfo);
        rdbUsers = findViewById(R.id.rdbUsers);
        pnlBasicInfo = findViewById(R.id.pnlBasicInfo);
        pnlSelectUsers = findViewById(R.id.pnlSelectUsers);

        ViewTreeObserver vto = segmented2.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    AddGroupActivity.this.segmented2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    AddGroupActivity.this.segmented2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = AddGroupActivity.this.segmented2.getMeasuredWidth();
                int height = AddGroupActivity.this.segmented2.getMeasuredHeight();
                rdbInfo.setWidth(width / 2);
                rdbUsers.setWidth(width / 2);
            }
        });


        rdbInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.d(Globals.LOG_TAG, b+"");
                if (b == true) {
                    pnlBasicInfo.setVisibility(View.VISIBLE);
                    pnlSelectUsers.setVisibility(View.GONE);
                }
            }
        });

        rdbUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    pnlBasicInfo.setVisibility(View.GONE);
                    pnlSelectUsers.setVisibility(View.VISIBLE);
                }
            }
        });


        rdbInfo.setChecked(true);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);


        rcUsers = findViewById(R.id.rcUsers);
        edtSearchUser = findViewById(R.id.edtSearchUser);

        ArrayList<User> users = new ArrayList<>();
//        users.add(Globals.loggedInUser);
        for (User user : Globals.friends) {

            users.add(user);
        }

        selectUserAdapter = new SelectUserAdapter(users);
        rcUsers.setAdapter(selectUserAdapter);
        LinearLayoutManager userslayoutManager = new LinearLayoutManager(AddGroupActivity.this, RecyclerView.VERTICAL, false);
        userslayoutManager.scrollToPosition(0);
        rcUsers.setLayoutManager(userslayoutManager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_add_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuSend:


                if (edtTitle.getText().toString().isEmpty() == true) {
                    rdbInfo.setChecked(true);
                    Toast.makeText(getBaseContext(), "Fill Title", Toast.LENGTH_LONG).show();
                    return false;
                }

                String users = TextUtils.join(",", selectUserAdapter.getSelectedUsers());

                dialog.show();
                Log.e(Globals.LOG_TAG, "ID :" + Globals.loggedInUser.getId());
                Call<WebServiceMessage> call = apiInterface.addGroup(
                        Globals.loggedInUser.getId(),
                        0,
                        edtTitle.getText().toString(),
                        edtDescription.getText().toString(),
                        users,
                        System.currentTimeMillis());
                call.enqueue(new Callback<WebServiceMessage>() {
                    @Override
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                        WebServiceMessage webServiceMessage = response.body();

                        if (webServiceMessage.isError() == false) {
                            String sid = webServiceMessage.getMessage();
                            Intent intent = new Intent();
                            intent.putExtra("id", Integer.parseInt(sid));
                            intent.putExtra("title", edtTitle.getText().toString());
                            intent.putExtra("description", edtDescription.getText().toString());

                            Globals.justGroups.add(new Group(Integer.parseInt(sid), edtTitle.getText().toString(), edtDescription.getText().toString()));
                            setResult(RESULT_OK, intent);
                            finish();

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


                break;
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }

        return true;
    }
}
