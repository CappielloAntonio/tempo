package com.cappielloantonio.play.subsonic.api.mediaretrieval;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MediaRetrievalClient {
    private static final String TAG = "BrowsingClient";

    private final Context context;
    private final Subsonic subsonic;
    private final MediaRetrievalService mediaRetrievalService;

    public MediaRetrievalClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.mediaRetrievalService = retrofit.create(MediaRetrievalService.class);
    }

    public Call<SubsonicResponse> stream(String id, Integer maxBitRate, String format) {
        Log.d(TAG, "stream()");
        return mediaRetrievalService.stream(subsonic.getParams(), id, maxBitRate, format);
    }

    public Call<SubsonicResponse> download(String id) {
        Log.d(TAG, "download()");
        return mediaRetrievalService.download(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getLyrics(String artist, String title) {
        Log.d(TAG, "getLyrics()");
        return mediaRetrievalService.getLyrics(subsonic.getParams(), artist, title);
    }

    private OkHttpClient getOkHttpClient() {
        CacheUtil cacheUtil = new CacheUtil(context, 0, 60 * 60 * 24 * 30);

        return new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(cacheUtil.offlineInterceptor)
                .addNetworkInterceptor(cacheUtil.onlineInterceptor)
                .cache(getCache())
                .build();
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    private Cache getCache() {
        int cacheSize = 10 * 1024 * 1024;
        return context != null ? new Cache(context.getCacheDir(), cacheSize) : null;
    }
}
