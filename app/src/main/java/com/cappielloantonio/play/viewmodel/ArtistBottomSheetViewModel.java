package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.model.Artist;

public class ArtistBottomSheetViewModel extends AndroidViewModel {
    private Artist artist;

    public ArtistBottomSheetViewModel(@NonNull Application application) {
        super(application);
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
