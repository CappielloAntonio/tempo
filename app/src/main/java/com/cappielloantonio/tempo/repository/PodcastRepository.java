package com.cappielloantonio.tempo.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;
import com.cappielloantonio.tempo.subsonic.models.PodcastChannel;
import com.cappielloantonio.tempo.subsonic.models.PodcastEpisode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PodcastRepository {
    private static final String TAG = "PodcastRepository";

    public MutableLiveData<List<PodcastChannel>> getPodcastChannels(boolean includeEpisodes, String channelId) {
        MutableLiveData<List<PodcastChannel>> livePodcastChannel = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getPodcastClient()
                .getPodcasts(includeEpisodes, channelId)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getPodcasts() != null) {
                            livePodcastChannel.setValue(response.body().getSubsonicResponse().getPodcasts().getChannels());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return livePodcastChannel;
    }

    public MutableLiveData<List<PodcastEpisode>> getNewestPodcastEpisodes(int count) {
        MutableLiveData<List<PodcastEpisode>> liveNewestPodcastEpisodes = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getPodcastClient()
                .getNewestPodcasts(count)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getNewestPodcasts() != null) {
                            liveNewestPodcastEpisodes.setValue(response.body().getSubsonicResponse().getNewestPodcasts().getEpisodes());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure()");
                    }
                });

        return liveNewestPodcastEpisodes;
    }

    public void refreshPodcasts() {
        App.getSubsonicClientInstance(false)
                .getPodcastClient()
                .refreshPodcasts()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void createPodcastChannel(String url) {
        App.getSubsonicClientInstance(false)
                .getPodcastClient()
                .createPodcastChannel(url)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void deletePodcastChannel(String channelId) {
        App.getSubsonicClientInstance(false)
                .getPodcastClient()
                .deletePodcastChannel(channelId)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void deletePodcastEpisode(String episodeId) {
        App.getSubsonicClientInstance(false)
                .getPodcastClient()
                .deletePodcastEpisode(episodeId)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }
}
