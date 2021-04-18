package com.cappielloantonio.play.repository;

import android.app.Application;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.PlaylistSongCrossDao;
import com.cappielloantonio.play.database.dao.SongArtistCrossDao;
import com.cappielloantonio.play.model.PlaylistSongCross;
import com.cappielloantonio.play.model.SongArtistCross;

import java.util.List;

public class PlaylistSongRepository {
    private static final String TAG = "AlbumArtistRepository";

    private PlaylistSongCrossDao playlistSongCrossDao;

    public PlaylistSongRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        playlistSongCrossDao = database.playlistSongCrossDao();
    }

    public void insertAll(List<PlaylistSongCross> crosses) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(playlistSongCrossDao, crosses);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    private static class InsertAllThreadSafe implements Runnable {
        private PlaylistSongCrossDao playlistSongCrossDao;
        private List<PlaylistSongCross> crosses;

        public InsertAllThreadSafe(PlaylistSongCrossDao playlistSongCrossDao, List<PlaylistSongCross> crosses) {
            this.playlistSongCrossDao = playlistSongCrossDao;
            this.crosses = crosses;
        }

        @Override
        public void run() {
            playlistSongCrossDao.insertAll(crosses);
        }
    }

    public void deleteAll() {
        DeleteAllPlaylistSongCrossThreadSafe delete = new DeleteAllPlaylistSongCrossThreadSafe(playlistSongCrossDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class DeleteAllPlaylistSongCrossThreadSafe implements Runnable {
        private PlaylistSongCrossDao playlistSongCrossDao;

        public DeleteAllPlaylistSongCrossThreadSafe(PlaylistSongCrossDao playlistSongCrossDao) {
            this.playlistSongCrossDao = playlistSongCrossDao;
        }

        @Override
        public void run() {
            playlistSongCrossDao.deleteAll();
        }
    }
}
