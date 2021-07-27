package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";
    private SongRepository songRepository;
    private AlbumRepository albumRepository;

    private List<Song> dicoverSongSample = new ArrayList<>();
    private LiveData<List<Song>> recentlyPlayedSongSample;
    private LiveData<List<Song>> recentlyAddedSongSample;
    private LiveData<List<Song>> mostPlayedSongSample;
    private LiveData<List<Song>> favoritesSongSample;
    private LiveData<List<Song>> downloadedSongSample;
    private List<Integer> years;

    private LiveData<List<Album>> mostPlayedAlbumSample;
    private LiveData<List<Album>> recentlyAddedAlbumSample;
    private LiveData<List<Album>> recentlyPlayedAlbumSample;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);

        recentlyPlayedSongSample = songRepository.getListLiveRecentlyPlayedSampleSong(20);
        recentlyAddedSongSample = songRepository.getListLiveRecentlyAddedSampleSong(20);
        mostPlayedSongSample = songRepository.getListLiveMostPlayedSampleSong(20);
        favoritesSongSample = songRepository.getListLiveFavoritesSampleSong(20);
        downloadedSongSample = songRepository.getListLiveDownloadedSampleSong(20);
        years = songRepository.getYearList();

        mostPlayedAlbumSample = albumRepository.getListLiveAlbums("frequent", 20);
        recentlyAddedAlbumSample = albumRepository.getListLiveAlbums("newest", 20);
        recentlyPlayedAlbumSample = albumRepository.getListLiveAlbums("recent", 20);
    }

    public SongRepository getSongRepository() {
        return songRepository;
    }

    public LiveData<List<Song>> getRecentlyAddedSongList() {
        return recentlyAddedSongSample;
    }

    public LiveData<List<Song>> getRecentlyPlayedSongList() {
        return recentlyPlayedSongSample;
    }

    public LiveData<List<Song>> getMostPlayedSongList() {
        return mostPlayedSongSample;
    }

    public List<Integer> getYearList() {
        return years;
    }

    public LiveData<List<Song>> getFavorites() {
        return favoritesSongSample;
    }

    public LiveData<List<Song>> getDownloaded() {
        return downloadedSongSample;
    }

    public LiveData<List<Album>> getMostPlayedAlbums()  {
        return mostPlayedAlbumSample;
    }

    public LiveData<List<Album>> getMostRecentlyAddedAlbums()  {
        return recentlyAddedAlbumSample;
    }

    public LiveData<List<Album>> getRecentlyPlayedAlbumList()  {
        return recentlyPlayedAlbumSample;
    }
}
