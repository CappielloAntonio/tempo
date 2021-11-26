package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.Collections;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;

    private final MutableLiveData<List<Song>> dicoverSongSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Song>> starredTracksSample = new MutableLiveData<>(null);

    private final MutableLiveData<List<Album>> mostPlayedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> recentlyPlayedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Integer>> years = new MutableLiveData<>(null);
    private final MutableLiveData<List<Song>> starredTracks = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> starredAlbums = new MutableLiveData<>(null);
    private final MutableLiveData<List<Artist>> starredArtists = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> recentlyAddedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Playlist>> pinnedPlaylists = new MutableLiveData<>(null);

    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        playlistRepository = new PlaylistRepository(application);

        songRepository.getRandomSample(10, null, null).observeForever(dicoverSongSample::postValue);
        songRepository.getStarredSongs(true, 10).observeForever(starredTracksSample::postValue);
    }

    public LiveData<List<Song>> getDiscoverSongSample() {
        return dicoverSongSample;
    }

    public LiveData<List<Song>> getStarredTracksSample() {
        return starredTracksSample;
    }

    public LiveData<List<Integer>> getYearList(LifecycleOwner owner) {
        albumRepository.getDecades().observe(owner, years::postValue);
        return years;
    }

    public LiveData<List<Song>> getStarredTracks(LifecycleOwner owner) {
        songRepository.getStarredSongs(false, -1).observe(owner, starredTracks::postValue);
        return starredTracks;
    }

    public LiveData<List<Album>> getStarredAlbums(LifecycleOwner owner) {
        albumRepository.getStarredAlbums().observe(owner, starredAlbums::postValue);
        return starredAlbums;
    }

    public LiveData<List<Artist>> getStarredArtists(LifecycleOwner owner) {
        artistRepository.getStarredArtists().observe(owner, starredArtists::postValue);
        return starredArtists;
    }

    public LiveData<List<Album>> getMostPlayedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("frequent", 20, null, null).observe(owner, mostPlayedAlbumSample::postValue);
        return mostPlayedAlbumSample;
    }

    public LiveData<List<Album>> getMostRecentlyAddedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("newest", 20, null, null).observe(owner, recentlyAddedAlbumSample::postValue);
        return recentlyAddedAlbumSample;
    }

    public LiveData<List<Album>> getRecentlyPlayedAlbumList(LifecycleOwner owner) {
        albumRepository.getAlbums("recent", 20, null, null).observe(owner, recentlyPlayedAlbumSample::postValue);
        return recentlyPlayedAlbumSample;
    }

    public LiveData<List<Playlist>> getPinnedPlaylistList(LifecycleOwner owner, int maxNumber, boolean random) {
        playlistRepository.getPinnedPlaylists().observe(owner, playlists -> {
            if (random) Collections.shuffle(playlists);
            List<Playlist> subPlaylist = playlists.subList(0, Math.min(maxNumber, playlists.size()));
            pinnedPlaylists.postValue(subPlaylist);
        });
        return pinnedPlaylists;
    }

    public LiveData<List<Song>> getPlaylistSongLiveList(String playlistId) {
        return playlistRepository.getPlaylistSongs(playlistId);
    }

    public void refreshDiscoverySongSample(LifecycleOwner owner) {
        songRepository.getRandomSample(10, null, null).observe(owner, dicoverSongSample::postValue);
    }

    public void refreshSimilarSongSample(LifecycleOwner owner) {
        songRepository.getStarredSongs(true, 10).observe(owner, starredTracksSample::postValue);
    }

    public void refreshStarredTracks(LifecycleOwner owner) {
        songRepository.getStarredSongs(false, -1).observe(owner, starredTracks::postValue);
    }

    public void refreshStarredAlbums(LifecycleOwner owner) {
        albumRepository.getStarredAlbums().observe(owner, starredAlbums::postValue);
    }

    public void refreshStarredArtists(LifecycleOwner owner) {
        artistRepository.getStarredArtists().observe(owner, starredArtists::postValue);
    }

    public void refreshMostPlayedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("frequent", 20, null, null).observe(owner, mostPlayedAlbumSample::postValue);
    }

    public void refreshMostRecentlyAddedAlbums(LifecycleOwner owner) {
        albumRepository.getAlbums("newest", 20, null, null).observe(owner, recentlyAddedAlbumSample::postValue);
    }

    public void refreshRecentlyPlayedAlbumList(LifecycleOwner owner) {
        albumRepository.getAlbums("recent", 20, null, null).observe(owner, recentlyPlayedAlbumSample::postValue);
    }
}
