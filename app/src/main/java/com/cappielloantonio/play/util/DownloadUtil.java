package com.cappielloantonio.play.util;

import android.content.Context;

import com.cappielloantonio.play.service.DownloadTracker;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.offline.ActionFileUpgradeUtil;
import com.google.android.exoplayer2.offline.DefaultDownloadIndex;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.Executors;

public final class DownloadUtil {

    public static final String DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel";
    private static final String TAG = "DemoUtil";
    private static final String DOWNLOAD_ACTION_FILE = "actions";
    private static final String DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions";
    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";

    private static HttpDataSource.Factory httpDataSourceFactory;
    private static DatabaseProvider databaseProvider;
    private static File downloadDirectory;
    private static Cache downloadCache;
    private static DownloadManager downloadManager;
    private static DownloadTracker downloadTracker;
    private static DownloadNotificationHelper downloadNotificationHelper;

    public static synchronized HttpDataSource.Factory getHttpDataSourceFactory(Context context) {
        if (httpDataSourceFactory == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
            httpDataSourceFactory = new DefaultHttpDataSourceFactory();
        }
        return httpDataSourceFactory;
    }

    public static synchronized DownloadNotificationHelper getDownloadNotificationHelper(Context context) {
        if (downloadNotificationHelper == null) {
            downloadNotificationHelper = new DownloadNotificationHelper(context, DOWNLOAD_NOTIFICATION_CHANNEL_ID);
        }
        return downloadNotificationHelper;
    }

    public static synchronized DownloadManager getDownloadManager(Context context) {
        ensureDownloadManagerInitialized(context);
        return downloadManager;
    }

    public static synchronized DownloadTracker getDownloadTracker(Context context) {
        ensureDownloadManagerInitialized(context);
        return downloadTracker;
    }

    public static synchronized Cache getDownloadCache(Context context) {
        if (downloadCache == null) {
            File downloadContentDirectory = new File(getDownloadDirectory(context), DOWNLOAD_CONTENT_DIRECTORY);
            downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor(), getDatabaseProvider(context));
        }
        return downloadCache;
    }

    private static synchronized void ensureDownloadManagerInitialized(Context context) {
        if (downloadManager == null) {
            DefaultDownloadIndex downloadIndex = new DefaultDownloadIndex(getDatabaseProvider(context));
            upgradeActionFile(context, DOWNLOAD_ACTION_FILE, downloadIndex, false);
            upgradeActionFile(context, DOWNLOAD_TRACKER_ACTION_FILE, downloadIndex, true);
            downloadManager = new DownloadManager(context, getDatabaseProvider(context), getDownloadCache(context), getHttpDataSourceFactory(context), Executors.newFixedThreadPool(6));
            downloadTracker = new DownloadTracker(context, getHttpDataSourceFactory(context), downloadManager);
        }
    }

    private static synchronized void upgradeActionFile(Context context, String fileName, DefaultDownloadIndex downloadIndex, boolean addNewDownloadsAsCompleted) {
        try {
            ActionFileUpgradeUtil.upgradeAndDelete(new File(getDownloadDirectory(context), fileName), null, downloadIndex, true, addNewDownloadsAsCompleted);
        } catch (IOException e) {
            Log.e(TAG, "Failed to upgrade action file: " + fileName, e);
        }
    }

    private static synchronized DatabaseProvider getDatabaseProvider(Context context) {
        if (databaseProvider == null) {
            databaseProvider = new ExoDatabaseProvider(context);
        }
        return databaseProvider;
    }

    private static synchronized File getDownloadDirectory(Context context) {
        if (downloadDirectory == null) {
            downloadDirectory = context.getExternalFilesDir(null);
            if (downloadDirectory == null) {
                downloadDirectory = context.getFilesDir();
            }
        }
        return downloadDirectory;
    }
}
