package com.cappielloantonio.tempo.subsonic.api.mediaretrieval;

import android.util.Log;

import com.cappielloantonio.tempo.subsonic.RetrofitClient;
import com.cappielloantonio.tempo.subsonic.Subsonic;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import retrofit2.Call;

public class MediaRetrievalClient {
    private static final String TAG = "BrowsingClient";

    private final Subsonic subsonic;
    private final MediaRetrievalService mediaRetrievalService;

    public MediaRetrievalClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.mediaRetrievalService = new RetrofitClient(subsonic).getRetrofit().create(MediaRetrievalService.class);
    }

    public Call<ApiResponse> stream(String id, Integer maxBitRate, String format) {
        Log.d(TAG, "stream()");
        return mediaRetrievalService.stream(subsonic.getParams(), id, maxBitRate, format);
    }

    public Call<ApiResponse> download(String id) {
        Log.d(TAG, "download()");
        return mediaRetrievalService.download(subsonic.getParams(), id);
    }

    public Call<ApiResponse> getLyrics(String artist, String title) {
        Log.d(TAG, "getLyrics()");
        return mediaRetrievalService.getLyrics(subsonic.getParams(), artist, title);
    }
}
