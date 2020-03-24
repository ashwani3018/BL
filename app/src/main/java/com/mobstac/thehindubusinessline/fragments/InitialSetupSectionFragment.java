package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.InitialSetupActivity;
import com.mobstac.thehindubusinessline.activity.SplashActivity;
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
 * A simple {@link Fragment} subclass.
 */
public class InitialSetupSectionFragment extends Fragment {
    private TagFlowLayout mFlowLayout;
    private TagAdapter<SectionBasicData> mAdapter;
    private List<SectionBasicData> mSectionList = new ArrayList<>();
    private boolean isCity;
    private InitialSetupActivity mInitialSetupActivity;
    private List<String> mSelectedFeeds = new ArrayList<>();
    private List<String> mSelectedFeedsPosition = new ArrayList<>();
    private boolean isFragmentVisibleToUser;

    public InitialSetupSectionFragment() {
        // Required empty public constructor
    }

    public static InitialSetupSectionFragment newInstance(boolean isCity) {
        InitialSetupSectionFragment fragment = new InitialSetupSectionFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IS_CITY, isCity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        Log.d(TAG, "onAttach: ");
        mInitialSetupActivity = (InitialSetupActivity) activity;
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

        View mRootView = inflater.inflate(R.layout.fragment_initial_setup_section, container, false);

        /** get the list of section in a dummy list*/
        RealmResults<SectionTable> mSectionTables = DatabaseJanitor.getSectionNewsFeed();

        /** iterating the dummy list to fill the mSectionList */
        for (int i = 0; i < mSectionTables.size(); i++) {
            mSectionList.add(new SectionBasicData(mSectionTables.get(i).getSecId(),
                    mSectionTables.get(i).getSecName()));
        }

        /** get the list of widgets */
//        List<WidgetsTable> mWidgetsList = DatabaseJanitor.getWidgetsTable();

//        List<SectionBasicData> mWidgetsTempList = new ArrayList<>();


        /** iterate the list to change the type of data to fill it in our own list */
//        for (int i = 0; i < mWidgetsList.size(); i++) {
//            mWidgetsTempList.add(new SectionBasicData(
//                    mWidgetsList.get(i).getSecId(),
//                    mWidgetsList.get(i).getSecName()
//            ));
//        }

        /**
         * removing widget list(mWidgetsList) from sectionlist(mSectionList) to show it in UI
         * */
//        for (int i = 0; i < mWidgetsTempList.size(); i++) {
//            if (mSectionList.contains(mWidgetsTempList.get(i))) {
//                Log.d("DISPs", "onCreateView: " + mSectionList);
//                mSectionList.remove(mWidgetsTempList.get(i));
//            }
//        }

        /** removing the topnews from the sectionlist */
//        for (SectionBasicData mBasicData : mSectionList) {
//            if (mBasicData.getSecId() == 43) {
//                mSectionList.remove(mBasicData);
//            }
//        }

        // Save and skip button Typecast
//        mSaveButton = (Button) mRootView.findViewById(R.id.button_feed_save);
//        mSkipButton = (Button) mRootView.findViewById(R.id.button_feed_skip);

        // sets the Firasans Regular ttf to save and skip button
//        mSaveButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/FiraSans-Regular.ttf"));
//        mSkipButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/FiraSans-Regular.ttf"));

/*
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                */
/**
 * The Constants.IS_INTEREST_SELECTED is used to determine
 * if user has finished the OnBoarding screen
 * it is set to true , since user skipped it
 * *//*

                SharedPreferenceHelper.putBoolean(
                        getActivity() ,
                        Constants.IS_INTEREST_SELECTED ,
                        true);
                */
/**
 * Constants.PREFERENCES_FETCH_PERSONALIZE_FEED is used to determine
 * if the section's id sent to server is decided by user or admin
 * if true -- user selected section
 * if false -- admin selected section
 *//*

                SharedPreferenceHelper.putBoolean(
                        getActivity(),
                        Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                        false);
                // start the SplashActivity -- since the user skipped the onBoarding sequence
                startActivity(new Intent(getActivity() , SplashActivity.class));
                // finish the current(InitialSetupActivity) Page
                mInitialSetupActivity.finish();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                */
/**
 * Users need to select atleast 5 sections , if user fails
 * the toast is shown
 *//*

                if (mSelectedFeeds != null && mSelectedFeeds.size() < 5) {
//                    Snackbar.make(mFlowLayout , getString(R.string.select_atleast_five_section) , Snackbar.LENGTH_LONG).show();
                    Toast.makeText(mInitialSetupActivity, getString(R.string.select_atleast_five_section), Toast.LENGTH_LONG).show();
                    return;
                }
                // saves the selected sections and section index to shared preferences
                saveUserData();
                // set the viewpager item to 1(InitialSetupCityFragment)
                InitialSetupActivity.mOnBoardingViewPager.setCurrentItem(1);
            }
        });
*/

        return mRootView;
    }

