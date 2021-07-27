package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.repository.AlbumRepository;

import java.util.List;

public class AlbumCatalogueViewModel extends AndroidViewModel {
    public LiveData<List<Album>> albumList;
    private AlbumRepository albumRepository;
    private String query = "";

    public AlbumCatalogueViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
    }

    public LiveData<List<Album>> getAlbumList() {
        // albumList = albumRepository.getListLiveAlbums();
        return albumList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
