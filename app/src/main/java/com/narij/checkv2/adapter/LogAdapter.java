package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.env.Globals;
import com.narij.checkv2.model.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyViewHolder> {

    private ArrayList<Log> logs = new ArrayList<>();
    Context context;

    public ArrayList<Log> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<Log> logs) {
        this.logs = logs;
    }

    public LogAdapter(ArrayList<Log> logs) {
        this.logs = logs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dutyView = inflater.inflate(R.layout.recycler_item_log, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(dutyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Log log = logs.get(i);
        myViewHolder.txtLog.setText(log.getLog());
        myViewHolder.txtCreator.setText(log.getUser().getName());
        myViewHolder.txtTime.setText(new SimpleDateFormat(Globals.dateFormat).format(log.getDate()));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, duty.getId() + " " + duty.getTitle() , Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtLog;
        TextView txtCreator;
        TextView txtTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtLog = itemView.findViewById(R.id.txtLog);
            txtCreator = itemView.findViewById(R.id.txtCreator);
            txtTime= itemView.findViewById(R.id.txtTime);
        }
    }


}
