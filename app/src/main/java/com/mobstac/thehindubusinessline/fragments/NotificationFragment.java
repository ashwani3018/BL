package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.adapter.NotificationAdapter;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.listener.NotificatiionBookmarkButtonClick;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.NotificationBean;
import com.mobstac.thehindubusinessline.model.SearchArticleById;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment implements NotificatiionBookmarkButtonClick {

    private final String TAG = "NotificationFragment";
    private RecyclerView mRecyclerView;
    private TextView mNoNotification;
    private AdsBaseActivity mMainActivity;
    private LinearLayout mSomeBookmarksLayout;
    private Realm mRealm;
    private NotificationAdapter mNotificationAdapter;
    private RealmResults<NotificationBean> mNotificationArticleList;
    private Dialog mWarningDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (AdsBaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        if (mMainActivity != null) {
            mMainActivity.showBottomNavigationBar(false);
        }
        mRealm = Businessline.getRealmInstance();

        RealmResults<BookmarkBean> mBookmarkList = mRealm.where(BookmarkBean.class).equalTo("isRead", false).findAll();
        mNotificationArticleList = mRealm.where(NotificationBean.class).findAllSorted("insertionTime", Sort.DESCENDING);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_notifications);
        mNoNotification = (TextView) view.findViewById(R.id.no_notification);
        mSomeBookmarksLayout = (LinearLayout) view.findViewById(R.id.some_bookmarks);

        if (mBookmarkList.size() > 0) {
            mSomeBookmarksLayout.setVisibility(View.VISIBLE);
        }

        if (mNotificationArticleList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));
            mRecyclerView.setHasFixedSize(true);
            mNotificationAdapter = new NotificationAdapter(mMainActivity, mNotificationArticleList, true, this);
            mRecyclerView.setAdapter(mNotificationAdapter);
        } else {
            mNoNotification.setVisibility(View.VISIBLE);
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");
//        mMainActivity.hideHomeBottomAdBanner();
//        mMainActivity.showRosBottomBanner();
        mMainActivity.createBannerAdRequest(true, false, "");
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "Notification Screen");
        FlurryAgent.logEvent("Notification Screen");
        FlurryAgent.onPageView();

        if (mNotificationAdapter != null) {
            mNotificationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.i(TAG, "onCreateOptionsMenu: ");

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMainActivity != null) {
            mMainActivity.hideToolbarLogo();
            mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
            mMainActivity.setToolbarTitle(getResources().getString(R.string.title_notifications));
        }
        if (menu != null) {
            menu.findItem(R.id.action_overflow).setVisible(false);
            if (Businessline.getRealmInstance().where(NotificationBean.class).findAllSorted("insertionTime", Sort.DESCENDING).size() > 0) {
                menu.findItem(R.id.action_clear_all).setVisible(true);
            } else {
                menu.findItem(R.id.action_clear_all).setVisible(false);
            }

        }
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()) {
            case android.R.id.home:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Notifications: Back button clicked",
                        getString(R.string.title_notifications));
                FlurryAgent.logEvent("Notifications: Back button clicked");
                mMainActivity.finish();
                return true;
            case R.id.action_clear_all:
                createWarningDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void bookMarkButtonClicked(String articleId) {
        new GetSpecificArticleTask().execute(articleId);
    }

    private void createWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setMessage("Do you want to delete all articles in this screen ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Notifications: ClearAll button clicked",
                        getString(R.string.title_notifications));
                FlurryAgent.logEvent("Notifications: ClearAll button clicked");
                DatabaseJanitor.deleteAllNotificationArticle();
                mRecyclerView.setVisibility(View.GONE);
                mNoNotification.setVisibility(View.VISIBLE);
                mMainActivity.invalidateOptionsMenu();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        mWarningDialog = builder.create();
        mWarningDialog.show();
    }

    private class GetSpecificArticleTask extends AsyncTask<String, Void, SearchArticleById> {

        @Override
        protected SearchArticleById doInBackground(String... params) {
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(RetrofitAPICaller.SEARCH_BY_ARTICLE_ID_URL + params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                //Get SingUpDetail
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Gson gson = new Gson();
                SearchArticleById articleDetailModel = gson.fromJson(response.toString(), SearchArticleById.class);
                return articleDetailModel;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(SearchArticleById articleDeatils) {
            super.onPostExecute(articleDeatils);
            if (getActivity() != null && isAdded()) {
                if (articleDeatils != null && articleDeatils.getS() == 1) {
                    ArticleDetail bean = articleDeatils.getData().get(0);
                    String multiMediaUrl = null;
                    if (bean.getArticleType().equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                        multiMediaUrl = bean.getYoutube_video_id();
                    }
                    if (bean.getArticleType().equalsIgnoreCase(Constants.TYPE_AUDIO)) {
                        multiMediaUrl = bean.getAudioLink();
                    }
                    ArticleUtil.markAsBookMark(mRealm, bean.getAid(), bean.getSid(), bean.getSname(), bean.getPd(),
                            bean.getOd(), bean.getPid(), bean.getTi(), bean.getAu(), bean.getAl(), bean.getBk(),
                            bean.getGmt(), bean.getDe(), bean.getLe(), bean.getHi(),
                            ArticleUtil.getImageBeanList(bean.getMe()), System.currentTimeMillis(),
                            false, bean.getAdd_pos(), multiMediaUrl,
                            bean.getArticleType(), bean.getVid(), bean.getParentId(), bean.getLocation());

                    AppUtils.showToast(getActivity(), getActivity().getResources().getString(R.string.added_to_bookmark));
                    mNotificationAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
