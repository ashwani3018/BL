package com.mobstac.thehindubusinessline.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.BookmarksFragment;

public class ReadLaterActivity extends AdsBaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_notification;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        BookmarksFragment notificationFragment = new BookmarksFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FRAME_CONTENT, notificationFragment).addToBackStack(null).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
}
