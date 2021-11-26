package com.cappielloantonio.play.util;

import android.content.Context;

import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public final class CacheUtil {

    private static final String CACHE_CONTENT_DIRECTORY = "cache";

    private static HttpDataSource.Factory httpDataSourceFactory;
    private static File cacheDirectory;
    private static SimpleCache simpleCache;
    private static ExoDatabaseProvider databaseProvider;

    public static synchronized HttpDataSource.Factory getHttpDataSourceFactory() {
        if (httpDataSourceFactory == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
            httpDataSourceFactory = new DefaultHttpDataSourceFactory();
        }
        return httpDataSourceFactory;
    }

    public static synchronized SimpleCache getCache(Context context) {
        if (simpleCache == null) {
            File downloadContentDirectory = new File(getCacheDirectory(context), CACHE_CONTENT_DIRECTORY);

            long cacheSize = PreferenceUtil.getInstance(context).getMediaCacheSize();
            LeastRecentlyUsedCacheEvictor cacheEvictor = new LeastRecentlyUsedCacheEvictor(cacheSize);
            ExoDatabaseProvider databaseProvider = getDatabaseProvider(context);

            simpleCache = new SimpleCache(downloadContentDirectory, cacheEvictor, databaseProvider);
        }
        return simpleCache;
    }

    private static synchronized ExoDatabaseProvider getDatabaseProvider(Context context) {
        if (databaseProvider == null) {
            databaseProvider = new ExoDatabaseProvider(context);
        }
        return databaseProvider;
    }

    private static synchronized File getCacheDirectory(Context context) {
        if (cacheDirectory == null) {
            cacheDirectory = context.getExternalFilesDir(null);
            if (cacheDirectory == null) {
                cacheDirectory = context.getFilesDir();
            }
        }
        return cacheDirectory;
    }
}
