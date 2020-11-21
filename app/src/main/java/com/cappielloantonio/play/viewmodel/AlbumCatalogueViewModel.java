package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;

import java.util.List;

public class AlbumCatalogueViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;

    private LiveData<List<Album>> albumList;

    public AlbumCatalogueViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);

        albumList = albumRepository.getListLiveAlbums();
    }

    public LiveData<List<Album>> getAlbumList() {
        return albumList;
    }
}
