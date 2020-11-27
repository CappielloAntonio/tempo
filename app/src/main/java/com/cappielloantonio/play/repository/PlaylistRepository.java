package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.PlaylistDao;
import com.cappielloantonio.play.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRepository {
    private PlaylistDao playlistDao;
    private LiveData<List<Playlist>> listLivePlaylists;

    public PlaylistRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        playlistDao = database.playlistDao();
        listLivePlaylists = playlistDao.getAll();
    }

    public LiveData<List<Playlist>> getListLivePlaylists() {
        return listLivePlaylists;
    }

    public boolean exist(Playlist playlist) {
        boolean exist = false;

        ExistThreadSafe existThread = new ExistThreadSafe(playlistDao, playlist);
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

    public void insert(Playlist playlist) {
        InsertThreadSafe insert = new InsertThreadSafe(playlistDao, playlist);
        Thread thread = new Thread(insert);
        thread.start();
    }

    public void insertAll(ArrayList<Playlist> playlists) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(playlistDao, playlists);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public void delete(Playlist playlist) {
        DeleteThreadSafe delete = new DeleteThreadSafe(playlistDao, playlist);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public void deleteAll() {
        DeleteAllThreadSafe delete = new DeleteAllThreadSafe(playlistDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class ExistThreadSafe implements Runnable {
        private PlaylistDao playlistDao;
        private Playlist playlist;
        private boolean exist = false;

        public ExistThreadSafe(PlaylistDao playlistDao, Playlist playlist) {
            this.playlistDao = playlistDao;
            this.playlist = playlist;
        }

        @Override
        public void run() {
            exist = playlistDao.exist(playlist.getId());
        }

        public boolean exist() {
            return exist;
        }
    }

    private static class InsertThreadSafe implements Runnable {
        private PlaylistDao playlistDao;
        private Playlist playlist;

        public InsertThreadSafe(PlaylistDao playlistDao, Playlist playlist) {
            this.playlistDao = playlistDao;
            this.playlist = playlist;
        }

        @Override
        public void run() {
            playlistDao.insert(playlist);
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private PlaylistDao playlistDao;
        private ArrayList<Playlist> playlists;

        public InsertAllThreadSafe(PlaylistDao playlistDao, ArrayList<Playlist> playlists) {
            this.playlistDao = playlistDao;
            this.playlists = playlists;
        }

        @Override
        public void run() {
            playlistDao.deleteAll();
            playlistDao.insertAll(playlists);
        }
    }

    private static class DeleteThreadSafe implements Runnable {
        private PlaylistDao playlistDao;
        private Playlist playlist;

        public DeleteThreadSafe(PlaylistDao playlistDao, Playlist playlist) {
            this.playlistDao = playlistDao;
            this.playlist = playlist;
        }

        @Override
        public void run() {
            playlistDao.delete(playlist);
        }
    }

    private static class DeleteAllThreadSafe implements Runnable {
        private PlaylistDao playlistDao;

        public DeleteAllThreadSafe(PlaylistDao playlistDao) {
            this.playlistDao = playlistDao;
        }

        @Override
        public void run() {
            playlistDao.deleteAll();
        }
    }
}
