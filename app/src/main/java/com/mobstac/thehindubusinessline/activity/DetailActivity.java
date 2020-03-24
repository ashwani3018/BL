package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.ArticleDetailFragment_new;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

/**
 * Created by ashwani on 5/22/17.
 */

public class DetailActivity extends AdsBaseActivity {
    public static final String ARTICLE_ID = "articleid";
    public static final String SECTION_ID = "sectionid";
    public static final String ARTICLE_LINKS = "articlelink";
    public static final String IS_FROM_PAGER = "is_frompager";
    public static final String ARTICLE_DETAILS = "articledetailss";
    private String TAG = "DetailActivity";
    private int mArticleID;
    private String mSectionId;
    private String mArticleLink;
    private ArticleDetail mArticleDetail;
    private boolean isFromPager;
    private FrameLayout mFrameLayout;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
        if (getIntent() != null) {
            mArticleID = getIntent().getIntExtra(ARTICLE_ID, 0);
            mSectionId = getIntent().getStringExtra(SECTION_ID);
            mArticleLink = getIntent().getStringExtra(ARTICLE_LINKS);
            isFromPager = getIntent().getBooleanExtra(IS_FROM_PAGER, false);
            mArticleDetail = getIntent().getParcelableExtra(ARTICLE_DETAILS);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setSubtitle("");
            mToolbar.setNavigationIcon(R.mipmap.arrow_back);
            mToolbar.setLogo(null);
        }


        mFrameLayout = (FrameLayout) findViewById(R.id.parentLayout);

        addDetailFragment();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setSubtitle("");
            mToolbar.setLogo(null);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return false;
            case R.id.action_bookmarks:
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setSubtitle("");
            mToolbar.setLogo(null);
        }
        return super.onPrepareOptionsMenu(menu);
    }
/*
    public void setToolbarTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
//            invalidateOptionsMenu();
        }
    }*/

    private void addDetailFragment() {
        ArticleDetailFragment_new linkifyFragment = ArticleDetailFragment_new.newInstance(mArticleID, mSectionId, mArticleLink,
                isFromPager, mArticleDetail);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.FRAME_CONTENT, linkifyFragment).commit();
    }


    public void showSnackBar() {
        Snackbar mSnackbar = Snackbar.make(mFrameLayout, "", BaseTransientBottomBar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView textView1 = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }


}
