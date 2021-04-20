package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;

import java.util.List;

public class PlaylistCatalogueViewModel extends AndroidViewModel {
    private PlaylistRepository playlistRepository;

    private LiveData<List<Playlist>> playlistList;

    public PlaylistCatalogueViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository(application);
    }

    public LiveData<List<Playlist>> getPlaylistList() {
        playlistList = playlistRepository.getListLivePlaylists();
        return playlistList;
    }
}
