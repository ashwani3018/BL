package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.UserProfileActivity;
import com.mobstac.thehindubusinessline.model.ApiSuccessResponse;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "BL_ChangePasswordFragment";
    private EditText mCurrentPasswordEditText;
    private EditText mNewPasswordEditText;
    private EditText mRepeatPasswordEditText;

    private UserProfileActivity mActivity;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

           /*Analytics*/
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_change_password));
        FlurryAgent.logEvent(getString(R.string.ga_change_password));
        FlurryAgent.onPageView();
    }

    private void initView(View view) {
        mCurrentPasswordEditText = (EditText) view.findViewById(R.id.current_password);
        mNewPasswordEditText = (EditText) view.findViewById(R.id.new_password);
        mRepeatPasswordEditText = (EditText) view.findViewById(R.id.repeat_password);
        view.findViewById(R.id.change_password_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_password_button:
                AppUtils.hideKeyBoard(getActivity(), mRepeatPasswordEditText);
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    changePasswordTask();
                } else {
                    AppUtils.showToast(getActivity(), "No Internet Connection Available");
                }
                break;
        }
    }

    private void changePasswordTask() {
        String accessToken = SharedPreferenceHelper.getString(getActivity(), Constants.PORTFOLIO_USER_ACCESS_TOKEN, "");
        String accessTokenType = SharedPreferenceHelper.getString(getActivity(), Constants.PORTFOLIO_USER_TOKEN_TYPE, "");
        String actualToken = accessTokenType + " " + accessToken;

        String currentPassword = mCurrentPasswordEditText.getText().toString();
        String newPassword = mNewPasswordEditText.getText().toString();

        if (currentPassword != null && TextUtils.isEmpty(currentPassword)) {
            AppUtils.showToast(getActivity(), getString(R.string.all_field_mendatory));
            return;
        }

        if (newPassword != null && !TextUtils.isEmpty(newPassword) && newPassword.length() < 6) {
            AppUtils.showToast(getActivity(), getString(R.string.password_field_required));
            return;
        }

        final String repeatPassword = mRepeatPasswordEditText.getText().toString();

        if (!newPassword.equals(repeatPassword)) {
            AppUtils.showToast(getActivity(), getString(R.string.password_not_matching));
            return;
        }

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("oldPassword", currentPassword);
        bodyMap.put("newPassword", newPassword);
        bodyMap.put("newPassword_repeat", repeatPassword);

        Call<ApiSuccessResponse> call = RetrofitAPICaller.getInstance(getActivity()).getWebserviceAPIs()
                .requestChangePassword(Constants.PORTFOLIO_CHANGE_PASSWORD_URL, actualToken, bodyMap);
        call.enqueue(new Callback<ApiSuccessResponse>() {
            @Override
            public void onResponse(Call<ApiSuccessResponse> call, Response<ApiSuccessResponse> response) {
                if (getActivity() != null && isAdded()) {
                    if (response.isSuccessful()) {
                        ApiSuccessResponse jsonObject = response.body();
                        if (jsonObject != null) {
                            try {
                                String status = jsonObject.getStatus();
                                String errorMsg = jsonObject.getError_Message();
                                switch (status) {
                                    case "success":
                                        AppUtils.showToast(getActivity(), errorMsg);
                                        getActivity().getSupportFragmentManager().popBackStack();
                                        break;
                                    case "error":
                                        AppUtils.showToast(getActivity(), errorMsg);
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiSuccessResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mActivity != null) {
            mActivity.setToolbarTitle(getString(R.string.change_password));
            mActivity.hideToolbarLogo();
        }
        if (menu != null) {
            menu.findItem(R.id.action_edit_profile).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }
}
