package com.cappielloantonio.tempo.subsonic.api.podcast;

import android.util.Log;

import com.cappielloantonio.tempo.subsonic.RetrofitClient;
import com.cappielloantonio.tempo.subsonic.Subsonic;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import retrofit2.Call;

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

    public Call<ApiResponse> createPodcastChannel(String url) {
        Log.d(TAG, "createPodcastChannel()");
        return podcastService.createPodcastChannel(subsonic.getParams(), url);
    }

    public Call<ApiResponse> deletePodcastChannel(String channelId) {
        Log.d(TAG, "deletePodcastChannel()");
        return podcastService.deletePodcastChannel(subsonic.getParams(), channelId);
    }

    public Call<ApiResponse> deletePodcastEpisode(String episodeId) {
        Log.d(TAG, "deletePodcastEpisode()");
        return podcastService.deletePodcastEpisode(subsonic.getParams(), episodeId);
    }
}
