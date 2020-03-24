package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.LoginActivity;
import com.mobstac.thehindubusinessline.activity.UserProfileActivity;
import com.mobstac.thehindubusinessline.model.LoginResponse;
import com.mobstac.thehindubusinessline.model.UserDetailResponse;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "BL_UserProfileFragment";

    private TextView mEmailTextView;
    private TextView mNameTextView;
    private TextView mMobileTextView;
    private TextView mGenderTextView;
    private UserDetailResponse mUserDetail;
    private View mProgressbar;
    private RelativeLayout mParentCardLayout;

    private UserProfileActivity mActivity;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (UserProfileActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (UserProfileActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        callUserDetailTask();

           /*Analytics*/
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_user_profile));
        FlurryAgent.logEvent(getString(R.string.ga_user_profile));
        FlurryAgent.onPageView();
    }


    private void initView(View view) {
        mNameTextView = (TextView) view.findViewById(R.id.name);
        mEmailTextView = (TextView) view.findViewById(R.id.email);
        mMobileTextView = (TextView) view.findViewById(R.id.mobile);
        mGenderTextView = (TextView) view.findViewById(R.id.gender);
        view.findViewById(R.id.change_password).setOnClickListener(this);
        view.findViewById(R.id.logout_button).setOnClickListener(this);
        mProgressbar = view.findViewById(R.id.login_progress);
        mParentCardLayout = (RelativeLayout) view.findViewById(R.id.parentCardLayout);
    }

    private void callUserDetailTask() {

        mProgressbar.setVisibility(View.VISIBLE);
        String accessToken = SharedPreferenceHelper.getString(getActivity(), Constants.PORTFOLIO_USER_ACCESS_TOKEN, "");
        String accessTokenType = SharedPreferenceHelper.getString(getActivity(), Constants.PORTFOLIO_USER_TOKEN_TYPE, "");
        String actualToken = accessTokenType + " " + accessToken;

        Call<UserDetailResponse> call = RetrofitAPICaller.getInstance(getActivity()).getWebserviceAPIs()
                .getUserDetails(Constants.PORTFOLIO_USER_DETAIL_URL, actualToken);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (getActivity() != null && isAdded()) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "onResponse: Successful");
                        UserDetailResponse userDetailResponse = response.body();
                        if (userDetailResponse != null) {
                            mUserDetail = userDetailResponse;
                            if (userDetailResponse.getError() == null) {
                                mProgressbar.setVisibility(View.GONE);
                                mParentCardLayout.setVisibility(View.VISIBLE);
                                mNameTextView.setText(userDetailResponse.getName());
                                mEmailTextView.setText(userDetailResponse.getLogin());
                                mMobileTextView.setText(userDetailResponse.getPhone());
                                mGenderTextView.setText(userDetailResponse.getGender());
                            }
                        }
                    } else {
                        Log.i(TAG, "onResponse: Unsuccessful");
                        ResponseBody body = response.errorBody();
                        try {
                            JSONObject object = new JSONObject(body.string());
                            Log.i(TAG, "onResponse: " + object.toString());
                            if (object != null && object.has("error")) {
                                requestForNewAccessToken();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout_button:
                performLogout();
                break;
            case R.id.change_password:
                launchChangePasswordFragment();
                break;
        }
    }

    private void performLogout() {
        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_ACCESS_TOKEN, "");
        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_REFRESH_TOKEN, "");
        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_SCOPE, "");
        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_TOKEN_TYPE, "");
        SharedPreferenceHelper.putLong(getActivity(), Constants.PORTFOLIO_USER_EXPIRES_IN, 0);
        SharedPreferenceHelper.putBoolean(getActivity(), Constants.PORTFOLIO_IS_USER_LOGIN, false);
        AppUtils.showToast(getActivity(), "Successfully Logout");
        getActivity().finish();
    }

    private void launchChangePasswordFragment() {
        ChangePasswordFragment fragment = ChangePasswordFragment.newInstance();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.user_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void launchEditProfileFragment() {
        EditProfileFragment fragment = EditProfileFragment.newInstance(mUserDetail);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.user_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                launchEditProfileFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mActivity != null) {
            mActivity.setToolbarTitle(getString(R.string.my_profile));
            mActivity.setToolbarLogo(R.drawable.user_profile);
        }
        if (menu != null) {
            menu.findItem(R.id.action_edit_profile).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }


    private void requestForNewAccessToken() {

        String refreshToken = SharedPreferenceHelper.getString(getActivity(), Constants.PORTFOLIO_USER_REFRESH_TOKEN, "");
        String authentication = AppUtils.encodeCredentialsForBasicAuthorization();
        Call<LoginResponse> call = RetrofitAPICaller.getInstance(getActivity()).getWebserviceAPIs()
                .requestRefreshToken(Constants.PORTFOLIO_REFRESH_TOKEN_URL, Constants.REFRESH_TOKEN_GRANT_TYPE,
                        refreshToken, authentication);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (getActivity() != null) {
                        LoginResponse mLoginResponse = response.body();
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_ACCESS_TOKEN, mLoginResponse.getAccess_token());
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_REFRESH_TOKEN, mLoginResponse.getRefresh_token());
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_SCOPE, mLoginResponse.getScope());
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_TOKEN_TYPE, mLoginResponse.getToken_type());
                        SharedPreferenceHelper.putLong(getActivity(), Constants.PORTFOLIO_USER_EXPIRES_IN, mLoginResponse.getExpires_in());
                        SharedPreferenceHelper.putBoolean(getActivity(), Constants.PORTFOLIO_IS_USER_LOGIN, true);
                        if (isAdded()) {
                            callUserDetailTask();
                        }
                    }
                } else {
                    Log.i(TAG, "onResponse: Unsuccessful");
                    ResponseBody body = response.errorBody();
                    try {
                        JSONObject object = new JSONObject(body.string());
                        Log.i(TAG, "onResponse: " + object.toString());
                        if (object != null && object.has("error") && getActivity() != null && isAdded()) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }
}
