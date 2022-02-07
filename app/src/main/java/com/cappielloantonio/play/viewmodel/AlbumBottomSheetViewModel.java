package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;

import java.util.List;

public class AlbumBottomSheetViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private Album album;

    public AlbumBottomSheetViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public LiveData<Artist> getArtist() {
        return artistRepository.getArtist(album.getArtistId());
    }

    public MutableLiveData<List<Media>> getAlbumTracks() {
        return albumRepository.getAlbumTracks(album.getId());
    }

    public void setFavorite() {
        if (album.isFavorite()) {
            artistRepository.unstar(album.getId());
            album.setFavorite(false);
        } else {
            artistRepository.star(album.getId());
            album.setFavorite(true);
        }
    }
}
