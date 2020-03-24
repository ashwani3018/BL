package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.activity.OverlayActivity;
import com.mobstac.thehindubusinessline.activity.ReadLaterActivity;
import com.mobstac.thehindubusinessline.adapter.HomeAdapter;
import com.mobstac.thehindubusinessline.adapter.WidgetAdapter;
import com.mobstac.thehindubusinessline.database.ApiCallHandler;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.model.ArticleBean;
import com.mobstac.thehindubusinessline.model.BSEData;
import com.mobstac.thehindubusinessline.model.HomeAdapterModel;
import com.mobstac.thehindubusinessline.model.HomeEvents;
import com.mobstac.thehindubusinessline.model.NSEData;
import com.mobstac.thehindubusinessline.model.SensexData;
import com.mobstac.thehindubusinessline.model.StaticPageUrl;
import com.mobstac.thehindubusinessline.model.WidgetsTable;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.moengage.MoEngageTracker;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.FeatureConstant;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.LotameAppTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.moe.pushlibrary.MoEHelper;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;


public class HomeFragment extends BaseFragment {

    private final String TAG = "BL_HomeFragment";
    public ProgressDialog mProgressDialog;
    private RecyclerView mHomeRecyclerView;
    private MainActivity mMainActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isFragmentVisibleToUser;
    private MoEHelper mHelper;
    private HomeAdapter mHomeAdapterNew;
    private List<HomeAdapterModel> mHomeDataList;
    private String firstArticleTitle;

    private String staticPageUrl;
    private boolean isStaticPageEnable;
    private int positionInListView;

