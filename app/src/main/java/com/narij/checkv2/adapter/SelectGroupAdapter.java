package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.model.Group;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectGroupAdapter extends RecyclerView.Adapter<SelectGroupAdapter.MyViewHolder> {


    private ArrayList<Group> groups = new ArrayList<>();
    Context context;

    private ArrayList<MyViewHolder> holders = new ArrayList<>();


    public int selectedGroupId= -1;

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public SelectGroupAdapter(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dutyView = inflater.inflate(R.layout.recycler_item_select_group, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(dutyView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        final Group group = groups.get(i);
        myViewHolder.txtTitle.setText(group.getTitle());
        myViewHolder.group = group;

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedGroupId = myViewHolder.group.getId();

//                android.util.Log.d(Globals.LOG_TAG, "selectedGroupId:" + selectedGroupId);


                for (SelectGroupAdapter.MyViewHolder holder : holders) {
                    if (holder.group.getId() == selectedGroupId) {
                        holder.rdbSelect.setChecked(true);
                    } else {
                        holder.rdbSelect.setChecked(false);
                    }
                }
            }
        });

        holders.add(myViewHolder);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        Group group;
        TextView txtTitle;
        RadioButton rdbSelect;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            rdbSelect = itemView.findViewById(R.id.rdbSelect);
            group = new Group();
        }
    }


}
