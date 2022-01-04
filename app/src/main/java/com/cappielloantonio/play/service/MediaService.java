package com.cappielloantonio.play.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.analytics.PlaybackStatsListener;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSourceFactory;
import androidx.media3.session.MediaLibraryService;
import androidx.media3.session.MediaSession;

import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;

public class MediaService extends MediaLibraryService {
    private static final String TAG = "MediaService";

    public static final int REQUEST_CODE = 432;

    private ExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private MediaSourceFactory mediaSourceFactory;
    private MediaLibrarySession mediaLibrarySession;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeMediaSource();
        initializePlayer();
        initializePlayerListener();
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    @Nullable
    @Override
    public MediaLibrarySession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return mediaLibrarySession;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeMediaSource() {
        dataSourceFactory = DownloadUtil.getDataSourceFactory(this);
        mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializePlayer() {
        player = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .setAudioAttributes(AudioAttributes.DEFAULT, true)
                .setHandleAudioBecomingNoisy(true)
                .setWakeMode(C.WAKE_MODE_NETWORK)
                .build();

        mediaLibrarySession = new MediaLibrarySession.Builder(this, player, new LibrarySessionCallback())
                .setMediaItemFiller(new CustomMediaItemFiller())
                .setSessionActivity(PendingIntent.getActivity(getApplicationContext(), REQUEST_CODE, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_IMMUTABLE))
                .build();
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void releasePlayer() {
        player.release();
        mediaLibrarySession.release();
    }

    private class LibrarySessionCallback implements MediaLibrarySession.MediaLibrarySessionCallback {
    }

    @SuppressLint("UnsafeOptInUsageError")
    private class CustomMediaItemFiller implements MediaSession.MediaItemFiller {
        @Override
        public MediaItem fillInLocalConfiguration(MediaSession session, MediaSession.ControllerInfo controller, MediaItem mediaItem) {
            return mediaItem.buildUpon()
                    .setUri(mediaItem.mediaMetadata.mediaUri)
                    .setMediaMetadata(mediaItem.mediaMetadata)
                    .build();
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializePlayerListener() {
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                MediaManager.setLastPlayedTimestamp(mediaItem);
                MediaManager.scrobble(mediaItem);
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if(isPlaying) {
                    MediaManager.setPlayingPausedTimestamp(player.getCurrentMediaItem(), player.getCurrentPosition());
                }
            }
        });
    }
}
