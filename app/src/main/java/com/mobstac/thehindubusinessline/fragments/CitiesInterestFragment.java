package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.CustomizeHomeScreenActivity;
import com.mobstac.thehindubusinessline.adapter.TagAdapter;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.mobstac.thehindubusinessline.view.FlowLayout;
import com.mobstac.thehindubusinessline.view.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesInterestFragment extends Fragment {

    private TagFlowLayout mFlowLayout;
    private TagAdapter<SectionTable> mAdapter;
    private RealmResults<SectionTable> mSectionList;
    private boolean isCity;
    private CustomizeHomeScreenActivity mMainActivity;
    private List<String> mSelectedCities = new ArrayList<>();
    private List<String> mSelectedCitiesName = new ArrayList<>();
    private List<String> mSelectedCitiesPosition = new ArrayList<>();
    private boolean isFragmentVisibleToUser;

    public static CitiesInterestFragment newInstance(boolean isCity) {
        CitiesInterestFragment fragment = new CitiesInterestFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_CITY, isCity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (CustomizeHomeScreenActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (CustomizeHomeScreenActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isCity = getArguments().getBoolean(Constants.IS_CITY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.fragment_cities_interest, container, false);

        mSectionList = DatabaseJanitor.getRegionalList();


        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout_city_interest);
        //mFlowLayout.setMaxSelectCount(3);
        if (mSectionList != null && mSectionList.size() > 0) {
            mFlowLayout.setAdapter(mAdapter = new TagAdapter<SectionTable>(mSectionList) {

                @Override
                public View getView(FlowLayout parent, int position, SectionTable s) {
                    View view = mInflater.inflate(R.layout.customize_news_feed_item, mFlowLayout, false);
                    TextView tv = (TextView) view.findViewById(R.id.news_feed_section_name);
                    tv.setText(s.getSecName());
//                    ImageView imageView = (ImageView) view.findViewById(R.id.checkIcon);
                    if (mAdapter != null && mAdapter.getPreCheckedList() != null && mAdapter.getPreCheckedList().size() > 0) {
                        if (getPreCheckedList().contains(position)) {
//                            imageView.setImageResource(R.mipmap.check);
                            tv.setTextColor(getResources().getColor(android.R.color.white));
                        }
                    }
                    return view;
                }
            });
        }

        List<String> mCitySelectedList = new ArrayList<>();
        for (int i = 0; i < mSectionList.size(); i++) {
            mCitySelectedList.add(String.valueOf(mSectionList.get(i).getSecId()));
        }

        mSelectedCities = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST);
        mSelectedCitiesPosition = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST_POSITION);
        ArrayList<Integer> mCitiesIndex = new ArrayList<>();

        if (mSelectedCities.size() > 0) {
            for (int i = 0; i < mSelectedCities.size(); i++) {
                int index = mCitySelectedList.indexOf(mSelectedCities.get(i));
                if (index >= 0) {
                    mCitiesIndex.add(mCitySelectedList.indexOf(mSelectedCities.get(i)));
                }
            }
        }

        if (mAdapter != null) {
            mAdapter.setSelectedArrayList(mCitiesIndex);
        }
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mFlowLayout != null && mFlowLayout.getSelectedList() != null && mFlowLayout.getSelectedList().size() >= 0) {
//                    ImageView imageView = (ImageView) view.findViewById(R.id.checkIcon);
                    TextView mSectionTextView = (TextView) view.findViewById(R.id.news_feed_section_name);
                    if (mFlowLayout.getSelectedList().contains(position)) {
//                        imageView.setImageResource(R.mipmap.check);
                        mSectionTextView.setTextColor(getResources().getColor(android.R.color.white));
                    } else {
//                        imageView.setImageResource(R.mipmap.plus);
                        mSectionTextView.setTextColor(getResources().getColor(R.color.color_customize_text_normal));
                    }
                }
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                getActivity().setTitle("choose:" + selectPosSet.toString());
                setSelectedCities(selectPosSet);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Cities of Interest: Back button clicked",
                        getString(R.string.title_city_interest));
                FlurryAgent.logEvent("Cities of Interest: Back button clicked");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "City Interest Screen");
        FlurryAgent.logEvent("City interest screen");
        FlurryAgent.onPageView();
    }

    private void setSelectedCities(Set<Integer> selectPosSet) {

        mSelectedCities.clear();
        mSelectedCitiesPosition.clear();
        mSelectedCitiesName.clear();
        for (int s : selectPosSet) {
            mSelectedCities.add(String.valueOf(mSectionList.get(s).getSecId()));
            mSelectedCitiesPosition.add(String.valueOf(s));
            mSelectedCitiesName.add(mSectionList.get(s).getSecName());
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mMainActivity != null) {
            mMainActivity.setNextButtonText("SAVE");
            mMainActivity.setVisiblityOfPriviousButton(View.VISIBLE);
            mMainActivity.setSkipButtonText("Previous");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFragmentVisibleToUser && mMainActivity != null) {
            mMainActivity.setNextButtonText("SAVE");
            mMainActivity.setVisiblityOfPriviousButton(View.VISIBLE);
            mMainActivity.setSkipButtonText("Previous");
        }
    }

    public void saveButtonClicked() {
        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                getActivity(),
                getString(R.string.ga_action),
                "Cities of interest: Save button clicked",
                getString(R.string.title_city_interest));
        FlurryAgent.logEvent("Cities of interest: Save button clicked");
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST,
                mSelectedCities);
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST_POSITION,
                mSelectedCitiesPosition);
        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.PREFERENCES_HOME_REFRESH,
                true);
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST_NAME,
                mSelectedCitiesName);
        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                true);
       /* SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED,
                CustomizeNewsFeedFragment.mSelectedSections);
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED_POSITION,
                CustomizeNewsFeedFragment.mSelectedSectionPosition);*/
        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();
        mMainActivity.finish();
    }
}
