package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class PlaylistPageViewModel extends AndroidViewModel {
    private static final String TAG = "PlaylistPageViewModel";

    private SongRepository songRepository;

    private LiveData<List<Song>> songLiveList;

    private Playlist playlist;

    public PlaylistPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
    }

    public LiveData<List<Song>> getPlaylistSongLiveList() {
        // songLiveList = songRepository.getPlaylistLiveSong(playlist.getId());
        return songLiveList;
    }

    public List<Song> getPlaylistSongList() {
        // return songRepository.getPlaylistSong(playlist.getId());
        return null;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
