package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.DownloadDao;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class DownloadRepository {
    private static final String TAG = "QueueRepository";

    private final DownloadDao downloadDao;

    public DownloadRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        downloadDao = database.downloadDao();
    }

    public LiveData<List<Download>> getLiveDownload() {
        return downloadDao.getAll(PreferenceUtil.getInstance(App.getInstance()).getServerId());
    }

    public LiveData<List<Download>> getLiveDownloadSample(int size) {
        return downloadDao.getSample(size, PreferenceUtil.getInstance(App.getInstance()).getServerId());
    }

    public LiveData<List<Download>> getLiveDownloadFromArtist(String artistId) {
        return downloadDao.getAllFromArtist(PreferenceUtil.getInstance(App.getInstance()).getServerId(), artistId);
    }

    public LiveData<List<Download>> getLiveDownloadFromAlbum(String albumId) {
        return downloadDao.getAllFromAlbum(PreferenceUtil.getInstance(App.getInstance()).getServerId(), albumId);
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
            downloadDao.deleteAll(PreferenceUtil.getInstance(App.getInstance()).getServerId());
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
            downloadDao.delete(download.getSongID());
        }
    }
}
