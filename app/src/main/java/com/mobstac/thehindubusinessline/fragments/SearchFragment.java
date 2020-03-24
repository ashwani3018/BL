package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.adapter.SearchAdapter;
import com.mobstac.thehindubusinessline.model.CompanyData;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.LotameAppTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;

import static android.support.v4.view.MenuItemCompat.getActionView;

public class SearchFragment extends BaseFragment {
    private MainActivity mMainActivity;
    private List<CompanyData> mSearchList;
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private TextView mNoArticleFoundTextview;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mMainActivity != null){
            mMainActivity.showBottomNavigationBar(false);
        }
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.searchRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mRecyclerView.setHasFixedSize(true);
        if (mSearchList != null && mSearchAdapter != null) {
            mRecyclerView.setAdapter(mSearchAdapter);
        } else {
            mSearchList = new RealmList<>();
            mSearchAdapter = new SearchAdapter(mMainActivity, mSearchList);
            mRecyclerView.setAdapter(mSearchAdapter);
        }
        mNoArticleFoundTextview = (TextView) view.findViewById(R.id.noArticle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity.createBannerAdRequest(true, false, "");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
//            menu.clear();
            inflater.inflate(R.menu.menu, menu);

            MenuItem item = menu.findItem(R.id.search);
            searchView = (SearchView) getActionView(item);
            searchView.setIconified(false);
            searchView.onActionViewExpanded();
            View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            ImageView closeButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            closeButton.setImageDrawable(AppCompatResources.getDrawable(mMainActivity, R.mipmap.close));
            SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchAutoComplete.setHint(getString(R.string.search_title));
            searchAutoComplete.setTextColor(getResources().getColor(R.color.Black));
            searchAutoComplete.setHintTextColor(getResources().getColor(R.color.search_hint));
            v.setBackgroundColor(Color.WHITE);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (isAdded() && getActivity() != null && NetworkUtils.isNetworkAvailable(mMainActivity)) {
                        try {
                            LotameAppTracker.sendDataToLotameAnalytics(getActivity(), getString(R.string.la_action), getString(R.string.la_search_colon) + query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        new SearchArticleTask().execute(query);
                        getSearchedData(query);
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Search Text",
                                "Searched Text: " + query, "Search Fragment");
                        FlurryAgent.logEvent("Searched Text: " + query);
                    } else {
                        Toast.makeText(mMainActivity, "No network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (isAdded() && getActivity() != null && NetworkUtils.isNetworkAvailable(mMainActivity)) {
                        try {
                            LotameAppTracker.sendDataToLotameAnalytics(getActivity(), getString(R.string.la_action), getString(R.string.la_search_colon) + newText);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        new SearchArticleTask().execute(query);
                        getSearchedData(newText);
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Search Text",
                                "Searched Text: " + newText, "Search Fragment");
                        FlurryAgent.logEvent("Searched Text: " + newText);
                    } else {
                        Toast.makeText(mMainActivity, "No network connectivity", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
    }


    private void getSearchedData(String searchText) {
        if (searchText.toString().equals("")) {
            mSearchAdapter.updateSearchedList(mSearchList);
            if (mSearchList.size() <= 0) {
//               showNoBookmarkView(false);
                searchView.clearFocus();
                mRecyclerView.setVisibility(View.GONE);
                mNoArticleFoundTextview.setVisibility(View.VISIBLE);
            } else {
                mNoArticleFoundTextview.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            Realm mRealm = Businessline.getRealmInstance();
            OrderedRealmCollection<CompanyData> bookmarkList = mRealm.where(CompanyData.class)
                    .contains("name", searchText, Case.INSENSITIVE).findAll();

            mSearchAdapter.updateSearchedList(bookmarkList);
            mNoArticleFoundTextview.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMainActivity != null) {
            mMainActivity.setToolbarTitle(null);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FlurryAgent.logEvent("Search: Back button clicked");
            GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                    getActivity(),
                    getString(R.string.ga_action),
                    "Search: Back button clicked",
                    "Search Fragment");
            mMainActivity.popTopFragmentFromBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.setToolbarBackground(getResources().getColor(R.color.toolbar_background_color));
        mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(mMainActivity, "Search Screen");
        try {
            LotameAppTracker.sendDataToLotameAnalytics(getActivity(), getString(R.string.la_action), getString(R.string.la_search));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FlurryAgent.logEvent(getString(R.string.flurry_search_view));
        FlurryAgent.onPageView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMainActivity != null) {
            mMainActivity.setToolbarBackground(getResources().getColor(R.color.toolbar_background_color));
            AppUtils.hideKeyBoard(mMainActivity, searchView);
        }
    }

    @Override
    public void getDataFromDB() {

    }

    @Override
    public void showLoadingFragment() {

    }

    @Override
    public void hideLoadingFragment() {

    }
}
