package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.repository.ArtistRepository;

public class AlbumBottomSheetViewModel extends AndroidViewModel {
    private ArtistRepository artistRepository;

    private Album album;

    public AlbumBottomSheetViewModel(@NonNull Application application) {
        super(application);

        artistRepository = new ArtistRepository(application);
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist() {
        // return artistRepository.getArtistByID(album.getArtistId());
        return null;
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
