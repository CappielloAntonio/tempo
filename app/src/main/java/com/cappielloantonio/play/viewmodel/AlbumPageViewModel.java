package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class AlbumPageViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;

    private LiveData<List<Song>> songLiveList = new MutableLiveData<>();
    private LiveData<Album> albumInfo = new MutableLiveData<>();

    private Album album;

    public AlbumPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongLiveList() {
        songLiveList = albumRepository.getAlbumTracks(album.getId());
        return songLiveList;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public LiveData<Artist> getArtist() {
        return artistRepository.getArtistInfo(album.getArtistId());
    }
}
