package com.narij.checkv2.segment;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.narij.checkv2.R;

import androidx.annotation.NonNull;
import segmented_control.widget.custom.android.com.segmentedcontrol.item_row_column.SegmentAdapter;

public class AppSegmentAdapter extends SegmentAdapter<String, AppSegmentViewHolder> {

    @NonNull
    @Override
    protected AppSegmentViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, int i) {
        return new AppSegmentViewHolder(layoutInflater.inflate(R.layout.segment_item, null));
    }
}