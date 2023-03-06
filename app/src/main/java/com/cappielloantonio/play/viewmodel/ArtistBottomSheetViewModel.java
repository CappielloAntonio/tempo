package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.subsonic.models.ArtistID3;

import java.util.Date;

public class ArtistBottomSheetViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;

    private ArtistID3 artist;

    public ArtistBottomSheetViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
    }

    public ArtistID3 getArtist() {
        return artist;
    }

    public void setArtist(ArtistID3 artist) {
        this.artist = artist;
    }

    public void setFavorite() {
        if (artist.getStarred() != null) {
            albumRepository.unstar(artist.getId());
            artist.setStarred(null);
        } else {
            albumRepository.star(artist.getId());
            artist.setStarred(new Date());
        }
    }
}
