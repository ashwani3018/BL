package com.mobstac.thehindubusinessline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by arvind on 27/10/15.
 */
public class ExpandedHeightGridView extends GridView {

    boolean expanded = false;

    public ExpandedHeightGridView(Context context) {
        super(context);
    }

    public ExpandedHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandedHeightGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (isExpanded()) {

            int expandSpec = MeasureSpec.makeMeasureSpec(View.MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}