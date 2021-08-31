package com.cappielloantonio.play.subsonic.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.Interceptor;
import okhttp3.Request;

public class CacheUtil {
    private final Context context;

    public CacheUtil(Context context) {
        this.context = context;
    }

    public Interceptor onlineInterceptor = chain -> {
        okhttp3.Response response = chain.proceed(chain.request());
        int maxAge = 60; // 60 seconds
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAge)
                .removeHeader("Pragma")
                .build();
    };

    public Interceptor offlineInterceptor = chain -> {
        Request request = chain.request();
        if (!isConnected()) {
            int maxStale = 60 * 60 * 24 * 30; // 30 days (60 seconds * 60 minutes * 24 hours * 30 days)
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return chain.proceed(request);
    };

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
}
