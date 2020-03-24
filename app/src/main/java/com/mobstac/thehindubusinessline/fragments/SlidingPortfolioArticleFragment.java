package com.mobstac.thehindubusinessline.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.adapter.SlidingPortfolioArticleAdapter;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

import java.util.ArrayList;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;


/**
 * Created by arvind on 30/8/16.
 */
public class SlidingPortfolioArticleFragment extends Fragment {

    private static final String ARTICLE_CLICKED_POSITON = "articleClickedPosition";
    private static final String ARTICLE_SECTION_ID = "articleSectionId";
    private static final String IS_PORTFOLIO = "isPortfolio";
    private final String TAG = "SlidingArticleFragment";
    private AdsBaseActivity mMainActivity;
    private ViewPager mViewPager;
    private ArrayList<ArticleDetail> mArticleList;
    private int articleClickedPosition;
    private String sectionId;
    private int scrollPageCount = 0;
    private int priviousArticlePosition;
    private RealmResults<ArticleBean> mSerctionArticleList;
    private boolean isFromPortfolioScreen;

    private OrderedRealmCollectionChangeListener<RealmResults<ArticleBean>> mOrderedRealmCollectionChangeListener = new OrderedRealmCollectionChangeListener<RealmResults<ArticleBean>>() {
        @Override
        public void onChange(RealmResults<ArticleBean> articleBeen, OrderedCollectionChangeSet changeSet) {
            ArrayList<ArticleDetail> mArticleData = ArticleUtil.convertArticleBeanListToArticleDetailList(articleBeen);
            if (mArticleData != null && mArticleData.size() > 0) {
                setupViewPager(mArticleData);
            }
        }
    };

    public static SlidingPortfolioArticleFragment newInstance(int articleClickedPosition,
                                                              String sectionId, boolean isFromPortfolioScreen) {

        SlidingPortfolioArticleFragment fragment = new SlidingPortfolioArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARTICLE_CLICKED_POSITON, articleClickedPosition);
        bundle.putString(ARTICLE_SECTION_ID, sectionId);
        bundle.putBoolean(IS_PORTFOLIO, isFromPortfolioScreen);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        mMainActivity = (AdsBaseActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach: ");
        mMainActivity = (AdsBaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        if (getArguments() != null) {
            articleClickedPosition = getArguments().getInt(ARTICLE_CLICKED_POSITON);
            sectionId = getArguments().getString(ARTICLE_SECTION_ID);
            priviousArticlePosition = articleClickedPosition;
            isFromPortfolioScreen = getArguments().getBoolean(IS_PORTFOLIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        if (mMainActivity != null) {
            mMainActivity.showBottomNavigationBar(false);
        }
        if (mArticleList == null) {
            mArticleList = new ArrayList<>();
            mSerctionArticleList = DatabaseJanitor.getSectionContentArticle(sectionId);
            mSerctionArticleList.addChangeListener(mOrderedRealmCollectionChangeListener);
        }
        return inflater.inflate(R.layout.fragment_sliding_article, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        if (mArticleList != null && mArticleList.size() > 0) {
            setupViewPager(mArticleList);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSerctionArticleList != null) {
            mSerctionArticleList.removeChangeListener(mOrderedRealmCollectionChangeListener);
        }
    }


    private void setupViewPager(ArrayList<ArticleDetail> mArticleList) {
        mViewPager.setAdapter(new SlidingPortfolioArticleAdapter(mMainActivity,getChildFragmentManager(), mArticleList, isFromPortfolioScreen));

        mViewPager.setCurrentItem(articleClickedPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrollPageCount++;
                if (scrollPageCount == 3) {
                    if (!SharedPreferenceHelper.getBoolean(mMainActivity, Constants.THIRD_SWIPE, false)) {
                        SharedPreferenceHelper.putBoolean(mMainActivity, Constants.THIRD_SWIPE, true);
                        mMainActivity.loadFullScreenAds(true);
                    }
                }

                if (priviousArticlePosition < position) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mMainActivity, "Article", "Swipe", "Article-Swipe-Left");
                    priviousArticlePosition = position;
                } else {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mMainActivity, "Article", "Swipe", "Article-Swipe-Right");
                    priviousArticlePosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
