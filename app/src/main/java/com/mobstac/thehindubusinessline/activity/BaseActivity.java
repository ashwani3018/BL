package com.mobstac.thehindubusinessline.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.PushType;
import com.facebook.AccessToken;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.listener.HomeButtonClickedListener;
import com.mobstac.thehindubusinessline.listener.OnSeeMoreFromSectionClickListener;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.NotificationBean;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.netoperation.net.ApiManager;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.NetConstants;
import com.netoperation.util.THPPreferences;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.utils.CallBackRelogin;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by arvind on 16/8/16.
 */
public class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        OnSeeMoreFromSectionClickListener {
    public static final int NO_ANIMATION = -1;
    private final int REQUEST_CODE = 1000;
    public BottomNavigationView mBottomNavigationView;
    private NotificationIncomingReceiver mNotificationIncomingReceiver;
    private String TAG = "BL_BaseActivity";
    private TextView txtViewOverFlowCount;
    private TextView mReadLaterCountTextView;
    private TextView mNotificationsCountTextView;
    private int mUnreadNotificationArticleCount;
    private int mUnreadBookmarkArticleCount;
    private int mUnreadArticleCount;
    private RealmResults<NotificationBean> mUnreadNotificationArticleList;
    private RealmResults<BookmarkBean> mUnreadBookmarkArticleList;
    private int selectedTabPosition = 0;
    private HomeButtonClickedListener mHomeButtonClickedListener;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {


        //Assigning base url
        if(BuildConfig.IS_PRODUCTION) {
            ServiceFactory.BASE_URL = BuildConfig.PRODUCTION_BASE_URL;
        } else {
            ServiceFactory.BASE_URL = BuildConfig.STATGGING_BASE_URL;
        }

        boolean isNightModeEnabled = SharedPreferenceHelper.getBoolean(getApplicationContext(),
                Constants.DAY_MODE,
                false);

        UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        if (isNightModeEnabled) {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        } else {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState, persistentState);
        mNotificationIncomingReceiver = new NotificationIncomingReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBottomNavigationView != null) {
            initBottomNavigationView();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.NOTIFICATION_INCOMING_FILTER);
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationIncomingReceiver, intentFilter);
        getUnreadNotificationAndBookmarkArticleNumber();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNotificationIncomingReceiver);
        mUnreadBookmarkArticleList.removeAllChangeListeners();
        mUnreadNotificationArticleList.removeAllChangeListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void popTopFragmentFromBackStack() {
        try {
            getSupportFragmentManager().popBackStack();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void pushView(Fragment fragment, final int animationSet, final boolean isRoot) {
        FragmentTransaction trasaction = getSupportFragmentManager().beginTransaction();
        if (animationSet != NO_ANIMATION)
            trasaction.setTransition(animationSet);
        if (isRoot) {
            clearBackStack();
            trasaction.replace(R.id.FRAME_CONTENT, fragment, getSupportFragmentManager().getBackStackEntryCount() + "");
        } else {
            trasaction.replace(R.id.FRAME_CONTENT, fragment, getSupportFragmentManager().getBackStackEntryCount() + "");
        }
        trasaction.addToBackStack(null);
        trasaction.commit();

    }

    void clearBackStack() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void pushSection(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        int fragCount = fm.getBackStackEntryCount();
        for (int i = 1; i < fragCount; i++) {
            fm.popBackStack();
        }

        FragmentTransaction trasaction = getSupportFragmentManager().beginTransaction();
        trasaction.replace(R.id.FRAME_CONTENT, fragment, getSupportFragmentManager().getBackStackEntryCount() + "");
        trasaction.addToBackStack(null);
        trasaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            manager.popBackStack();
            return;
        } else {
            finish();
//            super.onBackPressed();
        }
    }

    private void initBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public void showBottomNavigationBar(boolean showNavigationBar) {
        if (mBottomNavigationView != null) {
            if (showNavigationBar) {
                mBottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                mBottomNavigationView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                if (mHomeButtonClickedListener != null) {
                    mHomeButtonClickedListener.homeButtonClicked();
                }
                startActivity(new Intent(this, MainActivity.class));
                if (mBottomNavigationView != null) {
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.home_select);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_unselected);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_market).setIcon(R.drawable.ic_market);
                }
                Log.i(TAG, "onNavigationItemSelected: home clicked");
                return true;
            case R.id.menu_portfolio:

                startActivity(new Intent(this, PortfolioActivity.class));
                if (mBottomNavigationView != null) {
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.ic_home);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_unselected);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio_select);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_market).setIcon(R.drawable.ic_market);
                }
                Log.i(TAG, "onNavigationItemSelected: portfolio clicked");
                return true;

            case R.id.menu_mystories:
                login(2);

                return true;


            case R.id.menu_market:
                Intent intent = new Intent(this, IndicesActivity.class);
                intent.putExtra("selectedPosition", selectedTabPosition);
                startActivity(intent);
                if (mBottomNavigationView != null) {
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.ic_home);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_unselected);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_market).setIcon(R.drawable.ic_market_select);
                }
                selectedTabPosition = 0;
                return true;

            case R.id.menu_profile:
                login(4);

                /*startActivity(new Intent(this, BLInkActivity.class));
                if (mBottomNavigationView != null) {
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.ic_home);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_selected);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_market).setIcon(R.drawable.ic_market);
                }*/
                Log.i(TAG, "onNavigationItemSelected: market clicked");
                return true;
        }
        return true;
    }

    public void setBottomNavigationViewSelectedItem(int itemID) {
        if (mBottomNavigationView != null) {
            mBottomNavigationView.getMenu().findItem(itemID).setChecked(true);
            switch (itemID) {
                case R.id.menu_home:
                    mBottomNavigationView.getMenu().findItem(itemID).setIcon(R.drawable.home_select);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_unselected);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_market).setIcon(R.drawable.ic_market);
                    break;
                case R.id.menu_portfolio:
                    mBottomNavigationView.getMenu().findItem(itemID).setIcon(R.drawable.ic_portfolio_select);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.ic_home);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_unselected);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_market).setIcon(R.drawable.ic_market);
                    break;
                case R.id.menu_mystories:
                    mBottomNavigationView.getMenu().findItem(itemID).setIcon(R.drawable.ic_market_select);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.ic_home);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_unselected);
                    break;
                case R.id.menu_market:
                    mBottomNavigationView.getMenu().findItem(itemID).setIcon(R.drawable.ic_market_select);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.ic_home);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_profile).setIcon(R.drawable.ic_thp_tab_profile_unselected);
                    break;
                case R.id.menu_profile:
                    mBottomNavigationView.getMenu().findItem(itemID).setIcon(R.drawable.ic_thp_tab_profile_selected);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_home).setIcon(R.drawable.ic_home);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_portfolio).setIcon(R.drawable.ic_portfolio);
                    mBottomNavigationView.getMenu().findItem(R.id.menu_market).setIcon(R.drawable.ic_market);
                    break;


            }
        }
    }

    public void createOverFlowMenuOption(Menu menu) {
        Log.i(TAG, "createOverFlowMenuOption: ");
        MenuItem actionOverflow = menu.findItem(R.id.action_overflow);
        final View overflow = (View) MenuItemCompat.getActionView(actionOverflow);
        txtViewOverFlowCount = (TextView) overflow.findViewById(R.id.textview_overflow_count);

        MenuItem actionComment = menu.findItem(R.id.action_overflow);
        final View notificaitons = (View) MenuItemCompat.getActionView(actionComment);

        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout viewGroup = (LinearLayout) findViewById(R.id.layout_custom);

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = layoutInflater.inflate(R.layout.layout_menu_overflow, viewGroup);


                final PopupWindow changeSortPopUp = new PopupWindow(BaseActivity.this);
                //Inflating the Popup using xml file
                changeSortPopUp.setContentView(layout);
                changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                changeSortPopUp.setFocusable(true);
                changeSortPopUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.overflow_shadow));


                mReadLaterCountTextView = (TextView) layout.findViewById(R.id.textview_menu_readlater_count);
                mNotificationsCountTextView = (TextView) layout.findViewById(R.id.textview_menu_notifications_count);
                mReadLaterCountTextView.setText(mUnreadBookmarkArticleCount + "");
                mNotificationsCountTextView.setText(mUnreadNotificationArticleCount + "");
                if (mUnreadBookmarkArticleCount == 0) {
                    mReadLaterCountTextView.setVisibility(View.GONE);
                }
                if (mUnreadNotificationArticleCount == 0) {
                    mNotificationsCountTextView.setVisibility(View.GONE);
                }

                LinearLayout mReadLayout = (LinearLayout) layout.findViewById(R.id.layout_readlater);
                mReadLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FlurryAgent.logEvent(getString(R.string.ga_bookmark_screen_button_clicked));
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(BaseActivity.this, getString(R.string.ga_action), getString(R.string.ga_bookmark_screen_button_clicked), "Home Fragment");
                        Intent intent = new Intent(BaseActivity.this, ReadLaterActivity.class);
                        startActivity(intent);
                        changeSortPopUp.dismiss();
                    }
                });
                LinearLayout mNotificationsLayout = (LinearLayout) layout.findViewById(R.id.layout_notifications);
                mNotificationsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(BaseActivity.this, getString(R.string.ga_action), "Notification: Action Button Clicked ", "Main Activity");
                        FlurryAgent.logEvent("Notification: Action Button clicked");
                        Intent intent = new Intent(BaseActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        SharedPreferenceHelper.putBoolean(getApplicationContext(), Constants.NEW_NOTIFICATION, false);
                        changeSortPopUp.dismiss();
                    }
                });

                TextView mHomeScreen = (TextView) layout.findViewById(R.id.textView_menu_customize_home_screen);
                mHomeScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                                BaseActivity.this,
                                getString(R.string.ga_action),
                                "Customise: Customise Button Clicked ",
                                getString(R.string.customize_news_feed_menu));
                        FlurryAgent.logEvent("Customise: Customise Button Clicked ");
                        startActivity(new Intent(BaseActivity.this, CustomizeHomeScreenActivity.class));
                        changeSortPopUp.dismiss();
                    }
                });

                TextView mSettigsScreen = (TextView) layout.findViewById(R.id.textView_menu_settings);
                mSettigsScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BaseActivity.this, SettingActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                        changeSortPopUp.dismiss();
                    }
                });

                TextView mShareAppScreen = (TextView) layout.findViewById(R.id.textView_menu_share_app);
                mShareAppScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = shareAppIntent();
                        if (intent != null) {
                            startActivity(intent);
                            GoogleAnalyticsTracker.setGoogleAnalyticsEvent(BaseActivity.this, getString(R.string.ga_action), "Share: Share App button clicked", "Main Activity");
                            FlurryAgent.logEvent("Share: Share App button clicked");
                        }
                        changeSortPopUp.dismiss();
                    }
                });

                changeSortPopUp.showAsDropDown(txtViewOverFlowCount, 0, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "onActivityResult: ");
            recreate();
        }
    }

    private void getUnreadNotificationAndBookmarkArticleNumber() {
        mUnreadNotificationArticleList = DatabaseJanitor.getAllUnreadNotificationArticles();
        mUnreadNotificationArticleList.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<NotificationBean>>() {
            @Override
            public void onChange(RealmResults<NotificationBean> notificationBeen, OrderedCollectionChangeSet changeSet) {
                mUnreadNotificationArticleCount = notificationBeen.size();
                mUnreadArticleCount = mUnreadBookmarkArticleCount + mUnreadNotificationArticleCount;
                updateOverFlowMenuActionButton();
            }
        });

        mUnreadBookmarkArticleList = DatabaseJanitor.getAllUnreadBookmarksArticles();
        mUnreadBookmarkArticleList.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<BookmarkBean>>() {
            @Override
            public void onChange(RealmResults<BookmarkBean> bookmarkBeen, OrderedCollectionChangeSet changeSet) {
                mUnreadBookmarkArticleCount = bookmarkBeen.size();
                mUnreadArticleCount = mUnreadBookmarkArticleCount + mUnreadNotificationArticleCount;
                updateOverFlowMenuActionButton();
            }
        });

        insertNotificationIntoDatabase();
    }

    private void insertNotificationIntoDatabase() {
        List<NotificationBean> mList = SharedPreferenceHelper.getDataFromSharedPreferences(BaseActivity.this);
        SharedPreferenceHelper.putString(BaseActivity.this, Constants.NOTIFICATION_STORE, "");
        if (mList != null && mList.size() > 0) {
            for (final NotificationBean bean : mList) {
                Realm mRealm = Businessline.getRealmInstance();
                if (mRealm != null) {
                    mRealm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealm(bean);
                        }
                    });
                }
            }
        }
    }

    public void showSnackBar(View view) {
        Snackbar mSnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView textView1 = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }

    public void updateOverFlowMenuActionButton() {
        Log.i(TAG, "updateOverFlowMenuActionButton: ");
        if (txtViewOverFlowCount != null) {
            if (mUnreadArticleCount > 0 && mUnreadArticleCount < 100) {
                txtViewOverFlowCount.setText("" + mUnreadArticleCount);
                txtViewOverFlowCount.setVisibility(View.VISIBLE);
            } else if (mUnreadArticleCount > 99) {
                txtViewOverFlowCount.setText("99+");
                txtViewOverFlowCount.setVisibility(View.VISIBLE);
            } else {
                txtViewOverFlowCount.setVisibility(View.INVISIBLE);
            }
        }
    }

    private Intent shareAppIntent() {
        String mShareTitle = null;
        String mShareUrl = null;
        if (mShareTitle == null) {
            mShareTitle = "Download Businessline official app.";
        }
        if (mShareUrl == null) {
            mShareUrl = "https://play.google.com/store/apps/details?id=com.mobstac.thehindubusinessline";
        }
        Intent sharingIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = mShareTitle
                + ": " + mShareUrl;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                mShareTitle);
        sharingIntent
                .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE,
                mShareTitle);
        return Intent.createChooser(sharingIntent, "Share Via");
    }

    @Override
    public void onSectionClick(int sectionId, int parentSectionId, String sectionName) {
        if (mBottomNavigationView != null) {
            if (sectionId == 5 || parentSectionId == 5) {
                mBottomNavigationView.setSelectedItemId(R.id.menu_portfolio);
            }

            if (sectionId == 37 || parentSectionId == 37) {
                mBottomNavigationView.setSelectedItemId(R.id.menu_profile);
            }
        }
    }

    @Override
    public void onIndicesClick(boolean isBseClicked) {
        if (mBottomNavigationView != null) {
            if (isBseClicked) {
                selectedTabPosition = 0;
                mBottomNavigationView.setSelectedItemId(R.id.menu_market);
            } else {
                selectedTabPosition = 1;
                mBottomNavigationView.setSelectedItemId(R.id.menu_market);
            }
        }
    }

    public void setHomeButtonClickedListener(HomeButtonClickedListener homeButtonClickedListener) {
        this.mHomeButtonClickedListener = homeButtonClickedListener;
    }

    public class NotificationIncomingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: NotificationReceiver");
            if (intent != null && intent.getAction().equalsIgnoreCase(Constants.NOTIFICATION_INCOMING_FILTER)) {
                insertNotificationIntoDatabase();
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////  BL SUBSCRIPTION ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /** Holds String value of User Name, to know whether user has logged in or not*/
    protected static String mUserId;
    private static boolean mIsUserLoggedIn;
    private static boolean mHasFreePlan;
    private static boolean mHasSubscriptionPlan;

    protected boolean mIsUserThemeDay;

    protected boolean isUserLoggedIn() {
        mIsUserLoggedIn = THPPreferences.getInstance(this).isUserLoggedIn();
        return mIsUserLoggedIn;
    }

    protected boolean isHasFreePlan() {
        return mHasFreePlan;
    }

    protected boolean isHasSubscriptionPlan() {
        return mHasSubscriptionPlan;
    }


    private void login(int pos) {

        if (isUserLoggedIn() && !THPPreferences.getInstance(BaseActivity.this).isRelogginSuccess() && !NetUtils.isConnected(BaseActivity.this)) {
            if(mBottomNavigationView != null) {
                Alerts.noConnectionSnackBar(mBottomNavigationView, BaseActivity.this);
            }
        } else if (isUserLoggedIn() && !THPPreferences.getInstance(BaseActivity.this).isRelogginSuccess() && NetUtils.isConnected(BaseActivity.this)) {
            //Show AlertDialog
            ProgressDialog progress = new ProgressDialog(BaseActivity.this);
            progress.setMessage("Please wait, fetching user profile");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            //ReLogin
            IntentUtil.loginApiCall2(BaseActivity.this, new CallBackRelogin() {
                @Override
                public void OnSuccess() {
                    progress.dismiss();
                    if (pos == 1) {
                        THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_1;
//                        IntentUtil.openTHPScreen(BaseActivity.this, isUserLoggedIn(), 0, isHasFreePlan(), isHasSubscriptionPlan());
                    } else if (pos == 2) {
                        THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_2;
                        CharSequence selectedItemName = mBottomNavigationView.getMenu().findItem(mBottomNavigationView.getSelectedItemId()).getTitle();
                        IntentUtil.openTHPScreen(BaseActivity.this, isUserLoggedIn(), 1, isHasFreePlan(), isHasSubscriptionPlan(), selectedItemName.toString());
                    } else if (pos == 3) {
                        THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_3;
//                        IntentUtil.openTHPScreen(BaseActivity.this, isUserLoggedIn(), 2, isHasFreePlan(), isHasSubscriptionPlan());
                    } else if (pos == 4) {
                        THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_4;
                        if (isUserLoggedIn()) {
                            IntentUtil.openUserProfileActivity(BaseActivity.this, THPConstants.FROM_USER_PROFILE);
                        } else {
                            IntentUtil.openMemberActivity(BaseActivity.this, "");
                        }
                    }
                }

                @Override
                public void OnFailure() {
                    progress.dismiss();
                    if (!NetUtils.isConnected(BaseActivity.this)) {
                        if(mBottomNavigationView != null) {
                            Alerts.noConnectionSnackBar(mBottomNavigationView, BaseActivity.this);
                        }
                        return;
                    }
                    //Clear session
                    clearDatabaseAndSession();
                }
            });
        } else if (pos == 1) {
            THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_1;
//            IntentUtil.openTHPScreen(BaseActivity.this, isUserLoggedIn(), 0, isHasFreePlan(), isHasSubscriptionPlan());
        } else if (pos == 2) {
            THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_2;
            CharSequence selectedItemName = mBottomNavigationView.getMenu().findItem(mBottomNavigationView.getSelectedItemId()).getTitle();

            IntentUtil.openTHPScreen(BaseActivity.this, isUserLoggedIn(), 1, isHasFreePlan(), isHasSubscriptionPlan(), selectedItemName.toString());
        } else if (pos == 3) {
            THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_3;
//            IntentUtil.openTHPScreen(BaseActivity.this, isUserLoggedIn(), 2, isHasFreePlan(), isHasSubscriptionPlan());
        } else if (pos == 4) {
            THPConstants.FLOW_TAB_CLICK = THPConstants.TAB_4;
            if (isUserLoggedIn()) {
                IntentUtil.openUserProfileActivity(BaseActivity.this, THPConstants.FROM_USER_PROFILE);
            } else {
                IntentUtil.openMemberActivity(BaseActivity.this, "");
            }
        }
    }

    private void clearDatabaseAndSession() {
        Observable observable = ApiManager.clearDatabaseAsync(this);
        observable.map(v -> {
            // Clear THP Preference
            THPPreferences.getInstance(this).clearPref();

            // Facebook Logout
            if (AccessToken.getCurrentAccessToken() != null) {
                AccessToken.setCurrentAccessToken(null);
            }
            try {
                // Twitter Logout
                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                    CookieSyncManager.createInstance(this);
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeSessionCookie();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
            } catch (Exception ignore) {
                Log.i("", "");
            }
            // Google Logout
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getResources().getString(com.ns.thpremium.R.string.default_web_client_id))
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            if (mGoogleSignInClient != null) {
                GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
                if (googleAccount != null) {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(BaseActivity.this, task -> {
                                //Signed out
                            });
                }

            }
            return "";
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                    Alerts.showToast(this, "Error occurred. Please Relogin.");
                    // Need to refresh the Default Hindu Screens for Ads
                    THPPreferences.getInstance(this).setIsRefreshRequired(true);
                    //Open SignIn Page
                    IntentUtil.openSignInOrUpActivity(BaseActivity.this, "signIn");
                });

    }

    /**
     * Checks whether user has logged in or not.
     */
    public void checkUserLoggedIn() {


//        String token = CleverTapAPI.getDefaultInstance(getApplicationContext()).getDevicePushToken(PushType.FCM);
//        String cleverTapId = CleverTapAPI.getDefaultInstance(getApplicationContext()).getCleverTapID();

//        Log.i("Ashwani", "PushToken :: "+token);
//        Log.i("Ashwani", "cleverTapId :: "+cleverTapId);

        boolean isUserFromEurope = SharedPreferenceHelper.isUserFromEurope(this);
        if(isUserFromEurope) {
//            updateBottomLayout(false, true);
//            hideBottomAdView();
//            hideSubscriptionLayout();
            return;
        }

        mIsUserLoggedIn = THPPreferences.getInstance(this).isUserLoggedIn();

        boolean isRefreshRequired = THPPreferences.getInstance(this).isRefreshRequired();

        if(!mIsUserLoggedIn) {
            mHasFreePlan = false;
            mHasSubscriptionPlan = false;
            SharedPreferenceHelper.setUserPreferAdsFree(this, isUserFromEurope);
//            updateBottomLayout(false, mIsHidingBottomTab);
//            showHideCrownAndSubscription();
            return;
        }
        else {
            final boolean isUserAdsFree = THPPreferences.getInstance(this).isUserAdsFree();
            SharedPreferenceHelper.setUserPreferAdsFree(this, isUserAdsFree);
            if(isUserAdsFree) {
//                hideBottomAdView();
            }
        }



        ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    mUserId = userProfile.getUserId();

                    if(mIsUserLoggedIn) {
                        String loginId = THPPreferences.getInstance(this).getLoginId();
                        String loginPasswd = THPPreferences.getInstance(this).getLoginPasswd();
                        // Fetch latest userinfo from server
                        ApiManager.getUserInfo(this, BuildConfig.SITEID,
                                ResUtil.getDeviceId(this), mUserId, loginId, loginPasswd)
                                .subscribe(val->{
                                    Log.i("", "");
                                }, thr->{
                                    Log.i("", "");
                                });

                        // Fetch latest bookmark list from server
                        ApiManager.getRecommendationFromServer(this, mUserId,
                                NetConstants.RECO_bookmarks, ""+1000, BuildConfig.SITEID)
                                .subscribe(val->{
                                    Log.i("", "");
                                }, thr->{
                                    Log.i("", "");
                                });
                    }

                    mHasFreePlan = userProfile.isHasFreePlan();
                    mHasSubscriptionPlan = userProfile.isHasSubscribedPlan();

//                    updateBottomLayout(false, mIsHidingBottomTab);

//                    FragmentManager fm = getSupportFragmentManager();
//                    List<Fragment> fragments = fm.getFragments();
//                    if(fragments != null
//                            && ((fragments.get(fragments.size()-1) instanceof SearchFragment)
//                            || (fragments.get(fragments.size()-1) instanceof NotificationFragment))
//                    ) {
//                        return;
//                    }
//
//                    showHideCrownAndSubscription();

                    // Send User Profile to CleverTap
                    CleverTapUtil.cleverTapUpdateProfile(this, false, userProfile, isHasSubscriptionPlan(), isHasFreePlan());

                });

    }

}
