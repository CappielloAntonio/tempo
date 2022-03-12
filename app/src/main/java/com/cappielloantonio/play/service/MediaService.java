package com.cappielloantonio.play.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.media3.cast.CastPlayer;
import androidx.media3.cast.SessionAvailabilityListener;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Player;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.session.MediaLibraryService;
import androidx.media3.session.MediaSession;

import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;
import com.google.android.gms.cast.framework.CastContext;

@SuppressLint("UnsafeOptInUsageError")
public class MediaService extends MediaLibraryService implements SessionAvailabilityListener {
    private static final String TAG = "MediaService";

    public static final int REQUEST_CODE = 432;

    private ExoPlayer player;
    private CastPlayer castPlayer;
    private DefaultMediaSourceFactory defaultMediaSourceFactory;
    private MediaLibrarySession mediaLibrarySession;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeMediaSource();
        initializePlayer();
        initializeCastPlayer();
        initializeMediaLibrarySession();
        initializePlayerListener();

        setPlayer(null, castPlayer.isCastSessionAvailable() ? castPlayer : player);
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

    private void initializeMediaSource() {
        DataSource.Factory dataSourceFactory = DownloadUtil.getDataSourceFactory(this);
        defaultMediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);
    }

    private void initializePlayer() {
        player = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(defaultMediaSourceFactory)
                .setAudioAttributes(AudioAttributes.DEFAULT, true)
                .setHandleAudioBecomingNoisy(true)
                .setWakeMode(C.WAKE_MODE_NETWORK)
                .build();
    }

    private void initializeCastPlayer() {
        castPlayer = new CastPlayer(CastContext.getSharedInstance(this));
        castPlayer.setSessionAvailabilityListener(this);
    }

    private void initializeMediaLibrarySession() {
        mediaLibrarySession = new MediaLibrarySession.Builder(this, player, new LibrarySessionCallback())
                .setMediaItemFiller(new CustomMediaItemFiller())
                .setSessionActivity(PendingIntent.getActivity(getApplicationContext(), REQUEST_CODE, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_IMMUTABLE))
                .build();
    }

    private void setPlayer(Player oldPlayer, Player newPlayer) {
        if (oldPlayer == newPlayer) return;
        if (oldPlayer != null) oldPlayer.stop();

        mediaLibrarySession.setPlayer(newPlayer);
    }

    @Override
    public void onCastSessionAvailable() {
        setPlayer(player, castPlayer);
    }

    @Override
    public void onCastSessionUnavailable() {
        setPlayer(castPlayer, player);
    }

    private void releasePlayer() {
        castPlayer.setSessionAvailabilityListener(null);
        castPlayer.release();
        player.release();
        mediaLibrarySession.release();
    }

    private class LibrarySessionCallback implements MediaLibrarySession.MediaLibrarySessionCallback {
    }

    private class CustomMediaItemFiller implements MediaSession.MediaItemFiller {
        @Override
        public MediaItem fillInLocalConfiguration(MediaSession session, MediaSession.ControllerInfo controller, MediaItem mediaItem) {
            return mediaItem.buildUpon()
                    .setUri(mediaItem.mediaMetadata.mediaUri)
                    .setMediaMetadata(mediaItem.mediaMetadata)
                    .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                    .build();
        }
    }

    private void initializePlayerListener() {
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                if (mediaItem == null) return;

                MediaManager.setLastPlayedTimestamp(mediaItem);
                if (mediaItem.mediaMetadata.extras.getString("mediaType").equals(Media.MEDIA_TYPE_MUSIC))
                    MediaManager.scrobble(mediaItem);
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    MediaManager.setPlayingPausedTimestamp(player.getCurrentMediaItem(), player.getCurrentPosition());
                }
            }
        });
    }
}
