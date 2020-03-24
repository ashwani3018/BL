package com.mobstac.thehindubusinessline.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.adapter.CustomSpinnerAdapter;
import com.mobstac.thehindubusinessline.model.SignupResponse;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private final String Item_Others = "others";
    private String TAG = "BL_SignUpFragment";
    private String mUserName;
    private String mUserEmail;
    private String mUserPassword;
    private String mUserPhoneNumber;
    private String mUserGender;
    private TextInputLayout mEmailTextInputLayout;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPhoneNumberEditText;
    private EditText mPasswordEditText;
    private ImageView mMaleImageView;
    private ImageView mFemaleImageView;
    private ScrollView mParentScrollview;
    private LinearLayout mSuccessLayout;
    private TextView mSuccessMessage;
    private View mProgressView;
    private Spinner mEmailSpinner;
    private String emailExtension;
    private AdsBaseActivity mBaseActivity;
    private TextView mMaleTextView;
    private TextView mFemaleTextView;

    private boolean isMaleSelected = true;


    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (AdsBaseActivity) context;
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
          /*Analytics*/
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_signup));
        FlurryAgent.logEvent(getString(R.string.ga_signup));
        FlurryAgent.onPageView();
    }

    private void initView(View view) {
        view.findViewById(R.id.email_sign_up_button).setOnClickListener(this);
        view.findViewById(R.id.signin).setOnClickListener(this);
        mEmailTextInputLayout = (TextInputLayout) view.findViewById(R.id.emailParent);
        mNameEditText = (EditText) view.findViewById(R.id.name);
        mEmailEditText = (EditText) view.findViewById(R.id.email);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.phone_number);
        mPasswordEditText = (EditText) view.findViewById(R.id.password);
        mFemaleImageView = (ImageView) view.findViewById(R.id.gender_female);
        mFemaleImageView.setOnClickListener(this);
        mMaleImageView = (ImageView) view.findViewById(R.id.gender_male);
        mMaleImageView.setOnClickListener(this);
        mParentScrollview = (ScrollView) view.findViewById(R.id.scrollView);
        mParentScrollview.setVerticalScrollBarEnabled(false);
        mParentScrollview.setHorizontalScrollBarEnabled(false);
        mSuccessLayout = (LinearLayout) view.findViewById(R.id.successLayout);
        mSuccessMessage = (TextView) view.findViewById(R.id.successMessage);
        mParentScrollview.setVisibility(View.VISIBLE);
        mMaleTextView = (TextView) view.findViewById(R.id.maleText);
        mFemaleTextView = (TextView) view.findViewById(R.id.female_text);

        mProgressView = view.findViewById(R.id.login_progress);
        mEmailSpinner = (Spinner) view.findViewById(R.id.email_spinner);
        final List<String> mEmailList = new ArrayList<>();
        mEmailList.add("@gmail.com");
        mEmailList.add("@outlook.com");
        mEmailList.add("@live.com");
        mEmailList.add(Item_Others);
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(),
                R.layout.spinner_item, mEmailList);
        mEmailSpinner.setAdapter(customSpinnerAdapter);
        mEmailSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: " + mEmailList.get(position));
                emailExtension = mEmailList.get(position);
                if (emailExtension.equalsIgnoreCase(Item_Others)) {
                    mEmailSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
    }

    private void signUpTask() {
        mUserName = mNameEditText.getText().toString();
        if (TextUtils.isEmpty(mUserName)) {
            AppUtils.showToast(getActivity(), "Please enter your full Name");
            return;
        }
        if (emailExtension.equalsIgnoreCase(Item_Others)) {
            mUserEmail = mEmailEditText.getText().toString();
        } else {
            mUserEmail = mEmailEditText.getText().toString() + emailExtension;
        }
        if (!isEmailValid(mUserEmail)) {
            AppUtils.showToast(getActivity(), getString(R.string.email_field_required));
            return;
        }
        mUserPhoneNumber = mPhoneNumberEditText.getText().toString();
        if (!isPhoneNumberValid(mUserPhoneNumber)) {
            AppUtils.showToast(getActivity(), "Enter a valid phone number.");
            return;
        }
        mUserPassword = mPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(mUserPassword)) {
            AppUtils.showToast(getActivity(), getString(R.string.all_field_mendatory));
            return;
        } else if (mUserPassword.length() < 6) {
            AppUtils.showToast(getActivity(), getString(R.string.password_field_required));
            return;
        }
        mUserGender = isMaleSelected ? "male" : "female";
        mParentScrollview.setVisibility(View.GONE);
        mSuccessLayout.setVisibility(View.GONE);
        showProgress(true);


        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("name", mUserName);
        bodyMap.put("email", mUserEmail);
        bodyMap.put("password", mUserPassword);
        bodyMap.put("phone_number", mUserPhoneNumber);
        bodyMap.put("gender", mUserGender);
        bodyMap.put("app_token", Constants.PORTFOLIO_APP_TOKEN);


        Call<SignupResponse> call = RetrofitAPICaller.getInstance(getActivity()).getWebserviceAPIs()
                .doPortfolioSignup(Constants.PORTFOLIO_SING_UP_URL, bodyMap);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Log.i(TAG, "onResponse: ");
                if (getActivity() != null && isAdded()) {
                    if (response.isSuccessful()) {
                        showProgress(false);
                        SignupResponse.SingUpDetail detail = response.body().getSingUpDetail();
                        if (detail != null) {
                            int httpCode = detail.getHttpCode();
                            switch (httpCode) {
                                case 200:
                                    mParentScrollview.setVisibility(View.GONE);
                                    mSuccessLayout.setVisibility(View.VISIBLE);
                                    String msg = "We have sent a verification mail to your email " + mUserEmail + ". Please verify email to activate";
                                    mSuccessMessage.setText(msg);
                                    mBaseActivity.setToolbarTitle("");
                                    break;
                                case 201:
                                    mEmailTextInputLayout.setHintTextAppearance(R.style.error_appearance);
                                    mParentScrollview.setVisibility(View.VISIBLE);
                                    mSuccessLayout.setVisibility(View.GONE);
                                    mEmailTextInputLayout.setHint(detail.getMessage());
                                    mEmailEditText.requestFocus();
                                    break;
                                case 404:
                                    mParentScrollview.setVisibility(View.VISIBLE);
                                    mSuccessLayout.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), detail.getMessage(), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_up_button:
                AppUtils.hideKeyBoard(getActivity(), mParentScrollview);
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    signUpTask();
                } else {
                    mBaseActivity.showSnackBar(mParentScrollview);
                }
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
            case R.id.signin:
                launchSignInFragment();
                break;
        }
    }

    private void launchSignInFragment() {
        SignInFragment fragment = SignInFragment.newInstance();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.login_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean isEmailValid(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern ptr = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = ptr.matcher(email);
        return matcher.find();
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() == 10) {
            return true;
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mBaseActivity.setToolbarTitle(getString(R.string.signup));
        super.onPrepareOptionsMenu(menu);
    }
}
