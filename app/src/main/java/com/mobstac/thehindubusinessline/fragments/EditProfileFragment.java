package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.UserProfileActivity;
import com.mobstac.thehindubusinessline.model.ApiSuccessResponse;
import com.mobstac.thehindubusinessline.model.UserDetailResponse;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "BL_EditProfileFragment";
    private EditText mNameEditText;
    private EditText mPhoneNumberEditText;
    private ImageView mMaleImageView;
    private ImageView mFemaleImageView;
    private boolean isMaleSelected = true;
    private UserDetailResponse mUserDetails;
    private EditText mEmailEditText;
    private TextView mMaleTextView;
    private TextView mFemaleTextView;

    private UserProfileActivity mActivity;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(UserDetailResponse detailResponse) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("UserDetail", detailResponse);
        fragment.setArguments(bundle);
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
        if (getArguments() != null) {
            mUserDetails = getArguments().getParcelable("UserDetail");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

           /*Analytics*/
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_edit_profile));
        FlurryAgent.logEvent(getString(R.string.ga_edit_profile));
        FlurryAgent.onPageView();
    }

    private void initView(View view) {
        mNameEditText = (EditText) view.findViewById(R.id.name);
        mEmailEditText = (EditText) view.findViewById(R.id.email);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.phone_number);
        mFemaleImageView = (ImageView) view.findViewById(R.id.gender_female);
        mFemaleImageView.setOnClickListener(this);
        mMaleImageView = (ImageView) view.findViewById(R.id.gender_male);
        mMaleImageView.setOnClickListener(this);
        view.findViewById(R.id.update_profile_button).setOnClickListener(this);
        mMaleTextView = (TextView) view.findViewById(R.id.maleText);
        mFemaleTextView = (TextView) view.findViewById(R.id.female_text);

        if (mUserDetails != null) {
            mNameEditText.setText(mUserDetails.getName());
            mPhoneNumberEditText.setText(mUserDetails.getPhone());
            mEmailEditText.setText(mUserDetails.getLogin());
            mEmailEditText.setEnabled(false);
            String gender = mUserDetails.getGender();
            switch (gender) {
                case "male":
                    isMaleSelected = true;
                    mMaleImageView.setImageResource(R.drawable.male_filled);
                    mFemaleImageView.setImageResource(R.drawable.female_line);
                    mMaleTextView.setTextColor(getResources().getColor(R.color.gender_selected_text_color));
                    mFemaleTextView.setTextColor(getResources().getColor(R.color.gender_unselected_text_color));
                    break;
                case "female":
                    isMaleSelected = false;
                    mMaleImageView.setImageResource(R.drawable.male_line);
                    mFemaleImageView.setImageResource(R.drawable.female_filled);
                    mMaleTextView.setTextColor(getResources().getColor(R.color.gender_unselected_text_color));
                    mFemaleTextView.setTextColor(getResources().getColor(R.color.gender_selected_text_color));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_profile_button:
                AppUtils.hideKeyBoard(getActivity(), mPhoneNumberEditText);
                updateProfileTask();

                break;
            case R.id.gender_female:
                isMaleSelected = false;
                mMaleImageView.setImageResource(R.drawable.male_line);
                mFemaleImageView.setImageResource(R.drawable.female_filled);
                mMaleTextView.setTextColor(getResources().getColor(R.color.gender_unselected_text_color));
                mFemaleTextView.setTextColor(getResources().getColor(R.color.gender_selected_text_color));
                break;
            case R.id.gender_male:
                isMaleSelected = true;
                mMaleImageView.setImageResource(R.drawable.male_filled);
                mFemaleImageView.setImageResource(R.drawable.female_line);
                mMaleTextView.setTextColor(getResources().getColor(R.color.gender_selected_text_color));
                mFemaleTextView.setTextColor(getResources().getColor(R.color.gender_unselected_text_color));
                break;
        }
    }

    private void updateProfileTask() {

        String accessToken = SharedPreferenceHelper.getString(getActivity(), Constants.PORTFOLIO_USER_ACCESS_TOKEN, "");
        String accessTokenType = SharedPreferenceHelper.getString(getActivity(), Constants.PORTFOLIO_USER_TOKEN_TYPE, "");
        String actualToken = accessTokenType + " " + accessToken;

        String name = mNameEditText.getText().toString();
        String phoneNumber = mPhoneNumberEditText.getText().toString();

        if (!isPhoneNumberValid(phoneNumber)) {
            AppUtils.showToast(getActivity(), "Please enter a valid phone number.");
            return;
        }


        if (name != null && phoneNumber != null && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneNumber)) {
            String gender = isMaleSelected ? "male" : "female";
            String prefrences = mUserDetails != null ? mUserDetails.getPreferences() : "1,254";
            HashMap<String, String> bodyMap = new HashMap<>();
            bodyMap.put("name", name);
            bodyMap.put("phone", phoneNumber);
            bodyMap.put("gender", gender);
            bodyMap.put("preferences", prefrences);

            Call<ApiSuccessResponse> call = RetrofitAPICaller.getInstance(getActivity()).getWebserviceAPIs()
                    .requestUpdateProfile(Constants.PORTFOLIO_UPDATE_PROFILE_URL, actualToken, bodyMap);
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
                    Log.i(TAG, "onFailure: ");
                }
            });
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mActivity != null) {
            mActivity.setToolbarTitle(getString(R.string.edit_profile));
            mActivity.hideToolbarLogo();
        }
        if (menu != null) {
            menu.findItem(R.id.action_edit_profile).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() == 10) {
            return true;
        }
        return false;
    }
}
