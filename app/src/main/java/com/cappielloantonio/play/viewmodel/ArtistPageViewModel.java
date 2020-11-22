package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class ArtistPageViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private AlbumRepository albumRepository;

    private LiveData<List<Song>> songList;
    private LiveData<List<Album>> albumList;

    public ArtistPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
    }

    public LiveData<List<Album>> getAlbumList(String artistID) {
        albumList = albumRepository.getArtistListLiveAlbums(artistID);
        return albumList;
    }

    public LiveData<List<Song>> getArtistTopSongList(String artistID) {
        songList = songRepository.getArtistListLiveTopSong(artistID);
        return songList;
    }
}
