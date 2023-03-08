package com.cappielloantonio.play.subsonic.api.playlist;

import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PlaylistService {
    @GET("getPlaylists")
    Call<ApiResponse> getPlaylists(@QueryMap Map<String, String> params);

    @GET("getPlaylist")
    Call<ApiResponse> getPlaylist(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("createPlaylist")
    Call<ApiResponse> createPlaylist(@QueryMap Map<String, String> params, @Query("playlistId") String playlistId, @Query("name") String name, @Query("songId") ArrayList<String> songsId);

    @GET("updatePlaylist")
    Call<ApiResponse> updatePlaylist(@QueryMap Map<String, String> params, @Query("playlistId") String playlistId, @Query("name") String name, @Query("public") boolean isPublic, @Query("songIdToAdd") ArrayList<String> songIdToAdd, @Query("songIndexToRemove") ArrayList<Integer> songIndexToRemove);

    @GET("deletePlaylist")
    Call<ApiResponse> deletePlaylist(@QueryMap Map<String, String> params, @Query("id") String id);
}
