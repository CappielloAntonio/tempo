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

public class AlbumPageViewModel extends AndroidViewModel {
    private SongRepository songRepository;

    private LiveData<List<Song>> songList;

    private Album album;

    public AlbumPageViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongList() {
        songList = songRepository.getAlbumListLiveSong(album.getId());
        return songList;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
