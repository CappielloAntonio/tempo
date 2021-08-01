package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
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

    private Application application;

    private MutableLiveData<List<Genre>> genres = new MutableLiveData<>();

    public GenreRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Genre>> getGenres(boolean random, int size) {
        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getGenres()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
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
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return genres;
    }
}
