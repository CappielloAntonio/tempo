package com.cappielloantonio.play.subsonic.api.mediaretrieval;

import android.util.Log;

import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.api.playlist.PlaylistService;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaRetrievalClient {
    private static final String TAG = "BrowsingClient";

    private Subsonic subsonic;
    private Retrofit retrofit;
    private MediaRetrievalService mediaRetrievalService;

    public MediaRetrievalClient(Subsonic subsonic) {
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.mediaRetrievalService = retrofit.create(MediaRetrievalService.class);
    }

    public Call<SubsonicResponse> stream(String id, int maxBitRate, String format) {
        Log.d(TAG, "stream()");
        return mediaRetrievalService.stream(subsonic.getParams(), id, maxBitRate, format);
    }

    public Call<SubsonicResponse> download(String id) {
        Log.d(TAG, "download()");
        return mediaRetrievalService.download(subsonic.getParams(), id);
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .build();
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }
}
