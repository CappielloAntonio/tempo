package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class ArtistPageViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private AlbumRepository albumRepository;

    private LiveData<List<Song>> songList;
    private LiveData<List<Album>> albumList;

    private Artist artist;

    public ArtistPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
    }

    public LiveData<List<Album>> getAlbumList() {
        albumList = albumRepository.getArtistListLiveAlbums(artist.id);
        return albumList;
    }

    public LiveData<List<Song>> getArtistTopSongList() {
        songList = songRepository.getArtistListLiveTopSongSample(artist.id);
        return songList;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
