package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class PlaylistPageViewModel extends AndroidViewModel {
    private static final String TAG = "PlaylistPageViewModel";

    private SongRepository songRepository;

    private LiveData<List<Song>> songList;

    private Playlist playlist;

    public PlaylistPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
    }

    public LiveData<List<Song>> getPlaylistSongList() {
        // Prendere le canzoni di ciascuna playlist
        Log.i(TAG, "getPlaylistSongList: " + playlist.getId());
        songList = songRepository.getPlaylistLiveSong(playlist.getId());
        return songList;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
