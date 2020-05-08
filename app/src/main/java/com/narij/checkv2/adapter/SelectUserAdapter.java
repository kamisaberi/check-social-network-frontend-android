package com.narij.checkv2.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectUserAdapter extends RecyclerView.Adapter<SelectUserAdapter.MyViewHolder> {


    private  ArrayList<User> users = new ArrayList<>();
    //private ArrayList<Duty> duties = new ArrayList<>();
    Context context;

    public ArrayList<User> getUsers() {
        return users;
    }

    public SelectUserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    private ArrayList<Integer> selectedUsers = new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Integer> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(ArrayList<Integer> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dutyView = inflater.inflate(R.layout.recycler_item_select_user, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(dutyView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        final User user = users.get(i);
        myViewHolder.txtName.setText("@" + user.getUsername() + " " + user.getName());
        myViewHolder.user = user;


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myViewHolder.checked == false) {
                    myViewHolder.checked = true;
                    selectedUsers.add(users.get(i).getId());
                    myViewHolder.chkSelect.setChecked(true);

                } else {

                    myViewHolder.checked = false;
                    for (int i = 0; i < selectedUsers.size(); i++) {
                        if (selectedUsers.get(i) == myViewHolder.user.getId()) {
                            selectedUsers.remove(i);
                            break;
                        }
                    }
                    myViewHolder.chkSelect.setChecked(false);
                }

                Log.d(Globals.LOG_TAG, TextUtils.join(",", selectedUsers));
            }
        });






//        myViewHolder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if (b == true) {
//                    selectedUsers.add(users.get(i).getId());
//                } else {
//                    selectedUsers.remove(users.get(i).getId());
//                }
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        boolean checked;
        User user;

        TextView txtName;
        CheckBox chkSelect;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            chkSelect = itemView.findViewById(R.id.chkSelect);
            checked = false;
            user = new User();
        }
    }


}
