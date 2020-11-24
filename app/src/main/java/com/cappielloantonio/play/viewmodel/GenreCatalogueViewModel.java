package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;

import java.util.List;

public class GenreCatalogueViewModel extends AndroidViewModel {
    private GenreRepository genreRepository;

    private LiveData<List<Genre>> genreList;

    public GenreCatalogueViewModel(@NonNull Application application) {
        super(application);

        genreRepository = new GenreRepository(application);
    }

    public LiveData<List<Genre>> getGenreList() {
        genreList = genreRepository.getListLiveGenres();
        return genreList;
    }

    public List<Genre> getGenres() {
        return genreRepository.getListGenre();
    }
}
