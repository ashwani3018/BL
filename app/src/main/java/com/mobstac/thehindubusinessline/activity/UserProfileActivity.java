package com.mobstac.thehindubusinessline.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.UserProfileFragment;

public class UserProfileActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbar.setTitle(getString(R.string.my_profile));
        mToolbar.setLogo(null);
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        mToolbar.setSubtitle("");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserProfileFragment fragment = UserProfileFragment.newInstance();
        fragmentTransaction.replace(R.id.user_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        setBottomNavigationViewSelectedItem(R.id.menu_portfolio);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 1) {
                    manager.popBackStack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbarTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    public void setToolbarLogo(int id) {
        if (mToolbar != null) {
            mToolbar.setLogo(id);
        }
    }

    public void hideToolbarLogo() {
        if (mToolbar != null) {
            mToolbar.setLogo(null);
        }
    }
}
