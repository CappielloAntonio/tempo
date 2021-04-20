package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.ArtistDao;
import com.cappielloantonio.play.database.dao.GenreDao;
import com.cappielloantonio.play.database.dao.SongGenreCrossDao;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreRepository {
    private GenreDao genreDao;
    private SongGenreCrossDao songGenreCrossDao;
    private LiveData<List<Genre>> listLiveGenres;
    private LiveData<List<Genre>> listLiveAlbumGenre;
    private LiveData<List<Genre>> searchListLiveGenre;

    public GenreRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        genreDao = database.genreDao();
        songGenreCrossDao = database.songGenreCrossDao();
    }

    public LiveData<List<Genre>> getListLiveGenres() {
        listLiveGenres = genreDao.getAll();
        return listLiveGenres;
    }

    public LiveData<List<Genre>> getListLiveSampleGenre() {
        listLiveAlbumGenre = genreDao.getSample(6 * 3);
        return listLiveAlbumGenre;
    }

    public List<Genre> getListGenre() {
        List<Genre> list = null;

        GetGenreListThreadSafe getGenreListThread = new GetGenreListThreadSafe(genreDao);
        Thread thread = new Thread(getGenreListThread);
        thread.start();

        try {
            thread.join();
            list = getGenreListThread.getList();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static class GetGenreListThreadSafe implements Runnable {
        private GenreDao genreDao;
        private List<Genre> list = null;

        public GetGenreListThreadSafe(GenreDao genreDao) {
            this.genreDao = genreDao;
        }

        @Override
        public void run() {
            list = genreDao.getGenreList();
        }

        public List<Genre> getList() {
            return list;
        }
    }

    public void insertAll(ArrayList<Genre> genres) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(genreDao, genres);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    private static class InsertAllThreadSafe implements Runnable {
        private GenreDao genreDao;
        private ArrayList<Genre> genres;

        public InsertAllThreadSafe(GenreDao genreDao, ArrayList<Genre> genres) {
            this.genreDao = genreDao;
            this.genres = genres;
        }

        @Override
        public void run() {
            genreDao.deleteAll();
            genreDao.insertAll(genres);
        }
    }

    public void deleteAll() {
        DeleteAllGenreThreadSafe delete = new DeleteAllGenreThreadSafe(genreDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class DeleteAllGenreThreadSafe implements Runnable {
        private GenreDao genreDao;

        public DeleteAllGenreThreadSafe(GenreDao genreDao) {
            this.genreDao = genreDao;
        }

        @Override
        public void run() {
            genreDao.deleteAll();
        }
    }

    public LiveData<List<Genre>> searchListLiveGenre(String name) {
        searchListLiveGenre = genreDao.searchGenre(name);
        return searchListLiveGenre;
    }

    public List<String> getSearchSuggestion(String query) {
        List<String> suggestions = new ArrayList<>();

        SearchSuggestionsThreadSafe suggestionsThread = new SearchSuggestionsThreadSafe(genreDao, query, 5);
        Thread thread = new Thread(suggestionsThread);
        thread.start();

        try {
            thread.join();
            suggestions = suggestionsThread.getSuggestions();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return suggestions;
    }

    private static class SearchSuggestionsThreadSafe implements Runnable {
        private GenreDao genreDao;
        private String query;
        private int number;
        private List<String> suggestions = new ArrayList<>();

        public SearchSuggestionsThreadSafe(GenreDao genreDao, String query, int number) {
            this.genreDao = genreDao;
            this.query = query;
            this.number = number;
        }

        @Override
        public void run() {
            suggestions = genreDao.searchSuggestions(query, number);
        }

        public List<String> getSuggestions() {
            return suggestions;
        }
    }
}
