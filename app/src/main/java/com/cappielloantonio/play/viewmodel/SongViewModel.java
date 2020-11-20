package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class SongViewModel extends AndroidViewModel {
    private SongRepository repository;
    private LiveData<List<Song>> allSongs;

    public SongViewModel(@NonNull Application application) {
        super(application);

        repository = new SongRepository(application);
        allSongs = repository.getListLiveSongs();
    }

    public boolean exist(Song song) {
        return repository.exist(song);
    }

    public void insert(Song song) {
        repository.insert(song);
    }

    public void delete(Song song) {
        repository.delete(song);
    }

    public LiveData<List<Song>> getAllSongs() {
        return allSongs;
    }
}
