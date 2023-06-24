package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.tempo.model.RecentSearch;
import com.cappielloantonio.tempo.repository.SearchingRepository;
import com.cappielloantonio.tempo.subsonic.models.SearchResult3;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private String query = "";

    private final SearchingRepository searchingRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);

        searchingRepository = new SearchingRepository();
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

    public LiveData<SearchResult3> search(String title) {
        return searchingRepository.search(title);
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
        suggestions.addAll(searchingRepository.getRecentSearchSuggestion());

        return suggestions;
    }
}
