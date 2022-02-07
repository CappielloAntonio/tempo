package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.util.MappingUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class QueueRepository {
    private static final String TAG = "QueueRepository";

    private final QueueDao queueDao;

    public QueueRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        queueDao = database.queueDao();
    }

    public LiveData<List<Queue>> getLiveQueue() {
        return queueDao.getAll();
    }

    public List<Media> getMedia() {
        List<Media> media = new ArrayList<>();

        GetMediaThreadSafe getMedia = new GetMediaThreadSafe(queueDao);
        Thread thread = new Thread(getMedia);
        thread.start();

        try {
            thread.join();
            media = getMedia.getMedia();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return media;
    }

    public void insert(Media media, boolean reset, int afterIndex) {
        try {
            List<Media> mediaList = new ArrayList<>();

            if (!reset) {
                GetMediaThreadSafe getMediaThreadSafe = new GetMediaThreadSafe(queueDao);
                Thread getMediaThread = new Thread(getMediaThreadSafe);
                getMediaThread.start();
                getMediaThread.join();

                mediaList = getMediaThreadSafe.getMedia();
            }

            mediaList.add(afterIndex, media);

            Thread delete = new Thread(new DeleteAllThreadSafe(queueDao));
            delete.start();
            delete.join();

            Thread insertAll = new Thread(new InsertAllThreadSafe(queueDao, mediaList));
            insertAll.start();
            insertAll.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertAll(List<Media> toAdd, boolean reset, int afterIndex) {
        try {
            List<Media> media = new ArrayList<>();

            if (!reset) {
                GetMediaThreadSafe getMediaThreadSafe = new GetMediaThreadSafe(queueDao);
                Thread getMediaThread = new Thread(getMediaThreadSafe);
                getMediaThread.start();
                getMediaThread.join();

                media = getMediaThreadSafe.getMedia();
            }

            media.addAll(afterIndex, toAdd);

            Thread delete = new Thread(new DeleteAllThreadSafe(queueDao));
            delete.start();
            delete.join();

            Thread insertAll = new Thread(new InsertAllThreadSafe(queueDao, media));
            insertAll.start();
            insertAll.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(int position) {
        DeleteThreadSafe delete = new DeleteThreadSafe(queueDao, position);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public void deleteAll() {
        DeleteAllThreadSafe deleteAll = new DeleteAllThreadSafe(queueDao);
        Thread thread = new Thread(deleteAll);
        thread.start();
    }

    public int count() {
        int count = 0;

        CountThreadSafe countThread = new CountThreadSafe(queueDao);
        Thread thread = new Thread(countThread);
        thread.start();

        try {
            thread.join();
            count = countThread.getCount();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return count;
    }

    public void setLastPlayedTimestamp(String id) {
        SetLastPlayedTimestampThreadSafe timestamp = new SetLastPlayedTimestampThreadSafe(queueDao, id);
        Thread thread = new Thread(timestamp);
        thread.start();
    }

    public void setPlayingPausedTimestamp(String id, long ms) {
        SetPlayingPausedTimestampThreadSafe timestamp = new SetPlayingPausedTimestampThreadSafe(queueDao, id, ms);
        Thread thread = new Thread(timestamp);
        thread.start();
    }

    public int getLastPlayedMediaIndex() {
        int index = 0;

        GetLastPlayedMediaThreadSafe getLastPlayedMediaThreadSafe = new GetLastPlayedMediaThreadSafe(queueDao);
        Thread thread = new Thread(getLastPlayedMediaThreadSafe);
        thread.start();

        try {
            thread.join();
            index = getLastPlayedMediaThreadSafe.getIndex();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return index;
    }

    public long getLastPlayedMediaTimestamp() {
        long timestamp = 0;

        GetLastPlayedMediaTimestampThreadSafe getLastPlayedMediaTimestampThreadSafe = new GetLastPlayedMediaTimestampThreadSafe(queueDao);
        Thread thread = new Thread(getLastPlayedMediaTimestampThreadSafe);
        thread.start();

        try {
            thread.join();
            timestamp = getLastPlayedMediaTimestampThreadSafe.getTimestamp();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    private static class GetMediaThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private List<Media> media;

        public GetMediaThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            media = MappingUtil.mapQueue(queueDao.getAllSimple());
        }

        public List<Media> getMedia() {
            return media;
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final List<Media> media;

        public InsertAllThreadSafe(QueueDao queueDao, List<Media> media) {
            this.queueDao = queueDao;
            this.media = media;
        }

        @Override
        public void run() {
            queueDao.insertAll(MappingUtil.mapMediaToQueue(media));
        }
    }

    private static class DeleteThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final int position;

        public DeleteThreadSafe(QueueDao queueDao, int position) {
            this.queueDao = queueDao;
            this.position = position;
        }

        @Override
        public void run() {
            queueDao.delete(position);
        }
    }

    private static class DeleteAllThreadSafe implements Runnable {
        private final QueueDao queueDao;

        public DeleteAllThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            queueDao.deleteAll();
        }
    }

    private static class CountThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private int count = 0;

        public CountThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            count = queueDao.count();
        }

        public int getCount() {
            return count;
        }
    }

    private static class SetLastPlayedTimestampThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final String mediaId;

        public SetLastPlayedTimestampThreadSafe(QueueDao queueDao, String mediaId) {
            this.queueDao = queueDao;
            this.mediaId = mediaId;
        }

        @Override
        public void run() {
            queueDao.setLastPlay(mediaId, Instant.now().toEpochMilli());
        }
    }

    private static class SetPlayingPausedTimestampThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final String mediaId;
        private final long ms;

        public SetPlayingPausedTimestampThreadSafe(QueueDao queueDao, String mediaId, long ms) {
            this.queueDao = queueDao;
            this.mediaId = mediaId;
            this.ms = ms;
        }

        @Override
        public void run() {
            queueDao.setPlayingChanged(mediaId, ms);
        }
    }

    private static class GetLastPlayedMediaThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private int index;

        public GetLastPlayedMediaThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            index = queueDao.getLastPlay();
        }

        public int getIndex() {
            return index;
        }
    }

    private static class GetLastPlayedMediaTimestampThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private long timestamp;

        public GetLastPlayedMediaTimestampThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            timestamp = queueDao.getLastPlayedTimestamp();
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
