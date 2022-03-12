package com.cappielloantonio.play.util;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.media3.database.DatabaseProvider;
import androidx.media3.database.StandaloneDatabaseProvider;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.NoOpCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadNotificationHelper;

import com.cappielloantonio.play.service.DownloaderManager;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.Executors;

public final class DownloadUtil {

    public static final String DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel";

    private static final String TAG = "DemoUtil";
    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";

    private static DataSource.Factory dataSourceFactory;
    private static HttpDataSource.Factory httpDataSourceFactory;
    private static DatabaseProvider databaseProvider;
    private static File downloadDirectory;
    private static Cache downloadCache;
    private static DownloadManager downloadManager;
    private static DownloaderManager downloaderManager;
    private static DownloadNotificationHelper downloadNotificationHelper;

    @SuppressLint("UnsafeOptInUsageError")
    public static synchronized HttpDataSource.Factory getHttpDataSourceFactory() {
        if (httpDataSourceFactory == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
            httpDataSourceFactory = new DefaultHttpDataSource.Factory();
        }

        return httpDataSourceFactory;
    }

    @SuppressLint("UnsafeOptInUsageError")
    public static synchronized DataSource.Factory getDataSourceFactory(Context context) {
        if (dataSourceFactory == null) {
            context = context.getApplicationContext();
            DefaultDataSource.Factory upstreamFactory = new DefaultDataSource.Factory(context, getHttpDataSourceFactory());
            dataSourceFactory = buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache(context));
        }

        return dataSourceFactory;
    }

    @SuppressLint("UnsafeOptInUsageError")
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

    public static synchronized DownloaderManager getDownloadTracker(Context context) {
        ensureDownloadManagerInitialized(context);
        return downloaderManager;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private static synchronized Cache getDownloadCache(Context context) {
        if (downloadCache == null) {
            File downloadContentDirectory = new File(getDownloadDirectory(context), DOWNLOAD_CONTENT_DIRECTORY);
            downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor(), getDatabaseProvider(context));
        }

        return downloadCache;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private static synchronized void ensureDownloadManagerInitialized(Context context) {
        if (downloadManager == null) {
            downloadManager =
                    new DownloadManager(
                            context,
                            getDatabaseProvider(context),
                            getDownloadCache(context),
                            getHttpDataSourceFactory(),
                            Executors.newFixedThreadPool(6));

            downloaderManager = new DownloaderManager(context, downloadManager);
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private static synchronized DatabaseProvider getDatabaseProvider(Context context) {
        if (databaseProvider == null) {
            databaseProvider = new StandaloneDatabaseProvider(context);
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

    @SuppressLint("UnsafeOptInUsageError")
    private static CacheDataSource.Factory buildReadOnlyCacheDataSource(DataSource.Factory upstreamFactory, Cache cache) {
        return new CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(upstreamFactory)
                .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
    }
}
