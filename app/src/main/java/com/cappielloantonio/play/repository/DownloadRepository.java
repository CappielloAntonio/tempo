package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.DownloadDao;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class DownloadRepository {
    private static final String TAG = "QueueRepository";

    private DownloadDao downloadDao;

    public DownloadRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        downloadDao = database.downloadDao();
    }

    public List<Download> getLiveDownload() {
        List<Download> downloads = new ArrayList<>();

        GetDownloadThreadSafe getDownloads = new GetDownloadThreadSafe(downloadDao);
        Thread thread = new Thread(getDownloads);
        thread.start();

        try {
            thread.join();
            downloads = getDownloads.getDownloads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return downloads;
    }

    private static class GetDownloadThreadSafe implements Runnable {
        private DownloadDao downloadDao;
        private List<Download> downloads;

        public GetDownloadThreadSafe(DownloadDao downloadDao) {
            this.downloadDao = downloadDao;
        }

        @Override
        public void run() {
            downloads = downloadDao.getAll();
        }

        public List<Download> getDownloads() {
            return downloads;
        }
    }

    public LiveData<List<Download>> getLiveDownloadSample(int size) {
        return downloadDao.getSample(size, PreferenceUtil.getInstance(App.getInstance()).getServerId());
    }

    public void insert(Download download) {
        InsertThreadSafe insert = new InsertThreadSafe(downloadDao, download);
        Thread thread = new Thread(insert);
        thread.start();
    }

    private static class InsertThreadSafe implements Runnable {
        private DownloadDao downloadDao;
        private Download download;

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
        private DownloadDao downloadDao;
        private List<Download> downloads;

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
        private DownloadDao downloadDao;

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
        private DownloadDao downloadDao;
        private Download download;

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
