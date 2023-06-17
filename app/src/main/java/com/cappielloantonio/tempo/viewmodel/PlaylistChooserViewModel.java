package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.repository.PlaylistRepository;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.Playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistChooserViewModel extends AndroidViewModel {
    private final PlaylistRepository playlistRepository;

    private final MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>(null);
    private Child toAdd;

    public PlaylistChooserViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository();
    }

    public LiveData<List<Playlist>> getPlaylistList(LifecycleOwner owner) {
        playlistRepository.getPlaylists(false, -1).observe(owner, playlists::postValue);
        return playlists;
    }

    public void addSongToPlaylist(String playlistId) {
        playlistRepository.addSongToPlaylist(playlistId, new ArrayList(Collections.singletonList(toAdd.getId())));
    }

    public void setSongToAdd(Child song) {
        toAdd = song;
    }

    public Child getSongToAdd() {
        return toAdd;
    }
}
