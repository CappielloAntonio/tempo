package com.cappielloantonio.tempo.subsonic.api.sharing;

import android.util.Log;

import com.cappielloantonio.tempo.subsonic.RetrofitClient;
import com.cappielloantonio.tempo.subsonic.Subsonic;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import retrofit2.Call;

public class SharingClient {
    private static final String TAG = "BrowsingClient";

    private final Subsonic subsonic;
    private final SharingService sharingService;

    public SharingClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.sharingService = new RetrofitClient(subsonic).getRetrofit().create(SharingService.class);
    }

    public Call<ApiResponse> getShares() {
        Log.d(TAG, "getShares()");
        return sharingService.getShares(subsonic.getParams());
    }

    public Call<ApiResponse> createShare(String id, String description, Long expires) {
        Log.d(TAG, "createShare()");
        return sharingService.createShare(subsonic.getParams(), id, description, expires);
    }

    public Call<ApiResponse> updateShare(String id, String description, Long expires) {
        Log.d(TAG, "updateShare()");
        return sharingService.updateShare(subsonic.getParams(), id, description, expires);
    }

    public Call<ApiResponse> deleteShare(String id) {
        Log.d(TAG, "deleteShare()");
        return sharingService.deleteShare(subsonic.getParams(), id);
    }
}
