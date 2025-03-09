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
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistChooserViewModel extends AndroidViewModel {
    private final PlaylistRepository playlistRepository;

    private final MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>(null);
    private ArrayList<Child> toAdd;

    public PlaylistChooserViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository();
    }

    public LiveData<List<Playlist>> getPlaylistList(LifecycleOwner owner) {
        playlistRepository.getPlaylists(false, -1).observe(owner, playlists::postValue);
        return playlists;
    }

    public void addSongsToPlaylist(String playlistId) {
        playlistRepository.addSongToPlaylist(playlistId, new ArrayList<>(Lists.transform(toAdd, Child::getId)));
    }

    public void setSongsToAdd(ArrayList<Child> songs) {
        toAdd = songs;
    }

    public ArrayList<Child> getSongsToAdd() {
        return toAdd;
    }
}
