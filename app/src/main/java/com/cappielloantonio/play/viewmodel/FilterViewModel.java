package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.RecentSearchRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class FilterViewModel extends AndroidViewModel {
    private GenreRepository genreRepository;

    private LiveData<List<Genre>> allGenres;

    public FilterViewModel(@NonNull Application application) {
        super(application);

        genreRepository = new GenreRepository(application);
    }

    public LiveData<List<Genre>> getGenreList() {
        allGenres = genreRepository.getListLiveGenres();
        return allGenres;
    }
}
