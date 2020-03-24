package com.ns.loginfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.netoperation.net.ApiManager;
import com.netoperation.util.UserPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.alerts.Alerts;
import com.ns.thpremium.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFragmentTHP extends Fragment {

    public abstract int getLayoutRes();

    protected boolean mIsOnline;
    protected boolean mIsVisible;
    protected int mSize = 30;
    protected String mUserId;
    protected static String mUserEmail;
    protected static String mUserPhone;

    protected BaseAcitivityTHP mActivity;

    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    protected boolean mIsDayTheme = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDayTheme = UserPref.getInstance(getActivity()).isUserThemeDay();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutRes(), container, false);
        netCheck(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisible = isVisibleToUser;
    }

    private void netCheck(View rootview) {
        mDisposable.add(ReactiveNetwork
                .observeNetworkConnectivity(getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if(connectivity.state() == NetworkInfo.State.CONNECTED) {
                        mIsOnline = true;
                    }
                    else {
                        mIsOnline = false;
                        if(mIsVisible && rootview!=null) {
                            //Alerts.noInternetSnackbar(rootview);
                            noConnectionSnackBar(getView());
                        }
                    }

                    Log.i("", "");
                }));
    }


    ProgressDialog progress;
    protected void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
        }
        progress.setTitle(getString(R.string.please_wait));
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    protected void hideProgressDialog() {
        if (progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.clear();
    }

    protected void readUserData() {
        if(mUserEmail == null && mUserPhone == null) {
            ApiManager.getUserProfile(getActivity())
                    .subscribe(userProfile -> {
                        mUserPhone = userProfile.getContact();
                        mUserEmail = userProfile.getEmailId();
                    });
        }
    }

    public void noConnectionSnackBar(View view) {
        if(view == null) {
            return;
        }
        Snackbar mSnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = mSnackbarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView textView1 = mSnackbarView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.thp_noconnection_snackbar, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }
}
