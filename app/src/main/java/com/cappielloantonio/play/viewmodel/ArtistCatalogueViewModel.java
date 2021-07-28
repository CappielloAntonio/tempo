package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.repository.ArtistRepository;

import java.util.List;

public class ArtistCatalogueViewModel extends AndroidViewModel {
    private ArtistRepository artistRepository;

    private LiveData<List<Artist>> artistList;

    public ArtistCatalogueViewModel(@NonNull Application application) {
        super(application);

        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<Artist>> getArtistList() {
        // artistList = artistRepository.getListLiveArtists();
        return artistList;
    }
}
