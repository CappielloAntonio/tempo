package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

public class LibraryViewModel extends AndroidViewModel {
    private GenreRepository genreRepository;
    private LiveData<List<Genre>> allGenres;

    public LibraryViewModel(@NonNull Application application) {
        super(application);

        genreRepository = new GenreRepository(application);
        allGenres = genreRepository.getListLiveGenres();
    }

    public LiveData<List<Genre>> getGenreList() {
        return allGenres;
    }

    public ArrayList<Album> getAlbumSample() {
        ArrayList<Album> albums = new ArrayList<>();
        albums.add(new Album("1", "aaaa", 1, "1", "qqqq", "", ""));
        albums.add(new Album("2", "ssss", 1, "2", "wwww", "", ""));
        albums.add(new Album("3", "dddd", 1, "3", "eeee", "", ""));
        albums.add(new Album("4", "ffff", 1, "4", "rrrr", "", ""));
        albums.add(new Album("5", "gggg", 1, "5", "tttt", "", ""));
        albums.add(new Album("6", "hhhh", 1, "6", "yyyy", "", ""));
        albums.add(new Album("7", "jjjj", 1, "7", "uuuu", "", ""));
        albums.add(new Album("8", "kkkk", 1, "8", "iiii", "", ""));
        albums.add(new Album("9", "llll", 1, "9", "oooo", "", ""));

        return albums;
    }

    public ArrayList<Artist> getArtistSample() {
        ArrayList<Artist> artists = new ArrayList<>();
        artists.add(new Artist("1", "dhgr", "", ""));
        artists.add(new Artist("2", "kdnu", "", ""));
        artists.add(new Artist("3", "wfty", "", ""));
        artists.add(new Artist("4", "hfds", "", ""));
        artists.add(new Artist("5", "jgab", "", ""));
        artists.add(new Artist("6", "iudg", "", ""));
        artists.add(new Artist("7", "istr", "", ""));
        artists.add(new Artist("8", "dger", "", ""));
        artists.add(new Artist("9", "jhjk", "", ""));

        return artists;
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
