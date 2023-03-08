package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.ScanCallback;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class ScanRepository {
    private static final String TAG = "SongRepository";

    private final Application application;

    public ScanRepository(Application application) {
        this.application = application;
    }

    public void startScan(ScanCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getMediaLibraryScanningClient()
                .startScan()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull retrofit2.Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getScanStatus() != null) {
                            callback.onSuccess(response.body().getSubsonicResponse().getScanStatus().isScanning(), response.body().getSubsonicResponse().getScanStatus().getCount());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public void getScanStatus(ScanCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getMediaLibraryScanningClient()
                .startScan()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull retrofit2.Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getScanStatus() != null) {
                            callback.onSuccess(response.body().getSubsonicResponse().getScanStatus().isScanning(), response.body().getSubsonicResponse().getScanStatus().getCount());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }
}
