package com.cappielloantonio.play.subsonic.api.playlist;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaylistClient {
    private static final String TAG = "BrowsingClient";

    private final Context context;
    private final Subsonic subsonic;
    private final PlaylistService playlistService;

    public PlaylistClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkHttpClient())
                .build();

        this.playlistService = retrofit.create(PlaylistService.class);
    }

    public Call<ApiResponse> getPlaylists() {
        Log.d(TAG, "getPlaylists()");
        return playlistService.getPlaylists(subsonic.getParams());
    }

    public Call<ApiResponse> getPlaylist(String id) {
        Log.d(TAG, "getPlaylist()");
        return playlistService.getPlaylist(subsonic.getParams(), id);
    }

    public Call<ApiResponse> createPlaylist(String playlistId, String name, ArrayList<String> songsId) {
        Log.d(TAG, "createPlaylist()");
        return playlistService.createPlaylist(subsonic.getParams(), playlistId, name, songsId);
    }

    public Call<ApiResponse> updatePlaylist(String playlistId, String name, boolean isPublic, ArrayList<String> songIdToAdd, ArrayList<Integer> songIndexToRemove) {
        Log.d(TAG, "updatePlaylist()");
        return playlistService.updatePlaylist(subsonic.getParams(), playlistId, name, isPublic, songIdToAdd, songIndexToRemove);
    }

    public Call<ApiResponse> deletePlaylist(String id) {
        Log.d(TAG, "deletePlaylist()");
        return playlistService.deletePlaylist(subsonic.getParams(), id);
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
