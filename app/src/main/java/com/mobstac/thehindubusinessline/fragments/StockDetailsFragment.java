/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Sep 23, 2014 
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.adapter.NiftyAndSensexAdapter;
import com.mobstac.thehindubusinessline.model.BSEData;
import com.mobstac.thehindubusinessline.model.BSETopGainer;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.view.ProgressView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class StockDetailsFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "BL_StockDetailsFragment";
    private int mTabIndex;
    private String mSortOrder, mSortBy;
    private ListView mStockList;
    private ProgressView mProgress;
    private int selectedPosition;
    private BSEData bseData;
    private BSEData nseData;
    private NiftyAndSensexAdapter mNsadapter;
    private GetTopGainerTask mGetTopGainerTask;
    private GetTopLoserTask mGetTopLoserTask;
    private LinearLayout mOptionLayout;
    private TextView mTopGainer;
    private TextView mTopLooser;
    private TextView mMainTextView;
    private boolean isExpand;
    private AdsBaseActivity mActivity;
    private TextView mActualPrice;
    private ImageView mProfitOrLossIcon;
    private TextView mChangeValue;
    private TextView mPercentageChange;
    private boolean isFragmentVisible;
    private TextView mLastUpdateDate;

    public StockDetailsFragment() {
    }

    public static StockDetailsFragment newInstance(int tag, String mSortBy, String mSortOrder) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("tag", tag);
        arguments.putString("sortBy", mSortBy);
        arguments.putString("sortOrder", mSortOrder);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AdsBaseActivity) context;
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setHasOptionsMenu(true);
        Bundle extras = getArguments();
        if (extras != null) {
            this.mTabIndex = extras.getInt("tag");
            this.mSortBy = extras.getString("sortBy");
            this.mSortOrder = extras.getString("sortOrder");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView: ");
        View mView = inflater.inflate(R.layout.stock_details_fragment, container, false);

        mStockList = (ListView) mView.findViewById(R.id.stock_list);
        LayoutInflater headerInflater = getLayoutInflater();
        View headerView = headerInflater.inflate(R.layout.stock_detail_heaader, mStockList, false);
        mStockList.addHeaderView(headerView, null, false);
        mProgress = (ProgressView) mView.findViewById(R.id.progress_container);
        mMainTextView = (TextView) headerView.findViewById(R.id.mainTextView);
        mTopGainer = (TextView) headerView.findViewById(R.id.topGainer);
        mTopGainer.setOnClickListener(this);
        mTopLooser = (TextView) headerView.findViewById(R.id.topLoser);
        mTopLooser.setOnClickListener(this);
        mOptionLayout = (LinearLayout) mView.findViewById(R.id.optionLayout);
        mMainTextView.setOnClickListener(this);

        mActualPrice = (TextView) mView.findViewById(R.id.actual_price);
        mProfitOrLossIcon = (ImageView) mView.findViewById(R.id.changeIcon);
        mChangeValue = (TextView) mView.findViewById(R.id.changesValue);
        mPercentageChange = (TextView) mView.findViewById(R.id.percentChange);

        mLastUpdateDate = (TextView) mView.findViewById(R.id.last_update_date);

        if (mTabIndex == 0) {
            ((TextView) mView.findViewById(R.id.sensexText)).setText("SENSEX");
        } else {
            ((TextView) mView.findViewById(R.id.sensexText)).setText("NIFTY");
        }

        loadStockContents();
        if (mActivity != null && isFragmentVisible) {
            String screenName;
            if (mTabIndex == 0) {
                screenName = getString(R.string.ga_stockdetail_bse_screen);
            } else {
                screenName = getString(R.string.ga_stockdetail_nse_screen);
            }
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), screenName);
            FlurryAgent.logEvent(screenName);
            FlurryAgent.onPageView();
            mActivity.createBannerAdRequest(true, false, Constants.THE_HINDU_URL);
        }
        return mView;
    }

    private void loadStockContents() {
        Log.i(TAG, "loadStockContents: ");
        mProgress.showProgress();
        mProgress.setVisibility(View.VISIBLE);
        mStockList.setVisibility(View.GONE);
        mNsadapter = new NiftyAndSensexAdapter(getActivity(), R.layout.nifty_sensex_row);
        mStockList.setAdapter(mNsadapter);
//		mQuery = new MobStacQuery(mTabIndex, getLoaderManager(), getActivity());
        if (mTabIndex == 0) {
            if (mSortBy != null && mSortOrder != null) {
                Bundle bundle = new Bundle();
                bundle.putString("sort_by", mSortBy);
                bundle.putString("sort_order", mSortOrder);
//				mQuery.setmQueryParams(bundle);
//				mQuery.setmEncodedPths(new String[]{"custom/bse/"});
            } else {
//				mQuery.setmEncodedPths(new String[]{"custom/bse/1001/"});
            }
        } else if (mTabIndex == 1) {
            if (mSortBy != null && mSortOrder != null) {
                Bundle bundle = new Bundle();
                bundle.putString("sort_by", mSortBy);
                bundle.putString("sort_order", mSortOrder);
//				mQuery.setmQueryParams(bundle);
//				mQuery.setmEncodedPths(new String[]{"custom/nse/"});
            } else {
//				mQuery.setmEncodedPths(new String[]{"custom/nse/NIFTY/"});
            }

        }
        if (selectedPosition == 0 && mTabIndex == 0) {
            if (mGetTopGainerTask == null) {
                mGetTopGainerTask = new GetTopGainerTask();
                mGetTopGainerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.BSETopGainer_URL);
            } else if (mGetTopGainerTask.getStatus() == AsyncTask.Status.PENDING || mGetTopGainerTask.getStatus() == AsyncTask.Status.FINISHED) {
                mGetTopGainerTask.cancel(true);
                mGetTopGainerTask = new GetTopGainerTask();
                mGetTopGainerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.BSETopGainer_URL);
            }

        }
        if (selectedPosition == 1 && mTabIndex == 0) {
            if (mGetTopLoserTask == null) {
                mGetTopLoserTask = new GetTopLoserTask();
                mGetTopLoserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.BSETopLoser_URL);
            } else if (mGetTopLoserTask.getStatus() == AsyncTask.Status.PENDING || mGetTopLoserTask.getStatus() == AsyncTask.Status.FINISHED) {
                mGetTopLoserTask.cancel(true);
                mGetTopLoserTask = new GetTopLoserTask();
                mGetTopLoserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.BSETopLoser_URL);
            }
        }

        if (mTabIndex == 0) {
            if (bseData == null) {
                new GetBSEDataTask().execute(Constants.BSE_URL);
            } else {
                mProgress.setVisibility(View.GONE);
                mStockList.setVisibility(View.VISIBLE);
                mNsadapter.addAll(bseData);
            }
        }
        if (selectedPosition == 0 && mTabIndex == 1) {
            if (mGetTopGainerTask == null) {
                mGetTopGainerTask = new GetTopGainerTask();
                mGetTopGainerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.NSETopGainer_URL);
            } else if (mGetTopGainerTask.getStatus() == AsyncTask.Status.PENDING || mGetTopGainerTask.getStatus() == AsyncTask.Status.FINISHED) {
                mGetTopGainerTask.cancel(true);
                mGetTopGainerTask = new GetTopGainerTask();
                mGetTopGainerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.NSETopGainer_URL);
            }

        }
        if (selectedPosition == 1 && mTabIndex == 1) {
            if (mGetTopLoserTask == null) {
                mGetTopLoserTask = new GetTopLoserTask();
                mGetTopLoserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.NSETopLoser_URL);
            } else if (mGetTopLoserTask.getStatus() == AsyncTask.Status.PENDING || mGetTopLoserTask.getStatus() == AsyncTask.Status.FINISHED) {
                mGetTopLoserTask.cancel(true);
                mGetTopLoserTask = new GetTopLoserTask();
                mGetTopLoserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.NSETopLoser_URL);
            }
        }
        if (mTabIndex == 1) {
            if (nseData == null) {
                new GetNSEDataTask().execute(Constants.NSE_URL);
            } else {
                mProgress.setVisibility(View.GONE);
                mStockList.setVisibility(View.VISIBLE);
                mNsadapter.addAll(nseData);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mActivity != null) {
            mActivity.updateOverFlowMenuActionButton();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainTextView:
                if (!isExpand)
                    mOptionLayout.setVisibility(View.VISIBLE);
                else mOptionLayout.setVisibility(View.GONE);
                isExpand = !isExpand;
                break;
            case R.id.topGainer:
                mMainTextView.setText("Top Gainers");
                mOptionLayout.setVisibility(View.GONE);
                loadTopGainerOrLooserData(0);
                break;
            case R.id.topLoser:
                mMainTextView.setText("Top Losers");
                mOptionLayout.setVisibility(View.GONE);

                loadTopGainerOrLooserData(1);
                break;
        }
    }

    private void loadTopGainerOrLooserData(int position) {
        mStockList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.showProgress();
        if (position == 0) {
            mSortBy = "percentageChange";
            mSortOrder = "desc";
            selectedPosition = 0;
        } else if (position == 1) {
            mSortBy = "percentageChange";
            mSortOrder = "asc";
            selectedPosition = 1;
        } else {
            mSortBy = null;
            mSortOrder = null;
            selectedPosition = 2;
        }
        loadStockContents();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint: ");
        isFragmentVisible = isVisibleToUser;
        if (mActivity != null && isVisibleToUser) {
            String screenName;
            if (mTabIndex == 0) {
                screenName = getString(R.string.ga_stockdetail_bse_screen);
            } else {
                screenName = getString(R.string.ga_stockdetail_nse_screen);
            }
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), screenName);
            FlurryAgent.logEvent(screenName);
            FlurryAgent.onPageView();
            mActivity.createBannerAdRequest(true, false, Constants.THE_HINDU_URL);
        }

    }

    private class GetTopGainerTask extends AsyncTask<String, Void, BSETopGainer> {
        private static final String TAG = "GetBseTopGainerTask";

        @Override
        protected BSETopGainer doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            BSETopGainer response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, BSETopGainer.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(BSETopGainer bseTopGainer) {
            super.onPostExecute(bseTopGainer);
            if (isCancelled()) {
                return;
            }
            if (!isAdded() && getActivity() != null) {
                return;
            }
            if (bseTopGainer != null && getActivity() != null) {
                final List<BSETopGainer.Data> list = bseTopGainer.getData();
                if (list != null && list.size() > 0) {
                    mProgress.setVisibility(View.GONE);
                    mStockList.setVisibility(View.VISIBLE);
                    mNsadapter.addAll(list);
                } else {
                    mProgress.setErrorText("Failed To Load Content !!");
                }
            } else {
                mProgress.setErrorText("Failed To Load Content !!");
            }
        }
    }

    private class GetTopLoserTask extends AsyncTask<String, Void, BSETopGainer> {
        private static final String TAG = "GetBseTopLoserTask";

        @Override
        protected BSETopGainer doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            BSETopGainer response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, BSETopGainer.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(BSETopGainer bseTopGainer) {
            super.onPostExecute(bseTopGainer);
            if (isCancelled()) {
                return;
            }
            if (!isAdded() && getActivity() != null) {
                return;
            }
            if (bseTopGainer != null && getActivity() != null) {
                final List<BSETopGainer.Data> list = bseTopGainer.getData();
                if (list != null && list.size() > 0) {
                    mProgress.setVisibility(View.GONE);
                    mStockList.setVisibility(View.VISIBLE);
                    mNsadapter.addAll(list);
                } else {
                    mProgress.setErrorText("Failed To Load Content !!");
                }
            } else {
                mProgress.setErrorText("Failed To Load Content !!");
            }
        }

    }

    private class GetNSEDataTask extends AsyncTask<String, Void, BSEData> {
        private static final String TAG = "NSEData";

        @Override
        protected BSEData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            BSEData response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, BSEData.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(BSEData s) {
            super.onPostExecute(s);
            if (isCancelled()) {
                return;
            }
            if (!isAdded() && getActivity() != null) {
                return;
            }

            if (s != null && getActivity() != null) {

                double latestPrice = s.getCp();
                double stockChange = s.getPer();
                double change = s.getCh();
                float changeIndicator;
                changeIndicator = (float) stockChange;
                String latestUpdatedData = NumberFormat.getNumberInstance(Locale.US).format(latestPrice);

                if (s.getDa() != null) {
                    mLastUpdateDate.setText((AppUtils.getDateFormateChange(s.getDa())));
                }

                mActualPrice.setText("₹ " + latestUpdatedData);
                if (changeIndicator < 0) {
                    mChangeValue.setText("" + change);
                    mPercentageChange.setText("(" + stockChange + "%" + ")");
                    mChangeValue.setTextColor(getResources().getColor(R.color.RedDown));
                    mPercentageChange.setTextColor(getResources().getColor(R.color.RedDown));
                    mProfitOrLossIcon.setImageResource(R.drawable.loss_arrow);

                } else if (changeIndicator > 0) {
                    mChangeValue.setText("" + change);
                    mPercentageChange.setText("(" + stockChange + "%" + ")");
                    mChangeValue.setTextColor(getResources().getColor(R.color.GreenUp));
                    mPercentageChange.setTextColor(getResources().getColor(R.color.GreenUp));
                    mProfitOrLossIcon.setImageResource(R.drawable.profit_arrow);
                } else {
                    mChangeValue.setText("N.A.");
                }

            } else {
                mProgress.setErrorText("Failed To Load Content !!");
            }

        }
    }

    private class GetBSEDataTask extends AsyncTask<String, Void, BSEData> {

        private static final String TAG = "BSEData";

        @Override
        protected BSEData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            BSEData response = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, BSEData.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(BSEData s) {
            super.onPostExecute(s);
            if (isCancelled()) {
                return;
            }
            if (!isAdded() && getActivity() != null) {
                return;
            }

            if (s != null && getActivity() != null) {
              /*  mProgress.setVisibility(View.GONE);
                mStockList.setVisibility(View.VISIBLE);
                mNsadapter.addAll(s);
                bseData = s;*/
                double latestPrice = s.getCp();
                double stockChange = s.getPer();
                double change = s.getCh();
                float changeIndicator;
                changeIndicator = (float) stockChange;
                String latestUpdatedData = NumberFormat.getNumberInstance(Locale.US).format(latestPrice);

                if (s.getDa() != null) {
                    mLastUpdateDate.setText((AppUtils.getDateFormateChange(s.getDa())));
                }

                mActualPrice.setText("₹ " + latestUpdatedData);
                if (changeIndicator < 0) {
                    mChangeValue.setText("" + change);
                    mPercentageChange.setText("(" + stockChange + "%" + ")");
                    mChangeValue.setTextColor(getResources().getColor(R.color.RedDown));
                    mPercentageChange.setTextColor(getResources().getColor(R.color.RedDown));
                    mProfitOrLossIcon.setImageResource(R.drawable.loss_arrow);

                } else if (changeIndicator > 0) {
                    mChangeValue.setText("" + change);
                    mPercentageChange.setText("(" + stockChange + "%" + ")");
                    mChangeValue.setTextColor(getResources().getColor(R.color.GreenUp));
                    mPercentageChange.setTextColor(getResources().getColor(R.color.GreenUp));
                    mProfitOrLossIcon.setImageResource(R.drawable.profit_arrow);
                } else {
                    mChangeValue.setText("N.A.");
                }
            } else {
                mProgress.setErrorText("Failed To Load Content !!");
            }
        }
    }




}
