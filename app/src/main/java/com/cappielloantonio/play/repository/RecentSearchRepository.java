package com.cappielloantonio.play.repository;

import android.app.Application;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.RecentSearchDao;
import com.cappielloantonio.play.model.RecentSearch;

import java.util.ArrayList;
import java.util.List;

public class RecentSearchRepository {
    private RecentSearchDao recentSearchDao;

    public RecentSearchRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        recentSearchDao = database.recentSearchDao();
    }

    public void insert(RecentSearch recentSearch) {
        InsertThreadSafe insert = new InsertThreadSafe(recentSearchDao, recentSearch);
        Thread thread = new Thread(insert);
        thread.start();
    }

    public void delete(RecentSearch recentSearch) {
        DeleteThreadSafe delete = new DeleteThreadSafe(recentSearchDao, recentSearch);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public void deleteAll() {
        DeleteAllThreadSafe delete = new DeleteAllThreadSafe(recentSearchDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    public List<String> getRecentSearchSuggestion(int limit) {
        List<String> recent = new ArrayList<>();

        RecentThreadSafe suggestionsThread = new RecentThreadSafe(recentSearchDao, limit);
        Thread thread = new Thread(suggestionsThread);
        thread.start();

        try {
            thread.join();
            recent = suggestionsThread.getRecent();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return recent;
    }

    private static class DeleteThreadSafe implements Runnable {
        private RecentSearchDao recentSearchDao;
        private RecentSearch recentSearch;

        public DeleteThreadSafe(RecentSearchDao recentSearchDao, RecentSearch recentSearch) {
            this.recentSearchDao = recentSearchDao;
            this.recentSearch = recentSearch;
        }

        @Override
        public void run() {
            recentSearchDao.delete(recentSearch);
        }
    }

    private static class InsertThreadSafe implements Runnable {
        private RecentSearchDao recentSearchDao;
        private RecentSearch recentSearch;

        public InsertThreadSafe(RecentSearchDao recentSearchDao, RecentSearch recentSearch) {
            this.recentSearchDao = recentSearchDao;
            this.recentSearch = recentSearch;
        }

        @Override
        public void run() {
            recentSearchDao.insert(recentSearch);
        }
    }

    private static class DeleteAllThreadSafe implements Runnable {
        private RecentSearchDao recentSearchDao;

        public DeleteAllThreadSafe(RecentSearchDao recentSearchDao) {
            this.recentSearchDao = recentSearchDao;
        }

        @Override
        public void run() {
            recentSearchDao.deleteAll();
        }
    }

    private static class RecentThreadSafe implements Runnable {
        private RecentSearchDao recentSearchDao;
        private int limit;
        private List<String> recent = new ArrayList<>();

        public RecentThreadSafe(RecentSearchDao recentSearchDao, int limit) {
            this.recentSearchDao = recentSearchDao;
            this.limit = limit;
        }

        @Override
        public void run() {
            recent = recentSearchDao.getRecent(limit);
        }

        public List<String> getRecent() {
            return recent;
        }
    }
}
