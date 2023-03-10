package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.Playlist;
import com.cappielloantonio.play.util.Preferences;

import java.util.List;

public class PlaylistPageViewModel extends AndroidViewModel {
    private final PlaylistRepository playlistRepository;
    private final DownloadRepository downloadRepository;

    private MutableLiveData<List<Child>> playlistSongLiveList = new MutableLiveData<>();

    private Playlist playlist;
    private boolean isOffline;

    public PlaylistPageViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository();
        downloadRepository = new DownloadRepository();
    }

    public LiveData<List<Child>> getPlaylistSongLiveList(LifecycleOwner owner) {
        if (isOffline) {
            // TODO
            // downloadRepository.getLiveDownloadFromPlaylist(playlist.getId()).observe(owner, downloads -> playlistSongLiveList.postValue(MappingUtil.mapDownloadToMedia(downloads)));
        } else {
            playlistSongLiveList = playlistRepository.getPlaylistSongs(playlist.getId());
        }

        return playlistSongLiveList;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
        // TODO
        // this.playlist.setServer(Preferences.getServerId());
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public LiveData<Boolean> isPinned(LifecycleOwner owner) {
        MutableLiveData<Boolean> isPinnedLive = new MutableLiveData<>();
        playlistRepository.getPinnedPlaylists(Preferences.getServerId()).observe(owner, playlists -> isPinnedLive.postValue(playlists.contains(playlist)));
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
