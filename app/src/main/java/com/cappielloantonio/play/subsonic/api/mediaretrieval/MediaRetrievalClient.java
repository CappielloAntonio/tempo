package com.cappielloantonio.play.subsonic.api.mediaretrieval;

import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaRetrievalClient {
    private static final String TAG = "BrowsingClient";

    private final Subsonic subsonic;
    private final MediaRetrievalService mediaRetrievalService;

    public MediaRetrievalClient(Subsonic subsonic) {
        this.subsonic = subsonic;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkHttpClient())
                .build();

        this.mediaRetrievalService = retrofit.create(MediaRetrievalService.class);
    }

    public Call<ApiResponse> stream(String id, Integer maxBitRate, String format) {
        Log.d(TAG, "stream()");
        return mediaRetrievalService.stream(subsonic.getParams(), id, maxBitRate, format);
    }

    public Call<ApiResponse> download(String id) {
        Log.d(TAG, "download()");
        return mediaRetrievalService.download(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getLyrics(String artist, String title) {
        Log.d(TAG, "getLyrics()");
        return mediaRetrievalService.getLyrics(subsonic.getParams(), artist, title);
    }

    private OkHttpClient getOkHttpClient() {
        CacheUtil cacheUtil = new CacheUtil(0, 60 * 60 * 24 * 30);

        return new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
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
        return new Cache(App.getContext().getCacheDir(), cacheSize);
    }
}
