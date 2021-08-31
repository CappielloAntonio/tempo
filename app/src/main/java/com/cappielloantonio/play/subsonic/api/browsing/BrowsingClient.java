package com.cappielloantonio.play.subsonic.api.browsing;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class BrowsingClient {
    private static final String TAG = "BrowsingClient";

    private final Context context;
    private final Subsonic subsonic;
    private Retrofit retrofit;
    private final BrowsingService browsingService;

    public BrowsingClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.browsingService = retrofit.create(BrowsingService.class);
    }

    public Call<SubsonicResponse> getMusicFolders() {
        Log.d(TAG, "getMusicFolders()");
        return browsingService.getMusicFolders(subsonic.getParams());
    }

    public Call<SubsonicResponse> getIndexes() {
        Log.d(TAG, "getIndexes()");
        return browsingService.getIndexes(subsonic.getParams());
    }

    public Call<SubsonicResponse> getMusicDirectory(String id) {
        Log.d(TAG, "getMusicDirectory()");
        return browsingService.getMusicDirectory(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getGenres() {
        Log.d(TAG, "getGenres()");
        return browsingService.getGenres(subsonic.getParams());
    }

    public Call<SubsonicResponse> getArtists() {
        Log.d(TAG, "getArtists()");
        return browsingService.getArtists(subsonic.getParams());
    }

    public Call<SubsonicResponse> getArtist(String id) {
        Log.d(TAG, "getArtist()");
        return browsingService.getArtist(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getAlbum(String id) {
        Log.d(TAG, "getAlbum()");
        return browsingService.getAlbum(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getSong(String id) {
        Log.d(TAG, "getSong()");
        return browsingService.getSong(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getVideos() {
        Log.d(TAG, "getVideos()");
        return browsingService.getVideos(subsonic.getParams());
    }

    public Call<SubsonicResponse> getVideoInfo(String id) {
        Log.d(TAG, "getVideoInfo()");
        return browsingService.getVideoInfo(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getArtistInfo(String id) {
        Log.d(TAG, "getArtistInfo()");
        return browsingService.getArtistInfo(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getArtistInfo2(String id) {
        Log.d(TAG, "getArtistInfo2()");
        return browsingService.getArtistInfo2(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getAlbumInfo(String id) {
        Log.d(TAG, "getAlbumInfo()");
        return browsingService.getAlbumInfo(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getAlbumInfo2(String id) {
        Log.d(TAG, "getAlbumInfo2()");
        return browsingService.getAlbumInfo2(subsonic.getParams(), id);
    }

    public Call<SubsonicResponse> getSimilarSongs(String id, int count) {
        Log.d(TAG, "getSimilarSongs()");
        return browsingService.getSimilarSongs(subsonic.getParams(), id, count);
    }

    public Call<SubsonicResponse> getSimilarSongs2(String id, int limit) {
        Log.d(TAG, "getSimilarSongs2()");
        return browsingService.getSimilarSongs2(subsonic.getParams(), id, limit);
    }

    public Call<SubsonicResponse> getTopSongs(String artist, int count) {
        Log.d(TAG, "getTopSongs()");
        return browsingService.getTopSongs(subsonic.getParams(), artist, count);
    }

    private OkHttpClient getOkHttpClient() {
        CacheUtil cacheUtil = new CacheUtil(context);

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
        return context != null ? new Cache(context.getCacheDir(), cacheSize) : null;    }
}
