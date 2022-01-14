package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class LibraryViewModel extends AndroidViewModel {
    private static final String TAG = "LibraryViewModel";

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final PlaylistRepository playlistRepository;

    private final MutableLiveData<List<Playlist>> playlistSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> sampleAlbum = new MutableLiveData<>(null);
    private final MutableLiveData<List<Artist>> sampleArtist = new MutableLiveData<>(null);
    private final MutableLiveData<List<Genre>> sampleGenres = new MutableLiveData<>(null);

    public LibraryViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        genreRepository = new GenreRepository(application);
        playlistRepository = new PlaylistRepository(application);

        // Inizializzate all'interno del costruttore, in modo da rimanere immutabili per tutto il
        // ciclo di vita dell'applicazione
        albumRepository.getAlbums("random", 20, null, null).observeForever(sampleAlbum::postValue);
        artistRepository.getArtists(true, 20).observeForever(sampleArtist::postValue);
        genreRepository.getGenres(true, 15).observeForever(sampleGenres::postValue);
        playlistRepository.getPlaylists(true, 10).observeForever(playlistSample::postValue);
    }

    public LiveData<List<Album>> getAlbumSample() {
        return sampleAlbum;
    }

    public LiveData<List<Artist>> getArtistSample() {
        return sampleArtist;
    }

    public LiveData<List<Genre>> getGenreSample() {
        return sampleGenres;
    }

    public LiveData<List<Playlist>> getPlaylistSample() {
        return playlistSample;
    }

    public void refreshAlbumSample(LifecycleOwner owner) {
        albumRepository.getAlbums("random", 20, null, null).observe(owner, sampleAlbum::postValue);
    }

    public void refreshArtistSample(LifecycleOwner owner) {
        artistRepository.getArtists(true, 20).observe(owner, sampleArtist::postValue);
    }

    public void refreshGenreSample(LifecycleOwner owner) {
        genreRepository.getGenres(true, 15).observe(owner, sampleGenres::postValue);
    }

    public void refreshPlaylistSample(LifecycleOwner owner) {
        playlistRepository.getPlaylists(true, 10).observe(owner, playlistSample::postValue);
    }
}
