package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.repository.AlbumRepository;

public class ArtistBottomSheetViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;

    private Artist artist;

    public ArtistBottomSheetViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setFavorite() {
        if (artist.isFavorite()) {
            albumRepository.unstar(artist.getId());
            artist.setFavorite(false);
        } else {
            albumRepository.star(artist.getId());
            artist.setFavorite(true);
        }
    }
}
