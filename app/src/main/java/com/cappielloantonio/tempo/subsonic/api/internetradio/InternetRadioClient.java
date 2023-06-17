package com.cappielloantonio.tempo.subsonic.api.internetradio;

import android.util.Log;

import com.cappielloantonio.tempo.subsonic.RetrofitClient;
import com.cappielloantonio.tempo.subsonic.Subsonic;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import retrofit2.Call;

public class InternetRadioClient {
    private static final String TAG = "InternetRadioClient";

    private final Subsonic subsonic;
    private final InternetRadioService internetRadioService;

    public InternetRadioClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.internetRadioService = new RetrofitClient(subsonic).getRetrofit().create(InternetRadioService.class);
    }

    public Call<ApiResponse> getInternetRadioStations() {
        Log.d(TAG, "getInternetRadioStations()");
        return internetRadioService.getInternetRadioStations(subsonic.getParams());
    }

    public Call<ApiResponse> createInternetRadioStation(String streamUrl, String name, String homepageUrl) {
        Log.d(TAG, "createInternetRadioStation()");
        return internetRadioService.createInternetRadioStation(subsonic.getParams(), streamUrl, name, homepageUrl);
    }

    public Call<ApiResponse> updateInternetRadioStation(String id, String streamUrl, String name, String homepageUrl) {
        Log.d(TAG, "updateInternetRadioStation()");
        return internetRadioService.updateInternetRadioStation(subsonic.getParams(), id, streamUrl, name, homepageUrl);
    }

    public Call<ApiResponse> deleteInternetRadioStation(String id) {
        Log.d(TAG, "deleteInternetRadioStation()");
        return internetRadioService.deleteInternetRadioStation(subsonic.getParams(), id);
    }
}
