package com.mobstac.thehindubusinessline.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.adapter.NavigationExpandableListViewAdapter;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.fragments.NewsDigestFragment;
import com.mobstac.thehindubusinessline.fragments.SlidingSectionFragment;
import com.mobstac.thehindubusinessline.listener.OnExpandableListViewItemClickListener;
import com.mobstac.thehindubusinessline.model.NotificationBean;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.netoperation.retrofit.ServiceFactory;
import com.ns.thpremium.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.comscore.android.id.IdHelperAndroid.md5;

public class MainActivity extends AdsBaseActivity implements OnExpandableListViewItemClickListener {

    private final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout mNavigationHeader;
    private ExpandableListView mNavigationExpandableListView;
    private NavigationExpandableListViewAdapter mNavigationExpandableListViewAdapter;
    private List<SectionTable> mActualSectionList;


    private RealmResults<SectionTable> mBurgerList;
    private RealmResults<SectionTable> mOverriddenSections;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Assigning base url
        if(BuildConfig.IS_PRODUCTION) {
            ServiceFactory.BASE_URL = BuildConfig.PRODUCTION_BASE_URL;
        } else {
            ServiceFactory.BASE_URL = BuildConfig.STATGGING_BASE_URL;
        }

        mActualSectionList = new ArrayList<>();
        mBurgerList = DatabaseJanitor.getBurgerList(0);
        mOverriddenSections = DatabaseJanitor.getOverriddenSections(0);
        mBurgerList.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SectionTable>>() {
            @Override
            public void onChange(RealmResults<SectionTable> sectionTables, OrderedCollectionChangeSet changeSet) {
                if (mBurgerList.isLoaded()) {
                    mActualSectionList.addAll(sectionTables);
                }
                if (mOverriddenSections.isLoaded()) {
                    setupDrawer();
                }
            }
        });

        mOverriddenSections.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SectionTable>>() {
            @Override
            public void onChange(RealmResults<SectionTable> sectionTables, OrderedCollectionChangeSet changeSet) {
                mOverriddenSections = sectionTables;
                if (mOverriddenSections.isLoaded() && mBurgerList.isLoaded()) {
                    setupDrawer();
                }
            }
        });

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        setBottomNavigationViewSelectedItem(R.id.menu_home);
        AppUtils.disableShiftMode(mBottomNavigationView);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");


        View view = mToolbar.getChildAt(1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Fragment> fragmetList = getSupportFragmentManager().getFragments();
                Fragment topFragment = null;
                if (fragmetList != null && fragmetList.size() > 0) {
                    topFragment = fragmetList.get(fragmetList.size() - 1);
                }
                if (topFragment != null && topFragment instanceof SlidingSectionFragment) {
                    ((SlidingSectionFragment) topFragment).setPagerPosition(0);
                } else {
                    SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL,0, false, 0, "Home");
                    pushView(fragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, true);
                }
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationExpandableListView = (ExpandableListView) findViewById(R.id.navigation_expandable_listview);
        mNavigationHeader = (RelativeLayout) findViewById(R.id.navigationParent);
        mNavigationHeader.setEnabled(false);
        mNavigationHeader.setClickable(false);
        mNavigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        setupDrawer();
        loadHomeFragment();
        loadNotification();


        deleteSevenDaysOldNotificationDataFromDatabase();

        if (!NetworkUtils.isNetworkAvailable(this)) {
            showSnackBar(mDrawerLayout);
        }

        String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();

        SharedPreferenceHelper.putString(MainActivity.this, "testAds", deviceId);

    }

    private void loadNotification() {
        if (getIntent() != null && getIntent().hasExtra(Constants.LOAD_NOTIFICATIONS)) {
            final Bundle arguments = getIntent().getExtras();
            String action = arguments.getString("ns_action");
            String type = arguments.getString("ns_type_PN");

            GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, "Notification: ", "Notification: Open from status bar", "MainAcctivitty");
            FlurryAgent.logEvent("Notification: Open from status bar");
            try {
                if (type.equalsIgnoreCase(Constants.ADVERTISEMENT)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(action));
                    startActivity(intent);
                    return;
                } else if (type.equalsIgnoreCase(Constants.ARTICLE)) {
                    setToolbarTitle("");
                    if (Boolean.parseBoolean(arguments.getString("ns_has_body"))) {
                        DatabaseJanitor.updateNotificationArticleReadFlag(Integer.parseInt(arguments.getString("ns_article_id")));
//                        ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(Integer.parseInt(arguments.getString("ns_article_id")), "0",
//                                action, false, null);
//                        pushSection(fragment);
                        ArticleUtil.launchDetailActivity(this, Integer.parseInt(arguments.getString("ns_article_id")),
                                "0", action, false, null);
                    } else {
//                        NotificationFragment notificationFragment = new NotificationFragment();
//                        pushSection(notificationFragment);
                        Intent intent = new Intent(this, NotificationActivity.class);
                        startActivity(intent);
                    }
                    return;

                } else if (type.equalsIgnoreCase(Constants.DIGEST)) {
                    if (action == null) {
                        action = RetrofitAPICaller.NEWS_DIGEST_URL;
                    }

                    final NewsDigestFragment newsDigestFragment = new NewsDigestFragment();
                    Bundle data = new Bundle();
                    data.putString(Constants.URL, action);
                    newsDigestFragment.setArguments(data);
                    pushSection(newsDigestFragment);

                    return;

                } else if (type.equalsIgnoreCase(Constants.UPGRADE)) {
                    try {
                        if (action != null)
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(action)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        anfe.printStackTrace();
                    }
                    return;
                } else if (type.equalsIgnoreCase(Constants.SECTION)) {
                    String parentId = arguments.getString("ns_parent_id");
                    String sectionId = arguments.getString("ns_section_id");
                    String sectionName = arguments.getString("ns_sec_name");
                    int sectionPosition;
                    if (parentId.equalsIgnoreCase("0")) {
                        sectionPosition = DatabaseJanitor.getSectionPosition(Integer.parseInt(sectionId));
                        if (sectionPosition >= 0) {
                            SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL,sectionPosition, false, 0, sectionName);
                            pushSection(fragment);
                        }
                    } else {
                        sectionPosition = DatabaseJanitor.getSubsectionPostion(Integer.parseInt(parentId), Integer.parseInt(sectionId));
                        if (sectionPosition >= 0) {
                            SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL,sectionPosition, true, Long.parseLong(parentId), sectionName);
                            pushSection(fragment);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupDrawer() {
        if (mActualSectionList.size() <= 0) {
            return;
        }
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(drawerToggle);

        for (SectionTable overReddenSection : mOverriddenSections) {
            int position = overReddenSection.getOverridePriority();
            if (position < mActualSectionList.size()) {
                mActualSectionList.add(position, overReddenSection);
            } else {
                mActualSectionList.add(overReddenSection);
            }
        }

        mNavigationExpandableListViewAdapter = new NavigationExpandableListViewAdapter(this, mActualSectionList, this);
        mNavigationExpandableListView.setAdapter(mNavigationExpandableListViewAdapter);

    }

    @Override
    public void onExpandButtonClick(int groupPostion, boolean isExpanded) {
        FlurryAgent.logEvent("Navigation drawer Expand Button Click");
        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(MainActivity.this, "Navigation Screen", "Navigation drawer Expand Button Click", "Drawer View");

        if (isExpanded) {

            mNavigationExpandableListView.collapseGroup(groupPostion);
        } else {
            mNavigationExpandableListView.expandGroup(groupPostion);
        }

    }

    @Override
    public void onGroupNameTextClick(int groupPostion, boolean isExpanded) {
        String lastSection = "";
        if (mActualSectionList.get(groupPostion).getSecName().equalsIgnoreCase(
                MainActivity.this.getResources().getString(R.string.info_aboutus))
                || mActualSectionList.get(groupPostion).getSecName().equalsIgnoreCase(
                MainActivity.this.getResources().getString(R.string.info_help))
                || mActualSectionList.get(groupPostion).getSecName().equalsIgnoreCase(
                MainActivity.this.getResources().getString(R.string.info_termsconditions))
                || mActualSectionList.get(groupPostion).getSecName().equalsIgnoreCase(
                MainActivity.this.getResources().getString(R.string.privacy_policy))) {
            Intent intent = new Intent(MainActivity.this, StaticActivity.class);
            intent.putExtra(StaticActivity.ARG_PARAM1, mActualSectionList.get(groupPostion).getSecName());
            startActivity(intent);
            lastSection = getLastDisplayedPosition();
            sendTrackedEvents("Hamburger - " + mActualSectionList.get(groupPostion).getSecName() + " - " + lastSection);
            mDrawerLayout.closeDrawers();
//            return;
        } else if (mActualSectionList.get(groupPostion).getSecName().equalsIgnoreCase(Constants.NEWS_DIGEST)) {
            final NewsDigestFragment newsDigestFragment = new NewsDigestFragment();
            Bundle data = new Bundle();
            data.putString(Constants.URL, RetrofitAPICaller.NEWS_DIGEST_URL);
            newsDigestFragment.setArguments(data);
            pushSection(newsDigestFragment);
            lastSection = getLastDisplayedPosition();
            sendTrackedEvents("Hamburger - " + mActualSectionList.get(groupPostion).getSecName() + " - " + lastSection);
            mDrawerLayout.closeDrawers();

        } else {
            int clickedPosition = DatabaseJanitor.getSectionPosition(mActualSectionList.get(groupPostion).getSecId());
            if (clickedPosition >= 0) {
                List<Fragment> fragmetList = getSupportFragmentManager().getFragments();
                Fragment topFragment = null;
                if (fragmetList != null && fragmetList.size() > 0) {
                    topFragment = fragmetList.get(fragmetList.size() - 1);
                }
                if (topFragment != null && topFragment instanceof SlidingSectionFragment) {
                    lastSection = getLastDisplayedPosition();
                    ((SlidingSectionFragment) topFragment).setPagerPosition(clickedPosition);
                } else {
                    SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL,clickedPosition, false, 0, mActualSectionList.get(groupPostion).getSecName());
                    pushView(fragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, true);
                    lastSection = getLastDisplayedPosition();
                }
            }
        }
        if (lastSection.isEmpty()) {
            sendTrackedEvents("Hamburger - " + mActualSectionList.get(groupPostion).getSecName() + " - Home");
        } else {
            sendTrackedEvents("Hamburger - " + mActualSectionList.get(groupPostion).getSecName() + " - " + lastSection);
        }
        mDrawerLayout.closeDrawers();

    }

    @Override
    public void onChildClick(int groupPostion, int childPosition, long parentId) {
        SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL,childPosition, true, parentId, mActualSectionList.get(groupPostion).getSecName());
        pushView(fragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, false);
        String lastSection = getLastDisplayedPosition();
        sendTrackedEvents("Hamburger - " +
                mActualSectionList.get(groupPostion).getSubSectionList().get(childPosition).getSecName() +
                " - " + lastSection);
        mDrawerLayout.closeDrawers();
    }

    private String getLastDisplayedPosition() {
        int pos = SlidingSectionFragment.mViewPager.getCurrentItem();
        String lastSection = (String) SlidingSectionFragment.mViewPager.getAdapter().getPageTitle(pos);
        if (lastSection.isEmpty()) {
            lastSection = "Home";
        }
        return lastSection;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null) {
            drawerToggle.syncState();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        createOverFlowMenuOption(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return false;

            case R.id.action_bookmarks:
                return false;

            case R.id.action_search:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, getString(R.string.ga_action), "Search: Search Button Clicked ", "Main Activity");
                FlurryAgent.logEvent("Search: Search Button clicked");
                Intent intent =new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("search_key", true);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setSubtitle("");
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        FlurryAgent.onStartSession(this, getResources().getString(R.string.flurryAppKey));
        FlurryAgent.onPageView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setBottomNavigationViewSelectedItem(R.id.menu_home);
        // Checks whether user has logged in or not.
        checkUserLoggedIn();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }


