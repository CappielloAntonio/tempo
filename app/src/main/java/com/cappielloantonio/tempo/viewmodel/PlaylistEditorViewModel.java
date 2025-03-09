package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.repository.PlaylistRepository;
import com.cappielloantonio.tempo.repository.SharingRepository;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.Playlist;
import com.cappielloantonio.tempo.subsonic.models.Share;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlaylistEditorViewModel extends AndroidViewModel {
    private static final String TAG = "PlaylistEditorViewModel";

    private final PlaylistRepository playlistRepository;
    private final SharingRepository sharingRepository;

    private ArrayList<Child> toAdd;
    private Playlist toEdit;

    private MutableLiveData<List<Child>> songLiveList = new MutableLiveData<>();

    public PlaylistEditorViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository();
        sharingRepository = new SharingRepository();
    }

    public void createPlaylist(String name) {
        playlistRepository.createPlaylist(null, name, new ArrayList(Lists.transform(toAdd, Child::getId)));
    }

    public void updatePlaylist(String name) {
        playlistRepository.updatePlaylist(toEdit.getId(), name, getPlaylistSongIds());
    }

    public void deletePlaylist() {
        if (toEdit != null) playlistRepository.deletePlaylist(toEdit.getId());
    }

    public void setSongsToAdd(ArrayList<Child> songs) {
        toAdd = songs;
    }

    public ArrayList<Child> getSongsToAdd() {
        return toAdd;
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

    public LiveData<List<Child>> getPlaylistSongLiveList() {
        return songLiveList;
    }

    public void removeFromPlaylistSongLiveList(int position) {
        List<Child> songs = songLiveList.getValue();
        Objects.requireNonNull(songs).remove(position);
        songLiveList.postValue(songs);
    }

    public void orderPlaylistSongLiveListAfterSwap(List<Child> songs) {
        songLiveList.postValue(songs);
    }

    private ArrayList<String> getPlaylistSongIds() {
        List<Child> songs = songLiveList.getValue();
        ArrayList<String> ids = new ArrayList<>();

        if (songs != null && !songs.isEmpty()) {
            for (Child song : songs) {
                ids.add(song.getId());
            }
        }

        return ids;
    }

    public MutableLiveData<Share> sharePlaylist() {
        return sharingRepository.createShare(toEdit.getId(), toEdit.getName(), null);
    }
}
