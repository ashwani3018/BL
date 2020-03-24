/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Nov 21, 2014 
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.adapter.ForexAdapter;
import com.mobstac.thehindubusinessline.model.ForexData;
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
import java.util.List;

public class ForexFragment extends Fragment {

    private ForexAdapter mAdpater;
    private List<ForexData.Data> forexDatas;
    private ListView listView;
    private GetForexDataTask mGetForexDataTask;
    private ProgressView mProgressView;
    private AdsBaseActivity mActivity;
    private boolean isFragmentVisible;
    private TextView mLastUpdateDate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AdsBaseActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_forex, container, false);
        listView = ((ListView) layoutView.findViewById(R.id.forex_list));
        mProgressView = (ProgressView) layoutView.findViewById(R.id.progress_container);
        mLastUpdateDate = (TextView) layoutView.findViewById(R.id.last_update_date);
        getForexItem();
        if (mActivity != null && isFragmentVisible) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_forex_screen));
            FlurryAgent.logEvent(getString(R.string.ga_forex_screen));
            FlurryAgent.onPageView();
            mActivity.createBannerAdRequest(true, false, Constants.THE_HINDU_URL);
        }
        return layoutView;
    }

    private void getForexItem() {
        if (mGetForexDataTask == null) {
            mGetForexDataTask = new GetForexDataTask();
            mGetForexDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.ForexData_URL);
        } else if (mGetForexDataTask.getStatus() == AsyncTask.Status.PENDING || mGetForexDataTask.getStatus() == AsyncTask.Status.FINISHED) {
            mGetForexDataTask.cancel(true);
            mGetForexDataTask = new GetForexDataTask();
            mGetForexDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.ForexData_URL);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        if (mActivity != null && isVisibleToUser) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_forex_screen));
            FlurryAgent.logEvent(getString(R.string.ga_forex_screen));
            FlurryAgent.onPageView();
            mActivity.createBannerAdRequest(true, false, Constants.THE_HINDU_URL);
        }
    }

    private class GetForexDataTask extends AsyncTask<String, Void, ForexData> {

        @Override
        protected ForexData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            ForexData response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, ForexData.class);
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
        protected void onPostExecute(ForexData forexData) {
            super.onPostExecute(forexData);
            if (isCancelled()) {
                return;
            }

            if (forexData != null) {
                mLastUpdateDate.setText(forexData.getLastUpdatedDate());
                mProgressView.setVisibility(View.GONE);
                forexDatas = forexData.getData();
                listView.setVisibility(View.VISIBLE);
                mAdpater = new ForexAdapter(getActivity(), forexDatas);
                listView.setAdapter(mAdpater);
            } else {
                mProgressView.setErrorText("Failed To Load Content !!");
            }
        }
    }
}
