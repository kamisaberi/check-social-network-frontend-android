package com.narij.checkv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.narij.checkv2.R;
import com.narij.checkv2.model.Group;

import java.util.ArrayList;

public class SelectGroupBaseAdapter extends BaseAdapter {


    private ArrayList<Group> groups = new ArrayList<>();
    Context context;

    private ArrayList<MyViewHolder> holders = new ArrayList<>();


    public int selectedGroupId = -1;

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


    public SelectGroupBaseAdapter(Context context, ArrayList<Group> groups) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.recycler_item_select_group, parent, false);
        }


        // get current item to be displayed
        Group group = groups.get(position);
        MyViewHolder viewHolder = new MyViewHolder();

        viewHolder.txtTitle = convertView.findViewById(R.id.txtTitle);
        viewHolder.rdbSelect = convertView.findViewById(R.id.rdbSelect);

        viewHolder.txtTitle.setText(group.getTitle());
        // returns the view for the current row
        return convertView;
    }


    public class MyViewHolder {

        Group group;
        TextView txtTitle;
        RadioButton rdbSelect;
    }


}
