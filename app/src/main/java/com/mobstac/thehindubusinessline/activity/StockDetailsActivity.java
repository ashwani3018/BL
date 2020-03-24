package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.GetQuoteFragment;
import com.mobstac.thehindubusinessline.fragments.StockDetailsFragment;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;


public class StockDetailsActivity extends AdsBaseActivity {

    private Toolbar mToolbar;
    private FragmentTabHost mTabHost;
    private TextView mCompanyNameTextView;

    @Override
    public int getLayoutRes() {
        return R.layout.stock_details_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mCompanyNameTextView = (TextView) findViewById(R.id.companyName);
        int current_tab = 0;
        if (!getIntent().hasExtra("bseCode")) {
            Bundle arguments = new Bundle();
            current_tab = getIntent().getIntExtra("tag", 0);
            arguments.putInt("tag", 0);
            arguments.putString("sortBy", "percentageChange");
            arguments.putString("sortOrder", "desc");
            mTabHost.addTab(mTabHost.newTabSpec("bse").setIndicator("BSE"),
                    StockDetailsFragment.class, arguments);
            arguments = new Bundle();
            arguments.putInt("tag", 1);
            arguments.putString("sortBy", "percentageChange");
            arguments.putString("sortOrder", "desc");
            mTabHost.addTab(mTabHost.newTabSpec("nse").setIndicator("NSE"),
                    StockDetailsFragment.class, arguments);
        } else {
            int bse_code = 0;
            String nse_symbol = "";
            String companyId = "";
            String companyName = "";
            Bundle b = getIntent().getExtras();
            if (b != null) {
                bse_code = b.getInt("bseCode");
                nse_symbol = b.getString("nseSymbol");
                current_tab = b.getInt("tag", 0);
                companyId = b.getString("companyId");
                companyName = b.getString("companyName");
            }
            Bundle arguments = new Bundle();
            arguments.putInt("bseCode", bse_code);
            arguments.putString("nseSymbol", nse_symbol);
            arguments.putString("tab", "BSE");
            arguments.putString("companyId", companyId);
            mTabHost.addTab(mTabHost.newTabSpec("bse").setIndicator("BSE"),
                    GetQuoteFragment.class, arguments);
            arguments = new Bundle();

            arguments.putString("tab", "NSE");
            arguments.putInt("bseCode", bse_code);
            arguments.putString("nseSymbol", nse_symbol);
            arguments.putString("companyId", companyId);
            mTabHost.addTab(mTabHost.newTabSpec("nse").setIndicator("NSE"),
                    GetQuoteFragment.class, arguments);
            mCompanyNameTextView.setText(companyName);
        }
        mTabHost.setCurrentTab(current_tab);
        boolean b = SharedPreferenceHelper.getBoolean(StockDetailsActivity.this, Constants.DAY_MODE, false);
        int textColorCode;
        if (b) {
            textColorCode = Color.WHITE;
        } else {
            textColorCode = Color.BLACK;
        }

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(textColorCode);
        }

        createBannerAdRequest(true, false, "");
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mToolbar != null) {
            mToolbar.setTitle(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.search:
//                startActivity(new Intent(StockDetailsActivity.this, SearchActivity.class));
                Intent intent =new Intent(this, SearchActivity.class);
                intent.putExtra("search_key", true);
                startActivity(intent);
                return true;
            case R.id.refresh:
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }
}
