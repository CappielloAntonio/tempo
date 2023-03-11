package com.cappielloantonio.play.subsonic.api.mediaretrieval;

import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.RetrofitClient;
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
        this.mediaRetrievalService = new RetrofitClient(subsonic).getRetrofit().create(MediaRetrievalService.class);
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
}
