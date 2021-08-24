package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.SystemCallback;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import retrofit2.Call;
import retrofit2.Callback;

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
                        if(response.body() != null) {
                            if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                                callback.onError(new Exception(response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage()));
                            } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                                String salt = response.raw().request().url().queryParameter("s");
                                String token = response.raw().request().url().queryParameter("t");
                                callback.onSuccess(token, salt);
                            } else {
                                callback.onError(new Exception("Empty response"));
                            }
                        }
                        else {
                            callback.onError(new Exception(String.valueOf(response.code())));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }
}
