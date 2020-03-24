package com.mobstac.thehindubusinessline.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.gson.JsonElement;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.adapter.SearchAdapter;
import com.mobstac.thehindubusinessline.adapter.SearchArticleAdapter;
import com.mobstac.thehindubusinessline.fragments.BookmarksFragment;
import com.mobstac.thehindubusinessline.fragments.SearchLandingFragment;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.CompanyData;
import com.mobstac.thehindubusinessline.model.SearchSectionContentModel;
import com.mobstac.thehindubusinessline.model.SectionConentModel;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.LotameAppTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller.SEARCH_BY_ARTICLE_TITLE_URL;

public class SearchActivity extends AdsBaseActivity {

    private static final String TAG = "SearchActivity";

    private Toolbar mToolbar;
    private LinearLayout vSearchPopUpMenu;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_search;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean checkPopTitle = getIntent().getBooleanExtra("search_key", false);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(null);
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        mToolbar.setLogo(null);

        Bundle bundle = new Bundle();
        bundle.putBoolean("search_title_show", checkPopTitle);

        final SearchLandingFragment searchLandingFragment = SearchLandingFragment.getInstance(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_landing_frameId, searchLandingFragment).addToBackStack(null).commit();


        vSearchPopUpMenu = findViewById(R.id.search_popup_menuId);

        final TextView searchTitleClick = mToolbar.findViewById(R.id.search_menu_title);

        ((ImageView) mToolbar.findViewById(R.id.icon_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSearchPopUpMenu.setVisibility(View.VISIBLE);
            }
        });

        if (checkPopTitle) {
            searchTitleClick.setText(getResources().getString(R.string.search_article_title));
        } else {
            searchTitleClick.setText(getResources().getString(R.string.search_stock_title));
        }

        ((TextView) findViewById(R.id.menu_articleId)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSearchPopUpMenu.setVisibility(View.GONE);
                searchTitleClick.setText(getResources().getString(R.string.search_article_title));
                searchLandingFragment.setEditValues(true);
            }
        });

        ((TextView) findViewById(R.id.menu_marketId)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSearchPopUpMenu.setVisibility(View.GONE);
                searchTitleClick.setText(getResources().getString(R.string.search_stock_title));
                searchLandingFragment.setEditValues(false);
            }
        });


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Log.d(TAG, "navigation clicked");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
}
