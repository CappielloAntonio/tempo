package com.cappielloantonio.play.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.InternetRadioStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RadioRepository {
    public MutableLiveData<List<InternetRadioStation>> getInternetRadioStations() {
        MutableLiveData<List<InternetRadioStation>> radioStation = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getInternetRadioClient()
                .getInternetRadioStations()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getInternetRadioStations() != null) {
                            radioStation.setValue(response.body().getSubsonicResponse().getInternetRadioStations().getInternetRadioStations());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return radioStation;
    }
}
