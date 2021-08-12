package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Server;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.repository.ServerRepository;

import java.util.List;

public class PlaylistEditorViewModel extends AndroidViewModel {
    private PlaylistRepository playlistRepository;

    private Song toAdd;

    public PlaylistEditorViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository(application);
    }

    public void createPlaylist(String name) {
        playlistRepository.createPlaylist(name, toAdd.getId());
    }

    public void updatePlaylist(String playlistId) {

    }

    public void setSongToAdd(Song song) {
        toAdd = song;
    }
}
