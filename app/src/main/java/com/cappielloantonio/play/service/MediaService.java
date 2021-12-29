package com.cappielloantonio.play.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.util.NotificationUtil;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaLibraryService;
import androidx.media3.session.MediaSession;
import androidx.media3.session.PlayerNotificationManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.ui.activity.MainActivity;

public class MediaService extends MediaLibraryService {
    private static final String TAG = "MediaService";

    public static final String CHANNEL_ID = "exampleServiceChannel";
    public static final int NOTIFICATION_ID = 1635;
    public static final int REQUEST_CODE = 432;

    private ExoPlayer player;
    private MediaLibrarySession mediaLibrarySession;
    private PlayerNotificationManager playerNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        initializePlayer();
        // createPlayerNotificationManager();
        addMediaItemToPlayer();
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
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void createPlayerNotificationManager() {
        playerNotificationManager = new PlayerNotificationManager.Builder(this, NOTIFICATION_ID, CHANNEL_ID)
                .setChannelNameResourceId(R.string.notification_channel_name)
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .setChannelImportance(NotificationUtil.IMPORTANCE_DEFAULT)
                .setMediaDescriptionAdapter(new MediaDescriptionAdapter())
                .setNotificationListener(new NotificationListener())
                .setSmallIconResourceId(R.drawable.ic_notification)
                .build();

        playerNotificationManager.setMediaSessionToken((MediaSessionCompat.Token) mediaLibrarySession.getSessionCompatToken());

        playerNotificationManager.setUseRewindAction(false);
        playerNotificationManager.setUseRewindActionInCompactView(false);
        playerNotificationManager.setUsePreviousAction(true);
        playerNotificationManager.setUsePreviousActionInCompactView(true);
        playerNotificationManager.setUseNextAction(true);
        playerNotificationManager.setUseNextActionInCompactView(true);
        playerNotificationManager.setUseFastForwardAction(false);
        playerNotificationManager.setUseFastForwardActionInCompactView(false);
        playerNotificationManager.setUsePlayPauseActions(true);
        playerNotificationManager.setUseStopAction(false);

        playerNotificationManager.setPlayer(player);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void addMediaItemToPlayer() {
        String title_1 = "Holiday";
        String album_1 = "American Idiot";
        String artist_1 = "Green Day";
        String stream_1 = "http://192.168.1.81:4533/rest/stream?u=antonio&t=28bbc18754cd3c08b00d59554b9583ec&s=00692d&f=json&v=1.8.0&c=NavidromeUI&id=cabe97ac9c9ce7858abeeb194c914bb3&_=1640453745178";
        String artwork_1 = "http://192.168.1.81:4533/rest/getCoverArt?u=antonio&t=28bbc18754cd3c08b00d59554b9583ec&s=00692d&f=json&v=1.8.0&c=NavidromeUI&id=cabe97ac9c9ce7858abeeb194c914bb3&_=2021-03-26T15%3A47%3A16.522602061Z&size=300";

        String title_2 = "21 Guns";
        String album_2 = "21st Century Breakdown";
        String artist_2 = "Green Day";
        String stream_2 = "http://192.168.1.81:4533/rest/stream?u=antonio&t=28bbc18754cd3c08b00d59554b9583ec&s=00692d&f=json&v=1.8.0&c=NavidromeUI&id=3807e34bb07f2359fa9f7d1a9bd593a6&_=1640533306840";
        String artwork_2 = "http://192.168.1.81:4533/rest/getCoverArt?u=antonio&t=28bbc18754cd3c08b00d59554b9583ec&s=00692d&f=json&v=1.8.0&c=NavidromeUI&id=3807e34bb07f2359fa9f7d1a9bd593a6&_=2021-03-26T15%3A46%3A34.942666103Z&size=300";

        Bundle bundle_1 = new Bundle();
        bundle_1.putString("id", "cabe97ac9c9ce7858abeeb194c914bb3");

        Bundle bundle_2 = new Bundle();
        bundle_2.putString("id", "3807e34bb07f2359fa9f7d1a9bd593a6");

        MediaItem item_1 = new MediaItem.Builder()
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setTitle(title_1)
                                .setAlbumTitle(album_1)
                                .setArtist(artist_1)
                                .setArtworkUri(Uri.parse(artwork_1))
                                .setExtras(bundle_1)
                                .build()
                )
                .setUri(Uri.parse(stream_1))
                .build();

        MediaItem item_2 = new MediaItem.Builder()
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setTitle(title_2)
                                .setAlbumTitle(album_2)
                                .setArtist(artist_2)
                                .setArtworkUri(Uri.parse(artwork_2))
                                .setExtras(bundle_2)
                                .build()
                )
                .setUri(Uri.parse(stream_2))
                .build();

        player.addMediaItem(item_1);
        player.addMediaItem(item_2);

        player.prepare();
    }

    private class MediaDescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter {
        @Override
        public CharSequence getCurrentContentTitle(Player player) {
            return player.getCurrentMediaItem() != null ? player.getCurrentMediaItem().mediaMetadata.title : null;
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            return PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {
            return player.getCurrentMediaItem() != null ? player.getCurrentMediaItem().mediaMetadata.description : null;
        }

        @Nullable
        @Override
        public CharSequence getCurrentSubText(Player player) {
            return player.getCurrentMediaItem() != null ? player.getCurrentMediaItem().mediaMetadata.albumTitle : null;
        }

        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            new Handler(Looper.getMainLooper()).post(() -> {
                if (player.getCurrentMediaItem() != null) {
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(player.getCurrentMediaItem().mediaMetadata.artworkUri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    callback.onBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }
            });

            return null;
        }
    }

    private class NotificationListener implements PlayerNotificationManager.NotificationListener {
        @Override
        public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            stopSelf();
        }

        @Override
        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            if (ongoing) {
                startForeground(notificationId, notification);
            } else {
                stopForeground(true);
            }
        }
    }

    private class LibrarySessionCallback implements MediaLibrarySession.MediaLibrarySessionCallback {
    }

    @SuppressLint("UnsafeOptInUsageError")
    private class CustomMediaItemFiller implements MediaSession.MediaItemFiller {
        @Override
        public MediaItem fillInLocalConfiguration(MediaSession session, MediaSession.ControllerInfo controller, MediaItem mediaItem) {
            return new MediaItem.Builder()
                    .setMediaId(mediaItem.mediaId)
                    .setMediaMetadata(mediaItem.mediaMetadata)
                    .setUri(mediaItem.mediaMetadata.mediaUri)
                    .build();
        }
    }
}
