package com.mobstac.thehindubusinessline.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import com.mobstac.thehindubusinessline.model.PersonalizeTable;
import com.mobstac.thehindubusinessline.model.SectionBasicData;
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
 * Created by zhy on 15/9/10.
 */
public class CustomizeNewsFeedFragment extends Fragment {
    private TagFlowLayout mFlowLayout;
    private TagAdapter<SectionBasicData> mAdapter;
    private List<SectionBasicData> mSectionList = new ArrayList<>();
    private boolean isCity;
    private CustomizeHomeScreenActivity mMainActivity;
    private List<String> mSelectedSections = new ArrayList<>();
    private List<String> mSelectedSectionPosition = new ArrayList<>();


    public static CustomizeNewsFeedFragment newInstance(boolean isCity) {
        CustomizeNewsFeedFragment fragment = new CustomizeNewsFeedFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_CITY, isCity);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        Log.d(TAG, "onAttach: ");
        mMainActivity = (CustomizeHomeScreenActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isCity = getArguments().getBoolean(Constants.IS_CITY);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        /** get the list of section in a dummy list*/
        RealmResults<SectionTable> mSectionTables = DatabaseJanitor.getSectionNewsFeed();

        /** iterating the dummy list to fill the mSectionList */
        for (int i = 0; i < mSectionTables.size(); i++) {
            mSectionList.add(new SectionBasicData(mSectionTables.get(i).getSecId(), mSectionTables.get(i).getSecName()));
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_flowlayout);
        if (mSectionList != null && mSectionList.size() > 0) {
            mFlowLayout.setAdapter(mAdapter = new TagAdapter<SectionBasicData>(mSectionList) {

                @Override
                public View getView(FlowLayout parent, int position, SectionBasicData s) {
                    View view = mInflater.inflate(R.layout.customize_news_feed_item, mFlowLayout, false);
                    TextView tv = (TextView) view.findViewById(R.id.news_feed_section_name);
                    tv.setText(s.getSectionName());
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

        List<String> mSecIdList = new ArrayList<>();
        for (int i = 0; i < mSectionList.size(); i++) {
            mSecIdList.add(String.valueOf(mSectionList.get(i).getSecId()));
        }

        mSelectedSections = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED);
        mSelectedSectionPosition = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED_POSITION);
        ArrayList<Integer> mCitiesIndex = new ArrayList<>();
        if (mSelectedSections.size() > 0) {
            for (int i = 0; i < mSelectedSections.size(); i++) {
                int index = mSecIdList.indexOf(mSelectedSections.get(i));
                if (index >= 0) {
                    mCitiesIndex.add(mSecIdList.indexOf(mSelectedSections.get(i)));
                }
            }
        } else {
            List<PersonalizeTable> mDefaultSections = DatabaseJanitor.getPersonalizeTable();
            for (PersonalizeTable mPersonalizeTable : mDefaultSections) {
                int index = mSecIdList.indexOf(String.valueOf(mPersonalizeTable.getSecId()));
                if (index >= 0) {
                    mSelectedSections.add(String.valueOf(mPersonalizeTable.getSecId()));
                    mCitiesIndex.add(mSecIdList.indexOf(String.valueOf(mPersonalizeTable.getSecId())));
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
                       /* mSelectedSections.add(String.valueOf(mSectionList.get(position).getSecId()));
                        mSelectedSectionPosition.add(String.valueOf(position));*/
                    } else {
//                        imageView.setImageResource(R.mipmap.plus);
                        mSectionTextView.setTextColor(getResources().getColor(R.color.color_customize_text_normal));
                        /*mSelectedSections.remove(String.valueOf(mSectionList.get(position).getSecId()));
                        mSelectedSectionPosition.remove(String.valueOf(position));*/
                    }
                }
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                getActivity().setTitle("choose:" + selectPosSet.toString());
                setSelectedFeeds(selectPosSet);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Customize news feed: Back button clicked",
                        getString(R.string.customize_news_feed_menu));
                FlurryAgent.logEvent("Customize news feed: Back button clicked");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "Customize News Feed Screen");
        FlurryAgent.logEvent("Customize News Feed Screen");
        FlurryAgent.onPageView();
    }


    private void setSelectedFeeds(Set<Integer> selectPosSet) {

        mSelectedSections.clear();
        mSelectedSectionPosition.clear();
        if (selectPosSet.size() > 0) {
            for (int s : selectPosSet) {
                mSelectedSections.add(String.valueOf(mSectionList.get(s).getSecId()));
                mSelectedSectionPosition.add(String.valueOf(s));
            }
        }

    }


    private boolean isFragmentVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mMainActivity != null) {
            mMainActivity.setNextButtonText("NEXT");
            mMainActivity.setVisiblityOfPriviousButton(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFragmentVisibleToUser && mMainActivity != null) {
            mMainActivity.setNextButtonText("Save");
            mMainActivity.setVisiblityOfPriviousButton(View.GONE);
        }
    }

    public void nextButtonClicked() {
        if (mSelectedSections != null && mSelectedSections.size() < 5) {
            Toast.makeText(mMainActivity, getString(R.string.select_atleast_five_section), Toast.LENGTH_LONG).show();
            return;
        }

        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                getActivity(),
                getString(R.string.ga_action),
                "Customize news feed: Save button clicked",
                getString(R.string.customize_news_feed_menu));
        FlurryAgent.logEvent("Customize news feed: Save button clicked");
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED,
                mSelectedSections);
        SharedPreferenceHelper.setStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED_POSITION,
                mSelectedSectionPosition);
        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                true);
        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.PREFERENCES_HOME_REFRESH,
                true);

       /* if (mMainActivity != null) {
            mMainActivity.setViewPagerItem(1);
        }
*/
        mMainActivity.finish();
    }
}
