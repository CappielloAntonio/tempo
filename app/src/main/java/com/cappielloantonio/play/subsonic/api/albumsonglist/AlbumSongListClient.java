package com.cappielloantonio.play.subsonic.api.albumsonglist;

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

public class AlbumSongListClient {
    private static final String TAG = "BrowsingClient";

    private final Subsonic subsonic;
    private final AlbumSongListService albumSongListService;

    public AlbumSongListClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.albumSongListService = new RetrofitClient(subsonic).getRetrofit().create(AlbumSongListService.class);
    }

    public Call<ApiResponse> getAlbumList(String type, int size, int offset) {
        Log.d(TAG, "getAlbumList()");
        return albumSongListService.getAlbumList(subsonic.getParams(), type, size, offset);
    }

    public Call<ApiResponse> getAlbumList2(String type, int size, int offset, Integer fromYear, Integer toYear) {
        Log.d(TAG, "getAlbumList2()");
        return albumSongListService.getAlbumList2(subsonic.getParams(), type, size, offset, fromYear, toYear);
    }

    public Call<ApiResponse> getRandomSongs(int size, Integer fromYear, Integer toYear) {
        Log.d(TAG, "getRandomSongs()");
        return albumSongListService.getRandomSongs(subsonic.getParams(), size, fromYear, toYear);
    }

    public Call<ApiResponse> getSongsByGenre(String genre, int count, int offset) {
        Log.d(TAG, "getSongsByGenre()");
        return albumSongListService.getSongsByGenre(subsonic.getParams(), genre, count, offset);
    }

    public Call<ApiResponse> getNowPlaying() {
        Log.d(TAG, "getNowPlaying()");
        return albumSongListService.getNowPlaying(subsonic.getParams());
    }

    public Call<ApiResponse> getStarred() {
        Log.d(TAG, "getStarred()");
        return albumSongListService.getStarred(subsonic.getParams());
    }

    public Call<ApiResponse> getStarred2() {
        Log.d(TAG, "getStarred2()");
        return albumSongListService.getStarred2(subsonic.getParams());
    }
}
