package com.cappielloantonio.play.subsonic.api.albumsonglist;

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

public class AlbumSongListClient {
    private static final String TAG = "BrowsingClient";

    private final Context context;
    private final Subsonic subsonic;
    private Retrofit retrofit;
    private final AlbumSongListService albumSongListService;

    public AlbumSongListClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.albumSongListService = retrofit.create(AlbumSongListService.class);
    }

    public Call<SubsonicResponse> getAlbumList(String type, int size, int offset) {
        Log.d(TAG, "getAlbumList()");
        return albumSongListService.getAlbumList(subsonic.getParams(), type, size, offset);
    }

    public Call<SubsonicResponse> getAlbumList2(String type, int size, int offset, Integer fromYear, Integer toYear) {
        Log.d(TAG, "getAlbumList2()");
        return albumSongListService.getAlbumList2(subsonic.getParams(), type, size, offset, fromYear, toYear);
    }

    public Call<SubsonicResponse> getRandomSongs(int size, Integer fromYear, Integer toYear) {
        Log.d(TAG, "getRandomSongs()");
        return albumSongListService.getRandomSongs(subsonic.getParams(), size, fromYear, toYear);
    }

    public Call<SubsonicResponse> getSongsByGenre(String genre, int count, int offset) {
        Log.d(TAG, "getSongsByGenre()");
        return albumSongListService.getSongsByGenre(subsonic.getParams(), genre, count, offset);
    }

    public Call<SubsonicResponse> getNowPlaying() {
        Log.d(TAG, "getNowPlaying()");
        return albumSongListService.getNowPlaying(subsonic.getParams());
    }

    public Call<SubsonicResponse> getStarred() {
        Log.d(TAG, "getStarred()");
        return albumSongListService.getStarred(subsonic.getParams());
    }

    public Call<SubsonicResponse> getStarred2() {
        Log.d(TAG, "getStarred2()");
        return albumSongListService.getStarred2(subsonic.getParams());
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
        return new Cache(context.getCacheDir(), cacheSize);
    }
}
