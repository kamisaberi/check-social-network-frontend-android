package com.narij.checkv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.activity.DutyActivity;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.Duty;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DutyAdapter extends RecyclerView.Adapter<DutyAdapter.MyViewHolder> {


    //private ArrayList<Duty> duties = new ArrayList<>();
    Context context;

    public ArrayList<Duty> getDuties() {
        return Globals.duties;
    }

    public void setDuties(ArrayList<Duty> duties) {
        Globals.duties = duties;
    }

    public DutyAdapter(ArrayList<Duty> duties) {
        Globals.duties = duties;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dutyView = inflater.inflate(R.layout.recycler_item_duty_normal_priority, viewGroup, false);

        MyViewHolder viewHolder = new MyViewHolder(dutyView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        final Duty duty = Globals.duties.get(i);
        myViewHolder.txtCreator.setText("@" + duty.getCreator().getUsername() + " " + duty.getCreator().getName());
        myViewHolder.txtTitle.setText(duty.getTitle());
        myViewHolder.txtLogsCount.setText(duty.getLogs().size() + "");
        myViewHolder.txtRemainingTime.setText("0");

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Globals.selectedDutyIndex = i;
                Globals.selectedDutyId = duty.getId();
                Intent intent = new Intent(context, DutyActivity.class);
                intent.putExtra("id" , duty.getId());
//                ActivityCompat.startActivity(context , intent, );
                ActivityCompat.startActivity(context, intent,
                        ActivityOptionsCompat
                                .makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight())
                                .toBundle()
                );
            }
        });


    }

    @Override
    public int getItemCount() {
        return Globals.duties.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txtCreator;
        TextView txtTitle;
        TextView txtRemainingTime;
        TextView txtLogsCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtCreator = itemView.findViewById(R.id.txtCreator);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRemainingTime = itemView.findViewById(R.id.txtRemainingTime);
            txtLogsCount = itemView.findViewById(R.id.txtLogsCount);
        }
    }


}
