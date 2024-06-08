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

import java.util.List;

public class PlaylistPageViewModel extends AndroidViewModel {
    private final PlaylistRepository playlistRepository;

    private Playlist playlist;
    private boolean isOffline;

    public PlaylistPageViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository();
    }

    public LiveData<List<Child>> getPlaylistSongLiveList() {
        return playlistRepository.getPlaylistSongs(playlist.getId());
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public LiveData<Boolean> isPinned(LifecycleOwner owner) {
        MutableLiveData<Boolean> isPinnedLive = new MutableLiveData<>();

        playlistRepository.getPinnedPlaylists().observe(owner, playlists -> {
            isPinnedLive.postValue(playlists.stream().anyMatch(obj -> obj.getId().equals(playlist.getId())));
        });

        return isPinnedLive;
    }

    public void setPinned(boolean isNowPinned) {
        if (isNowPinned) {
            playlistRepository.insert(playlist);
        } else {
            playlistRepository.delete(playlist);
        }
    }
}
