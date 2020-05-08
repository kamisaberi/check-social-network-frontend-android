package com.narij.checkv2.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.arnahit.chipinputlayout.ChipsInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.narij.checkv2.R;
import com.narij.checkv2.a.CoolChip;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.library.GlideRenderer;
import com.narij.checkv2.model.Group;
import com.narij.checkv2.model.User;
import com.narij.checkv2.retrofit.APIClient;
import com.narij.checkv2.retrofit.APIInterface;
import com.narij.checkv2.retrofit.model.WebServiceMessage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class AddGroupDialogFragment extends DialogFragment {


    ChipsInputLayout cilUsers;
    TextInputEditText edtTitle;
    TextInputEditText edtDescription;
    AppCompatButton btnSend;
    AppCompatButton btnDelete;

    ProgressDialog dialog;

    APIInterface apiInterface;
    Group group;
    AppCompatTextView txtDialogTitle;

    @SuppressLint("ValidFragment")
    public AddGroupDialogFragment(@Nullable Group group) {

        if (group != null)
            this.group = group;

    }

    public static interface OnCompleteListener {
        public abstract void onComplete(@Nullable Group group, boolean remove);
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_add_group, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        dialog = new ProgressDialog(getActivity(), R.style.ThemeOverlay_MaterialComponents_Dialog);
        dialog.setTitle("Sending...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        txtDialogTitle = v.findViewById(R.id.txtDialogTitle);

        edtTitle = v.findViewById(R.id.edtTitle);
        edtDescription = v.findViewById(R.id.edtDescription);


        cilUsers = v.findViewById(R.id.cilUsers);
        List<CoolChip> users = new ArrayList<>();
//        users.add(new CoolChip(Globals.loggedInUser.getId(), Globals.loggedInUser.getName()));
        for (User user : Globals.friends) {
            users.add(new CoolChip(user.getId(), user.getName()));
        }
        cilUsers.setFilterableChipList(users);
        cilUsers.setImageRenderer(new GlideRenderer());

        // Do all the stuff to initialize your custom view


        btnSend = v.findViewById(R.id.btnSend);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnDelete.setVisibility(View.GONE);
        if (group != null) {
            edtTitle.setText(group.getTitle());
            edtDescription.setText(group.getDescription());
            btnDelete.setVisibility(View.VISIBLE);
            txtDialogTitle.setText("Edit Group");
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

                Call<WebServiceMessage> call = apiInterface.removeGroup(group.getId(), System.currentTimeMillis());
                call.enqueue(new Callback<WebServiceMessage>() {
                    public void onResponse(Call<WebServiceMessage> call, Response<WebServiceMessage> response) {

                        WebServiceMessage webServiceMessage = response.body();
                        if (webServiceMessage.isError() == false) {
                            mListener.onComplete(group, true);
                            dismiss();

                        } else {
                            Toast.makeText(getActivity(), webServiceMessage.getMessage(), Toast.LENGTH_LONG).show();
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
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (com.arnahit.chipinputlayout.Chip a : cilUsers.getSelectedChips()) {
                    CoolChip cc = (CoolChip) a;
                    Globals.newDutyToRegister.getUsers().add(new User((Integer) cc.getId()));
                }

                ArrayList<String> us = new ArrayList<>();
                for (User u : Globals.newDutyToRegister.getUsers()) {
                    us.add(u.getId() + "");
                }
                String users = TextUtils.join(",", us);


                dialog.show();
                Log.e(Globals.LOG_TAG, "ID :" + Globals.loggedInUser.getId());
                Call<WebServiceMessage> call = apiInterface.addGroup(
                        Globals.loggedInUser.getId(),
                        (group == null ? 0 : group.getId()),
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
//                            Globals.groups.add(new Group(Integer.parseInt(sid), edtTitle.getText().toString(), edtDescription.getText().toString()));
//                            Globals.justGroups.add(new Group(Integer.parseInt(sid), edtTitle.getText().toString(), edtDescription.getText().toString()));
                            group = new Group(Integer.parseInt(sid), edtTitle.getText().toString(), edtDescription.getText().toString());
                            Log.d(Globals.LOG_TAG, group.getId() + "   " + group.getTitle());
                            mListener.onComplete(group, false);
                            dismiss();
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
        });

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                //do your stuff
                if (cilUsers.getFilteredRecycler().getVisibility() == View.VISIBLE) {
                    cilUsers.getFilteredRecycler().setVisibility(View.GONE);
                } else {
                    //super.onBackPressed();
                }
//                Toast.makeText(getActivity(), "BACK PRESS", Toast.LENGTH_LONG).show();
            }
        };
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
//        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setLayout((int) (size.x * 0.9), (int) (size.y * 0.8));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }


}
