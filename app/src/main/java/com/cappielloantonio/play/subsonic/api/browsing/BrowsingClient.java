package com.cappielloantonio.play.subsonic.api.browsing;

import android.util.Log;

import com.cappielloantonio.play.subsonic.RetrofitClient;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.base.ApiResponse;

import retrofit2.Call;

public class BrowsingClient {
    private static final String TAG = "BrowsingClient";

    private final Subsonic subsonic;
    private final BrowsingService browsingService;

    public BrowsingClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.browsingService = new RetrofitClient(subsonic).getRetrofit().create(BrowsingService.class);
    }

    public Call<ApiResponse> getMusicFolders() {
        Log.d(TAG, "getMusicFolders()");
        return browsingService.getMusicFolders(subsonic.getParams());
    }

    public Call<ApiResponse> getIndexes(String musicFolderId, Long ifModifiedSince) {
        Log.d(TAG, "getIndexes()");
        return browsingService.getIndexes(subsonic.getParams(), musicFolderId, ifModifiedSince);
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
}
