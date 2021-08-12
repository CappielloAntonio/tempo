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

import java.util.ArrayList;
import java.util.List;

public class ArtistPageViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;

    private List<Song> randomList = new ArrayList<>();
    private LiveData<List<Song>> songList = new MutableLiveData<>();
    private LiveData<List<Album>> albumList = new MutableLiveData<>();
    private LiveData<List<Artist>> artistList = new MutableLiveData<>();
    private LiveData<Artist> artistInfo = new MutableLiveData<>();

    private Artist artist;

    public ArtistPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<Album>> getAlbumList() {
        albumList = albumRepository.getArtistAlbums(artist.getId());
        return albumList;
    }

    public LiveData<Artist> getArtistInfo(String id) {
        artistInfo = artistRepository.getArtistFullInfo(id);
        return artistInfo;
    }

    public List<Song> getArtistRandomSongList() {
        // randomList = songRepository.getArtistListLiveRandomSong(artist.id);
        return randomList;
    }

    public LiveData<List<Song>> getArtistTopSongList(int count) {
        songList = artistRepository.getTopSongs(artist.getName(), count);
        return songList;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
