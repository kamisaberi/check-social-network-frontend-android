package com.narij.checkv2.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.arnahit.chipinputlayout.ChipsInputLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.narij.checkv2.R;
import com.narij.checkv2.a.CoolChip;
import com.narij.checkv2.dialog.AddGroupDialogFragment;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.library.GlideRenderer;
import com.narij.checkv2.model.Duty;
import com.narij.checkv2.model.Expert;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.Priority;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDuty2Activity extends AppCompatActivity implements AddGroupDialogFragment.OnCompleteListener {

    APIInterface apiInterface;
    int id = 0;


    TextInputEditText edtTitle;
    TextInputEditText edtDescription;
    //    AppCompatButton btnPriority;
//    Button btnGroup;
//    Button btnUsers;
    AppCompatButton btnStartDateTime;
    AppCompatButton btnEndDateTime;
    AppCompatButton btnSend;
    AppCompatButton btnSetAlarms;


    Date dtStarts;
    Date dtEnds;


    @BindView(R.id.chgPriorities)
    ChipGroup chgPriorities;

    @BindView(R.id.chgGroups)
    ChipGroup chgGroups;

    ChipsInputLayout cilUsers;
    ChipsInputLayout cilExperts;


    @BindView(R.id.txtUsers)
    AppCompatTextView txtUsers;
    @BindView(R.id.txtExperts)
    AppCompatTextView txtExperts;

    @BindView(R.id.txtPriorities)
    AppCompatTextView txtPriorities;
    @BindView(R.id.txtGroups)
    AppCompatTextView txtGroups;

    int selectedGroupId = -1;
    int selectedPriorityId = 0;

    ProgressDialog dialog;

    Duty duty = null;
    Group group = null;

    private boolean validate() {

        boolean valid = true;

        if (edtTitle.getText().toString().trim().isEmpty()) {
            edtTitle.setError("Fill Title");
            valid = false;
        } else {
            edtTitle.setError(null);

        }
        if (cilUsers.getSelectedChips().size() == 0) {
            txtUsers.setError("Choose At least One User");
            valid = false;
        } else {
            txtUsers.setError(null);

        }

//        if (cilExperts.getSelectedChips().size() == 0) {
//            txtExperts.setError("Choose At least One Expert");
//            valid = false;
//        } else {
//            txtExperts.setError(null);
//
//        }

        if (selectedGroupId == -1) {
            txtGroups.setError("Select Group");
            valid = false;
        } else {
            txtGroups.setError(null);
        }

        if (selectedPriorityId == 0) {
            txtPriorities.setError("Select Priority");
            valid = false;
        } else {
            txtPriorities.setError(null);
        }

        return valid;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duty2);
        ButterKnife.bind(this);


        if (getIntent() != null) {
            id = getIntent().getIntExtra("duty", 0);
        }

        try {
            for (Group group : Globals.groups) {
                for (Duty duty : group.getDuties()) {
                    if (duty.getId() == id) {
                        this.duty = duty;
                        this.group = group;
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
        }


        apiInterface = APIClient.getClient().create(APIInterface.class);

        dialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog);
        dialog.setTitle("Authenticating...");
        dialog.setIndeterminate(true);


        cilUsers = findViewById(R.id.cilUsers);
        List<CoolChip> users = new ArrayList<>();
        users.add(new CoolChip(Globals.loggedInUser.getId(), Globals.loggedInUser.getName()));
        for (User user : Globals.friends) {
            users.add(new CoolChip(user.getId(), user.getName()));
        }
        cilUsers.setFilterableChipList(users);
        cilUsers.setImageRenderer(new GlideRenderer());


        cilExperts = findViewById(R.id.cilExperts);
        List<CoolChip> experts = new ArrayList<>();
        for (Expert exp : Globals.experts) {
            experts.add(new CoolChip(exp.getId(), exp.getTitle()));
        }


        cilExperts.setFilterableChipList(experts);
        cilExperts.setImageRenderer(new GlideRenderer());

//        cilUsers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus)
//                {
//
//                }
//            }
//        });


        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        btnStartDateTime = findViewById(R.id.btnStartDateTime);
        btnEndDateTime = findViewById(R.id.btnEndDateTime);
        btnSetAlarms = findViewById(R.id.btnSetAlarms);

        dtStarts = Calendar.getInstance().getTime();
        dtEnds = Calendar.getInstance().getTime();
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Add Duty");


        btnStartDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog startDate = new DatePickerDialog(AddDuty2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        dtStarts = newDate.getTime();
                        btnStartDateTime.setText(new SimpleDateFormat(Globals.dateFormat).format(newDate.getTime()));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                startDate.show();

            }
        });

        btnEndDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog endDate = new DatePickerDialog(AddDuty2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        dtEnds = newDate.getTime();
                        btnEndDateTime.setText(new SimpleDateFormat(Globals.dateFormat).format(newDate.getTime()));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                endDate.show();
            }
        });


        btnSetAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(getBaseContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_set_alarms);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnEdit = dialog.findViewById(R.id.btnEdit);
                Button btnShare = dialog.findViewById(R.id.btnShare);

                dialog.show();


            }
        });


