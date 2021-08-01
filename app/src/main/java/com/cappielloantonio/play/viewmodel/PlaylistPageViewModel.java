package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.PlaylistRepository;

import java.util.List;

public class PlaylistPageViewModel extends AndroidViewModel {
    private PlaylistRepository playlistRepository;

    private MutableLiveData<List<Song>> songLiveList = new MutableLiveData<>();

    private Playlist playlist;

    public PlaylistPageViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository(application);
    }

    public MutableLiveData<List<Song>> getPlaylistSongLiveList() {
        songLiveList = playlistRepository.getPlaylistSongs(playlist.getId());
        return songLiveList;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
