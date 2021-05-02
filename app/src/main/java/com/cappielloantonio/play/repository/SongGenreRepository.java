package com.cappielloantonio.play.repository;

import android.app.Application;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.SongArtistCrossDao;
import com.cappielloantonio.play.database.dao.SongGenreCrossDao;
import com.cappielloantonio.play.model.SongArtistCross;
import com.cappielloantonio.play.model.SongGenreCross;

import java.util.List;

public class SongGenreRepository {
    private static final String TAG = "AlbumArtistRepository";

    private SongGenreCrossDao songGenreCrossDao;

    public SongGenreRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        songGenreCrossDao = database.songGenreCrossDao();
    }

    public void insertAll(List<SongGenreCross> crosses) {
        InsertAllThreadSafe insert = new InsertAllThreadSafe(songGenreCrossDao, crosses);
        Thread thread = new Thread(insert);
        thread.start();
    }

    public void deleteAll() {
        DeleteAllSongGenreCrossThreadSafe delete = new DeleteAllSongGenreCrossThreadSafe(songGenreCrossDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class InsertAllThreadSafe implements Runnable {
        private SongGenreCrossDao songGenreCrossDao;
        private List<SongGenreCross> crosses;

        public InsertAllThreadSafe(SongGenreCrossDao songGenreCrossDao, List<SongGenreCross> crosses) {
            this.songGenreCrossDao = songGenreCrossDao;
            this.crosses = crosses;
        }

        @Override
        public void run() {
            songGenreCrossDao.insertAll(crosses);
        }
    }

    private static class DeleteAllSongGenreCrossThreadSafe implements Runnable {
        private SongGenreCrossDao songGenreCrossDao;

        public DeleteAllSongGenreCrossThreadSafe(SongGenreCrossDao songGenreCrossDao) {
            this.songGenreCrossDao = songGenreCrossDao;
        }

        @Override
        public void run() {
            songGenreCrossDao.deleteAll();
        }
    }
}
