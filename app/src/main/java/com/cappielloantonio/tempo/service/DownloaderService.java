package com.cappielloantonio.tempo.service;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.util.NotificationUtil;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.offline.Download;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadNotificationHelper;
import androidx.media3.exoplayer.scheduler.PlatformScheduler;
import androidx.media3.exoplayer.scheduler.Requirements;
import androidx.media3.exoplayer.scheduler.Scheduler;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.util.DownloadUtil;

import java.util.List;

@UnstableApi
public class DownloaderService extends androidx.media3.exoplayer.offline.DownloadService {

    private static final int JOB_ID = 1;
    private static final int FOREGROUND_NOTIFICATION_ID = 1;

    public DownloaderService() {
        super(FOREGROUND_NOTIFICATION_ID, DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL, DownloadUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID, R.string.exo_download_notification_channel_name, 0);
    }

    @NonNull
    @Override
    protected DownloadManager getDownloadManager() {
        DownloadManager downloadManager = DownloadUtil.getDownloadManager(this);
        DownloadNotificationHelper downloadNotificationHelper = DownloadUtil.getDownloadNotificationHelper(this);
        downloadManager.addListener(new TerminalStateNotificationHelper(this, downloadNotificationHelper, FOREGROUND_NOTIFICATION_ID + 1));
        return downloadManager;
    }

    @NonNull
    @Override
    protected Scheduler getScheduler() {
        return new PlatformScheduler(this, JOB_ID);
    }

    @NonNull
    @Override
    protected Notification getForegroundNotification(@NonNull List<Download> downloads, @Requirements.RequirementFlags int notMetRequirements) {
        return DownloadUtil.getDownloadNotificationHelper(this).buildProgressNotification(this, R.drawable.ic_download, null, null, downloads, notMetRequirements);
    }

    private static final class TerminalStateNotificationHelper implements DownloadManager.Listener {
        private final Context context;
        private final DownloadNotificationHelper notificationHelper;

        private final Notification successfulDownloadGroupNotification;
        private final Notification failedDownloadGroupNotification;

        private final int successfulDownloadGroupNotificationId;
        private final int failedDownloadGroupNotificationId;

        private int nextNotificationId;

        public TerminalStateNotificationHelper(Context context, DownloadNotificationHelper notificationHelper, int firstNotificationId) {
            this.context = context.getApplicationContext();
            this.notificationHelper = notificationHelper;
            nextNotificationId = firstNotificationId;

            successfulDownloadGroupNotification = DownloadUtil.buildGroupSummaryNotification(
                    this.context,
                    DownloadUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                    DownloadUtil.DOWNLOAD_NOTIFICATION_SUCCESSFUL_GROUP,
                    R.drawable.ic_check_circle,
                    "Downloads completed"
            );

            failedDownloadGroupNotification = DownloadUtil.buildGroupSummaryNotification(
                    this.context,
                    DownloadUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                    DownloadUtil.DOWNLOAD_NOTIFICATION_FAILED_GROUP,
                    R.drawable.ic_error,
                    "Downloads failed"
            );

            successfulDownloadGroupNotificationId = nextNotificationId++;
            failedDownloadGroupNotificationId = nextNotificationId++;
        }

        @Override
        public void onDownloadChanged(@NonNull DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
            Notification notification;

            if (download.state == Download.STATE_COMPLETED) {
                notification = notificationHelper.buildDownloadCompletedNotification(context, R.drawable.ic_check_circle, null, DownloaderManager.getDownloadNotificationMessage(download.request.id));
                notification = Notification.Builder.recoverBuilder(context, notification).setGroup(DownloadUtil.DOWNLOAD_NOTIFICATION_SUCCESSFUL_GROUP).build();
                NotificationUtil.setNotification(this.context, successfulDownloadGroupNotificationId, successfulDownloadGroupNotification);
                DownloaderManager.updateDatabase(download.request.id);
            } else if (download.state == Download.STATE_FAILED) {
                notification = notificationHelper.buildDownloadFailedNotification(context, R.drawable.ic_error, null, DownloaderManager.getDownloadNotificationMessage(download.request.id));
                notification = Notification.Builder.recoverBuilder(context, notification).setGroup(DownloadUtil.DOWNLOAD_NOTIFICATION_FAILED_GROUP).build();
                NotificationUtil.setNotification(this.context, failedDownloadGroupNotificationId, failedDownloadGroupNotification);
            } else {
                return;
            }

            NotificationUtil.setNotification(context, nextNotificationId++, notification);
        }

        @Override
        public void onDownloadRemoved(@NonNull DownloadManager downloadManager, Download download) {
            DownloaderManager.deleteDatabase(download.request.id);
        }
    }
}
