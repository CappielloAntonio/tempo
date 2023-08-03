package com.cappielloantonio.tempo.repository;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.tempo.database.AppDatabase;
import com.cappielloantonio.tempo.database.dao.DownloadDao;
import com.cappielloantonio.tempo.database.dao.FavoriteDao;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.model.Favorite;

import java.util.ArrayList;
import java.util.List;

public class DownloadRepository {
    private final DownloadDao downloadDao = AppDatabase.getInstance().downloadDao();

    public LiveData<List<Download>> getLiveDownload() {
        return downloadDao.getAll();
    }

    public Download getDownload(String id) {
        Download download = null;

        GetDownloadThreadSafe getDownloadThreadSafe = new GetDownloadThreadSafe(downloadDao, id);
        Thread thread = new Thread(getDownloadThreadSafe);
        thread.start();

        try {
            thread.join();
            download = getDownloadThreadSafe.getDownload();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return download;
    }

    private static class GetDownloadThreadSafe implements Runnable {
        private final DownloadDao downloadDao;
        private final String id;
        private Download download;

        public GetDownloadThreadSafe(DownloadDao downloadDao, String id) {
            this.downloadDao = downloadDao;
            this.id = id;
        }

        @Override
        public void run() {
            download = downloadDao.getOne(id);
        }

        public Download getDownload() {
            return download;
        }
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

    public void update(String id) {
        UpdateThreadSafe update = new UpdateThreadSafe(downloadDao, id);
        Thread thread = new Thread(update);
        thread.start();
    }

    private static class UpdateThreadSafe implements Runnable {
        private final DownloadDao downloadDao;
        private final String id;

        public UpdateThreadSafe(DownloadDao downloadDao, String id) {
            this.downloadDao = downloadDao;
            this.id = id;
        }

        @Override
        public void run() {
            downloadDao.update(id);
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
            downloadDao.deleteAll();
        }
    }

    public void delete(String id) {
        DeleteThreadSafe delete = new DeleteThreadSafe(downloadDao, id);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class DeleteThreadSafe implements Runnable {
        private final DownloadDao downloadDao;
        private final String id;

        public DeleteThreadSafe(DownloadDao downloadDao, String id) {
            this.downloadDao = downloadDao;
            this.id = id;
        }

        @Override
        public void run() {
            downloadDao.delete(id);
        }
    }
}
