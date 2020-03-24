/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobstac.thehindubusinessline.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.activity.BLInkActivity;
import com.mobstac.thehindubusinessline.adapter.SlidingSectionFragmentAdapter;
import com.mobstac.thehindubusinessline.database.ApiCallHandler;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.view.CustomTabLayout;
import com.mobstac.thehindubusinessline.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;


public class SlidingPortfolioSectionFragment extends Fragment implements DFPConsent.ConsentSelectionListener{

    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "fromSection";
    private static final String ARG_PARAM3 = "parentSectionName";
    private final String TAG = "BL_SlidingPortfolioSectionFragment";
    private CustomViewPager mViewPager;
    private RealmResults<SectionTable> mSectionTables;
    private int clickedPosition;
    private String parentSectionName;
    private String fromSection;
    private AdsBaseActivity mMainActivity;
    private SlidingSectionFragmentAdapter mSlidingFragmentAdapter;
    private CustomTabLayout mTabLayout;
    private boolean isFromPortfolioScreen;

    public static SlidingPortfolioSectionFragment newInstance(int clickedPosition, String parentSectionName, String fromSection) {
        SlidingPortfolioSectionFragment fragment = new SlidingPortfolioSectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, clickedPosition);
        args.putString(ARG_PARAM3, parentSectionName);
        args.putString(ARG_PARAM2, fromSection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (AdsBaseActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (AdsBaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clickedPosition = getArguments().getInt(ARG_PARAM1);
            parentSectionName = getArguments().getString(ARG_PARAM3);
            fromSection = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_section, container, false);
        if (mMainActivity != null) {
            mMainActivity.showBottomNavigationBar(true);
        }
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (CustomTabLayout) view.findViewById(R.id.tabs);

        mSectionTables = DatabaseJanitor.getSectionUsingFromKey(fromSection);
        mMainActivity.hideToolbarLogo();
        mMainActivity.hideHomeButton();
        mMainActivity.setToolbarTitle(parentSectionName);

        if (fromSection.equalsIgnoreCase(ApiCallHandler.FROM_PORTFOLIO)) {
            isFromPortfolioScreen = true;
        } else {
            isFromPortfolioScreen = false;
        }

        mSectionTables.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SectionTable>>() {
            @Override
            public void onChange(RealmResults<SectionTable> sectionTables, OrderedCollectionChangeSet changeSet) {
                if (sectionTables.size() > 0) {
//                    if (mSlidingFragmentAdapter == null) {
                    mSlidingFragmentAdapter = new SlidingSectionFragmentAdapter(mMainActivity,getChildFragmentManager(), mSectionTables, isFromPortfolioScreen);
//                    }
                    mViewPager.setAdapter(mSlidingFragmentAdapter);
                    if (mSectionTables != null) {
                        if (mSectionTables.size() > 3) {
                            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        } else {
                            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                        }
                    }
                    mViewPager.setCurrentItem(clickedPosition);
                    mTabLayout.setupWithViewPager(mViewPager);
                }
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                clickedPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mMainActivity != null) {
            mMainActivity.updateOverFlowMenuActionButton();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        mMainActivity.hideToolbarLogo();
        mMainActivity.hideHomeButton();
        mMainActivity.setToolbarTitle(parentSectionName);
        List<Integer> mHideMenu = new ArrayList<>();
        /* Profile icon should not be visible - requirement from TH Team
        if (isFromPortfolioScreen) {
            mHideMenu.add(R.id.action_user);
        }*/
        mHideMenu.add(R.id.search);
        mHideMenu.add(R.id.action_overflow);

        mMainActivity.showMenus(mHideMenu, menu);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FlurryAgent.logEvent(getString(R.string.ga_sidebar_category));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), getString(R.string.ga_sidebar_category), "Section Fragment");
                mMainActivity.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSectionTables.removeAllChangeListeners();
    }

    public void setPagerPosition(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // DFP Consent Check
        setupDFPConsent();
    }

    @Override
    public void isUserInEurope(boolean isInEurope) {
        if(getActivity() != null) {
            DFPConsent dfpConsent = new DFPConsent();
            dfpConsent.initUserConsentForm(getActivity());
        }
    }

    private void setupDFPConsent() {
        // For Testing and Debugging
        DFPConsent.GDPR_Testing(getActivity());

        DFPConsent dfpConsent = new DFPConsent();
        dfpConsent.setConsentSelectionListener(this);
        dfpConsent.init(getActivity(), false);
    }

    public void changePagerAtIndex(int pos) {
        mViewPager.setCurrentItem(pos);
    }

}
