package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class AlbumPageViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private ArtistRepository artistRepository;

    private LiveData<List<Song>> songLiveList;

    private Album album;

    public AlbumPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongLiveList() {
        songLiveList = songRepository.getAlbumListLiveSong(album.getId());
        return songLiveList;
    }

    public List<Song> getAlbumSongList() {
        return songRepository.getAlbumListSong(album.getId(), false);
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Artist getArtist() {
        return artistRepository.getArtistByID(album.getArtistId());
    }
}
