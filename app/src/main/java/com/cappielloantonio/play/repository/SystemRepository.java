package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.SystemCallback;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SystemRepository {
    private static final String TAG = "SongRepository";

    private final Application application;

    public SystemRepository(Application application) {
        this.application = application;
    }

    public void checkUserCredential(SystemCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getSystemClient()
                .ping()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull retrofit2.Response<SubsonicResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                                callback.onError(new Exception(response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage()));
                            } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                                String password = response.raw().request().url().queryParameter("p");
                                String token = response.raw().request().url().queryParameter("t");
                                String salt = response.raw().request().url().queryParameter("s");
                                callback.onSuccess(password, token, salt);
                            } else {
                                callback.onError(new Exception("Empty response"));
                            }
                        } else {
                            callback.onError(new Exception(String.valueOf(response.code())));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public MutableLiveData<Boolean> ping() {
        MutableLiveData<Boolean> pingResult = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getSystemClient()
                .ping()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            pingResult.postValue(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        pingResult.postValue(false);
                    }
                });

        return pingResult;
    }
}
