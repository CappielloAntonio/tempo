package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";
    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private DownloadRepository downloadRepository;

    private MutableLiveData<List<Song>> dicoverSongSample;
    private MutableLiveData<List<Album>> mostPlayedAlbumSample;
    private MutableLiveData<List<Album>> recentlyAddedAlbumSample;
    private MutableLiveData<List<Album>> recentlyPlayedAlbumSample;
    private LiveData<List<Download>> downloadedSongSample;
    private LiveData<List<Integer>> years;

    private MutableLiveData<List<Song>> starredTracks;
    private MutableLiveData<List<Album>> starredAlbums;
    private MutableLiveData<List<Artist>> starredArtists;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        downloadRepository = new DownloadRepository(application);

        dicoverSongSample = songRepository.getRandomSample(10, null, null);
        mostPlayedAlbumSample = albumRepository.getAlbums("frequent", 20);
        recentlyAddedAlbumSample = albumRepository.getAlbums("newest", 20);
        recentlyPlayedAlbumSample = albumRepository.getAlbums("recent", 20);
        downloadedSongSample = downloadRepository.getLiveDownloadSample(10);
        years = albumRepository.getDecades();

        starredTracks = songRepository.getStarredSongs();
        starredAlbums = albumRepository.getStarredAlbums();
        starredArtists = artistRepository.getStarredArtists();
    }

    public SongRepository getSongRepository() {
        return songRepository;
    }

    public LiveData<List<Song>> getDiscoverSongSample() {
        return dicoverSongSample;
    }

    public LiveData<List<Integer>> getYearList() {
        return years;
    }

    public LiveData<List<Song>> getStarredTracks() {
        return starredTracks;
    }

    public LiveData<List<Album>> getStarredAlbums() {
        return starredAlbums;
    }

    public LiveData<List<Artist>> getStarredArtists() {
        return starredArtists;
    }

    public LiveData<List<Download>> getDownloaded() {
        return downloadedSongSample;
    }

    public LiveData<List<Album>> getMostPlayedAlbums() {
        return mostPlayedAlbumSample;
    }

    public LiveData<List<Album>> getMostRecentlyAddedAlbums() {
        return recentlyAddedAlbumSample;
    }

    public LiveData<List<Album>> getRecentlyPlayedAlbumList() {
        return recentlyPlayedAlbumSample;
    }

    public void refreshDiscoverySongSample(LifecycleOwner owner) {
        songRepository.getRandomSample(10, null, null).observe(owner, songs -> dicoverSongSample.postValue(songs));
    }

    public void refreshStarredTracks(LifecycleOwner owner) {
        songRepository.getStarredSongs().observe(owner, songs -> starredTracks.postValue(songs));
    }

    public void refreshStarredAlbums(LifecycleOwner owner) {
        albumRepository.getStarredAlbums().observe(owner, albums -> starredAlbums.postValue(albums));
    }

    public void refreshStarredArtists(LifecycleOwner owner) {
        artistRepository.getStarredArtists().observe(owner, artists -> starredArtists.postValue(artists));
    }

    public void refreshMostPlayedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("frequent", 20).observe(owner, albums -> mostPlayedAlbumSample.postValue(albums));
    }

    public void refreshMostRecentlyAddedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("newest", 20).observe(owner, albums -> recentlyAddedAlbumSample.postValue(albums));
    }

    public void refreshRecentlyPlayedAlbumList(LifecycleOwner owner) {
        albumRepository.getAlbums("recent", 20).observe(owner, albums -> recentlyPlayedAlbumSample.postValue(albums));
    }
}
