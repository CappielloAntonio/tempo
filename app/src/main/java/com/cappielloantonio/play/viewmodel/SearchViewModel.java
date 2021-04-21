package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

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
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private String query = "";

    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private GenreRepository genreRepository;
    private RecentSearchRepository recentSearchRepository;

    private LiveData<List<Song>> searchSong;
    private LiveData<List<Album>> searchAlbum;
    private LiveData<List<Artist>> searchArtist;
    private LiveData<List<Genre>> searchGenre;

    public SearchViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        genreRepository = new GenreRepository(application);
        recentSearchRepository = new RecentSearchRepository(application);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;

        if(!query.isEmpty()) {
            insertNewSearch(query);
        }
    }

    public LiveData<List<Song>> searchSong(String title, Context context) {
        searchSong = songRepository.searchListLiveSong(title, PreferenceUtil.getInstance(context).getSearchElementPerCategory());
        return searchSong;
    }

    public LiveData<List<Album>> searchAlbum(String name, Context context) {
        searchAlbum = albumRepository.searchListLiveAlbum(name, PreferenceUtil.getInstance(context).getSearchElementPerCategory());
        return searchAlbum;
    }

    public LiveData<List<Artist>> searchArtist(String name, Context context) {
        searchArtist = artistRepository.searchListLiveArtist(name, PreferenceUtil.getInstance(context).getSearchElementPerCategory());
        return searchArtist;
    }

    public LiveData<List<Genre>> searchGenre(String name, Context context) {
        searchGenre = genreRepository.searchListLiveGenre(name, PreferenceUtil.getInstance(context).getSearchElementPerCategory());
        return searchGenre;
    }

    public void insertNewSearch(String search) {
        recentSearchRepository.insert(new RecentSearch(search));
    }

    public void deleteRecentSearch(String search) {
        recentSearchRepository.delete(new RecentSearch(search));
    }

    public List<String> getSearchSuggestion(String query) {
        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.addAll(songRepository.getSearchSuggestion(query));
        suggestions.addAll(albumRepository.getSearchSuggestion(query));
        suggestions.addAll(artistRepository.getSearchSuggestion(query));
        suggestions.addAll(genreRepository.getSearchSuggestion(query));

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(suggestions);
        ArrayList<String> suggestionsWithoutDuplicates = new ArrayList<>(hashSet);

        return suggestionsWithoutDuplicates;
    }

    public List<String> getRecentSearchSuggestion() {
        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.addAll(recentSearchRepository.getRecentSearchSuggestion());

        return suggestions;
    }
}
