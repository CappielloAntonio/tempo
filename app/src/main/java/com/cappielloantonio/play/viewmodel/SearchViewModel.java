package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.RecentSearchRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private RecentSearchRepository recentSearchRepository;

    private LiveData<List<Song>> searchSong;
    private LiveData<List<Album>> searchAlbum;
    private LiveData<List<Artist>> searchArtist;
    private LiveData<List<RecentSearch>> recentSearches;

    public SearchViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        recentSearchRepository = new RecentSearchRepository(application);
    }

    public LiveData<List<Song>> searchSong(String title) {
        searchSong = songRepository.searchListLiveSong(title);
        return searchSong;
    }

    public LiveData<List<Album>> searchAlbum(String name) {
        searchAlbum = albumRepository.searchListLiveAlbum(name);
        return searchAlbum;
    }

    public LiveData<List<Artist>> searchArtist(String name) {
        searchArtist = artistRepository.searchListLiveArtist(name);
        return searchArtist;
    }

    public LiveData<List<RecentSearch>> getSearchList() {
        recentSearches = recentSearchRepository.getListLiveRecentSearches();
        return recentSearches;
    }

    public void insertNewSearch(String search) {
        recentSearchRepository.insert(new RecentSearch(search));
    }

    public void deleteAllRecentSearch() {
        recentSearchRepository.deleteAll();
    }
}
