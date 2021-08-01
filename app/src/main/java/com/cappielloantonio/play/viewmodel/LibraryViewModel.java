package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;

import java.util.List;

public class LibraryViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private GenreRepository genreRepository;
    private PlaylistRepository playlistRepository;

    private LiveData<List<Playlist>> playlistSample;
    private LiveData<List<Album>> sampleAlbum;
    private LiveData<List<Artist>> sampleArtist;
    private LiveData<List<Genre>> sampleGenres;

    public LibraryViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        genreRepository = new GenreRepository(application);
        playlistRepository = new PlaylistRepository(application);

        // Inizializzate all'interno del costruttore, in modo da rimanere immutabili per tutto il
        // ciclo di vita dell'applicazione
        sampleAlbum = albumRepository.getAlbums("random", 20);
        sampleArtist = artistRepository.getArtists(true, 20);
        sampleGenres = genreRepository.getGenres(true, 15);
        playlistSample = playlistRepository.getPlaylists(true, 10);
    }

    public LiveData<List<Playlist>> getPlaylistSample() {
        return playlistSample;
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
}