    public static HomeFragment getInstance(String staticPageUrl, boolean isStaticPageEnable, int positionInListView) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("staticPageUrl", staticPageUrl);
        args.putBoolean("isStaticPageEnable", isStaticPageEnable);
        args.putInt("positionInListView", positionInListView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null) {
            this.staticPageUrl = getArguments().getString("staticPageUrl");
            this.isStaticPageEnable = getArguments().getBoolean("isStaticPageEnable");
            this.positionInListView = getArguments().getInt("positionInListView");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("SlidingFragment", "onCreateView: Called");
        View mRootView = inflater.inflate(R.layout.fragment_home, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefreshLayout_home);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                    if (mProgressDialog != null && !mProgressDialog.isShowing())
                    mProgressDialog.show();
                    mHomeAdapterNew.reInitInlineAds();
                    long sycTime = NetworkUtils.getSyncTimePref(mMainActivity).getLong("Home", 0);
                    ApiCallHandler.fetchHomeData(mMainActivity, true, sycTime, false);
                } else {
                    mMainActivity.showSnackBar(mHomeRecyclerView);
                }
            }
        });
        mHomeRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview_home);
        mHomeRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mProgressDialog = new ProgressDialog(mMainActivity, R.style.DialogTheme);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_Holo_Light_ProgressBar_Large);

        mHomeDataList = getHomeDataList();
        mHomeAdapterNew = new HomeAdapter(mMainActivity, mHomeDataList, mMainActivity, mMainActivity);
        mHomeRecyclerView.setAdapter(mHomeAdapterNew);
        return mRootView;
    }


    private void fillNativeAdData(final int position, final String adId) {
        // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
        if (SharedPreferenceHelper.isUserPreferAdsFree(mMainActivity)) {
            return;
        }
        AdLoader.Builder builder = new AdLoader.Builder(getActivity(), adId);
        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {

                if (mHomeAdapterNew != null) {
                    HomeAdapterModel homeAdapterModel = new HomeAdapterModel(HomeAdapter.VIEW_NATIVE_AD, 2, null, "",
                            "", 0, 0, "", null);
                    homeAdapterModel.setmNativeContentAd(ad);
                    mHomeDataList.add(position, homeAdapterModel);
                    mHomeAdapterNew.notifyDataSetChanged();
                }

            }
        });


        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "onAdFailedToLoad: error code " + errorCode);
            }
        }).build();

        adLoader.loadAd(DFPConsent.getWithoutUrlAdsRequest(getActivity()));

    }

    /**
     * Loading Ads on given position
     */
    private void loadAds() {
        // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
        if(SharedPreferenceHelper.isUserPreferAdsFree(mMainActivity)) {
            return;
        }
        if (mHomeDataList != null) {
            for (int i = 0; i < mHomeDataList.size(); i++) {
                if (i == 5 || i == 11 || i == 17) {

                    String adId = "";

                    switch (i) {
                        case 5:
                            adId = getString(R.string.dfp_native_first);
                            break;
                      /*  case 11:
                            adId = getString(R.string.dfp_native_second);
                            break;
                        case 17:
                            adId = getString(R.string.dfp_native_third);
                            break;*/
                    }

                    fillNativeAdData(i, adId);

                }
            }

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // loadAds();
        try {
            LotameAppTracker.sendDataToLotameAnalytics(mMainActivity, getString(R.string.la_action), getString(R.string.la_section) + "Home");
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (isFragmentVisibleToUser && mHomeAdapterNew != null) {
            mHomeAdapterNew.setAdViewNull();
            mHomeAdapterNew.setIsFragmentVisibleToUser(isFragmentVisibleToUser);
        }
        if (isFragmentVisibleToUser && mMainActivity != null) {
            mMainActivity.createBannerAdRequest(false, false, "");

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean isNeedToShowOverlayScreen = SharedPreferenceHelper.getBoolean(mMainActivity, Constants.HOME_OVERLAY_SCREEN_LOADED, false);
        if (isFragmentVisibleToUser && isNeedToShowOverlayScreen && (firstArticleTitle != null)) {
            Intent intent = new Intent(mMainActivity, OverlayActivity.class);
            intent.putExtra(Constants.OVERLAY_TYPE, OverlayActivity.TYPE_HOME);
            intent.putExtra(Constants.OVERLAY_LIST, firstArticleTitle);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Businessline.getmEventBus().isRegistered(this)) {
            Businessline.getmEventBus().register(this);
        }
        mHelper = MoEHelper.getInstance(mMainActivity);
        MoEngageTracker.getInstance().setUserData(mMainActivity);

        boolean isHomeRefresh = SharedPreferenceHelper.getBoolean(
                mMainActivity,
                Constants.PREFERENCES_HOME_REFRESH,
                false);

        boolean syncStatus = NetworkUtils.isSectionNotNeedToSync(mMainActivity, "Home");
        if (!syncStatus && isFragmentVisibleToUser && !isHomeRefresh) {
            if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                if (mProgressDialog != null && !mProgressDialog.isShowing())
                mProgressDialog.show();
                long sycTime = NetworkUtils.getSyncTimePref(mMainActivity).getLong("Home", 0);
                ApiCallHandler.fetchHomeData(mMainActivity, true, sycTime, false);
            } else {
                mMainActivity.showSnackBar(mHomeRecyclerView);
            }
        } else {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }



        /* Refreshing Home Screen when NewsFeed/CitiesofInterest is changed by user */


        if (isHomeRefresh && isFragmentVisibleToUser) {
            if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null && !mProgressDialog.isShowing())
                        mProgressDialog.show();
                        ApiCallHandler.fetchHomeData(mMainActivity, true, 0, true);
                    }
                });
            } else {
                mMainActivity.showSnackBar(mHomeRecyclerView);
            }
        }

        if (isFragmentVisibleToUser) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(mMainActivity, GoogleAnalyticsTracker.getSectionName(getString(R.string.ga_home)));
            FlurryAgent.logEvent("Home Screen");
            FlurryAgent.onPageView();
        }
        if (mHomeAdapterNew != null) {
            mHomeAdapterNew.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Businessline.getmEventBus().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentVisibleToUser = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
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


    private List<HomeAdapterModel> getHomeDataList() {
        ArrayList<ArticleDetail> mHomeArticleList = new ArrayList<>();
        List<WidgetsTable> mWidgetData = DatabaseJanitor.getWidgetsTable();
        List<ArticleBean> mBannerData = DatabaseJanitor.getBannerArticle();
        ArrayList<HomeAdapterModel> mHomeDataList = new ArrayList<>();
        int bannerArticleSize = mBannerData.size();


//        int chunkSize = SharedPreferenceHelper.getInt(mMainActivity, Constants.NEWSFEED_CHUNK_SIZE, 3);
        int chunkSize = 3;
        int iTmpForBanner = 0;
        if (mBannerData != null && mBannerData.size() > 0) {
            for (ArticleBean mBannerArticle : mBannerData) {
                mHomeArticleList.add(ArticleUtil.convertToArticleDetail(mBannerArticle));
                if (iTmpForBanner == 0) {
                    firstArticleTitle = mBannerArticle.getTi();
                    iTmpForBanner++;
                }
            }
        }

        mHomeArticleList.addAll(ArticleUtil.convertArticleBeanListToArticleDetailList(DatabaseJanitor.getAllOverRidePersonalizedFeedArticle()));
        int listSize = mHomeArticleList.size();
        if (listSize > 0) {
            for (int iTmp = 0; iTmp < listSize; iTmp++) {
                if (iTmp == 0 && bannerArticleSize > 0) {
                    mHomeDataList.add(new HomeAdapterModel(HomeAdapter.VIEW_BANNER, iTmp, mHomeArticleList.get(iTmp)
                            , "", "", 0, 0, "", null));
                } else {
                    mHomeDataList.add(new HomeAdapterModel(HomeAdapter.VIEW_NORMAL_ARTICLE, iTmp, mHomeArticleList.get(iTmp)
                            , "", "", 0, 0, "", null));
                }
            }

            /*Sensex View*/
            HomeAdapterModel sensexAdapterData = new HomeAdapterModel(HomeAdapter.VIEW_SENSEX, mBannerData.size(), null, "",
                    "", 0, 0, "", null);
            SensexData sensexData = new SensexData();
            sensexData.setBSEData(new BSEData());
            sensexData.setNSEData(new NSEData());
//            sensexData.setGoldDollarDataModel(new GoldDollarDataModel());
            sensexAdapterData.setSensexData(sensexData);
            mHomeDataList.add(mBannerData.size(), sensexAdapterData);


            /*Inline ad*/
           /* if (mHomeDataList.size() > 6) {
                mHomeDataList.add(6, new HomeAdapterModel(HomeAdapter.VIEW_INLINE_AD, bannerArticleSize, null, "",
                        "", 0, 0, "", null));
            } else {
                mHomeDataList.add(new HomeAdapterModel(HomeAdapter.VIEW_INLINE_AD, bannerArticleSize, null, "",
                        "", 0, 0, "", null));
            }*/


            //insert widgets in list
            int widgetsPostion = 0;
            if (mHomeDataList.size() > bannerArticleSize) {
                widgetsPostion = bannerArticleSize + 1;
            }
            for (int i = 0; i < mWidgetData.size(); i++) {
                WidgetsTable bean = mWidgetData.get(i);
                RealmResults<ArticleBean> mSectionContentModel = DatabaseJanitor.
                        getWidgetSectionContentArticle(String.valueOf(bean.getSecId()));
                if (mSectionContentModel == null || mSectionContentModel.size() == 0) {
                    continue;
                }
                ArrayList mWidgetList = ArticleUtil.convertArticleBeanListToArticleDetailList(mSectionContentModel);
                WidgetAdapter mWidgetAdapter = new WidgetAdapter(mMainActivity, mWidgetList, bean.getSecId());
//                mWidgetAdapter.setHasStableIds(true);
                mHomeDataList.add(widgetsPostion, new HomeAdapterModel(HomeAdapter.VIEW_WIDGET, i, null, bean.getSecName(), bean.getSecName(),
                        bean.getSecId(), bean.getParentSecId(), bean.getType(), mWidgetAdapter));
                if (mHomeDataList.size() > (widgetsPostion + chunkSize)) {
                    if (i == 0) {
                        widgetsPostion = widgetsPostion + chunkSize + 1;
                    } else {
                        widgetsPostion = widgetsPostion + chunkSize + 1;
                    }
                } else {
                    widgetsPostion = mHomeDataList.size() - 1;
                }
            }

            /*Insert Customise home buttom at bottom of list*/
            mHomeDataList.add(new HomeAdapterModel(HomeAdapter.VIEW_FOOTER, 0, null
                    , "", "", 0, 0, "", null));


            final boolean isNetworkAvailable = NetworkUtils.isNetworkAvailable(getActivity());


            // Ads Implementation
            // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
            if(!SharedPreferenceHelper.isUserPreferAdsFree(mMainActivity)) {
                if (isNetworkAvailable) {
                    String firstInline = FeatureConstant.firstInline;
                    String secondInline = FeatureConstant.secondInline;
                    String thirdInline = FeatureConstant.thirdInline;

                    String firstNative = FeatureConstant.firstNative;
                    String secondNative = FeatureConstant.secondNative;
                    String thirdNative = FeatureConstant.thirdNative;

                    int firstNativeIndex = 11;
                    int secondNativeIndex = 21;
                    int thirdNativeIndex = 31;

                    int firstInlineIndex = 4;
                    int secondInlineIndex = 16;
                    int thirdInlineIndex = 26;


                    // First Inline Ads  // index 4
                    if (firstInlineIndex < mHomeDataList.size()) {
                        HomeAdapterModel firstInlineModel = new HomeAdapterModel(HomeAdapter.VIEW_INLINE_AD, 2, null, "",
                                "", 0, 0, "", null);
                        firstInlineModel.setAdId(firstInline);
                        mHomeDataList.add(firstInlineIndex, firstInlineModel);
                    }

                    // First Native Ads  // index 11
                    if (firstNativeIndex < mHomeDataList.size()) {
                        HomeAdapterModel firstNativeModel = new HomeAdapterModel(HomeAdapter.VIEW_NATIVE_AD, 2, null, "",
                                "", 0, 0, "", null);
                        firstNativeModel.setAdId(firstNative);
                        mHomeDataList.add(firstNativeIndex, firstNativeModel);
                    }

                    // Second Inline Ads  // index 16
                    if (secondInlineIndex < mHomeDataList.size()) {
                        HomeAdapterModel secondInlineModel = new HomeAdapterModel(HomeAdapter.VIEW_INLINE_AD, 2, null, "",
                                "", 0, 0, "", null);
                        secondInlineModel.setAdId(secondInline);
                        mHomeDataList.add(secondInlineIndex, secondInlineModel);
                    }

                    // Second Native Ads  // index 21
                    if (secondNativeIndex < mHomeDataList.size()) {
                        HomeAdapterModel secondNativeModel = new HomeAdapterModel(HomeAdapter.VIEW_NATIVE_AD, 2, null, "",
                                "", 0, 0, "", null);
                        secondNativeModel.setAdId(secondNative);
                        mHomeDataList.add(secondNativeIndex, secondNativeModel);
                    }


                    // Third Inline Ads  // index 26
                    if (thirdInlineIndex < mHomeDataList.size()) {
                        HomeAdapterModel thirdInlineModel = new HomeAdapterModel(HomeAdapter.VIEW_INLINE_AD, 2, null, "",
                                "", 0, 0, "", null);
                        thirdInlineModel.setAdId(thirdInline);
                        mHomeDataList.add(thirdInlineIndex, thirdInlineModel);
                    }

                    // Third Native Ads  // index 31
                    if (thirdNativeIndex < mHomeDataList.size()) {
                        HomeAdapterModel thirdNativeModel = new HomeAdapterModel(HomeAdapter.VIEW_NATIVE_AD, 2, null, "",
                                "", 0, 0, "", null);
                        thirdNativeModel.setAdId(thirdNative);
                        mHomeDataList.add(thirdNativeIndex, thirdNativeModel);
                    }
                }

            }

            staticItemLoad(mHomeDataList, isNetworkAvailable, staticPageUrl, isStaticPageEnable, positionInListView);




        }
        return mHomeDataList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mMainActivity != null) {
            mMainActivity.updateOverFlowMenuActionButton();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMainActivity.toggleDrawer();
                FlurryAgent.logEvent(getString(R.string.ga_sidebar_category));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mMainActivity, getString(R.string.ga_action), getString(R.string.ga_sidebar_category), "Home Fragment");
                return true;

            case R.id.action_bookmarks:
                FlurryAgent.logEvent(getString(R.string.ga_bookmark_screen_button_clicked));

                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mMainActivity, getString(R.string.ga_action), getString(R.string.ga_bookmark_screen_button_clicked), "Home Fragment");
                Intent intent = new Intent(mMainActivity, ReadLaterActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mMainActivity.setToolbarLogo(R.mipmap.logo_actionbar);
        mMainActivity.setToolbarTitle(null);
        mMainActivity.setToolbarHomeButton();
        if (menu != null) {
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.action_overflow).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }


    @Subscribe
    public void onEvent(HomeEvents event) {

        switch (event.getMessage()) {
            case Constants.EVENT_INSERTED_WIDGET_API_DATABASE:
                SharedPreferenceHelper.putBoolean(
                        mMainActivity,
                        Constants.IS_HOME_FIRST_TIME,
                        true
                );
                SharedPreferenceHelper.putInt(mMainActivity, Constants.PREFERENCE_SECTION_COUNT, 0);

                mHomeDataList = getHomeDataList();

                if (mHomeAdapterNew != null) {
                    loadAds();
                    mHomeAdapterNew.setUpdatedListToHomeAdapter(mHomeDataList);
                }
                SharedPreferenceHelper.putBoolean(
                        mMainActivity,
                        Constants.PREFERENCES_HOME_REFRESH,
                        false);

                if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case Constants.EVENT_INSERTED_NEWSFEED_API_DATABASE:
                final List<WidgetsTable> mWidgetsList = DatabaseJanitor.getWidgetsTable();
                SharedPreferenceHelper.putInt(mMainActivity, Constants.PREFERENCE_SECTION_COUNT, 0);
                for (WidgetsTable mWidget : mWidgetsList) {
                    String id = String.valueOf(mWidget.getSecId());
                    String type = mWidget.getType();
                    String mSectionName = mWidget.getSecName();
                    long syncTime = NetworkUtils.getSyncTimePref(mMainActivity).getLong(mSectionName, 0);
                    Log.i(TAG, "onEvent: syncTime for section " + mSectionName + " " + syncTime);
                    ApiCallHandler.fetchSectionContent(mMainActivity,
                            id,
                            type,
                            true, syncTime);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case Constants.EVENT_HOME_FAILURE:
                mHomeDataList = getHomeDataList();
                if (mHomeAdapterNew != null) {
                    loadAds();
                    mHomeAdapterNew.setUpdatedListToHomeAdapter(mHomeDataList);
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
                break;
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        Log.i(TAG, "setUserVisibleHint: " + isVisibleToUser + " Home ");
        if (isVisibleToUser && mMainActivity != null) {
            boolean isHomeRefresh = SharedPreferenceHelper.getBoolean(mMainActivity, Constants.PREFERENCES_HOME_REFRESH, false);
            boolean syncStatus = NetworkUtils.isSectionNotNeedToSync(mMainActivity, "Home");
            if (!syncStatus && isVisibleToUser && !isHomeRefresh) {
                if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                    if (mProgressDialog != null && !mProgressDialog.isShowing())
                    mProgressDialog.show();
                    long sycTime = NetworkUtils.getSyncTimePref(mMainActivity).getLong("Home", 0);
                    ApiCallHandler.fetchHomeData(mMainActivity, true, sycTime, false);
                } else {
                    mMainActivity.showSnackBar(mHomeRecyclerView);
                }
            } else {
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            if (isHomeRefresh && isVisibleToUser) {
                if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mProgressDialog != null && !mProgressDialog.isShowing())
                            mProgressDialog.show();
                            ApiCallHandler.fetchHomeData(mMainActivity, true, 0, true);
                        }
                    });
                } else {
                    mMainActivity.showSnackBar(mHomeRecyclerView);
                }
            }
        }
        if (mHomeAdapterNew != null && isVisibleToUser) {
            mHomeAdapterNew.reInitInlineAds();
            mHomeAdapterNew.setAdViewNull();
            mHomeAdapterNew.setIsFragmentVisibleToUser(isVisibleToUser);
        }
        if (mMainActivity != null && isVisibleToUser && mHomeAdapterNew != null) {
            if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
            } else {
                if (mMainActivity != null)
                    mMainActivity.showSnackBar(mHomeRecyclerView);
            }
        }
        if (isVisibleToUser && mMainActivity != null) {
            Log.i(TAG, "setUserVisibleHint: Event sent to analytics home");
            mMainActivity.createBannerAdRequest(false, false, "");
//            loadAds();
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(mMainActivity, GoogleAnalyticsTracker.getSectionName(getString(R.string.ga_home)));
            FlurryAgent.logEvent("Home Screen");
            FlurryAgent.onPageView();
        }

    }

    public void staticItemLoad(ArrayList<HomeAdapterModel> mHomeDataList, boolean isNetworkAvailable, String staticPageUrl, boolean isStaticPageEnable, int positionInListView) {

        if(!isNetworkAvailable) {
            return;
        }
        // Below commented code is for Testing Purpose
        /*positionInListView = 2;
        isStaticPageEnable = true;
        staticPageUrl = "https://google.com"+"00#00"+"51";*/

        if(isStaticPageEnable && staticPageUrl != null && positionInListView != -1) {
            final HomeAdapterModel staticItem = new HomeAdapterModel(HomeAdapter.HOME_WEB_CONTAINT);
            staticItem.setStaticPageUrl(staticPageUrl);
            if(!mHomeDataList.contains(staticItem)) {
                int size = mHomeDataList.size();
                if(size > positionInListView) {
                    mHomeDataList.add(positionInListView, staticItem);
                } else {
                    mHomeDataList.add(staticItem);
                }
            }
        }
    }
}
