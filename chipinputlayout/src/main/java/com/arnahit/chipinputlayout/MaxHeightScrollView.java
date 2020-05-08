package com.arnahit.chipinputlayout;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.widget.NestedScrollView;

/**
 * Copyright © 2017 Tyler Suehr
 *
 * Subclass of {@link NestedScrollView} that allows a maximum height to be specified
 * such that this views height cannot exceed it.
 *
 * @author Tyler Suehr
 * @version 1.0
 */
public class MaxHeightScrollView extends NestedScrollView {
    private int mMaxHeight;


    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
    }

    public MaxHeightScrollView(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
        this.mMaxHeight = a.getDimensionPixelSize(R.styleable.MaxHeightScrollView_android_maxHeight, Utils.dp(300));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMaxHeight(int height) {
        this.mMaxHeight = height;
        invalidate();
    }
}
