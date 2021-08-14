package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.SearchingRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private String query = "";

    private SearchingRepository searchingRepository;

    private LiveData<List<Song>> searchSong = new MutableLiveData<>(new ArrayList<>());
    private LiveData<List<Album>> searchAlbum = new MutableLiveData<>(new ArrayList<>());
    private LiveData<List<Artist>> searchArtist = new MutableLiveData<>(new ArrayList<>());

    public SearchViewModel(@NonNull Application application) {
        super(application);

        searchingRepository = new SearchingRepository(application);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;

        if (!query.isEmpty()) {
            insertNewSearch(query);
        }
    }

    public LiveData<List<Song>> searchSong(String title) {
        searchSong = searchingRepository.getSearchedSongs(title);
        return searchSong;
    }

    public LiveData<List<Album>> searchAlbum(String name) {
        searchAlbum = searchingRepository.getSearchedAlbums(name);
        return searchAlbum;
    }

    public LiveData<List<Artist>> searchArtist(String name) {
        searchArtist = searchingRepository.getSearchedArtists(name);
        return searchArtist;
    }

    public void insertNewSearch(String search) {
        searchingRepository.insert(new RecentSearch(search));
    }

    public void deleteRecentSearch(String search) {
        searchingRepository.delete(new RecentSearch(search));
    }

    public LiveData<List<String>> getSearchSuggestion(String query) {
        return searchingRepository.getSuggestions(query);
    }

    public List<String> getRecentSearchSuggestion() {
        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.addAll(searchingRepository.getRecentSearchSuggestion(5));

        return suggestions;
    }
}
