package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.tempo.interfaces.StarCallback;
import com.cappielloantonio.tempo.repository.ArtistRepository;
import com.cappielloantonio.tempo.repository.FavoriteRepository;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.util.NetworkUtil;

import java.util.Date;

public class ArtistBottomSheetViewModel extends AndroidViewModel {
    private final ArtistRepository artistRepository;
    private final FavoriteRepository favoriteRepository;

    private ArtistID3 artist;

    public ArtistBottomSheetViewModel(@NonNull Application application) {
        super(application);

        artistRepository = new ArtistRepository();
        favoriteRepository = new FavoriteRepository();
    }

    public ArtistID3 getArtist() {
        return artist;
    }

    public void setArtist(ArtistID3 artist) {
        this.artist = artist;
    }

    public void setFavorite() {
        if (artist.getStarred() != null) {
            if (NetworkUtil.isOffline()) {
                removeFavoriteOffline();
            } else {
                removeFavoriteOnline();
            }
        } else {
            if (NetworkUtil.isOffline()) {
                setFavoriteOffline();
            } else {
                setFavoriteOnline();
            }
        }
    }

    private void removeFavoriteOffline() {
        favoriteRepository.starLater(null, null, artist.getId(), false);
        artist.setStarred(null);
    }

    private void removeFavoriteOnline() {
        favoriteRepository.unstar(null, null, artist.getId(), new StarCallback() {
            @Override
            public void onError() {
                // artist.setStarred(new Date());
                favoriteRepository.starLater(null, null, artist.getId(), false);
            }
        });

        artist.setStarred(null);
    }

    private void setFavoriteOffline() {
        favoriteRepository.starLater(null, null, artist.getId(), true);
        artist.setStarred(new Date());
    }

    private void setFavoriteOnline() {
        favoriteRepository.star(null, null, artist.getId(), new StarCallback() {
            @Override
            public void onError() {
                // artist.setStarred(null);
                favoriteRepository.starLater(null, null, artist.getId(), true);
            }
        });

        artist.setStarred(new Date());
    }
}
