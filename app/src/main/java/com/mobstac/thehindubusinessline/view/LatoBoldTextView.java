package com.mobstac.thehindubusinessline.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Vicky on 12/10/16.
 */

public class LatoBoldTextView extends AppCompatTextView {

    public LatoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(
                    getContext().getAssets(),
                    "fonts/Lato-Bold.ttf");
            setTypeface(tf);
        }
    }

}