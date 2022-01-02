package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.List;

public class AlbumPageViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final DownloadRepository downloadRepository;

    private MutableLiveData<List<Song>> songLiveList = new MutableLiveData<>();

    private Album album;
    private boolean isOffline;

    public AlbumPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongLiveList(LifecycleOwner owner) {
        if (isOffline) {
            downloadRepository.getLiveDownloadFromAlbum(album.getId()).observe(owner, downloads -> songLiveList.postValue(MappingUtil.mapDownloadToSong(downloads)));
        } else {
            songLiveList = albumRepository.getAlbumTracks(album.getId());
        }

        return songLiveList;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public LiveData<Artist> getArtist() {
        return artistRepository.getArtistInfo(album.getArtistId());
    }
}
