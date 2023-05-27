package com.cappielloantonio.play.subsonic.api.browsing;

import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface BrowsingService {
    @GET("getMusicFolders")
    Call<ApiResponse> getMusicFolders(@QueryMap Map<String, String> params);

    @GET("getIndexes")
    Call<ApiResponse> getIndexes(@QueryMap Map<String, String> params, @Query("musicFolderId") String musicFolderId, @Query("ifModifiedSince") Long ifModifiedSince);

    @GET("getMusicDirectory")
    Call<ApiResponse> getMusicDirectory(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getGenres")
    Call<ApiResponse> getGenres(@QueryMap Map<String, String> params);

    @GET("getArtists")
    Call<ApiResponse> getArtists(@QueryMap Map<String, String> params);

    @GET("getArtist")
    Call<ApiResponse> getArtist(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getAlbum")
    Call<ApiResponse> getAlbum(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getSong")
    Call<ApiResponse> getSong(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getVideos")
    Call<ApiResponse> getVideos(@QueryMap Map<String, String> params);

    @GET("getVideoInfo")
    Call<ApiResponse> getVideoInfo(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getArtistInfo")
    Call<ApiResponse> getArtistInfo(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getArtistInfo2")
    Call<ApiResponse> getArtistInfo2(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getAlbumInfo")
    Call<ApiResponse> getAlbumInfo(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getAlbumInfo2")
    Call<ApiResponse> getAlbumInfo2(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getSimilarSongs")
    Call<ApiResponse> getSimilarSongs(@QueryMap Map<String, String> params, @Query("id") String id, @Query("count") int count);

    @GET("getSimilarSongs2")
    Call<ApiResponse> getSimilarSongs2(@QueryMap Map<String, String> params, @Query("id") String id, @Query("count") int count);

    @GET("getTopSongs")
    Call<ApiResponse> getTopSongs(@QueryMap Map<String, String> params, @Query("artist") String artist, @Query("count") int count);
}
