package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.List;

public class PlaylistPageViewModel extends AndroidViewModel {
    private final PlaylistRepository playlistRepository;
    private final DownloadRepository downloadRepository;

    private MutableLiveData<List<Song>> playlistSongLiveList = new MutableLiveData<>();

    private Playlist playlist;
    private boolean isOffline;

    public PlaylistPageViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Song>> getPlaylistSongLiveList(FragmentActivity activity) {
        if (isOffline) {
            downloadRepository.getLiveDownloadFromPlaylist(playlist.getId()).observe(activity, downloads -> playlistSongLiveList.postValue(MappingUtil.mapDownloadToSong(downloads)));
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
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public LiveData<Boolean> isPinned(FragmentActivity activity) {
        MutableLiveData<Boolean> isPinnedLive = new MutableLiveData<>();
        playlistRepository.getPinnedPlaylists().observe(activity, playlists -> isPinnedLive.postValue(playlists.contains(playlist)));
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