//        chgGroups = findViewById(R.id.chgGroups);
//        for (Group group : Globals.justGroups) {
//            Chip chip = (Chip) LayoutInflater.from(getBaseContext()).inflate(R.layout.chip_group_choice, null);
//            chip.setText(group.getTitle());
//            chip.setTag(group.getId() + "");
//            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        selectedGroupId = Integer.parseInt(chip.getTag() + "");
//                    }
//
//                    boolean b = false;
//                    for (int i = 0; i < chgGroups.getChildCount(); i++) {
//                        Chip chip1 = (Chip) chgGroups.getChildAt(i);
//                        if (chip1.isChecked()) {
//                            b = true;
//                            break;
//                        }
//                    }
//                    if (!b) {
//                        selectedGroupId = -1;
//                    }
//
//                }
//            });
//            chgGroups.addView(chip);
//        }
//
//
//        Chip chip1 = (Chip) LayoutInflater.from(getBaseContext()).inflate(R.layout.chip_group_action, null);
//        chip1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(AddDuty2Activity.this, "New Group Fragment", Toast.LENGTH_LONG).show();
//                AddGroupDialogFragment fragment = new AddGroupDialogFragment();
//                fragment.show(getSupportFragmentManager(), fragment.getTag());
//            }
//        });
//        chgGroups.addView(chip1);

        createGroupsChips();

        for (Priority p : Globals.priorities) {
            Chip chip = (Chip) LayoutInflater.from(getBaseContext()).inflate(R.layout.chip_group_choice, null);
            chip.setText(p.getTitle());
            chip.setTag(p.getId() + "");
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedPriorityId = Integer.parseInt(chip.getTag() + "");
                    }


                    boolean b = false;
                    for (int i = 0; i < chgPriorities.getChildCount(); i++) {
                        Chip chip1 = (Chip) chgPriorities.getChildAt(i);
                        if (chip1.isChecked()) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        selectedPriorityId = 0;
                    }


                }
            });
            chgPriorities.addView(chip);
        }


        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate())
                    return;


                Globals.newDutyToRegister.setTitle(edtTitle.getText().toString());
                Globals.newDutyToRegister.setDescription(edtDescription.getText().toString());
                Globals.newDutyToRegister.setPriority(new Priority(selectedPriorityId, ""));
                Globals.newDutyToRegister.setGroupids(selectedGroupId + "");

                for (com.arnahit.chipinputlayout.Chip a : cilUsers.getSelectedChips()) {
                    CoolChip cc = (CoolChip) a;
                    Globals.newDutyToRegister.getUsers().add(new User((Integer) cc.getId()));
                }

                long duration;
                int parent = 0;
                int group;
                String users;
                String experts;
                int priority = 0;

                ArrayList<String> us = new ArrayList<>();
                for (User u : Globals.newDutyToRegister.getUsers()) {
                    us.add(u.getId() + "");
                }
                users = TextUtils.join(",", us);


                for (com.arnahit.chipinputlayout.Chip a : cilExperts.getSelectedChips()) {
                    CoolChip cc = (CoolChip) a;
                    Globals.newDutyToRegister.getExperts().add(new Expert((Integer) cc.getId()));
                }
                ArrayList<String> exps = new ArrayList<>();
                for (Expert exp : Globals.newDutyToRegister.getExperts()) {
                    exps.add(exp.getId() + "");
                }
                experts = TextUtils.join(",", exps);
                Log.d(Globals.LOG_TAG, "EXP : " + experts);

                priority = Globals.newDutyToRegister.getPriority().getId();
                group = Integer.parseInt(Globals.newDutyToRegister.getGroupids());
                duration = Math.abs(dtEnds.getTime() - dtStarts.getTime());
                long sd = dtStarts.getTime();

                int et = 0;
                int cc = 1;


                dialog.show();

                if (id == 0) {

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
                            experts,
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
                            experts,
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


            }
        });


        if (id != 0) {


            List<CoolChip> selectedUsers = new ArrayList<>();
            Log.d(Globals.LOG_TAG, "DU US:" + duty.getUsers().size());
            for (User user : duty.getUsers()) {
                selectedUsers.add(new CoolChip(user.getId(), user.getName()));
            }
            cilUsers.setSelectedChipList(selectedUsers);

            List<CoolChip> selectedExperts = new ArrayList<>();
            for (Expert exp : duty.getExperts()) {
                selectedExperts.add(new CoolChip(exp.getId(), exp.getTitle()));
            }
            cilExperts.setSelectedChipList(selectedExperts);

            edtTitle.setText(duty.getTitle());
            edtDescription.setText(duty.getDescription());
            btnStartDateTime.setText(new SimpleDateFormat(Globals.dateFormat).format(duty.getStartDate()));
            btnEndDateTime.setText(new SimpleDateFormat(Globals.dateFormat).format(duty.getStartDate() + duty.getDuration()));


            for (int i = 0; i < chgPriorities.getChildCount(); i++) {
                Chip chip = (Chip) chgPriorities.getChildAt(i);
                if (chip.getTag().toString().trim().equals(duty.getPriority().getId() + "") == true) {
                    chip.setChecked(true);
                    break;
                }
            }


            for (int i = 0; i < chgGroups.getChildCount(); i++) {
                Chip chip = (Chip) chgGroups.getChildAt(i);
                if (chip.getTag().toString().equals(group.getId() + "")) {
                    chip.setChecked(true);
                    break;
                }
            }

        }


