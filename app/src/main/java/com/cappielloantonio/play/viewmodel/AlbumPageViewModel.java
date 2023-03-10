package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;

import java.util.List;

public class AlbumPageViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final DownloadRepository downloadRepository;

    private MutableLiveData<List<Child>> songLiveList = new MutableLiveData<>();

    private AlbumID3 album;
    private boolean isOffline;

    public AlbumPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository();
        artistRepository = new ArtistRepository();
        downloadRepository = new DownloadRepository();
    }

    public LiveData<List<Child>> getAlbumSongLiveList(LifecycleOwner owner) {
        if (isOffline) {
            // TODO
            //downloadRepository.getLiveDownloadFromAlbum(album.getId()).observe(owner, downloads -> songLiveList.postValue(MappingUtil.mapDownloadToMedia(downloads)));
        } else {
            songLiveList = albumRepository.getAlbumTracks(album.getId());
        }

        return songLiveList;
    }

    public AlbumID3 getAlbum() {
        return album;
    }

    public void setAlbum(AlbumID3 album) {
        this.album = album;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public LiveData<ArtistID3> getArtist() {
        return artistRepository.getArtistInfo(album.getArtistId());
    }
}
