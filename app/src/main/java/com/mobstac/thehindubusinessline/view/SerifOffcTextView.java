package com.mobstac.thehindubusinessline.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Vicky on 12/10/16.
 */

public class SerifOffcTextView extends android.support.v7.widget.AppCompatTextView {

    public SerifOffcTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SerifOffcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SerifOffcTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(
                    getContext().getAssets(),
                    "fonts/SerifOffc.ttf");
            setTypeface(tf);
        }
    }

}