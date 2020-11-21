package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.GenreDao;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreRepository {
    private GenreDao genreDao;
    private LiveData<List<Genre>> listLiveGenres;
    private LiveData<List<Genre>> listLiveAlbumGenre;

    public GenreRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        genreDao = database.genreDao();
    }

    public LiveData<List<Genre>> getListLiveGenres() {
        listLiveGenres = genreDao.getAll();
        return listLiveGenres;
    }

    public LiveData<List<Genre>> getListLiveSampleGenre() {
        listLiveAlbumGenre = genreDao.getSample(6 * 3);
        return listLiveAlbumGenre;
    }

    public boolean exist(Genre genre) {
        boolean exist = false;

        ExistThreadSafe existThread = new ExistThreadSafe(genreDao, genre);
        Thread thread = new Thread(existThread);
        thread.start();

        try {
            thread.join();
            exist = existThread.exist();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exist;
    }

    public void insert(Genre genre) {
        InsertThreadSafe insert = new InsertThreadSafe(genreDao, genre);
        Thread thread = new Thread(insert);
        thread.start();
    }

    public void insertAll(ArrayList<Genre> genres) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(genreDao, genres);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public void delete(Genre genre) {
        DeleteThreadSafe delete = new DeleteThreadSafe(genreDao, genre);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class ExistThreadSafe implements Runnable {
        private GenreDao genreDao;
        private Genre genre;
        private boolean exist = false;

        public ExistThreadSafe(GenreDao genreDao, Genre genre) {
            this.genreDao = genreDao;
            this.genre = genre;
        }

        @Override
        public void run() {
            exist = genreDao.exist(genre.getId());
        }

        public boolean exist() {
            return exist;
        }
    }

    private static class InsertThreadSafe implements Runnable {
        private GenreDao genreDao;
        private Genre genre;

        public InsertThreadSafe(GenreDao genreDao, Genre genre) {
            this.genreDao = genreDao;
            this.genre = genre;
        }

        @Override
        public void run() {
            genreDao.insert(genre);
        }
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
            genreDao.insertAll(genres);
        }
    }

    private static class DeleteThreadSafe implements Runnable {
        private GenreDao genreDao;
        private Genre genre;

        public DeleteThreadSafe(GenreDao genreDao, Genre genre) {
            this.genreDao = genreDao;
            this.genre = genre;
        }

        @Override
        public void run() {
            genreDao.delete(genre);
        }
    }
}
