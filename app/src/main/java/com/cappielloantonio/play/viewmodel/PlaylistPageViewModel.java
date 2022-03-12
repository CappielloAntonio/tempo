package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.List;

public class PlaylistPageViewModel extends AndroidViewModel {
    private final PlaylistRepository playlistRepository;
    private final DownloadRepository downloadRepository;

    private MutableLiveData<List<Media>> playlistSongLiveList = new MutableLiveData<>();

    private Playlist playlist;
    private boolean isOffline;

    public PlaylistPageViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Media>> getPlaylistSongLiveList(LifecycleOwner owner) {
        if (isOffline) {
            downloadRepository.getLiveDownloadFromPlaylist(playlist.getId()).observe(owner, downloads -> playlistSongLiveList.postValue(MappingUtil.mapDownloadToMedia(downloads)));
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
        this.playlist.setServer(PreferenceUtil.getInstance(App.getInstance()).getServerId());
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public LiveData<Boolean> isPinned(LifecycleOwner owner) {
        MutableLiveData<Boolean> isPinnedLive = new MutableLiveData<>();
        playlistRepository.getPinnedPlaylists(PreferenceUtil.getInstance(App.getInstance()).getServerId()).observe(owner, playlists -> isPinnedLive.postValue(playlists.contains(playlist)));
        return isPinnedLive;
    }

    public void setPinned(boolean isNowPinned) {
        if(isNowPinned) {
            playlistRepository.insert(playlist);
        } else {
            playlistRepository.delete(playlist);
        }
    }
}
