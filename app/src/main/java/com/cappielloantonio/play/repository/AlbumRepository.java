package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.AlbumDao;
import com.cappielloantonio.play.model.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumRepository {
    private static final String TAG = "AlbumRepository";

    private AlbumDao albumDao;
    private LiveData<List<Album>> listLiveAlbums;
    private LiveData<List<Album>> artistListLiveAlbums;
    private LiveData<List<Album>> listLiveSampleAlbum;
    private LiveData<List<Album>> searchListLiveAlbum;


    public AlbumRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        albumDao = database.albumDao();
    }

    public LiveData<List<Album>> getListLiveAlbums() {
        listLiveAlbums = albumDao.getAll();
        return listLiveAlbums;
    }

    public LiveData<List<Album>> getArtistListLiveAlbums(String artistId) {
        artistListLiveAlbums = albumDao.getArtistAlbums(artistId);
        return artistListLiveAlbums;
    }

    public LiveData<List<Album>> getListLiveSampleAlbum() {
        listLiveSampleAlbum = albumDao.getSample(10);
        return listLiveSampleAlbum;
    }

    public LiveData<List<Album>> searchListLiveAlbum(String name) {
        searchListLiveAlbum = albumDao.searchAlbum(name);
        return searchListLiveAlbum;
    }

    public List<String> getSearchSuggestion(String query) {
        List<String> suggestions = new ArrayList<>();

        SearchSuggestionsThreadSafe suggestionsThread = new SearchSuggestionsThreadSafe(albumDao, query, 5);
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
        private AlbumDao albumDao;
        private String query;
        private int number;
        private List<String> suggestions = new ArrayList<>();

        public SearchSuggestionsThreadSafe(AlbumDao albumDao, String query, int number) {
            this.albumDao = albumDao;
            this.query = query;
            this.number = number;
        }

        @Override
        public void run() {
            suggestions = albumDao.searchSuggestions(query, number);
        }

        public List<String> getSuggestions() {
            return suggestions;
        }
    }

    public void insertAll(ArrayList<Album> albums) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(albumDao, albums);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public void deleteAll() {
        DeleteAllThreadSafe delete = new DeleteAllThreadSafe(albumDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class DeleteAllThreadSafe implements Runnable {
        private AlbumDao albumDao;

        public DeleteAllThreadSafe(AlbumDao albumDao) {
            this.albumDao = albumDao;
        }

        @Override
        public void run() {
            albumDao.deleteAll();
        }
    }

    public Album getAlbumByID(String id) {
        Album album = null;

        GetAlbumByIDThreadSafe getAlbum = new GetAlbumByIDThreadSafe(albumDao, id);
        Thread thread = new Thread(getAlbum);
        thread.start();

        try {
            thread.join();
            album = getAlbum.getAlbum();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return album;
    }

    private static class GetAlbumByIDThreadSafe implements Runnable {
        private Album album;
        private AlbumDao albumDao;
        private String id;

        public GetAlbumByIDThreadSafe(AlbumDao albumDao, String id) {
            this.albumDao = albumDao;
            this.id = id;
        }

        @Override
        public void run() {
            album = albumDao.getAlbumByID(id);
        }

        public Album getAlbum() {
            return album;
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private AlbumDao albumDao;
        private ArrayList<Album> albums;

        public InsertAllThreadSafe(AlbumDao albumDao, ArrayList<Album> albums) {
            this.albumDao = albumDao;
            this.albums = albums;
        }

        @Override
        public void run() {
            albumDao.deleteAll();
            albumDao.insertAll(albums);
        }
    }
}
