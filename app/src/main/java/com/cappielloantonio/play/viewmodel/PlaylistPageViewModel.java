package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.Playlist;

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
}
