package com.ns.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.netoperation.util.UserPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    void init(Context context) {
        boolean isUserThemeDay = UserPref.getInstance(context).isUserThemeDay();
        if(isUserThemeDay) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_background));
        }
        else {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_background));
        }
    }
}
