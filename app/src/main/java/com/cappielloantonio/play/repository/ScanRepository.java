package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.ScanCallback;
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
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull retrofit2.Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getScanStatus() != null) {
                            callback.onSuccess(response.body().getScanStatus().isScanning(), response.body().getScanStatus().getCount());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public void getScanStatus(ScanCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getMediaLibraryScanningClient()
                .startScan()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull retrofit2.Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getScanStatus() != null) {
                            callback.onSuccess(response.body().getScanStatus().isScanning(), response.body().getScanStatus().getCount());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }
}
