package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
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
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getGenres() != null) {
                            List<Genre> genreList = new ArrayList<>(MappingUtil.mapGenre(response.body().getGenres().getGenres()));

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
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return genres;
    }
}
