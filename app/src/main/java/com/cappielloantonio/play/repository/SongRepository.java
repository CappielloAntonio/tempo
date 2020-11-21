package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.SongDao;
import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongRepository {
    private SongDao songDao;
    private LiveData<List<Song>> listLiveSongs;
    private LiveData<List<Song>> searchListLiveSongs;

    public SongRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        songDao = database.songDao();
    }

    public LiveData<List<Song>> getListLiveSongs() {
        listLiveSongs = songDao.getAll();
        return listLiveSongs;
    }

    public LiveData<List<Song>> searchListLiveSongs(String title) {
        searchListLiveSongs = songDao.searchSong(title);
        return searchListLiveSongs;
    }

    public boolean exist(Song song) {
        boolean exist = false;

        ExistThreadSafe existThread = new ExistThreadSafe(songDao, song);
        Thread thread = new Thread(existThread);
        thread.start();

        try {
            thread.join();
            exist = existThread.exist();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exist;
    }

    public void insert(Song song) {
        InsertThreadSafe insert = new InsertThreadSafe(songDao, song);
        Thread thread = new Thread(insert);
        thread.start();
    }

    public void insertAll(ArrayList<Song> songs) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(songDao, songs);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public void delete(Song song) {
        DeleteThreadSafe delete = new DeleteThreadSafe(songDao, song);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class ExistThreadSafe implements Runnable {
        private SongDao songDao;
        private Song song;
        private boolean exist = false;

        public ExistThreadSafe(SongDao songDao, Song song) {
            this.songDao = songDao;
            this.song = song;
        }

        @Override
        public void run() {
            exist = songDao.exist(song.getId());
        }

        public boolean exist() {
            return exist;
        }
    }

    private static class InsertThreadSafe implements Runnable {
        private SongDao songDao;
        private Song song;

        public InsertThreadSafe(SongDao songDao, Song song) {
            this.songDao = songDao;
            this.song = song;
        }

        @Override
        public void run() {
            songDao.insert(song);
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private SongDao songDao;
        private ArrayList<Song> songs;

        public InsertAllThreadSafe(SongDao songDao, ArrayList<Song> songs) {
            this.songDao = songDao;
            this.songs = songs;
        }

        @Override
        public void run() {
            songDao.insertAll(songs);
        }
    }

    private static class DeleteThreadSafe implements Runnable {
        private SongDao songDao;
        private Song song;

        public DeleteThreadSafe(SongDao songDao, Song song) {
            this.songDao = songDao;
            this.song = song;
        }

        @Override
        public void run() {
            songDao.delete(song);
        }
    }
}
