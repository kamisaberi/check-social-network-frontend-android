package com.narij.checkv2.segment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.narij.checkv2.R;

import androidx.annotation.NonNull;
import segmented_control.widget.custom.android.com.segmentedcontrol.item_row_column.SegmentViewHolder;

public class AppSegmentViewHolder extends SegmentViewHolder<String> {

    public TextView textView;
    public EditText editText;

    public AppSegmentViewHolder(@NonNull View sectionView) {
        super(sectionView);
        textView = (TextView) sectionView.findViewById(R.id.text_view);
        editText = sectionView.findViewById(R.id.edit_text);
    }

    @Override
    protected void onSegmentBind(String segmentData) {
        textView.setText(segmentData);
        editText.setHint("enter name");
    }
}