/*
    */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("VHP", "Activty onDestroyCHECK: hit");
    }

    public void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    SlidingSectionFragment fragment;

    private void loadHomeFragment() {
        if (mToolbar != null) {
            mToolbar.setTitle("");
            mToolbar.setSubtitle("");
        }
        fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL,0, false, 0, "Home");
        pushView(fragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, true);
    }

    public void hideToolbarLogo() {
        if (mToolbar != null) {
            mToolbar.setLogo(null);
//            invalidateOptionsMenu();
        }
    }

    public void setToolbarLogo(int id) {
        if (mToolbar != null) {
            mToolbar.setLogo(id);
//            invalidateOptionsMenu();
        }
    }

    public void setToolbarBackButton(@DrawableRes int resId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(resId);
            lockDrawer();
//            invalidateOptionsMenu();
            mToolbar.setLogo(null);
        }
    }

    public void setToolbarHomeButton() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.mipmap.menu);
            unlockDrawer();
//            invalidateOptionsMenu();
        }
    }

    public void setToolbarTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
//            invalidateOptionsMenu();
        }
    }

    public void setToolbarBackground(int color) {
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(color);
//            invalidateOptionsMenu();
        }
    }

    public void toggleDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }


    public void pushFragmentToBackStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
    }


    public void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public void unlockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void deleteSevenDaysOldNotificationDataFromDatabase() {
        long currentTimeInMilliSecconds = System.currentTimeMillis();
        long sevenDaysInMilliSeconds = 7 * 24 * 60 * 60 * 1000;
        final long timeDifferenc = currentTimeInMilliSecconds - sevenDaysInMilliSeconds;
        final Realm mRealm = Businessline.getRealmInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<NotificationBean> mRealmResults = realm.where(NotificationBean.class).lessThan("insertionTime", timeDifferenc).findAll();
                mRealmResults.deleteAllFromRealm();
            }
        });
//        RealmResults<NotificationBean> mRealmResults = mRealm.where(NotificationBean.class).lessThan("insertionTime", timeDifferenc).findAll();
    }

    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                Toast.makeText(MainActivity.this, "show msg ", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onKeyUp(keycode, e);
    }

    private void sendTrackedEvents(String event) {
        Log.d("Analytics", event);
        FlurryAgent.logEvent(event);
        GoogleAnalyticsTracker
                .setGoogleAnalyticsEvent(
                        MainActivity.this,
                        "Hamburger",
                        "Clicked",
                        event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setBottomNavigationViewSelectedItem(R.id.menu_home);
        if(intent != null && intent.getExtras() != null) {
            int clickPosition = intent.getExtras().getInt("clickPosition");
            changeFragmenAtIndex(clickPosition);
        }
    }

    public void changeFragmenAtIndex(int pos) {
        fragment.changePagerAtIndex(pos);
    }

}


