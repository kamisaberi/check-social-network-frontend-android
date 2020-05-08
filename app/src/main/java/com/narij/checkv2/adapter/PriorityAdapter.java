package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.model.Priority;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PriorityAdapter extends RecyclerView.Adapter<PriorityAdapter.MyViewHolder> {

    private ArrayList<Priority> priorities = new ArrayList<>();
    Context context;


    public int selectedPriorityId;

    private ArrayList<MyViewHolder> holders = new ArrayList<>();


    public ArrayList<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(ArrayList<Priority> priorities) {
        this.priorities = priorities;
    }

    public PriorityAdapter(ArrayList<Priority> priorities) {
        this.priorities = priorities;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dutyView = inflater.inflate(R.layout.recycler_item_select_priority, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(dutyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Priority priority = priorities.get(i);
        myViewHolder.txtTitle.setText(priority.getTitle());

        myViewHolder.priority = priority;

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPriorityId = myViewHolder.priority.getId();

//                android.util.Log.d(Globals.LOG_TAG, "selectedGroupId:" + selectedPriorityId);


                for (MyViewHolder holder : holders) {
                    if (holder.priority.getId() == selectedPriorityId) {
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
        return priorities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Priority priority;
        TextView txtTitle;
        RadioButton rdbSelect;

        public MyViewHolder(View itemView) {
            super(itemView);
            priority = new Priority();
            txtTitle = itemView.findViewById(R.id.txtTitle);
            rdbSelect = itemView.findViewById(R.id.rdbSelect);
        }
    }


}
