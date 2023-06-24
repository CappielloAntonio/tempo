package com.cappielloantonio.tempo.repository;

import androidx.annotation.NonNull;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.interfaces.ScanCallback;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class ScanRepository {
    public void startScan(ScanCallback callback) {
        App.getSubsonicClientInstance(false)
                .getMediaLibraryScanningClient()
                .startScan()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull retrofit2.Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse() != null) {
                            if (response.body().getSubsonicResponse().getError() != null) {
                                callback.onError(new Exception(response.body().getSubsonicResponse().getError().getMessage()));
                            } else if (response.body().getSubsonicResponse().getScanStatus() != null) {
                                callback.onSuccess(response.body().getSubsonicResponse().getScanStatus().isScanning(), response.body().getSubsonicResponse().getScanStatus().getCount());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public void getScanStatus(ScanCallback callback) {
        App.getSubsonicClientInstance(false)
                .getMediaLibraryScanningClient()
                .startScan()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull retrofit2.Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse() != null) {
                            if (response.body().getSubsonicResponse().getError() != null) {
                                callback.onError(new Exception(response.body().getSubsonicResponse().getError().getMessage()));
                            } else if (response.body().getSubsonicResponse().getScanStatus() != null) {
                                callback.onSuccess(response.body().getSubsonicResponse().getScanStatus().isScanning(), response.body().getSubsonicResponse().getScanStatus().getCount());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }
}
