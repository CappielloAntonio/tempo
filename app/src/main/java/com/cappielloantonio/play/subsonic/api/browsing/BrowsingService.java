package com.cappielloantonio.play.subsonic.api.browsing;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface BrowsingService {
    @GET("getMusicFolders")
    Call<SubsonicResponse> getMusicFolders(@QueryMap Map<String, String> params);

    @GET("getIndexes")
    Call<SubsonicResponse> getIndexes(@QueryMap Map<String, String> params);

    @GET("getMusicDirectory")
    Call<SubsonicResponse> getMusicDirectory(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getGenres")
    Call<SubsonicResponse> getGenres(@QueryMap Map<String, String> params);

    @GET("getArtists")
    Call<SubsonicResponse> getArtists(@QueryMap Map<String, String> params);

    @GET("getArtist")
    Call<SubsonicResponse> getArtist(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getAlbum")
    Call<SubsonicResponse> getAlbum(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getSong")
    Call<SubsonicResponse> getSong(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getVideos")
    Call<SubsonicResponse> getVideos(@QueryMap Map<String, String> params);

    @GET("getVideoInfo")
    Call<SubsonicResponse> getVideoInfo(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getArtistInfo")
    Call<SubsonicResponse> getArtistInfo(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getArtistInfo2")
    Call<SubsonicResponse> getArtistInfo2(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getAlbumInfo")
    Call<SubsonicResponse> getAlbumInfo(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getAlbumInfo2")
    Call<SubsonicResponse> getAlbumInfo2(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getSimilarSongs")
    Call<SubsonicResponse> getSimilarSongs(@QueryMap Map<String, String> params, @Query("id") String id, @Query("count") int count);

    @GET("getSimilarSongs2")
    Call<SubsonicResponse> getSimilarSongs2(@QueryMap Map<String, String> params, @Query("id") String id, @Query("count") int count);

    @GET("getTopSongs")
    Call<SubsonicResponse> getTopSongs(@QueryMap Map<String, String> params, @Query("artist") String artist, @Query("count") int count);
}
