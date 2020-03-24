package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.SignInFragment;
import com.mobstac.thehindubusinessline.fragments.SignUpFragment;
import com.mobstac.thehindubusinessline.utils.AppUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AdsBaseActivity {

    public static final String SCREEN_TYPE = "screen_type";
    public static final int SIGN_IN = 0;
    public static final int SIGN_UP = 1;
    private int loadScreen = 0;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        setBottomNavigationViewSelectedItem(R.id.menu_portfolio);
        AppUtils.disableShiftMode(mBottomNavigationView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbar.setLogo(null);
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        mToolbar.setSubtitle("");


        Intent intent = getIntent();
        if (intent.hasExtra(SCREEN_TYPE)) {
            loadScreen = intent.getIntExtra(SCREEN_TYPE, 0);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (loadScreen) {
            case SIGN_IN:
                SignInFragment fragment = SignInFragment.newInstance();
                fragmentTransaction.replace(R.id.login_fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case SIGN_UP:
                SignUpFragment signUpFragment = SignUpFragment.newInstance();
                fragmentTransaction.replace(R.id.login_fragment_container, signUpFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
        setBottomNavigationViewSelectedItem(R.id.menu_portfolio);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setBottomNavigationViewSelectedItem(R.id.menu_portfolio);
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
}

