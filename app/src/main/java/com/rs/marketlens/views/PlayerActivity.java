/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rs.marketlens.views;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rs.marketlens.R;

/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity {

  private PlaybackStateListener playbackStateListener;
  private static final String TAG = PlayerActivity.class.getName();

  private PlayerView playerView;
  private SimpleExoPlayer player;
  private boolean playWhenReady = true;
  private int currentWindow = 0;
  private long playbackPosition = 0;
//  String videoURL = "https://marketlens.in/assets/video/market-lens-trailer.mp4";
  String videoURL = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    videoURL=  VideoViewArgs.fromBundle(getIntent().getExtras()).getUrl();



            playerView = findViewById(R.id.video_view);

    playbackStateListener = new PlaybackStateListener();
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    hideSystemUi();
    if ((Util.SDK_INT <= 23 || player == null)) {
      initializePlayer();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  private void initializePlayer() {
    if (player == null) {
      DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
      trackSelector.setParameters(
              trackSelector.buildUponParameters().setMaxVideoSizeSd());

      player = new SimpleExoPlayer.Builder(this)
              .setTrackSelector(trackSelector)
              .build();
    }

    playerView.setPlayer(player);
    //Uri uri = Uri.parse(getString(R.string.media_url_mp4));
  // Uri uri = Uri.parse("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
   // Uri uri = Uri.parse(videoURL);
   // MediaSource mediaSource = buildMediaSource(uri);

    String playerInfo = Util.getUserAgent(this, "ExoPlayerInfo");
    DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
            this, playerInfo
    );

    MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
            .setExtractorsFactory(new DefaultExtractorsFactory())
            .createMediaSource(Uri.parse("https://marketlens.in/assets/video/"+videoURL));

    player.prepare(mediaSource);
    player.setPlayWhenReady(playWhenReady);
    player.seekTo(currentWindow, playbackPosition);
    player.addListener(playbackStateListener);
    playerView.setKeepScreenOn(true);

    // player.prepare(mediaSource, false, false);
  }

  private void releasePlayer() {
    if (player != null) {
      playbackPosition = player.getCurrentPosition();
      currentWindow = player.getCurrentWindowIndex();
      playWhenReady = player.getPlayWhenReady();
      player.removeListener(playbackStateListener);
      player.release();
      player = null;
    }
  }

  private MediaSource buildMediaSource(Uri uri) {
    DataSource.Factory dataSourceFactory =
            new DefaultDataSourceFactory(this, "exoplayer-codelab");
    DashMediaSource.Factory mediaSourceFactory = new DashMediaSource.Factory(dataSourceFactory);
    return mediaSourceFactory.createMediaSource(uri);
  }

  @SuppressLint("InlinedApi")
  private void hideSystemUi() {
    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  private class PlaybackStateListener implements Player.EventListener{

    @Override
    public void onPlayerStateChanged(boolean playWhenReady,
                                     int playbackState) {
      String stateString;
      switch (playbackState) {
        case ExoPlayer.STATE_IDLE:
          stateString = "ExoPlayer.STATE_IDLE      -";
          break;
        case ExoPlayer.STATE_BUFFERING:
          stateString = "ExoPlayer.STATE_BUFFERING -";
          break;
        case ExoPlayer.STATE_READY:
          stateString = "ExoPlayer.STATE_READY     -";
          break;
        case ExoPlayer.STATE_ENDED:
          stateString = "ExoPlayer.STATE_ENDED     -";
          break;
        default:
          stateString = "UNKNOWN_STATE             -";
          break;
      }
      Log.d(TAG, "changed state to " + stateString
              + " playWhenReady: " + playWhenReady);
    }
  }

}
