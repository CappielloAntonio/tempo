package com.cappielloantonio.play.subsonic.api.browsing;

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

public class BrowsingClient {
    private static final String TAG = "BrowsingClient";

    private final Subsonic subsonic;
    private final BrowsingService browsingService;

    public BrowsingClient(Subsonic subsonic) {
        this.subsonic = subsonic;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(getOkHttpClient())
                .build();

        this.browsingService = retrofit.create(BrowsingService.class);
    }

    public Call<ApiResponse> getMusicFolders() {
        Log.d(TAG, "getMusicFolders()");
        return browsingService.getMusicFolders(subsonic.getParams());
    }

    public Call<ApiResponse> getIndexes() {
        Log.d(TAG, "getIndexes()");
        return browsingService.getIndexes(subsonic.getParams());
    }

    public Call<ApiResponse> getMusicDirectory(String id) {
        Log.d(TAG, "getMusicDirectory()");
        return browsingService.getMusicDirectory(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getGenres() {
        Log.d(TAG, "getGenres()");
        return browsingService.getGenres(subsonic.getParams());
    }

    public Call<ApiResponse> getArtists() {
        Log.d(TAG, "getArtists()");
        return browsingService.getArtists(subsonic.getParams());
    }

    public Call<ApiResponse> getArtist(String id) {
        Log.d(TAG, "getArtist()");
        return browsingService.getArtist(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getAlbum(String id) {
        Log.d(TAG, "getAlbum()");
        return browsingService.getAlbum(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getSong(String id) {
        Log.d(TAG, "getSong()");
        return browsingService.getSong(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getVideos() {
        Log.d(TAG, "getVideos()");
        return browsingService.getVideos(subsonic.getParams());
    }

    public Call<ApiResponse> getVideoInfo(String id) {
        Log.d(TAG, "getVideoInfo()");
        return browsingService.getVideoInfo(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getArtistInfo(String id) {
        Log.d(TAG, "getArtistInfo()");
        return browsingService.getArtistInfo(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getArtistInfo2(String id) {
        Log.d(TAG, "getArtistInfo2()");
        return browsingService.getArtistInfo2(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getAlbumInfo(String id) {
        Log.d(TAG, "getAlbumInfo()");
        return browsingService.getAlbumInfo(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getAlbumInfo2(String id) {
        Log.d(TAG, "getAlbumInfo2()");
        return browsingService.getAlbumInfo2(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getSimilarSongs(String id, int count) {
        Log.d(TAG, "getSimilarSongs()");
        return browsingService.getSimilarSongs(subsonic.getParams(), id, count);
    }

    public Call<ApiResponse> getSimilarSongs2(String id, int limit) {
        Log.d(TAG, "getSimilarSongs2()");
        return browsingService.getSimilarSongs2(subsonic.getParams(), id, limit);
    }

    public Call<ApiResponse> getTopSongs(String artist, int count) {
        Log.d(TAG, "getTopSongs()");
        return browsingService.getTopSongs(subsonic.getParams(), artist, count);
    }

    private OkHttpClient getOkHttpClient() {
        CacheUtil cacheUtil = new CacheUtil(60, 60 * 60 * 24 * 30);

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
