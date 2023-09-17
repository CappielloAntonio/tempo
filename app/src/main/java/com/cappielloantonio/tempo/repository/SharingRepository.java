package com.cappielloantonio.tempo.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;
import com.cappielloantonio.tempo.subsonic.models.Share;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharingRepository {
    public MutableLiveData<List<Share>> getShares() {
        MutableLiveData<List<Share>> shares = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getSharingClient()
                .getShares()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getShares() != null && response.body().getSubsonicResponse().getShares().getShares() != null) {
                            shares.setValue(response.body().getSubsonicResponse().getShares().getShares());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return shares;
    }

    public MutableLiveData<Share> createShare(String id, String description, Long expires) {
        MutableLiveData<Share> share = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getSharingClient()
                .createShare(id, description, expires)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getShares() != null && response.body().getSubsonicResponse().getShares().getShares() != null && response.body().getSubsonicResponse().getShares().getShares().get(0) != null) {
                            share.setValue(response.body().getSubsonicResponse().getShares().getShares().get(0));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return share;
    }

    public void updateShare(String id, String description, Long expires) {
        App.getSubsonicClientInstance(false)
                .getSharingClient()
                .updateShare(id, description, expires)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void deleteShare(String id) {
        App.getSubsonicClientInstance(false)
                .getSharingClient()
                .deleteShare(id)
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
