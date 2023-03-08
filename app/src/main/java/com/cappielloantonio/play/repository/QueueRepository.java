package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.media3.common.MediaItem;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.subsonic.models.Child;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Child> getMedia() {
        List<Child> media = new ArrayList<>();

        GetMediaThreadSafe getMedia = new GetMediaThreadSafe(queueDao);
        Thread thread = new Thread(getMedia);
        thread.start();

        try {
            thread.join();
            media = getMedia.getMedia().stream()
                    .map(Child.class::cast)
                    .collect(Collectors.toList());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return media;
    }

    public void insert(Child media, boolean reset, int afterIndex) {
        try {
            List<Queue> mediaList = new ArrayList<>();

            if (!reset) {
                GetMediaThreadSafe getMediaThreadSafe = new GetMediaThreadSafe(queueDao);
                Thread getMediaThread = new Thread(getMediaThreadSafe);
                getMediaThread.start();
                getMediaThread.join();

                mediaList = getMediaThreadSafe.getMedia();
            }

            Queue queueItem = new Queue(media);
            mediaList.add(afterIndex, queueItem);

            for (int i = 0; i < mediaList.size(); i++) {
                mediaList.get(i).setTrackOrder(i);
            }

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

    public void insertAll(List<Child> toAdd, boolean reset, int afterIndex) {
        try {
            List<Queue> media = new ArrayList<>();

            if (!reset) {
                GetMediaThreadSafe getMediaThreadSafe = new GetMediaThreadSafe(queueDao);
                Thread getMediaThread = new Thread(getMediaThreadSafe);
                getMediaThread.start();
                getMediaThread.join();

                media = getMediaThreadSafe.getMedia();
            }

            for (int i = 0; i < toAdd.size(); i++) {
                Queue queueItem = new Queue(toAdd.get(i));
                media.add(afterIndex + i, queueItem);
            }

            for (int i = 0; i < media.size(); i++) {
                media.get(i).setTrackOrder(i);
            }

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
            Queue lastMediaPlayed = getLastPlayedMediaThreadSafe.getQueueItem();
            index = lastMediaPlayed.getTrackOrder();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return index;
    }

    public long getLastPlayedMediaTimestamp() {
        long timestamp = 0;

        GetLastPlayedMediaThreadSafe getLastPlayedMediaThreadSafe = new GetLastPlayedMediaThreadSafe(queueDao);
        Thread thread = new Thread(getLastPlayedMediaThreadSafe);
        thread.start();

        try {
            thread.join();
            Queue lastMediaPlayed = getLastPlayedMediaThreadSafe.getQueueItem();
            timestamp = lastMediaPlayed.getPlayingChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public boolean isMediaPlayingPlausible(MediaItem mediaItem) {
        boolean isPlausible = true;

        GetLastPlayedMediaThreadSafe getLastPlayedMediaThreadSafe = new GetLastPlayedMediaThreadSafe(queueDao);
        Thread thread = new Thread(getLastPlayedMediaThreadSafe);
        thread.start();

        try {
            thread.join();
            Queue lastMediaPlayed = getLastPlayedMediaThreadSafe.getQueueItem();

            if (mediaItem.mediaId.equals(lastMediaPlayed.getId())) {

                if (System.currentTimeMillis() > lastMediaPlayed.getLastPlay() + lastMediaPlayed.getDuration() * 1000) {
                    isPlausible = true;
                } else {
                    isPlausible = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isPlausible;
    }

    private static class GetMediaThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private List<Queue> media;

        public GetMediaThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            media = queueDao.getAllSimple();
        }

        public List<Queue> getMedia() {
            return media;
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final List<Queue> media;

        public InsertAllThreadSafe(QueueDao queueDao, List<Queue> media) {
            this.queueDao = queueDao;
            this.media = media;
        }

        @Override
        public void run() {
            queueDao.insertAll(media);
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
        private Queue lastMediaPlayed;

        public GetLastPlayedMediaThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            lastMediaPlayed = queueDao.getLastPlayed();
        }

        public Queue getQueueItem() {
            return lastMediaPlayed;
        }
    }
}
