package com.mobstac.thehindubusinessline.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.activity.ShowLoginActivity;
import com.mobstac.thehindubusinessline.model.LoginResponse;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {
    private String TAG = "BL_SignInFragment";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mSignUpTextView;
    private TextView mForgotTextView;
    private AdsBaseActivity mBaseActivity;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
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
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        /*Analytics*/
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_signin));
        FlurryAgent.logEvent(getString(R.string.ga_signin));
        FlurryAgent.onPageView();
    }

    private void initView(View view) {
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        mForgotTextView = (TextView) view.findViewById(R.id.forgot_password);
        mForgotTextView.setOnClickListener(this);
        mPasswordView = (EditText) view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mLoginFormView.setVerticalScrollBarEnabled(false);
        mLoginFormView.setHorizontalScrollBarEnabled(false);
        mProgressView = view.findViewById(R.id.login_progress);
        mSignUpTextView = (TextView) view.findViewById(R.id.signup);
        mSignUpTextView.setOnClickListener(this);
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), getString(R.string.all_field_mendatory), Toast.LENGTH_LONG).show();
            focusView = mEmailView;
            focusView.requestFocus();
            return;
        }

        if (!isEmailValid(email)) {
            Toast.makeText(getActivity(), getString(R.string.email_field_required), Toast.LENGTH_LONG).show();
            focusView = mEmailView;
            focusView.requestFocus();
            return;
        }
        // Check for a valid password address.
        if (password == null || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), getString(R.string.all_field_mendatory), Toast.LENGTH_LONG).show();
            focusView = mPasswordView;
            focusView.requestFocus();
            return;
        }


        AppUtils.hideKeyBoard(getActivity(), mLoginFormView);
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            showProgress(true);
            userLoginTask(email, password);
        } else {
            mBaseActivity.showSnackBar(mLoginFormView);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                launchSignUpFragment();
                break;
            case R.id.forgot_password:
                launchForgotPasswordFragment();
                break;

        }
    }

    private void launchForgotPasswordFragment() {
        ForgotPasswordFragment fragment = ForgotPasswordFragment.newInstance();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.login_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void launchSignUpFragment() {
        SignUpFragment fragment = SignUpFragment.newInstance();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.login_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    private void userLoginTask(String email, String password) {
        String authentication = AppUtils.encodeCredentialsForBasicAuthorization();

        Call<LoginResponse> call = RetrofitAPICaller.getInstance(getActivity()).getWebserviceAPIs()
                .doPortfolioLogin(Constants.PORTFOLIO_SING_IN_URL, Constants.GRANT_TYPE, password, email,
                        authentication);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (getActivity() != null && isAdded()) {
                    Log.i(TAG, "onResponse: ");

                    showProgress(false);

                    if (response.isSuccessful()) {
                        Log.i(TAG, "onResponse: Successful");
                        LoginResponse mLoginResponse = response.body();
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_ACCESS_TOKEN, mLoginResponse.getAccess_token());
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_REFRESH_TOKEN, mLoginResponse.getRefresh_token());
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_SCOPE, mLoginResponse.getScope());
                        SharedPreferenceHelper.putString(getActivity(), Constants.PORTFOLIO_USER_TOKEN_TYPE, mLoginResponse.getToken_type());
                        SharedPreferenceHelper.putLong(getActivity(), Constants.PORTFOLIO_USER_EXPIRES_IN, mLoginResponse.getExpires_in());
                        SharedPreferenceHelper.putBoolean(getActivity(), Constants.PORTFOLIO_IS_USER_LOGIN, true);

                        getActivity().finish();
                    } else {
                        Log.i(TAG, "onResponse: Unsuccessful");
                        ResponseBody body = response.errorBody();
                        try {
                            JSONObject object = new JSONObject(body.string());
                            Log.i(TAG, "onResponse: " + object.toString());
                            if (object != null && object.has("error_description")) {
                                String errorMessage = object.get("error_description").toString();
                                String userDisabled = "User is disabled";
                                if (errorMessage.equalsIgnoreCase(userDisabled)) {
                                    Intent intent = new Intent(getActivity(), ShowLoginActivity.class);
                                    intent.putExtra(ShowLoginActivity.FROM_SCREEN, ShowLoginActivity.FORGOT_PASSWORD);
                                    intent.putExtra(ShowLoginActivity.HEADING_TEXT, getString(R.string.verifyEmailHeading));
                                    intent.putExtra(ShowLoginActivity.MESSAGE_TEXT, getString(R.string.verifyEmailMessage));
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.badCredential), Toast.LENGTH_SHORT).show();
                                }
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
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (getActivity() != null && isAdded()) {
                    Log.i(TAG, "onFailure: ");
                    showProgress(false);
                }
            }
        });
    }


    private boolean isEmailValid(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern ptr = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = ptr.matcher(email);
        return matcher.find();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mBaseActivity.setToolbarTitle(getString(R.string.action_sign_in));
        super.onPrepareOptionsMenu(menu);
    }
}
