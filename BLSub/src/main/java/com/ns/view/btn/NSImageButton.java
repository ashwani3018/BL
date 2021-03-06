package com.ns.view.btn;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.netoperation.util.UserPref;
import com.ns.thpremium.R;

public class NSImageButton extends android.support.v7.widget.AppCompatImageButton {

    private int viewType = -1;

    public NSImageButton(Context context) {
        super(context);
        init(context, null);
    }

    public NSImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        boolean isDayTheme = UserPref.getInstance(context).isUserThemeDay();
        setBackground(null);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSImageButton);
            if (typedArray.hasValue(R.styleable.NSImageButton_btnType)) {
                viewType = typedArray.getInt(R.styleable.NSImageButton_btnType, 0);
            } else {
                viewType = -1;
            }
        }

        // arrow_no_line
        if(viewType == 0) {
            if(isDayTheme) {
                setImageResource(R.drawable.ic_back_copy_2);
            } else {
                setImageResource(R.drawable.ic_back_copy_2_dark);
            }
        }
        // back_circle
        else if(viewType == 1) {
            if(isDayTheme) {
                setImageResource(R.drawable.ic_left_arrow_disable);
            } else {
                setImageResource(R.drawable.ic_left_arrow_disable);
            }
        }
        // arrow_back
        else if(viewType == 2) {
            if(isDayTheme) {
                setImageResource(R.drawable.arrow_back);
            } else {
                setImageResource(R.drawable.ic_arrow_back_w);
            }
        }
        // cross_circle
        else if(viewType == 3) {
            if(isDayTheme) {
                setImageResource(R.drawable.ic_close_ss);
            } else {
                setImageResource(R.drawable.ic_close_ss_dark);
            }
        }
        // password_field
        else if(viewType == 4) {
            if(isDayTheme) {
                setImageResource(R.drawable.ic_show_password);
            } else {
                setImageResource(R.drawable.ic_show_password_dark);
            }
        }

        // back_circle_dark_arrow
        else if(viewType == 5) {
            if(isDayTheme) {
                setImageResource(R.drawable.ic_back_copy_42);
            } else {
                setImageResource(R.drawable.ic_back_copy_42_dark);
            }
        }



    }


}
