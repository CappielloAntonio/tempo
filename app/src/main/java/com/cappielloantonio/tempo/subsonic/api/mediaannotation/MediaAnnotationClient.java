package com.cappielloantonio.tempo.subsonic.api.mediaannotation;

import android.util.Log;

import com.cappielloantonio.tempo.subsonic.RetrofitClient;
import com.cappielloantonio.tempo.subsonic.Subsonic;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import retrofit2.Call;

public class MediaAnnotationClient {
    private static final String TAG = "MediaAnnotationClient";

    private final Subsonic subsonic;
    private final MediaAnnotationService mediaAnnotationService;

    public MediaAnnotationClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.mediaAnnotationService = new RetrofitClient(subsonic).getRetrofit().create(MediaAnnotationService.class);
    }

    public Call<ApiResponse> star(String id, String albumId, String artistId) {
        Log.d(TAG, "star()");
        return mediaAnnotationService.star(subsonic.getParams(), id, albumId, artistId);
    }

    public Call<ApiResponse> unstar(String id, String albumId, String artistId) {
        Log.d(TAG, "unstar()");
        return mediaAnnotationService.unstar(subsonic.getParams(), id, albumId, artistId);
    }

    public Call<ApiResponse> setRating(String id, int rating) {
        Log.d(TAG, "setRating()");
        return mediaAnnotationService.setRating(subsonic.getParams(), id, rating);
    }

    public Call<ApiResponse> scrobble(String id, boolean submission) {
        Log.d(TAG, "scrobble()");
        return mediaAnnotationService.scrobble(subsonic.getParams(), id, submission);
    }
}
