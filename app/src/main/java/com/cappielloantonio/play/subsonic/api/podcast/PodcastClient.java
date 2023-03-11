package com.cappielloantonio.play.subsonic.api.podcast;

import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.RetrofitClient;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PodcastClient {
    private static final String TAG = "SystemClient";

    private final Subsonic subsonic;
    private final PodcastService podcastService;

    public PodcastClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.podcastService = new RetrofitClient(subsonic).getRetrofit().create(PodcastService.class);
    }

    public Call<ApiResponse> getPodcasts(boolean includeEpisodes, String channelId) {
        Log.d(TAG, "getPodcasts()");
        return podcastService.getPodcasts(subsonic.getParams(), includeEpisodes, channelId);
    }

    public Call<ApiResponse> getNewestPodcasts(int count) {
        Log.d(TAG, "getNewestPodcasts()");
        return podcastService.getNewestPodcasts(subsonic.getParams(), count);
    }

    public Call<ApiResponse> refreshPodcasts() {
        Log.d(TAG, "refreshPodcasts()");
        return podcastService.refreshPodcasts(subsonic.getParams());
    }
}
