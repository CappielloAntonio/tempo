package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.DownloadDao;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.util.Preferences;

import java.util.List;

public class DownloadRepository {
    private final DownloadDao downloadDao;

    public DownloadRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        downloadDao = database.downloadDao();
    }

    public LiveData<List<Download>> getLiveDownload() {
        return downloadDao.getAll(Preferences.getServerId());
    }

    public LiveData<List<Download>> getLiveDownloadSample(int size, boolean isArtist, boolean isAlbum, boolean isTrack, boolean isPlaylist) {
        if (isArtist) return downloadDao.getSampleArtist(size, Preferences.getServerId());
        else if (isAlbum) return downloadDao.getSampleAlbum(size, Preferences.getServerId());
        else if (isTrack) return downloadDao.getSample(size, Preferences.getServerId());
        else if (isPlaylist) return downloadDao.getSamplePlaylist(size, Preferences.getServerId());
        else return downloadDao.getSample(size, Preferences.getServerId());
    }

    public LiveData<List<Download>> getLiveDownloadFromArtist(String artistId) {
        return downloadDao.getAllFromArtist(Preferences.getServerId(), artistId);
    }

    public LiveData<List<Download>> getLiveDownloadFromAlbum(String albumId) {
        return downloadDao.getAllFromAlbum(Preferences.getServerId(), albumId);
    }

    public LiveData<List<Download>> getLiveDownloadFromPlaylist(String playlistId) {
        return downloadDao.getAllFromPlaylist(Preferences.getServerId(), playlistId);
    }

    public LiveData<List<Download>> getLivePlaylist() {
        return downloadDao.getAllPlaylists(Preferences.getServerId());
    }

    public void insert(Download download) {
        InsertThreadSafe insert = new InsertThreadSafe(downloadDao, download);
        Thread thread = new Thread(insert);
        thread.start();
    }

    private static class InsertThreadSafe implements Runnable {
        private final DownloadDao downloadDao;
        private final Download download;

        public InsertThreadSafe(DownloadDao downloadDao, Download download) {
            this.downloadDao = downloadDao;
            this.download = download;
        }

        @Override
        public void run() {
            downloadDao.insert(download);
        }
    }

    public void insertAll(List<Download> downloads) {
        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(downloadDao, downloads);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    private static class InsertAllThreadSafe implements Runnable {
        private final DownloadDao downloadDao;
        private final List<Download> downloads;

        public InsertAllThreadSafe(DownloadDao downloadDao, List<Download> downloads) {
            this.downloadDao = downloadDao;
            this.downloads = downloads;
        }

        @Override
        public void run() {
            downloadDao.insertAll(downloads);
        }
    }

    public void deleteAll() {
        DeleteAllThreadSafe deleteAll = new DeleteAllThreadSafe(downloadDao);
        Thread thread = new Thread(deleteAll);
        thread.start();
    }

    private static class DeleteAllThreadSafe implements Runnable {
        private final DownloadDao downloadDao;

        public DeleteAllThreadSafe(DownloadDao downloadDao) {
            this.downloadDao = downloadDao;
        }

        @Override
        public void run() {
            downloadDao.deleteAll(Preferences.getServerId());
        }
    }

    public void delete(Download download) {
        DeleteThreadSafe delete = new DeleteThreadSafe(downloadDao, download);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class DeleteThreadSafe implements Runnable {
        private final DownloadDao downloadDao;
        private final Download download;

        public DeleteThreadSafe(DownloadDao downloadDao, Download download) {
            this.downloadDao = downloadDao;
            this.download = download;
        }

        @Override
        public void run() {
            downloadDao.delete(download.getMediaID());
        }
    }
}
