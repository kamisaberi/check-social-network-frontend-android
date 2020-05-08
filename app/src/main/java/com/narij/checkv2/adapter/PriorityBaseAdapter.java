package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.model.Priority;

import java.util.ArrayList;

public class PriorityBaseAdapter extends BaseAdapter {

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

    public PriorityBaseAdapter(Context context, ArrayList<Priority> priorities) {
        this.priorities = priorities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return priorities.size();
    }

    @Override
    public Object getItem(int position) {
        return priorities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.recycler_item_select_priority, parent, false);
        }


        // get current item to be displayed
        Priority priority = priorities.get(position);
        MyViewHolder viewHolder = new MyViewHolder();

        viewHolder.txtTitle = convertView.findViewById(R.id.txtTitle);
        viewHolder.rdbSelect = convertView.findViewById(R.id.rdbSelect);

        viewHolder.txtTitle.setText(priority.getTitle());
        // returns the view for the current r    }
        return convertView;

    }

    public class MyViewHolder {

        Priority priority;
        TextView txtTitle;
        RadioButton rdbSelect;
    }


}
