package com.mobstac.thehindubusinessline.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.jwplayer.KeepScreenOnHandler;

/**
 * Created by ashwani on 16/10/16.
 */

public class JWPlayerActivity extends BaseActivity
        implements VideoPlayerEvents.OnFullscreenListener {

    private JWPlayerView mPlayerView;
    private String mVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayer);
        mVideoUrl = getIntent().getStringExtra("videoId");
        mPlayerView = (JWPlayerView) findViewById(R.id.jwplayer);
        mPlayerView.addOnFullscreenListener(this);
        new KeepScreenOnHandler(mPlayerView, getWindow());
        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .file(mVideoUrl)
                .autostart(true)
                .build();
        mPlayerView.setFullscreen(true, true);
        mPlayerView.setup(playerConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mPlayerView.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onFullscreen(boolean b) {

    }


}
