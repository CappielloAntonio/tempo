package com.cappielloantonio.tempo.util;

import android.content.Context;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.database.DatabaseProvider;
import androidx.media3.database.StandaloneDatabaseProvider;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.NoOpCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.offline.DownloadManager;
import androidx.media3.exoplayer.offline.DownloadNotificationHelper;

import com.cappielloantonio.tempo.service.DownloaderManager;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@UnstableApi
public final class DownloadUtil {

    public static final String DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel";

    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";

    private static DataSource.Factory dataSourceFactory;
    private static DataSource.Factory httpDataSourceFactory;
    private static DatabaseProvider databaseProvider;
    private static File downloadDirectory;
    private static Cache downloadCache;
    private static DownloadManager downloadManager;
    private static DownloaderManager downloaderManager;
    private static DownloadNotificationHelper downloadNotificationHelper;

    public static boolean useExtensionRenderers() {
        // return true;
        return false;
    }

    public static RenderersFactory buildRenderersFactory(Context context, boolean preferExtensionRenderer) {
        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                useExtensionRenderers()
                        ? (preferExtensionRenderer ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;

        return new DefaultRenderersFactory(context.getApplicationContext()).setExtensionRendererMode(extensionRendererMode);
    }

    public static synchronized DataSource.Factory getHttpDataSourceFactory() {
        if (httpDataSourceFactory == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
            httpDataSourceFactory = new DefaultHttpDataSource.Factory();
        }

        return httpDataSourceFactory;
    }

    public static synchronized DataSource.Factory getDataSourceFactory(Context context) {
        if (dataSourceFactory == null) {
            context = context.getApplicationContext();
            DefaultDataSource.Factory upstreamFactory = new DefaultDataSource.Factory(context, getHttpDataSourceFactory());
            dataSourceFactory = buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache(context));
        }

        return dataSourceFactory;
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

    public static synchronized DownloaderManager getDownloadTracker(Context context) {
        ensureDownloadManagerInitialized(context);
        return downloaderManager;
    }

    private static synchronized Cache getDownloadCache(Context context) {
        if (downloadCache == null) {
            File downloadContentDirectory = new File(getDownloadDirectory(context), DOWNLOAD_CONTENT_DIRECTORY);
            downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor(), getDatabaseProvider(context));
        }

        return downloadCache;
    }

    private static synchronized void ensureDownloadManagerInitialized(Context context) {
        if (downloadManager == null) {
            downloadManager = new DownloadManager(
                    context,
                    getDatabaseProvider(context),
                    getDownloadCache(context),
                    getHttpDataSourceFactory(),
                    Executors.newFixedThreadPool(6)
            );

            downloaderManager = new DownloaderManager(context, getHttpDataSourceFactory(), downloadManager);
        }
    }

    private static synchronized DatabaseProvider getDatabaseProvider(Context context) {
        if (databaseProvider == null) {
            databaseProvider = new StandaloneDatabaseProvider(context);
        }

        return databaseProvider;
    }

    private static synchronized File getDownloadDirectory(Context context) {
        if (downloadDirectory == null) {
            if (Preferences.getDownloadStoragePreference() == 0) {
                downloadDirectory = context.getExternalFilesDirs(null)[0];
                if (downloadDirectory == null) {
                    downloadDirectory = context.getFilesDir();
                }
            } else {
                try {
                    downloadDirectory = context.getExternalFilesDirs(null)[1];
                } catch (Exception exception) {
                    downloadDirectory = context.getExternalFilesDirs(null)[0];
                    Preferences.setDownloadStoragePreference(0);
                }

            }
        }

        return downloadDirectory;
    }

    private static CacheDataSource.Factory buildReadOnlyCacheDataSource(DataSource.Factory upstreamFactory, Cache cache) {
        return new CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(upstreamFactory)
                .setCacheWriteDataSinkFactory(null)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
    }

    public static synchronized void eraseDownloadFolder(Context context) {
        File directory = getDownloadDirectory(context);

        ArrayList<File> files = listFiles(directory, new ArrayList<>());

        for (File file : files) {
            file.delete();
        }
    }

    private static synchronized ArrayList<File> listFiles(File directory, ArrayList<File> files) {
        if (directory.isDirectory()) {
            File[] list = directory.listFiles();

            if (list != null) {
                for (File file : list) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".exo")) {
                        files.add(file);
                    } else if (file.isDirectory()) {
                        listFiles(file, files);
                    }
                }
            }
        }

        return files;
    }
}
