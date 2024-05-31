package com.cappielloantonio.tempo.github.api.release;

import android.util.Log;

import com.cappielloantonio.tempo.github.Github;
import com.cappielloantonio.tempo.github.GithubRetrofitClient;
import com.cappielloantonio.tempo.github.models.LatestRelease;

import retrofit2.Call;

public class ReleaseClient {
    private static final String TAG = "ReleaseClient";

    private final ReleaseService releaseService;

    public ReleaseClient(Github github) {
        this.releaseService = new GithubRetrofitClient(github).getRetrofit().create(ReleaseService.class);
    }

    public Call<LatestRelease> getLatestRelease() {
        Log.d(TAG, "getLatestRelease()");
        return releaseService.getLatestRelease(Github.getOwner(), Github.getRepo());
    }
}