//        cilUsers.clearFocus();
//        cilUsers.getChipsInput().clearFocus();
//        cilUsers.getChipsInput().setSelected(false);
//        cilUsers.getChipsInput().setFocusable(false);
//        cilUsers.getChipsInput().setFocusedByDefault(false);
//        cilUsers.setSelected(false);
//        edtTitle.setSelected(true);
//        edtTitle.requestFocus();
//        cilUsers.getChipsInput().setFocusable(false);
//        cilUsers.getChipsInput().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cilUsers.setFocusable(true);
//                cilUsers.getChipsInput().setFocusable(true);
////                cilUsers.requestFocus();
//                cilUsers.getChipsInput().requestFocus();
//                Log.d(Globals.LOG_TAG, "CLICKEDDDDDDDDDD");
//            }
//        });
//
//        cilUsers.getChipsInput().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cilUsers.getChipsInput().setFocusable(true);
//                cilUsers.setFocusable(true);
//            }
//        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        cilUsers.getChipsInput().clearFocus();
        cilUsers.clearFocus();

        edtTitle.requestFocus();

//        onBackPressed();
        //        cilUsers.getChipsInput().clearFocus();
//        cilUsers.getChipsInput().setSelected(false);
//        cilUsers.setFocusable(false);
//        cilUsers.getChipsInput().setFocusable(false);
//        cilUsers.getChipsInput().setFocusedByDefault(false);
//        cilUsers.setSelected(false);
//        edtTitle.setSelected(true);
//        edtTitle.requestFocus();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        cilUsers.getChipsInput().setSelected(false);
//        cilUsers.getChipsInput().setFocusable(false);
//        cilUsers.getChipsInput().setFocusedByDefault(false);
//        cilUsers.setSelected(false);
//        edtTitle.setSelected(true);
//        cilUsers.setFocusable(true);
//        cilUsers.getChipsInput().setFocusable(true);

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


                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {

        if (cilUsers.getFilteredRecycler().getVisibility() == View.VISIBLE) {
            cilUsers.getFilteredRecycler().setVisibility(View.GONE);
        } else if (cilExperts.getFilteredRecycler().getVisibility() == View.VISIBLE) {
            cilExperts.getFilteredRecycler().setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onComplete(Group group1, boolean remove) {

//        Globals.groups.add(group1);
        if (remove == false) {
            boolean add = true;
            for (int i = 0; i < Globals.justGroups.size(); i++) {
                if (group1.getId() == Globals.justGroups.get(i).getId()) {
                    Globals.justGroups.get(i).setTitle(group1.getTitle());
                    Globals.justGroups.get(i).setDescription(group1.getDescription());
                    add = false;
                    break;
                }
            }

            if (add)
                Globals.justGroups.add(group1);
        } else {
            for (int i = 0; i < Globals.justGroups.size(); i++) {
                if (group1.getId() == Globals.justGroups.get(i).getId()) {
                    Globals.justGroups.remove(i);
                    break;
                }
            }
        }
        createGroupsChips();


    }


    private void createGroupsChips() {
        chgGroups.removeAllViews();

        for (Group group : Globals.justGroups) {
            Chip chip = (Chip) LayoutInflater.from(getBaseContext()).inflate(R.layout.chip_group_choice, null);
            chip.setText(group.getTitle());
            chip.setTag(group.getId() + "");
            android.util.Log.d(Globals.LOG_TAG, "TAG :" + chip.getTag() + "");

            chip.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //Toast.makeText(AddDuty2Activity.this, chip.getTag() + " " + chip.getText(), Toast.LENGTH_LONG).show();

                    AddGroupDialogFragment fragment = new AddGroupDialogFragment(new Group(Integer.parseInt(chip.getTag().toString().trim()), chip.getText().toString().trim()));
                    fragment.show(getSupportFragmentManager(), fragment.getTag());

                    return true;
                }
            });

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedGroupId = Integer.parseInt(chip.getTag() + "");
                    }

                    boolean b = false;
                    for (int i = 0; i < chgGroups.getChildCount(); i++) {
                        Chip chip1 = (Chip) chgGroups.getChildAt(i);
                        if (chip1.isChecked()) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        selectedGroupId = -1;
                    }

                }
            });
            chgGroups.addView(chip);
        }


        Chip chip1 = (Chip) LayoutInflater.from(getBaseContext()).inflate(R.layout.chip_group_action, null);
        chip1.setTag("-1");
        chip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(AddDuty2Activity.this, "New Group Fragment", Toast.LENGTH_LONG).show();
                AddGroupDialogFragment fragment = new AddGroupDialogFragment(null);
                fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });
        chgGroups.addView(chip1);
    }

}
