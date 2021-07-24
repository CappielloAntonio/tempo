package com.cappielloantonio.play.subsonic.api.browsing;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface BrowsingService {
    @GET("getMusicFolders")
    Call<SubsonicResponse> getMusicFolders(@QueryMap Map<String, String> params);

    @GET("getIndexes")
    Call<SubsonicResponse> getIndexes(@QueryMap Map<String, String> params);

    @GET("getMusicDirectory?id={id}")
    Call<SubsonicResponse> getMusicDirectory(@QueryMap Map<String, String> params, String id);

    @GET("getGenres")
    Call<SubsonicResponse> getGenres(@QueryMap Map<String, String> params);

    @GET("getArtists")
    Call<SubsonicResponse> getArtists(@QueryMap Map<String, String> params);

    @GET("getArtist?id={id}")
    Call<SubsonicResponse> getArtist(@QueryMap Map<String, String> params, String id);

    @GET("getAlbum?id={id}")
    Call<SubsonicResponse> getAlbum(@QueryMap Map<String, String> params, String id);

    @GET("getSong?id={id}")
    Call<SubsonicResponse> getSong(@QueryMap Map<String, String> params, String id);

    @GET("getVideos")
    Call<SubsonicResponse> getVideos(@QueryMap Map<String, String> params);

    @GET("getVideoInfo?id={id}")
    Call<SubsonicResponse> getVideoInfo(@QueryMap Map<String, String> params, String id);

    @GET("getArtistInfo?id={id}")
    Call<SubsonicResponse> getArtistInfo(@QueryMap Map<String, String> params, String id);

    @GET("getArtistInfo2?id={id}")
    Call<SubsonicResponse> getArtistInfo2(@QueryMap Map<String, String> params, String id);

    @GET("getAlbumInfo?id={id}")
    Call<SubsonicResponse> getAlbumInfo(@QueryMap Map<String, String> params, String id);

    @GET("getAlbumInfo2?id={id}")
    Call<SubsonicResponse> getAlbumInfo2(@QueryMap Map<String, String> params, String id);

    @GET("getSimilarSongs?id={id}?count={count}")
    Call<SubsonicResponse> getSimilarSongs(@QueryMap Map<String, String> params, String id, int count);

    @GET("getSimilarSongs2?id={id}?count={count}")
    Call<SubsonicResponse> getSimilarSongs2(@QueryMap Map<String, String> params, String id, int count);

    @GET("getTopSongs?id={id}?count={count}")
    Call<SubsonicResponse> getTopSongs(@QueryMap Map<String, String> params, String id, int count);
}
