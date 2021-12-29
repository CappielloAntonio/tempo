package com.cappielloantonio.play.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaLibraryService;
import androidx.media3.session.MediaSession;

import com.cappielloantonio.play.ui.activity.MainActivity;

public class MediaService extends MediaLibraryService {
    private static final String TAG = "MediaService";

    public static final int REQUEST_CODE = 432;

    private ExoPlayer player;
    private MediaLibrarySession mediaLibrarySession;

    @Override
    public void onCreate() {
        super.onCreate();
        initializePlayer();
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
    private void initializePlayer() {
        player = new ExoPlayer.Builder(this)
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
}
