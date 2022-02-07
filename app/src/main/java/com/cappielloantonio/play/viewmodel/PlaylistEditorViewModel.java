package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.PlaylistRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlaylistEditorViewModel extends AndroidViewModel {
    private static final String TAG = "PlaylistEditorViewModel";

    private final PlaylistRepository playlistRepository;

    private Media toAdd;
    private Playlist toEdit;

    private MutableLiveData<List<Media>> songLiveList = new MutableLiveData<>();

    public PlaylistEditorViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository(application);
    }

    public void createPlaylist(String name) {
        playlistRepository.createPlaylist(null, name, new ArrayList(Collections.singletonList(toAdd.getId())));
    }

    public void updatePlaylist(String name) {
        playlistRepository.deletePlaylist(toEdit.getId());
        playlistRepository.createPlaylist(toEdit.getId(), name, getPlaylistSongIds());
    }

    public void deletePlaylist() {
        if (toEdit != null) playlistRepository.deletePlaylist(toEdit.getId());
    }

    public Media getSongToAdd() {
        return toAdd;
    }

    public void setSongToAdd(Media song) {
        this.toAdd = song;
    }

    public Playlist getPlaylistToEdit() {
        return toEdit;
    }

    public void setPlaylistToEdit(Playlist playlist) {
        this.toEdit = playlist;

        if (playlist != null) {
            this.songLiveList = playlistRepository.getPlaylistSongs(toEdit.getId());
        } else {
            this.songLiveList = new MutableLiveData<>();
        }
    }

    public LiveData<List<Media>> getPlaylistSongLiveList() {
        return songLiveList;
    }

    public void removeFromPlaylistSongLiveList(int position) {
        List<Media> songs = songLiveList.getValue();
        Objects.requireNonNull(songs).remove(position);
        songLiveList.postValue(songs);
    }

    public void orderPlaylistSongLiveListAfterSwap(List<Media> songs) {
        songLiveList.postValue(songs);
    }

    private ArrayList<String> getPlaylistSongIds() {
        List<Media> songs = songLiveList.getValue();
        ArrayList<String> ids = new ArrayList<>();

        if (songs != null && !songs.isEmpty()) {
            for (Media song : songs) {
                ids.add(song.getId());
            }
        }

        return ids;
    }
}
