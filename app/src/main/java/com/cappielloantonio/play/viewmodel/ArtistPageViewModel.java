package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;

import java.util.List;

public class ArtistPageViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private Artist artist;

    public ArtistPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<Album>> getAlbumList() {
        return albumRepository.getArtistAlbums(artist.getId());
    }

    public LiveData<Artist> getArtistInfo(String id) {
        return artistRepository.getArtistFullInfo(id);
    }

    public LiveData<List<Media>> getArtistTopSongList(int count) {
        return artistRepository.getTopSongs(artist.getName(), count);
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