    /**
     * This method does the following
     * 1.Logs the Google analytics tracker and Flurry
     * 2.Saves the selected sections and its index in sharedpreference
     */
    public void saveUserData() {
        if (mInitialSetupActivity != null) {
            GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                    getActivity(),
                    getString(R.string.ga_action),
                    "Customize news feed: Save button clicked",
                    getString(R.string.customize_news_feed_menu));
            FlurryAgent.logEvent("Customize news feed: Save button clicked");
            SharedPreferenceHelper.setStringArrayPref(
                    getActivity(),
                    Constants.PREFERENCES_NEWS_FEED,
                    mSelectedFeeds);
            SharedPreferenceHelper.setStringArrayPref(
                    getActivity(),
                    Constants.PREFERENCES_NEWS_FEED_POSITION,
                    mSelectedFeedsPosition);

            SharedPreferenceHelper.putBoolean(
                    getActivity(),
                    Constants.PREFERENCES_HOME_REFRESH,
                    true);
            SharedPreferenceHelper.putBoolean(
                    getActivity(),
                    Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                    true);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_flowlayout);
        //mFlowLayout.setMaxSelectCount(3);
        if (mSectionList != null && mSectionList.size() > 0) {
            mFlowLayout.setAdapter(mAdapter = new TagAdapter<SectionBasicData>(mSectionList) {

                @Override
                public View getView(FlowLayout parent, int position, SectionBasicData s) {
                    View view = mInflater.inflate(R.layout.onboarding_item, mFlowLayout, false);
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

        mSelectedFeeds = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED);
        mSelectedFeedsPosition = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_NEWS_FEED_POSITION);
        ArrayList<Integer> mCitiesIndex = new ArrayList<>();
        if (mSelectedFeeds.size() > 0) {
            for (int i = 0; i < mSelectedFeeds.size(); i++) {
                int index = mSecIdList.indexOf(mSelectedFeeds.get(i));
                if (index >= 0) {
                    mCitiesIndex.add(mSecIdList.indexOf(mSelectedFeeds.get(i)));
                }
            }
        } else {
            List<PersonalizeTable> mDefaultSections = DatabaseJanitor.getPersonalizeTable();
            for (PersonalizeTable mPersonalizeTable : mDefaultSections) {
                int index = mSecIdList.indexOf(String.valueOf(mPersonalizeTable.getSecId()));
                if (index >= 0) {
                    mSelectedFeeds.add(String.valueOf(mPersonalizeTable.getSecId()));
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
                setSelectedFeeds(selectPosSet);
            }
        });

    }

    /**
     * method saves the selected sections and its index in list
     *
     * @param selectPosSet
     */
    private void setSelectedFeeds(Set<Integer> selectPosSet) {
        mSelectedFeeds.clear();
        mSelectedFeedsPosition.clear();
        if (selectPosSet.size() > 0) {
            for (int s : selectPosSet) {
                mSelectedFeeds.add(String.valueOf(mSectionList.get(s).getSecId()));
                mSelectedFeedsPosition.add(String.valueOf(s));
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mInitialSetupActivity != null) {
            mInitialSetupActivity.setNextButtonText("Next");
            mInitialSetupActivity.setSkipButtonText("Skip");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFragmentVisibleToUser && mInitialSetupActivity != null) {
            mInitialSetupActivity.setNextButtonText("Done");
            mInitialSetupActivity.setSkipButtonText("Skip");
        }
    }

    public void saveButtonClicked() {
        if (mSelectedFeeds != null && mSelectedFeeds.size() < 5) {
            Toast.makeText(mInitialSetupActivity, getString(R.string.select_atleast_five_section), Toast.LENGTH_LONG).show();
            return;
        }
        saveUserData();
      /*  if (mInitialSetupActivity != null) {
            mInitialSetupActivity.setViewPagerItem(1);
        }*/

        SharedPreferenceHelper.putBoolean(
                getActivity(),
                Constants.IS_INTEREST_SELECTED,
                true
        );
        Intent mIntent = new Intent(getActivity(), SplashActivity.class);
        startActivity(mIntent);
        mInitialSetupActivity.finish();

    }
}
