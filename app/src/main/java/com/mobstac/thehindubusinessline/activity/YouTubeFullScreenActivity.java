package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mobstac.thehindubusinessline.R;

/**
 * Created by ashwani on 16/10/16.
 */

public class YouTubeFullScreenActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnFullscreenListener, YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerView playerView;
    private String mVideoId;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youttubefullscreen);
        playerView = (YouTubePlayerView) findViewById(R.id.youTubePlayer);
        playerView.setVisibility(View.VISIBLE);
        mVideoId = getIntent().getStringExtra("videoId");
        playerView.initialize("AIzaSyDTUKKtxHyJeRu0VqYjpc0K2PcM-2YZFhY", this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onFullscreen(boolean b) {

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            player.loadVideo(mVideoId);
        }
        player.setFullscreen(true);
        player.setShowFullscreenButton(false);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            Toast.makeText(this, "There was an error initializing the YouTubePlayer " + errorReason.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            playerView.initialize("AIzaSyDTUKKtxHyJeRu0VqYjpc0K2PcM-2YZFhY", this);
        }
    }
}
