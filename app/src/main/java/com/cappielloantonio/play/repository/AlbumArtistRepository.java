package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.AlbumArtistCrossDao;
import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.database.dao.SongArtistCrossDao;
import com.cappielloantonio.play.model.AlbumArtistCross;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.util.QueueUtil;

import java.util.ArrayList;
import java.util.List;

public class AlbumArtistRepository {
    private static final String TAG = "AlbumArtistRepository";

    private AlbumArtistCrossDao albumArtistCrossDao;

    public AlbumArtistRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        albumArtistCrossDao = database.albumArtistCrossDao();
    }

    public void insertAll(List<AlbumArtistCross> crosses) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(albumArtistCrossDao, crosses);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    private static class InsertAllThreadSafe implements Runnable {
        private AlbumArtistCrossDao albumArtistCrossDao;
        private List<AlbumArtistCross> crosses;

        public InsertAllThreadSafe(AlbumArtistCrossDao albumArtistCrossDao, List<AlbumArtistCross> crosses) {
            this.albumArtistCrossDao = albumArtistCrossDao;
            this.crosses = crosses;
        }

        @Override
        public void run() {
            albumArtistCrossDao.insertAll(crosses);
        }
    }

    public void deleteAll() {
        DeleteAllAlbumArtistCrossThreadSafe delete = new DeleteAllAlbumArtistCrossThreadSafe(albumArtistCrossDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class DeleteAllAlbumArtistCrossThreadSafe implements Runnable {
        private AlbumArtistCrossDao albumArtistCrossDao;

        public DeleteAllAlbumArtistCrossThreadSafe(AlbumArtistCrossDao albumArtistCrossDao) {
            this.albumArtistCrossDao = albumArtistCrossDao;
        }

        @Override
        public void run() {
            albumArtistCrossDao.deleteAll();
        }
    }
}
