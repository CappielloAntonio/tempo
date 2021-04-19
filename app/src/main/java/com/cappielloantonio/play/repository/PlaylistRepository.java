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

    public void insertAll(ArrayList<Playlist> playlists) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(playlistDao, playlists);
        Thread thread = new Thread(insertAll);
        thread.start();
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

    public void deleteAll() {
        DeleteAllThreadSafe delete = new DeleteAllThreadSafe(playlistDao);
        Thread thread = new Thread(delete);
        thread.start();
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
