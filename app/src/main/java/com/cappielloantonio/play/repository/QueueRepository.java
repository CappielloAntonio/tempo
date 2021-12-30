package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;
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

    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();

        GetSongsThreadSafe getSongs = new GetSongsThreadSafe(queueDao);
        Thread thread = new Thread(getSongs);
        thread.start();

        try {
            thread.join();
            songs = getSongs.getSongs();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return songs;
    }

    public void insert(Song song, boolean reset) {
        try {
            if(reset) {
                Thread delete = new Thread(new DeleteAllThreadSafe(queueDao));
                delete.start();
                delete.join();
            }

            Thread insert = new Thread(new InsertThreadSafe(queueDao, song));
            insert.start();
            insert.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertAll(List<Song> songs, boolean reset) {
        try {
            if(reset) {
                Thread delete = new Thread(new DeleteAllThreadSafe(queueDao));
                delete.start();
                delete.join();
            }

            Thread insertAll = new Thread(new InsertAllThreadSafe(queueDao, songs));
            insertAll.start();
            insertAll.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertImmediatelyAfter(Song song, int afterIndex) {
        try {
            GetSongsThreadSafe getSongsThreadSafe = new GetSongsThreadSafe(queueDao);
            Thread getSongsThread = new Thread(getSongsThreadSafe);
            getSongsThread.start();
            getSongsThread.join();

            List<Song> songs = getSongsThreadSafe.getSongs();
            songs.add(afterIndex, song);

            Thread delete = new Thread(new DeleteAllThreadSafe(queueDao));
            delete.start();
            delete.join();

            Thread insertAll = new Thread(new InsertAllThreadSafe(queueDao, songs));
            insertAll.start();
            insertAll.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertAllImmediatelyAfter(List<Song> toAdd, int afterIndex) {
        try {
            GetSongsThreadSafe getSongsThreadSafe = new GetSongsThreadSafe(queueDao);
            Thread getSongsThread = new Thread(getSongsThreadSafe);
            getSongsThread.start();
            getSongsThread.join();

            List<Song> songs = getSongsThreadSafe.getSongs();
            songs.addAll(afterIndex, toAdd);

            Thread delete = new Thread(new DeleteAllThreadSafe(queueDao));
            delete.start();
            delete.join();

            Thread insertAll = new Thread(new InsertAllThreadSafe(queueDao, songs));
            insertAll.start();
            insertAll.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteByPosition(int position) {
        DeleteByPositionThreadSafe delete = new DeleteByPositionThreadSafe(queueDao, position);
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

    public void setTimestamp(Song song) {
        SetTimestampThreadSafe delete = new SetTimestampThreadSafe(queueDao, song.getId());
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class GetSongsThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private List<Song> songs;

        public GetSongsThreadSafe(QueueDao queueDao) {
            this.queueDao = queueDao;
        }

        @Override
        public void run() {
            songs = MappingUtil.mapQueue(queueDao.getAllSimple());
        }

        public List<Song> getSongs() {
            return songs;
        }
    }

    private static class InsertThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final Song song;

        public InsertThreadSafe(QueueDao queueDao, Song song) {
            this.queueDao = queueDao;
            this.song = song;
        }

        @Override
        public void run() {
            queueDao.insert(MappingUtil.mapSongToQueue(song));
        }
    }

    private static class InsertAllThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final List<Song> songs;

        public InsertAllThreadSafe(QueueDao queueDao, List<Song> songs) {
            this.queueDao = queueDao;
            this.songs = songs;
        }

        @Override
        public void run() {
            queueDao.insertAll(MappingUtil.mapSongsToQueue(songs));
        }
    }

    private static class DeleteByPositionThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final int position;

        public DeleteByPositionThreadSafe(QueueDao queueDao, int position) {
            this.queueDao = queueDao;
            this.position = position;
        }

        @Override
        public void run() {
            queueDao.deleteByPosition(position);
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

    private static class SetTimestampThreadSafe implements Runnable {
        private final QueueDao queueDao;
        private final String songId;

        public SetTimestampThreadSafe(QueueDao queueDao, String songId) {
            this.queueDao = queueDao;
            this.songId = songId;
        }

        @Override
        public void run() {
            queueDao.setLastPlay(songId, Instant.now().toEpochMilli());
        }
    }
}
