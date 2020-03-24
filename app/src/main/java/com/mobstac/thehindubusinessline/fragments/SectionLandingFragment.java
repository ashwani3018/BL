package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.adapter.SectionLandingAdapter;
import com.mobstac.thehindubusinessline.database.ApiCallHandler;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.RetrofitResponseFromEventBus;
import com.mobstac.thehindubusinessline.model.SectionAdapterItem;
import com.mobstac.thehindubusinessline.model.SectionArticleList;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.FeatureConstant;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.LotameAppTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.PaginationScrollListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class SectionLandingFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "section_type";
    private static final String ARG_PARAM3 = "section_name";
    private static final String ARG_PARAM4 = "outbrain_link";
    RealmResults<ArticleBean> realmResults;
    private String TAG = "SectionLandingFragment";
    private long mSectionId;
    private String sectionType;
    private String mSectionName;
    private RecyclerView mRecyclerView;
    private MainActivity mMainActivity;
    private SectionLandingAdapter mSectionLandingAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;
    private boolean isFragmentVisibleToUser;
    private View mBlockUi;
    private String mOutBrainLink;
    private boolean hasSubsection;
    private LinearLayoutManager linearLayoutManager;

    private String staticPageUrl;
    private boolean isStaticPageEnable;
    private int positionInListView;


    private OrderedRealmCollectionChangeListener<RealmResults<ArticleBean>> mOrderedRealmCollectionChangeListener = new OrderedRealmCollectionChangeListener<RealmResults<ArticleBean>>() {
        @Override
        public void onChange(RealmResults<ArticleBean> articleBeen, OrderedCollectionChangeSet changeSet) {
            ArrayList<ArticleDetail> mArticleData = ArticleUtil.convertArticleBeanListToArticleDetailList(articleBeen);
            if (mSectionLandingAdapter != null && mArticleData.size() > 0) {
                mSectionLandingAdapter.setUpdatedListToHomeAdapter(mArticleData);
                mSectionLandingAdapter.staticItemLoad(staticPageUrl, isStaticPageEnable, positionInListView);
                mNextPage = mArticleData.get((mArticleData.size() - 1)).getPage();
                Log.i("" ,"");
            }
        }
    };

    public static SectionLandingFragment newInstance(long sectionId, String sectionType, String sectionName,
                                                     String outbrainLink, String staticPageUrl, boolean isStaticPageEnable, int positionInListView) {
        SectionLandingFragment fragment = new SectionLandingFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, sectionId);
        args.putString(ARG_PARAM2, sectionType);
        args.putString(ARG_PARAM3, sectionName);
        args.putString(ARG_PARAM4, outbrainLink);

        args.putString("staticPageUrl", staticPageUrl);
        args.putBoolean("isStaticPageEnable", isStaticPageEnable);
        args.putInt("positionInListView", positionInListView);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        Log.i(TAG, "onAttach: context");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity) activity;
        Log.i(TAG, "onAttach: activity");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionId = getArguments().getLong(ARG_PARAM1);
            sectionType = getArguments().getString(ARG_PARAM2);
            mSectionName = getArguments().getString(ARG_PARAM3);
            mOutBrainLink = getArguments().getString(ARG_PARAM4);

            this.staticPageUrl = getArguments().getString("staticPageUrl");
            this.isStaticPageEnable = getArguments().getBoolean("isStaticPageEnable");
            this.positionInListView = getArguments().getInt("positionInListView");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_section_landing, container, false);
        Log.i("SlidingFragment", "onCreateView: " + mSectionName);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_section_landing);
        mBlockUi = view.findViewById(R.id.mView);
        realmResults = DatabaseJanitor.getSectionContentArticle(String.valueOf(mSectionId));

        ArrayList<ArticleDetail> mArticleData = ArticleUtil.convertArticleBeanListToArticleDetailList(realmResults);
        RealmResults<SectionTable> subSections = Businessline.getRealmInstance().where(SectionTable.class).equalTo("parentId", mSectionId).findAll();

        if (subSections.size() > 0) {
            hasSubsection = true;
        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
//        ArrayList<SectionArticleList> mArticleList = ArticleUtil.convertArticleDetailToSectionArticleList(mArticleData);
//
//        addExtraView(mArticleList);

        // Revising Section Row Data
        ArrayList<SectionAdapterItem> sectionAdapterItems = FeatureConstant.getSectionRevisedData(0, mArticleData, subSections.size() > 0);

        boolean isNetworkAvailable = NetworkUtils.isNetworkAvailable(getContext());

        mSectionLandingAdapter = new SectionLandingAdapter(getActivity(), sectionAdapterItems,
                subSections, mSectionName, sectionType, mOutBrainLink, isNetworkAvailable);

        mRecyclerView.setAdapter(mSectionLandingAdapter);

        realmResults.addChangeListener(mOrderedRealmCollectionChangeListener);

        mBlockUi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    mSectionLandingAdapter.reInitInlineAds();
                    resetNextPage();
                    refreshSectionContentData(PAGE_START, false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mMainActivity.showSnackBar(mRecyclerView);
                }
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            LotameAppTracker.sendDataToLotameAnalytics(getActivity(), getString(R.string.la_action), getString(R.string.la_section) + mSectionName);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (isFragmentVisibleToUser && mSectionLandingAdapter != null) {
            mSectionLandingAdapter.setAdViewNull();
            mSectionLandingAdapter.setIsFragmentVisibleToUser(isFragmentVisibleToUser);
        }
        if (isFragmentVisibleToUser && mMainActivity != null) {
            mMainActivity.createBannerAdRequest(true, false, mOutBrainLink);
        }

        if (FeatureConstant.PAGINATION_REQUIRED) {
            loadMoreListener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerEventBus();

        Log.i(TAG, "onResume: " + isFragmentVisibleToUser + " " + mSectionName);
        if (isFragmentVisibleToUser && !NetworkUtils.isSectionNotNeedToSync(getActivity(), mSectionName)) {
            if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                resetNextPage();
                refreshSectionContentData(PAGE_START, false);
            } else {
                mMainActivity.showSnackBar(mRecyclerView);
            }
        }
        if (isFragmentVisibleToUser) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), GoogleAnalyticsTracker.getSectionName(mSectionName));
            FlurryAgent.logEvent("Section: " + mSectionName);
            FlurryAgent.onPageView();
        }

        if (mSectionLandingAdapter != null) {
            mSectionLandingAdapter.notifyDataSetChanged();
        }
    }

    /*private void refreshSectionContentData() {
        showBlockUi();
        mSwipeRefreshLayout.setRefreshing(true);
        long syncTime = NetworkUtils.getSyncTimePref(mMainActivity).getLong(mSectionName, 0);
        Log.i(TAG, "Pull to refresh syncTime for section " + mSectionName + " " + syncTime);
        ApiCallHandler.fetchSectionContent(mMainActivity, String.valueOf(mSectionId), sectionType, false, syncTime);
    }*/

    private void refreshSectionContentData(int pages, boolean isFromAutoLoading) {
        // adding for pagination for infinite scrolling
//        showBlockUi();
        if (!isFromAutoLoading) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        long syncTime = NetworkUtils.getSyncTimePref(mMainActivity).getLong(mSectionName, 0);
        Log.i(TAG, "Pull to refresh syncTime for section " + mSectionName + " " + syncTime);

        Consumer<Boolean> onNext = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                hideProgressBar();
                hideBlockUi();
                isLoading = false;
                mSectionLandingAdapter.setLoadingPosition(false);
            }
        };

        Consumer<Boolean> onError = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                hideProgressBar();
