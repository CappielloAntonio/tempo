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

import java.util.ArrayList;
import java.util.List;

public class LibraryViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private GenreRepository genreRepository;

    private LiveData<List<Album>> sampleAlbum;
    private LiveData<List<Artist>> sampleArtist;
    private LiveData<List<Genre>> sampleGenres;

    private LiveData<List<Genre>> allGenres;

    public LibraryViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        genreRepository = new GenreRepository(application);

        // Inizializzate all'interno del costruttore, in modo da rimanere immutabili per tutto il
        // ciclo di vita dell'applicazione
        sampleAlbum = albumRepository.getListLiveSampleAlbum();
        sampleArtist = artistRepository.getListLiveSampleArtist();
        sampleGenres = genreRepository.getListLiveSampleGenre();
    }

    public LiveData<List<Genre>> getGenreList() {
        allGenres = genreRepository.getListLiveGenres();
        return allGenres;
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

    public ArrayList<Playlist> getPlaylist() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        playlists.add(new Playlist("1", "sdad", "", ""));
        playlists.add(new Playlist("2", "rwef", "", ""));
        playlists.add(new Playlist("3", "khjf", "", ""));
        playlists.add(new Playlist("4", "thfd", "", ""));
        playlists.add(new Playlist("5", "jhku", "", ""));
        playlists.add(new Playlist("6", "tuid", "", ""));
        playlists.add(new Playlist("7", "hfrt", "", ""));
        playlists.add(new Playlist("8", "qedg", "", ""));
        playlists.add(new Playlist("9", "tugh", "", ""));

        return playlists;
    }
}
