package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.Genre;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreRepository {
    private static final String TAG = "GenreRepository";

    private final Application application;

    public GenreRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Genre>> getGenres(boolean random, int size) {
        MutableLiveData<List<Genre>> genres = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getGenres()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getGenres() != null) {
                            List<Genre> genreList = response.body().getSubsonicResponse().getGenres().getGenres();

                            if (random) {
                                Collections.shuffle(genreList);
                            }

                            if (size != -1) {
                                genres.setValue(genreList.subList(0, Math.min(size, genreList.size())));
                            } else {
                                genres.setValue(genreList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return genres;
    }
}
