package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;

import java.util.Date;
import java.util.List;

public class AlbumBottomSheetViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private AlbumID3 album;

    public AlbumBottomSheetViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public AlbumID3 getAlbum() {
        return album;
    }

    public void setAlbum(AlbumID3 album) {
        this.album = album;
    }

    public LiveData<ArtistID3> getArtist() {
        return artistRepository.getArtist(album.getArtistId());
    }

    public MutableLiveData<List<Child>> getAlbumTracks() {
        return albumRepository.getAlbumTracks(album.getId());
    }

    public void setFavorite() {
        if (album.getStarred() != null) {
            artistRepository.unstar(album.getId());
            album.setStarred(null);
        } else {
            artistRepository.star(album.getId());
            album.setStarred(new Date());
        }
    }
}
