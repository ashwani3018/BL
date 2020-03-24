package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;

public class ShowLoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FROM_SCREEN = "fromScreen";
    public static final String HEADING_TEXT = "headingText";
    public static final String MESSAGE_TEXT = "messageText";
    public static final int LOGIN_VIEW = 0;
    public static final int FORGOT_PASSWORD = 1;
    private int fromScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_login);
        Intent intent = getIntent();
        if (intent.hasExtra(FROM_SCREEN)) {
            fromScreen = intent.getIntExtra(FROM_SCREEN, 0);
        }

        String headingText = "";
        String messageText = "";

        if (intent.hasExtra(HEADING_TEXT)) {
            headingText = intent.getStringExtra(HEADING_TEXT);
        }
        if (intent.hasExtra(MESSAGE_TEXT)) {
            messageText = intent.getStringExtra(MESSAGE_TEXT);
        }

        switch (fromScreen) {
            case LOGIN_VIEW:
                findViewById(R.id.login_view).setVisibility(View.VISIBLE);
                findViewById(R.id.forgot_password_view).setVisibility(View.GONE);
                findViewById(R.id.login_button).setOnClickListener(this);
                findViewById(R.id.register_button).setOnClickListener(this);
                break;
            case FORGOT_PASSWORD:
                findViewById(R.id.login_view).setVisibility(View.GONE);
                findViewById(R.id.forgot_password_view).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.heading_message)).setText(headingText);
                ((TextView) findViewById(R.id.forgot_password_text)).setText(messageText);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                startLoginActivity();
                break;
            case R.id.register_button:
                startUserRegisterActivity();
                break;
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.SCREEN_TYPE, LoginActivity.SIGN_IN);
        startActivity(intent);
        finish();
    }

    private void startUserRegisterActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.SCREEN_TYPE, LoginActivity.SIGN_UP);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (fromScreen == FORGOT_PASSWORD) {
            finish();
        }
        return super.dispatchTouchEvent(ev);
    }
}
