package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.util.MappingUtil;

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

        playlistRepository = new PlaylistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Playlist>> getPlaylistList(FragmentActivity activity) {
        playlistList = new MutableLiveData<>(new ArrayList<>());

        switch (type) {
            case Playlist.ALL:
                playlistRepository.getPlaylists(false, -1).observe(activity, playlists -> playlistList.postValue(playlists));
                break;
            case Playlist.DOWNLOADED:
                downloadRepository.getLivePlaylist().observe(activity, downloads -> playlistList.setValue(MappingUtil.mapDownloadToPlaylist(downloads)));
                break;
        }

        playlistRepository.getPlaylists(false, -1);

        return playlistList;
    }

    public LiveData<List<Playlist>> getPinnedPlaylistList(FragmentActivity activity) {
        pinnedPlaylistList = new MutableLiveData<>(new ArrayList<>());
        playlistRepository.getPinnedPlaylists().observe(activity, playlists -> pinnedPlaylistList.postValue(playlists));
        return pinnedPlaylistList;
    }

    public void unpinPlaylist(List<Playlist> playlists) {
        if(type.equals(Playlist.ALL)) {
            for(Playlist playlist: playlists) {
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
