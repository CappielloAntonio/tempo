package com.cappielloantonio.play.subsonic.utils;

import okhttp3.Interceptor;
import okhttp3.Request;

public class CacheUtil {
    public static Interceptor onlineInterceptor = chain -> {
        okhttp3.Response response = chain.proceed(chain.request());
        int maxAge = 60;
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAge)
                .removeHeader("Pragma")
                .build();
    };

    public static Interceptor offlineInterceptor = chain -> {
        Request request = chain.request();
        if (!false) {
            int maxStale = 60 * 60 * 24 * 30;
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
        return chain.proceed(request);
    };
}
