package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.subsonic.models.Playlist;
import com.cappielloantonio.play.util.Preferences;

import java.util.ArrayList;
import java.util.List;

public class PlaylistCatalogueViewModel extends AndroidViewModel {
    private final PlaylistRepository playlistRepository;
    private final DownloadRepository downloadRepository;

    private String type;

    private MutableLiveData<List<Playlist>> playlistList;
    private MutableLiveData<List<Playlist>> pinnedPlaylistList;

    public PlaylistCatalogueViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository();
        downloadRepository = new DownloadRepository();
    }

    public LiveData<List<Playlist>> getPlaylistList(LifecycleOwner owner) {
        playlistList = new MutableLiveData<>(new ArrayList<>());

        switch (type) {
            case com.cappielloantonio.play.model.Playlist.ALL:
                playlistRepository.getPlaylists(false, -1).observe(owner, playlists -> playlistList.postValue(playlists));
                break;
            case com.cappielloantonio.play.model.Playlist.DOWNLOADED:
                // TODO
                //downloadRepository.getLivePlaylist().observe(owner, downloads -> playlistList.setValue(MappingUtil.mapDownloadToPlaylist(downloads)));
                break;
        }

        playlistRepository.getPlaylists(false, -1);

        return playlistList;
    }

    public LiveData<List<Playlist>> getPinnedPlaylistList(LifecycleOwner owner) {
        pinnedPlaylistList = new MutableLiveData<>(new ArrayList<>());
        playlistRepository.getPinnedPlaylists(Preferences.getServerId()).observe(owner, playlists -> pinnedPlaylistList.postValue(playlists));
        return pinnedPlaylistList;
    }

    public void unpinPlaylist(List<Playlist> playlists) {
        if (type.equals(com.cappielloantonio.play.model.Playlist.ALL)) {
            for (Playlist playlist : playlists) {
                playlistRepository.delete(playlist);
            }
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
