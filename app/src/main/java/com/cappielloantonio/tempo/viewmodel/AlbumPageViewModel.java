package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.tempo.repository.AlbumRepository;
import com.cappielloantonio.tempo.repository.ArtistRepository;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.AlbumInfo;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.subsonic.models.Child;

import java.util.List;

public class AlbumPageViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private AlbumID3 album;

    public AlbumPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository();
        artistRepository = new ArtistRepository();
    }

    public LiveData<List<Child>> getAlbumSongLiveList() {
        return albumRepository.getAlbumTracks(album.getId());
    }

    public AlbumID3 getAlbum() {
        return album;
    }

    public void setAlbum(AlbumID3 album) {
        this.album = album;
    }

    public LiveData<ArtistID3> getArtist() {
        return artistRepository.getArtistInfo(album.getArtistId());
    }

    public LiveData<AlbumInfo> getAlbumInfo() {
        return albumRepository.getAlbumInfo(album.getId());
    }
}
