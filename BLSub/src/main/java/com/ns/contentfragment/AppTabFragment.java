package com.ns.contentfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.netoperation.model.TxnDataBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.THPPreferences;
import com.netoperation.util.UserPref;
import com.ns.adapter.AppTabPagerAdapter;
import com.ns.callbacks.OnSubscribeBtnClick;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.TabUtils;
import com.ns.view.ViewPagerScroller;

import java.lang.reflect.Field;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AppTabFragment extends BaseFragmentTHP implements OnSubscribeBtnClick {

    private ConstraintLayout subscribeLayout;
    private String mUserId, mFrom;
//    private TabUtils mTabUtils;

    public static AppTabFragment getInstance(String from, String userId, int tabIndex) {
        AppTabFragment fragment = new AppTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putInt("tabIndex", tabIndex);
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }


    //    private TabLayout mTabLayout;
    private ViewPager viewPager;
    private AppTabPagerAdapter pagerAdapter;
//    private int tabIndex = 0;

    /**
     * Holds String value of User Name, to know whether user has logged in or not
     */
    protected String mUserLoggedName;

    private boolean mIsUserThemeDay;

    public BottomNavigationView mBottomNavigationView;

    private String[] tabNames = {THPConstants.TAB_1, THPConstants.TAB_2, THPConstants.TAB_3};
    private int[] tabUnSelectedIcons = {
//            com.ns.thpremium.R.drawable.ic_thp_tab_home_unselected,
            com.ns.thpremium.R.drawable.ic_tab_briefcase_unselected,
            com.ns.thpremium.R.drawable.ic_tab_dashboard_unselected,
            com.ns.thpremium.R.drawable.ic_tab_suggested_unselected,
//            com.ns.thpremium.R.drawable.ic_thp_tab_profile_unselected
    };
    private int[] tabSelectedIcons = {
//            com.ns.thpremium.R.drawable.ic_thp_tab_home_selected,
            com.ns.thpremium.R.drawable.ic_tab_briefcase_selected,
            com.ns.thpremium.R.drawable.ic_tab_dashboard_selected,
            com.ns.thpremium.R.drawable.ic_tab_suggested_selected,
//            com.ns.thpremium.R.drawable.ic_thp_tab_profile_selected
    };

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_apptab;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString("userId");
//            tabIndex = getArguments().getInt("tabIndex");
            mFrom = getArguments().getString("from");
        }

        mIsUserThemeDay = UserPref.getInstance(getActivity()).isUserThemeDay();
    }

    /*public void updateTabIndex() {
        if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_PERSONALISE)) {
            tabIndex = 1;
        } else if (mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.equalsIgnoreCase(THPConstants.FROM_USER_SignUp)) {
            tabIndex = 0;
        } else if(THPConstants.FLOW_TAB_CLICK != null && THPConstants.FLOW_TAB_CLICK.equals(THPConstants.TAB_1)) {
            tabIndex = 0;
        } else if(THPConstants.FLOW_TAB_CLICK != null && THPConstants.FLOW_TAB_CLICK.equals(THPConstants.TAB_2)) {
            tabIndex = 1;
        } else if(THPConstants.FLOW_TAB_CLICK != null && THPConstants.FLOW_TAB_CLICK.equals(THPConstants.TAB_3)) {
            tabIndex = 2;
        }

        // To select default tab
        mTabUtils.SetOnSelectView(getActivity(), mTabLayout, tabIndex);
        viewPager.setCurrentItem(tabIndex);
    }*/

    public void updateFromValue(String from) {
        mFrom = from;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subscribeLayout = view.findViewById(R.id.subscribeLayout);
//        mTabLayout = view.findViewById(R.id.appTabsTabLayout);
        viewPager = view.findViewById(R.id.appTabsViewPager);

        mBottomNavigationView = view.findViewById(R.id.bottom_navigation_bar);

        pagerAdapter = new AppTabPagerAdapter(getChildFragmentManager(), mUserId, tabNames);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        // This is smooth scroll of ViewPager
        smoothPagerScroll();

        mBottomNavigationView.getMenu().findItem(R.id.menu_mystories).setChecked(true);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Log.i("Ashwani", "From :: " + mFrom);

                if (menuItem.getItemId() == R.id.menu_home) {
                    if (mFrom != null && mFrom.equalsIgnoreCase(getString(R.string.menu_home))) {
                        getActivity().finish();
                    }

                    Intent intent = new Intent("com.mobstac.thehindubusinessline.activity.openHomeActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("selectedPosition", 1);
                    startActivity(intent);

                    return false;

                } else if (menuItem.getItemId() == R.id.menu_portfolio) {
                    if (mFrom != null && mFrom.equalsIgnoreCase(getString(R.string.menu_portfolio))) {
                        getActivity().finish();

                    }

                    Intent intent = new Intent("com.mobstac.thehindubusinessline.activity.openPortfolioActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("selectedPosition", 2);
                    startActivity(intent);

                    return false;

                } else if (menuItem.getItemId() == R.id.menu_mystories) {

                } else if (menuItem.getItemId() == R.id.menu_market) {
                    if (mFrom != null && mFrom.equalsIgnoreCase(getString(R.string.menu_market))) {
                        getActivity().finish();

                    }

                    Intent intent = new Intent("com.mobstac.thehindubusinessline.activity.openIndicesActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("selectedPosition", 4);
                    startActivity(intent);

                    return false;
                } else if (menuItem.getItemId() == R.id.menu_profile) {
                    IntentUtil.openUserProfileActivity(getActivity(), THPConstants.FROM_USER_PROFILE);
                }

                mBottomNavigationView.getMenu().findItem(R.id.menu_mystories).setChecked(true);

                return false;
            }
        });


        // Subscribe Button Click Listener
        view.findViewById(R.id.subscribeBtn_Txt).setOnClickListener(v -> {
            if (mIsOnline) {
                IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
            } else {
                noConnectionSnackBar(getView());
            }
        });
        view.findViewById(R.id.subscribeLayout).setOnClickListener(v -> {
            if (!mIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
        });
        view.findViewById(R.id.subsCloseImg).setOnClickListener(v -> {
            THPPreferences.getInstance(getActivity()).setIsSubscribeClose(true);
            view.findViewById(R.id.subscribeLayout).setVisibility(View.GONE);
        });


        // Back Button Click Listener
        view.findViewById(R.id.backBtn).setOnClickListener(v ->
                getActivity().finish()
        );

        // Premium Logo Button Click Listener
        view.findViewById(R.id.premiumLogoBtn).setOnClickListener(v -> {
                    if (!mIsOnline) {
                        noConnectionSnackBar(getView());
                        return;
                    }
                    IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);

                }
        );

    }

    /**
     * Loads User Profile Data
     */
    private void loadUserProfile() {
        ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    if (userProfile != null && !TextUtils.isEmpty(userProfile.getFullName())) {
                        mUserLoggedName = userProfile.getFullName().toUpperCase();
                    } else if (userProfile != null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                        mUserLoggedName = userProfile.getEmailId().toUpperCase();
                    } else if (userProfile != null && !TextUtils.isEmpty(userProfile.getContact())) {
                        mUserLoggedName = userProfile.getContact().toUpperCase();
                    }

                    boolean hasFreePlan = userProfile.isHasFreePlan();
                    boolean hasSubscriptionPlan = userProfile.isHasSubscribedPlan();

                    if (hasSubscriptionPlan) {
                        getView().findViewById(R.id.subscribeLayout).setVisibility(View.GONE);
                        getView().findViewById(R.id.premiumLogoBtn).setVisibility(View.GONE);
                    } else if (THPPreferences.getInstance(getActivity()).isSubscribeClose()) {
                        getView().findViewById(R.id.subscribeLayout).setVisibility(View.GONE);
                        getView().findViewById(R.id.premiumLogoBtn).setVisibility(View.VISIBLE);
                    } else {
                        getView().findViewById(R.id.subscribeLayout).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.premiumLogoBtn).setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * This is ViewPager Page Scroll Animation
     */
    private void smoothPagerScroll() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, new ViewPagerScroller(getActivity(),
                    new LinearInterpolator(), 250));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubscribeBtnClick(TxnDataBean bean) {

    }

    @Override
    public void onResume() {
        super.onResume();
        // Shows user name
        loadUserProfile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "");
    }
}