//                hideBlockUi();
                isLoading = false;
                mSectionLandingAdapter.setLoadingPosition(false);
            }
        };

        if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
            if (pages == 1) {
                ApiCallHandler.fetchSectionContentRX(onNext, onError, mMainActivity, pages, String.valueOf(mSectionId), sectionType, false, syncTime);
            } else {
                mSectionLandingAdapter.setLoadingPosition(true);
                ApiCallHandler.fetchSectionContentRX(onNext, onError, mMainActivity, pages, String.valueOf(mSectionId), sectionType, false, 0);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegisterEventBus();
    }

    @Override
    public void onStop() {
        super.onStop();
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

   /* private void showProgressBar() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }*/

    private void hideProgressBar() {
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void registerEventBus() {
        if (Businessline.getmEventBus() != null) {
            Businessline.getmEventBus().register(this);
        }
    }

    private void unRegisterEventBus() {
        Businessline.getmEventBus().unregister(this);
    }

    @Subscribe
    public void onEvent(RetrofitResponseFromEventBus response) {
        if (String.valueOf(mSectionId).equalsIgnoreCase(response.getId())) {
            hideProgressBar();
            hideBlockUi();

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        Log.i(TAG, "setUserVisibleHint: " + isFragmentVisibleToUser + " " + mSectionName);
        if (isVisibleToUser && sectionType != null && mSectionId != 0 && mSectionName != null) {
            Log.i(TAG, "setUserVisibleHint: Event sent to analytics" + mSectionName);
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(mMainActivity, GoogleAnalyticsTracker.getSectionName(mSectionName));
            FlurryAgent.logEvent("Section: " + mSectionName);
            FlurryAgent.onPageView();
            if (!NetworkUtils.isSectionNotNeedToSync(mMainActivity, mSectionName)) {
                if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                    resetNextPage();
                    refreshSectionContentData(PAGE_START, false);
                } else {
                    mMainActivity.showSnackBar(mRecyclerView);
                }
            }
        }
        if (isVisibleToUser && mSectionLandingAdapter != null) {
            mSectionLandingAdapter.setAdViewNull();
            mSectionLandingAdapter.setIsFragmentVisibleToUser(isVisibleToUser);
        }

        if (isVisibleToUser && mMainActivity != null) {
            mMainActivity.createBannerAdRequest(true, false, mOutBrainLink);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i(TAG, "onHiddenChanged: ");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentVisibleToUser = false;
        realmResults.removeChangeListener(mOrderedRealmCollectionChangeListener);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void hideBlockUi() {
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBlockUi.setVisibility(View.GONE);
            }
        });

    }

    private void showBlockUi() {
        mBlockUi.setVisibility(View.VISIBLE);

    }

    private void addExtraView(ArrayList<SectionArticleList> mSectionList) {
        /*for (int i = 0; i < mSectionList.size(); i++) {
            switch (i) {
                case 2:
                    if (hasSubsection) {
                        mSectionList.add(i, new SectionArticleList(Constants.VIEW_TYPE_EXPLORE, null, 0));
                    }
                    break;
                case 3:
                    mSectionList.add(i, new SectionArticleList(Constants.VIEW_TYPE_NATIVE_AD, null, 0));
                    break;
                case 6:
                    mSectionList.add(i, new SectionArticleList(Constants.VIEW_TYPE_OUTBRAIN, null, 0));
                    break;
            }
        }
        if (mSectionLandingAdapter != null) {
            mSectionLandingAdapter.notifyDataSetChanged();
        }*/
    }


    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mNextPage = 0;

    private void loadMoreListener() {

        mRecyclerView.clearOnScrollListeners();

        PaginationScrollListener ll = new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (NetworkUtils.isNetworkAvailable(mMainActivity)) {

                    if (!isLoading) {
                        // API call
                        loadNextPage();
                    }
                }
            }

            @Override
            public int getTotalPageCount() {
                return -1;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };


        mRecyclerView.addOnScrollListener(ll);
    }



    private void loadNextPage() {
        if (NetworkUtils.isSectionNotNeedToSync(mMainActivity, mSectionName)) {
            int nextPage = mNextPage+1;
            if(mOnGoingPage !=nextPage) {
                isLoading = true;
                mOnGoingPage = nextPage;
                refreshSectionContentData(mNextPage + 1, true);
            }
//            refreshSectionContentData(mNextPage + 1, true);
        } else {
            isLoading = false;
        }
    }


    private int mOnGoingPage = -1;
    public static final int PAGE_START = 1;

    private void resetNextPage() {
        mNextPage = 0;
        resetLoadMore();
    }


    private void resetLoadMore() {
        loadMoreListener();
    }

}
