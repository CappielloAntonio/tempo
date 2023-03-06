package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.ArtistInfo2;
import com.cappielloantonio.play.subsonic.models.Child;

import java.util.List;

public class ArtistPageViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private ArtistID3 artist;

    public ArtistPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<AlbumID3>> getAlbumList() {
        return albumRepository.getArtistAlbums(artist.getId());
    }

    public LiveData<ArtistInfo2> getArtistInfo(String id) {
        return artistRepository.getArtistFullInfo(id);
    }

    public LiveData<List<Child>> getArtistTopSongList(int count) {
        return artistRepository.getTopSongs(artist.getName(), count);
    }

    public ArtistID3 getArtist() {
        return artist;
    }

    public void setArtist(ArtistID3 artist) {
        this.artist = artist;
    }
}
