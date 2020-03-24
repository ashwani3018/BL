package com.mobstac.thehindubusinessline.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Vicky on 12/10/16.
 */

public class LatoRegularTextView extends AppCompatTextView {

    public LatoRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoRegularTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(
                    getContext().getAssets(),
                    "fonts/Lato-Regular.ttf");
            setTypeface(tf);
        }
    }

}