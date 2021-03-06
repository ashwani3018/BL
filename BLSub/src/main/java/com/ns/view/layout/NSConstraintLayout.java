package com.ns.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.netoperation.util.UserPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSConstraintLayout extends ConstraintLayout {

    private int viewType;

    public NSConstraintLayout(Context context) {
        super(context);
        init(context, null);
    }

    public NSConstraintLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSConstraintLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, @Nullable AttributeSet attrs) {

        boolean isUserThemeDay = UserPref.getInstance(context).isUserThemeDay();

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSLayout);
            if (typedArray.hasValue(R.styleable.NSLayout_layoutType)) {
                viewType = typedArray.getInt(R.styleable.NSLayout_layoutType, 0);
            } else {
                viewType = -1;
            }
        }

        // 5 = Black_Light
        if(viewType == 5){
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_background));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_background));
            }
        }
        // 6 = Black_Dark
        else if(viewType == 6){
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_home_banner_background));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_home_banner_background));
            }
        }
        // 7 = Black_Full
        else if(viewType == 7) {
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_white));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_dark));
            }
        }
        else {
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_background));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_background));
            }
        }
    }
}
