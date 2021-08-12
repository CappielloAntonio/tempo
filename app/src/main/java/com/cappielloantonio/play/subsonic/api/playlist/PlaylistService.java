package com.cappielloantonio.play.subsonic.api.playlist;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PlaylistService {
    @GET("getPlaylists")
    Call<SubsonicResponse> getPlaylists(@QueryMap Map<String, String> params);

    @GET("getPlaylist")
    Call<SubsonicResponse> getPlaylist(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("createPlaylist")
    Call<SubsonicResponse> createPlaylist(@QueryMap Map<String, String> params, @Query("playlistId") String playlistId, @Query("name") String name, @Query("songId") ArrayList<String> songsId);

    @GET("updatePlaylist")
    Call<SubsonicResponse> updatePlaylist(@QueryMap Map<String, String> params, @Query("playlistId") String playlistId, @Query("name") String name, @Query("public") boolean isPublic, @Query("songIdToAdd") ArrayList<String> songIdToAdd, @Query("songIndexToRemove") ArrayList<Integer> songIndexToRemove);

    @GET("deletePlaylist")
    Call<SubsonicResponse> deletePlaylist(@QueryMap Map<String, String> params, @Query("id") String id);
}
