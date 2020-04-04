package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.adapter.BookmarkAdapter;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.ImageBean;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.old.database.db.HinduProvider;
import com.old.database.to.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;


public class BookmarksFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int LOADER_ID = 0;
    private final String TAG = "BookmarksFragment";
    private AdsBaseActivity mMainActivity;
    private RecyclerView mRecyclerView;
    private BookmarkAdapter mBookmarkAdapter;
    private EditText mSearchTextView;
    private ImageView mCloseButtom;
    private LinearLayout mSearchParentLayout;
    private LinearLayout mNoBookmarksLayout;
    private OrderedRealmCollection<BookmarkBean> mBookmarkArticlesList;
    private ImageView mSearchImageView;
    private TextView mSearchResultTextView;
    private Dialog mWarningDialog;
    private String mFrom;

    public BookmarksFragment() {
        // Required empty public constructor
    }

    public static BookmarksFragment getInstance(String from) {
        BookmarksFragment fragment = new BookmarksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdsBaseActivity) {
            mMainActivity = (AdsBaseActivity) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AdsBaseActivity) {
            mMainActivity = (AdsBaseActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        if (!SharedPreferenceHelper.getBoolean(getActivity(), Constants.OLD_DATABASE, false)) {
            getActivity().getLoaderManager().initLoader(LOADER_ID, null, this);
        }

        mBookmarkArticlesList = DatabaseJanitor.getBookmarksArticles();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_bookmarks);
        mSearchImageView = (ImageView) view.findViewById(R.id.searchResultImage);
        mSearchResultTextView = (TextView) view.findViewById(R.id.searchResultMsg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mBookmarkAdapter == null) {
            mBookmarkAdapter = new BookmarkAdapter(getActivity(), mBookmarkArticlesList, BookmarksFragment.this, mMainActivity);
        }
        mRecyclerView.setAdapter(mBookmarkAdapter);
        mSearchParentLayout = (LinearLayout) view.findViewById(R.id.searchLayout);
        mNoBookmarksLayout = (LinearLayout) view.findViewById(R.id.noBookmarks);
        mSearchTextView = (EditText) view.findViewById(R.id.searchText);
        mSearchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    mBookmarkAdapter.setBookmarkList(mBookmarkArticlesList);
                    if (mBookmarkArticlesList.size() <= 0) {
                        showNoBookmarkView(false);
                    } else {
                        mNoBookmarksLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Realm mRealm = Businessline.getRealmInstance();
                    OrderedRealmCollection<BookmarkBean> bookmarkList = mRealm.where(BookmarkBean.class)
                            .contains("ti", mSearchTextView.getText().toString(), Case.INSENSITIVE)
                            .or()
                            .contains("de", mSearchTextView.getText().toString(), Case.INSENSITIVE)
                            .findAllSorted("bookmarkDate", Sort.DESCENDING);

                    mBookmarkAdapter.setBookmarkList(bookmarkList);
                    setVisibilityOfViews(bookmarkList);
                }
            }
        });
        mSearchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AppUtils.hideKeyBoard(getActivity(), mSearchTextView);
                    return true;
                }
                return false;
            }
        });
        if (mBookmarkArticlesList.size() <= 0) {
            showNoBookmarkView(false);
        }
