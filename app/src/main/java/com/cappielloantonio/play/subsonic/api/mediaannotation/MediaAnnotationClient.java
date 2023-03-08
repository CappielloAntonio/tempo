package com.cappielloantonio.play.subsonic.api.mediaannotation;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaAnnotationClient {
    private static final String TAG = "BrowsingClient";

    private final Context context;
    private final Subsonic subsonic;
    private final MediaAnnotationService mediaAnnotationService;

    public MediaAnnotationClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkHttpClient())
                .build();

        this.mediaAnnotationService = retrofit.create(MediaAnnotationService.class);
    }

    public Call<ApiResponse> star(String id, String albumId, String artistId) {
        Log.d(TAG, "star()");
        return mediaAnnotationService.star(subsonic.getParams(), id, albumId, artistId);
    }

    public Call<ApiResponse> unstar(String id, String albumId, String artistId) {
        Log.d(TAG, "unstar()");
        return mediaAnnotationService.unstar(subsonic.getParams(), id, albumId, artistId);
    }

    public Call<ApiResponse> setRating(String id, int rating) {
        Log.d(TAG, "setRating()");
        return mediaAnnotationService.setRating(subsonic.getParams(), id, rating);
    }

    public Call<ApiResponse> scrobble(String id) {
        Log.d(TAG, "scrobble()");
        return mediaAnnotationService.scrobble(subsonic.getParams(), id);
    }

    private OkHttpClient getOkHttpClient() {
        CacheUtil cacheUtil = new CacheUtil(context, 0, 60 * 60 * 24 * 30);

        return new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(cacheUtil.offlineInterceptor)
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
