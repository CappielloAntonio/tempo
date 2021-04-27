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

    public void deleteAll() {
        DeleteAllThreadSafe delete = new DeleteAllThreadSafe(playlistDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public List<Playlist> getRandomSample(int number) {
        List<Playlist> sample = new ArrayList<>();

        PickRandomThreadSafe randomThread = new PickRandomThreadSafe(playlistDao, number);
        Thread thread = new Thread(randomThread);
        thread.start();

        try {
            thread.join();
            sample = randomThread.getSample();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return sample;
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

    private static class PickRandomThreadSafe implements Runnable {
        private PlaylistDao playlistDao;
        private int elementNumber;
        private List<Playlist> sample;

        public PickRandomThreadSafe(PlaylistDao playlistDao, int number) {
            this.playlistDao = playlistDao;
            this.elementNumber = number;
        }

        @Override
        public void run() {
            sample = playlistDao.random(elementNumber);
        }

        public List<Playlist> getSample() {
            return sample;
        }
    }
}