//        mMainActivity.hideHomeBottomAdBanner();
//        mMainActivity.showRosBottomBanner();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCloseButtom = (ImageView) view.findViewById(R.id.close_button);
        mCloseButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTextView.setText("");
                AppUtils.hideKeyBoard(getActivity(), v);
             /*   if (mBookmarkAdapter != null) {
                    mBookmarkAdapter.setBookmarkList(mBookmarkArticlesList);
                    if (mBookmarkArticlesList.size() <= 0) {
                        showNoBookmarkView(false);
                    } else {
                        mNoBookmarksLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                }*/
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mMainActivity != null) {
            mMainActivity.createBannerAdRequest(true, false, "");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        FlurryAgent.logEvent("Read Later Screen");
        FlurryAgent.onPageView();
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "Read Later Screen");
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
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMainActivity != null) {
            mMainActivity.setToolbarTitle("Read Later");
            mMainActivity.hideToolbarLogo();
            mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
        }
        if (menu != null) {
            menu.findItem(R.id.action_overflow).setVisible(false);
            if (DatabaseJanitor.getBookmarksArticles().size() > 0) {
                menu.findItem(R.id.action_clear_all).setVisible(true);
            } else {
                menu.findItem(R.id.action_clear_all).setVisible(false);
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Bookmark: Back button clicked",
                        "Read Later");
                FlurryAgent.logEvent("Bookmark: Back button clicked");
                getActivity().finish();
                return true;
            case R.id.action_clear_all:
                createWarningDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppUtils.hideKeyBoard(getActivity(), mSearchTextView);
    }

    private void setVisibilityOfViews(OrderedRealmCollection<BookmarkBean> mBookmarkArticlesList) {
        if (mBookmarkArticlesList.size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNoBookmarksLayout.setVisibility(View.VISIBLE);
            mSearchResultTextView.setVisibility(View.VISIBLE);
            mSearchResultTextView.setText("Oops! there is no results found for your search.");
            mSearchImageView.setVisibility(View.GONE);
        } else {
            mNoBookmarksLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), HinduProvider.FAVORITE_ARTICLE_URI, null, null, null, "_id DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG, "onLoadFinished: ");
        if (cursor != null) {
            Gson gson = new Gson();
            Realm mRealm = Businessline.getRealmInstance();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.i(TAG, "onLoadFinished: " + cursor.getCount());
                String articleId = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.ARTICLE_ID));
                String sectionId = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.SECTION_CONTENT_ID));
                String publish_date = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.ARTICLE_PUBLISH_DATE));
                String origin_date = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.ORIGIN_DATE));
                String priority = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.PRIORITY_OF_ARTICLE));
                String title = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.ARTICLE_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.AUTHOR));
                String article_link = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.ARTICLE_LINK));
                String imageUrl = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.HOME_IMG_URL));
                String description = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.ARTICLE_DESCRIPTION));
                String article_lead = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.ARTICLE_LEAD));
                String section_name = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.SECTION_CONTENT_NAME));
                String media = cursor.getString(cursor.getColumnIndex(Article.ArticleColumns.MEDIA));
                RealmList<ImageBean> imageBean = gson.fromJson(media, new TypeToken<RealmList<ImageBean>>() {
                }.getType());
                Log.i(TAG, "onLoadFinished: PD " + publish_date);
                Log.i(TAG, "onLoadFinished: OD " + origin_date);
                Long originDate = AppUtils.changeStringToMillis(origin_date);
                String result = "";
                try {
                    Date date = new Date(originDate - TimeUnit.HOURS.toMillis(5) - TimeUnit.MINUTES.toMillis(30));
                    result = "" + new SimpleDateFormat(AppUtils.yyyy_MM_dd_HH_mm_ss).format(date);
                    Log.i(TAG, "onLoadFinished: New Date" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(new BookmarkBean(Integer.valueOf(articleId), sectionId, section_name, publish_date,
                        origin_date, priority, title, author, article_link, null, result, description,
                        article_lead, 0, imageBean, System.currentTimeMillis(), false, 0, "", Constants.TYPE_ARTICLE, null,
                        "0", ""));
                mRealm.commitTransaction();
                cursor.moveToNext();
            }
            SharedPreferenceHelper.putBoolean(getActivity(), Constants.OLD_DATABASE, true);

            mBookmarkArticlesList = DatabaseJanitor.getBookmarksArticles();
            if (mBookmarkArticlesList.size() <= 0) {
                showNoBookmarkView(false);
            } else {
                mNoBookmarksLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            if (mBookmarkAdapter != null) {
                mBookmarkAdapter.setBookmarkList(mBookmarkArticlesList);
            } else {
                mBookmarkAdapter = new BookmarkAdapter(getActivity(), mBookmarkArticlesList, this, mMainActivity);
                mRecyclerView.setAdapter(mBookmarkAdapter);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void showNoBookmarkView(boolean isFromAdapter) {
        if (isFromAdapter) {
            if (DatabaseJanitor.getBookmarksArticles().size() > 1) {
                mRecyclerView.setVisibility(View.GONE);
                mNoBookmarksLayout.setVisibility(View.VISIBLE);
                mSearchResultTextView.setVisibility(View.VISIBLE);
                mSearchResultTextView.setText("Oops! there is no results found for your search.");
                mSearchImageView.setVisibility(View.GONE);
            } else {
                mSearchParentLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mNoBookmarksLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mSearchParentLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mNoBookmarksLayout.setVisibility(View.VISIBLE);
        }

    }

    private void createWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to delete all articles in this screen ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Bookmark: ClearAll button clicked",
                        "Clear All");
                FlurryAgent.logEvent("Bookmark: ClearAll button clicked");
                DatabaseJanitor.deleteAllBookmarksArticle();
                mSearchParentLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mNoBookmarksLayout.setVisibility(View.VISIBLE);
                if (mMainActivity != null) {
                    mMainActivity.invalidateOptionsMenu();
                }
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

}
