package com.cappielloantonio.play.ui.notification;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.cappielloantonio.play.service.MusicService.ACTION_REWIND;
import static com.cappielloantonio.play.service.MusicService.ACTION_SKIP;
import static com.cappielloantonio.play.service.MusicService.ACTION_TOGGLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.service.MusicService;
import com.cappielloantonio.play.ui.activity.MainActivity;

public class PlayingNotification {

    private static final int NOTIFICATION_ID = 1;
    static final String NOTIFICATION_CHANNEL_ID = "playing_notification";

    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_BACKGROUND = 0;

    private int notifyMode = NOTIFY_MODE_BACKGROUND;

    private NotificationManager notificationManager;
    protected MusicService service;
    boolean stopped;

    public synchronized void init(MusicService service) {
        this.service = service;
        notificationManager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    public synchronized void update() {
        stopped = false;

        final Song song = service.getCurrentSong();
        final boolean isPlaying = service.isPlaying();
        final int playButtonResId = isPlaying ? R.drawable.ic_pause_white_24dp : R.drawable.ic_play_arrow_white_24dp;

        Intent action = new Intent(service, MainActivity.class);
        action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent clickIntent = PendingIntent.getActivity(service, 0, action, PendingIntent.FLAG_IMMUTABLE);

        final ComponentName serviceName = new ComponentName(service, MusicService.class);
        Intent intent = new Intent(MusicService.ACTION_QUIT);
        intent.setComponent(serviceName);
        final PendingIntent deleteIntent = PendingIntent.getService(service, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        final int bigNotificationImageSize = service.getResources().getDimensionPixelSize(R.dimen.notification_big_image_size);
        service.runOnUiThread(() -> CustomGlideRequest.Builder
                .from(service, song.getPrimary(), CustomGlideRequest.SONG_PIC, null)
                .bitmap()
                .build()
                .into(new CustomTarget<Bitmap>(bigNotificationImageSize, bigNotificationImageSize) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                        update(resource);
                    }

                    @Override
                    public void onLoadFailed(Drawable drawable) {
                        update(null);
                    }

                    @Override
                    public void onLoadCleared(Drawable drawable) {
                        update(null);
                    }

                    void update(Bitmap bitmap) {
                        if (bitmap == null) {
                            bitmap = BitmapFactory.decodeResource(service.getResources(), R.drawable.default_album_art);
                        }

                        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(playButtonResId, service.getString(R.string.exo_action_play_pause), retrievePlaybackAction(ACTION_TOGGLE));
                        NotificationCompat.Action previousAction = new NotificationCompat.Action(R.drawable.ic_skip_previous_white_24dp, service.getString(R.string.exo_action_previous), retrievePlaybackAction(ACTION_REWIND));
                        NotificationCompat.Action nextAction = new NotificationCompat.Action(R.drawable.ic_skip_next_white_24dp, service.getString(R.string.exo_action_next), retrievePlaybackAction(ACTION_SKIP));

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_notification)
                                .setSubText(song.getAlbumName())
                                .setLargeIcon(bitmap)
                                .setContentIntent(clickIntent)
                                .setDeleteIntent(deleteIntent)
                                .setContentTitle(song.getTitle())
                                .setContentText(song.getArtistName())
                                .setOngoing(isPlaying)
                                .setShowWhen(false)
                                .addAction(previousAction)
                                .addAction(playPauseAction)
                                .addAction(nextAction);

                        builder.setStyle(new MediaStyle().setMediaSession(service.getMediaSession().getSessionToken()).setShowActionsInCompactView(0, 1, 2)).setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                        // notification has been stopped before loading was finished
                        if (stopped) return;

                        updateNotifyModeAndPostNotification(builder.build());
                    }
                }));
    }

    public synchronized void stop() {
        stopped = true;
        service.stopForeground(true);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent retrievePlaybackAction(final String action) {
        final ComponentName serviceName = new ComponentName(service, MusicService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getService(service, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    void updateNotifyModeAndPostNotification(Notification notification) {
        int newNotifyMode;

        if (service.isPlaying()) {
            newNotifyMode = NOTIFY_MODE_FOREGROUND;
        } else {
            newNotifyMode = NOTIFY_MODE_BACKGROUND;
        }

        if (notifyMode != newNotifyMode && newNotifyMode == NOTIFY_MODE_BACKGROUND) {
            service.stopForeground(false);
        }

        if (newNotifyMode == NOTIFY_MODE_FOREGROUND) {
            service.startForeground(NOTIFICATION_ID, notification);
        } else if (newNotifyMode == NOTIFY_MODE_BACKGROUND) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        notifyMode = newNotifyMode;
    }

    @RequiresApi(26)
    private void createNotificationChannel() {
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
        if (notificationChannel == null) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, service.getString(R.string.playing_notification_name), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(service.getString(R.string.playing_notification_description));
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
