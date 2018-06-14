/*
 * Copyright 2016 The Android Open Source Project
 * Copyright 2017 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exoplayer.exoplayer;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;
import com.exoplayer.glide.GlideApp;
import com.github.rubensousa.previewseekbar.base.PreviewLoader;
import com.github.rubensousa.previewseekbar.exoplayer.PreviewTimeBarLayout;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ExoPlayerManager implements PreviewLoader {

    private ExoPlayerMediaSourceBuilder mediaSourceBuilder;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private PreviewTimeBarLayout previewTimeBarLayout;
    private String thumbnailsUrl;
    private ImageView imageView;
    private Context context;
    private Player.EventListener eventListener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                previewTimeBarLayout.hidePreview();
            }
        }
    };

    public ExoPlayerManager(SimpleExoPlayerView playerView,
                            PreviewTimeBarLayout previewTimeBarLayout, ImageView imageView,
                            String thumbnailsUrl, Context context) {
        this.playerView = playerView;
        this.imageView = imageView;
        this.previewTimeBarLayout = previewTimeBarLayout;
        this.mediaSourceBuilder = new ExoPlayerMediaSourceBuilder(playerView.getContext());
        this.thumbnailsUrl = thumbnailsUrl;
        this.context = context;
    }

    public void play(Uri uri) {
        mediaSourceBuilder.setUri(uri);
    }

    public void onStart() {
        if (Util.SDK_INT > 23) {
            createPlayers();
        }
    }

    public void onResume() {
        if (Util.SDK_INT <= 23) {
            createPlayers();
        }
    }

    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayers();
        }
    }

    public void onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayers();
        }
    }

    public void stopPreview() {
        player.setPlayWhenReady(true);
    }

    private void releasePlayers() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void createPlayers() {
        if (player != null) {
            player.release();
        }
        player = createFullPlayer();
        playerView.setPlayer(player);
    }

    private SimpleExoPlayer createFullPlayer() {
        TrackSelection.Factory videoTrackSelectionFactory
                = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(playerView.getContext()),
                trackSelector, loadControl);
        player.setPlayWhenReady(true);
        player.prepare(mediaSourceBuilder.getMediaSource(false));
        player.addListener(eventListener);
        return player;
    }

    @Override
    public void loadPreview(long currentPosition, long max) {
        player.setPlayWhenReady(true);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(thumbnailsUrl, new HashMap<String, String>());
        Bitmap image = retriever.getFrameAtTime(2*1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        GlideApp.with(context)
                .load(stream.toByteArray())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imageView);

        /*GlideApp.with(imageView)
                .load("https://s3-eu-west-1.amazonaws.com/dailybestpubblici/wp-content/uploads/2018/03/jurassic_park.jpg")
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imageView);*/
    }
}
