package com.cappielloantonio.play.repository;

import android.app.Application;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.AlbumArtistCrossDao;
import com.cappielloantonio.play.database.dao.SongArtistCrossDao;
import com.cappielloantonio.play.database.dao.SongGenreCrossDao;
import com.cappielloantonio.play.model.AlbumArtistCross;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongArtistCross;

import java.util.List;

public class SongArtistRepository {
    private static final String TAG = "AlbumArtistRepository";

    private SongArtistCrossDao songArtistCrossDao;

    public SongArtistRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        songArtistCrossDao = database.songArtistCrossDao();
    }

    public void insertAll(List<SongArtistCross> crosses) {
        try {
            final Thread delete = new Thread(new DeleteAllSongArtistCrossThreadSafe(songArtistCrossDao));
            final Thread insertAll = new Thread(new InsertAllThreadSafe(songArtistCrossDao, crosses));

            delete.start();
            delete.join();
            insertAll.start();
            insertAll.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private SongArtistCrossDao songArtistCrossDao;
        private List<SongArtistCross> crosses;

        public InsertAllThreadSafe(SongArtistCrossDao songArtistCrossDao, List<SongArtistCross> crosses) {
            this.songArtistCrossDao = songArtistCrossDao;
            this.crosses = crosses;
        }

        @Override
        public void run() {
            songArtistCrossDao.insertAll(crosses);
        }
    }

    public void deleteAll() {
        DeleteAllSongArtistCrossThreadSafe delete = new DeleteAllSongArtistCrossThreadSafe(songArtistCrossDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class DeleteAllSongArtistCrossThreadSafe implements Runnable {
        private SongArtistCrossDao songArtistCrossDao;

        public DeleteAllSongArtistCrossThreadSafe(SongArtistCrossDao songArtistCrossDao) {
            this.songArtistCrossDao = songArtistCrossDao;
        }

        @Override
        public void run() {
            songArtistCrossDao.deleteAll();
        }
    }
}
