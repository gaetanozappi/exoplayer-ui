package com.exoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.exoplayer.exoplayer.ExoPlayerManager;
import com.github.rubensousa.previewseekbar.base.PreviewView;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBar;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBarLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class SimpleVideoStream extends AppCompatActivity implements PreviewView.OnPreviewChangeListener {

    private static final int PICK_FILE_REQUEST_CODE = 2;

    private ExoPlayerManager exoPlayerManager;
    private PreviewTimeBarLayout previewTimeBarLayout;
    private PreviewTimeBar previewTimeBar;

    //PlayerView exoPlayerView;
    //SimpleExoPlayer exoPlayer;
    String title,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        title = b.getString("mediaTitle");
        url = b.getString("mediaUrl");

        SimpleExoPlayerView playerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
        previewTimeBar = playerView.findViewById(R.id.exo_progress);
        previewTimeBarLayout = findViewById(R.id.previewTimeBarLayout);
        //previewTimeBarLayout.setTintColorResource(R.color.colorPrimary);
        previewTimeBar.addOnPreviewChangeListener(this);
        //previewTimeBar.setPreviewColorTint(R.color.colorPrimary);
        exoPlayerManager = new ExoPlayerManager(playerView, previewTimeBarLayout,(ImageView) findViewById(R.id.imageView), url, this);
        exoPlayerManager.play(Uri.parse(url));
        previewTimeBarLayout.setPreviewLoader(exoPlayerManager);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        ImageButton exo_rew = (ImageButton) findViewById(R.id.exo_rew);
        exo_rew.setTranslationX(-(width+125)/2);

        ImageButton exo_ffwd = (ImageButton) findViewById(R.id.exo_ffwd);
        exo_ffwd.setTranslationX((width+125)/2);

        TextView video_title = (TextView)findViewById(R.id.video_title);
        video_title.setText(title);

        //ImageView preview = (ImageView) findViewById(R.id.imageView);
        //preview.setImageBitmap(image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            exoPlayerManager.play(data.getData());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        exoPlayerManager.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        exoPlayerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayerManager.onStop();
    }

    private void requestFullScreenIfLandscape() {
        if (getResources().getBoolean(R.bool.landscape)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }


    @Override
    public void onStartPreview(PreviewView previewView) {
        if (getResources().getBoolean(R.bool.landscape)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onStopPreview(PreviewView previewView) {
        exoPlayerManager.stopPreview();
    }

    @Override
    public void onPreview(PreviewView previewView, int progress, boolean fromUser) {}
}
