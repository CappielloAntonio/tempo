package com.cappielloantonio.play.subsonic.api.playlist;

import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.RetrofitClient;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
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

    private final Subsonic subsonic;
    private final PlaylistService playlistService;

    public PlaylistClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.playlistService = new RetrofitClient(subsonic).getRetrofit().create(PlaylistService.class);
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
}
