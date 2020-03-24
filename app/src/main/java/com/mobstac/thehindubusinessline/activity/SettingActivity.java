package com.mobstac.thehindubusinessline.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.SettingsFragment;

public class SettingActivity extends AdsBaseActivity {


    @Override
    public int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SettingsFragment notificationFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FRAME_CONTENT, notificationFragment).addToBackStack(null).commit();
    }


}
