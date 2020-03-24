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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.activity.ReadLaterActivity;
import com.mobstac.thehindubusinessline.adapter.SlidingFragmentAdapter;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.listener.HomeButtonClickedListener;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.view.CustomTabLayout;
import com.mobstac.thehindubusinessline.view.CustomViewPager;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;


public class SlidingSectionFragment extends BaseFragment implements HomeButtonClickedListener , DFPConsent.ConsentSelectionListener{

    static final String TAG = "SlidingSectionFragment";
    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "subsection";
    private static final String ARG_PARAM3 = "subSectionId";
    private static final String ARG_PARAM4 = "parentSectionName";
    public static final String FROM_NORMAL = "fromNormal";

    public static CustomViewPager mViewPager;
    private RealmResults<SectionTable> mSectionTables;
    private int clickedPosition;
    private boolean isSubsection;
    private long parentSectionId;
    private String parentSectionName;
    private MainActivity mMainActivity;
    private SlidingFragmentAdapter mSlidingFragmentAdapter;
    private CustomTabLayout mTabLayout;
    private String mFrom;

    public static SlidingSectionFragment newInstance(String from, int clickedPosition, boolean isSubsection, long parentSectionId, String parentSectionName) {
        SlidingSectionFragment fragment = new SlidingSectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, clickedPosition);
        args.putBoolean(ARG_PARAM2, isSubsection);
        args.putLong(ARG_PARAM3, parentSectionId);
        args.putString(ARG_PARAM4, parentSectionName);
        args.putString("from", from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clickedPosition = getArguments().getInt(ARG_PARAM1);
            isSubsection = getArguments().getBoolean(ARG_PARAM2);
            parentSectionId = getArguments().getLong(ARG_PARAM3);
            parentSectionName = getArguments().getString(ARG_PARAM4);
            mFrom = getArguments().getString("from");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sliding_section, container, false);
        if (mMainActivity != null) {
            mMainActivity.showBottomNavigationBar(true);
            mMainActivity.setHomeButtonClickedListener(this);
        }
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (CustomTabLayout) view.findViewById(R.id.tabs);
        if (!isSubsection) {
            mSectionTables = DatabaseJanitor.getTopScrollSection();
            mMainActivity.setToolbarLogo(R.mipmap.logo_actionbar);
            mMainActivity.setToolbarHomeButton();
            mMainActivity.setToolbarTitle(null);
        } else {
            mSectionTables = DatabaseJanitor.getSectionList(parentSectionId);
            mMainActivity.hideToolbarLogo();
            mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
            mMainActivity.setToolbarTitle(parentSectionName);
        }
        mSectionTables.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SectionTable>>() {
            @Override
            public void onChange(RealmResults<SectionTable> sectionTables, OrderedCollectionChangeSet changeSet) {
                if (sectionTables.size() > 0) {
                    mSlidingFragmentAdapter = new SlidingFragmentAdapter(getChildFragmentManager(), mSectionTables, isSubsection, mMainActivity);
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
        Log.i(TAG, "onCreateView: " + parentSectionName);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppUtils.hideKeyboard(mViewPager);
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        return view;
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
        if (menu != null) {
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.action_overflow).setVisible(true);
            menu.findItem(R.id.action_bookmarks).setVisible(false);

            if (!isSubsection) {
                mMainActivity.setToolbarLogo(R.mipmap.logo_actionbar);
                mMainActivity.setToolbarHomeButton();
                mMainActivity.setToolbarTitle("");
            } else {
                mMainActivity.hideToolbarLogo();
                mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
                mMainActivity.setToolbarTitle(parentSectionName);
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FlurryAgent.logEvent(getString(R.string.ga_sidebar_category));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), getString(R.string.ga_sidebar_category), "Section Fragment");
                if (!isSubsection) {
                    mMainActivity.toggleDrawer();
                } else {
                    mMainActivity.popTopFragmentFromBackStack();
                }
                return true;
            case R.id.action_bookmarks:
                FlurryAgent.logEvent(getString(R.string.ga_bookmark_screen_button_clicked));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), getString(R.string.ga_bookmark_screen_button_clicked), "Section Fragment");
                Intent intent = new Intent(mMainActivity, ReadLaterActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
//        mMainActivity.hideRosBottomBanner();
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
    public void homeButtonClicked() {
        if (isSubsection) {
            Log.i(TAG, "homeButtonClicked: SubSection");
            mMainActivity.popTopFragmentFromBackStack();
        } else {
            Log.i(TAG, "homeButtonClicked: MainSection");
            if (mViewPager != null) {
                mViewPager.setCurrentItem(0);
            }
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
        if(getActivity() == null) {
            return;
        }
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
