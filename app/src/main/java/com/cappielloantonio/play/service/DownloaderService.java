package com.cappielloantonio.play.service;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.util.DownloadUtil;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class DownloaderService extends DownloadService {

    private static final int FOREGROUND_NOTIFICATION_ID = 1;

    public DownloaderService() {
        super(FOREGROUND_NOTIFICATION_ID, DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL, DownloadUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID, R.string.exo_download_notification_channel_name, 0);
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return null;
    }

    @Override
    @NonNull
    protected DownloadManager getDownloadManager() {
        DownloadManager downloadManager = DownloadUtil.getDownloadManager(this);
        DownloadNotificationHelper downloadNotificationHelper = DownloadUtil.getDownloadNotificationHelper(this);
        downloadManager.addListener(new TerminalStateNotificationHelper(this, downloadNotificationHelper, FOREGROUND_NOTIFICATION_ID + 1));
        return downloadManager;
    }

    @Override
    @NonNull
    protected Notification getForegroundNotification(@NonNull List<Download> downloads) {
        return DownloadUtil.getDownloadNotificationHelper(this).buildProgressNotification(this, R.drawable.ic_downloading, null, null, downloads);
    }

    private static final class TerminalStateNotificationHelper implements DownloadManager.Listener {
        private final Context context;
        private final DownloadNotificationHelper notificationHelper;

        private int nextNotificationId;

        public TerminalStateNotificationHelper(Context context, DownloadNotificationHelper notificationHelper, int firstNotificationId) {
            this.context = context.getApplicationContext();
            this.notificationHelper = notificationHelper;
            nextNotificationId = firstNotificationId;
        }

        @Override
        public void onDownloadChanged(DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
            Notification notification;

            if (download.state == Download.STATE_COMPLETED) {
                notification = notificationHelper.buildDownloadCompletedNotification(context, R.drawable.ic_check_circle, null, Util.fromUtf8Bytes(download.request.data));
            } else if (download.state == Download.STATE_FAILED) {
                notification = notificationHelper.buildDownloadFailedNotification(context, R.drawable.ic_error, null, Util.fromUtf8Bytes(download.request.data));
            } else {
                return;
            }

            NotificationUtil.setNotification(context, nextNotificationId++, notification);
        }
    }
}
