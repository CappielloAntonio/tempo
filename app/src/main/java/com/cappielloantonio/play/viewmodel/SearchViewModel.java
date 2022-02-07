package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.SearchingRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private String query = "";

    private final SearchingRepository searchingRepository;

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

    public LiveData<List<Media>> searchSong(String title) {
        return searchingRepository.getSearchedSongs(title);
    }

    public LiveData<List<Album>> searchAlbum(String name) {
        return searchingRepository.getSearchedAlbums(name);
    }

    public LiveData<List<Artist>> searchArtist(String name) {
        return searchingRepository.getSearchedArtists(name);
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
