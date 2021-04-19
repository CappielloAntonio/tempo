package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.RecentSearchDao;
import com.cappielloantonio.play.model.RecentSearch;

import java.util.List;

public class RecentSearchRepository {
    private RecentSearchDao recentSearchDao;
    private LiveData<List<RecentSearch>> listLiveRecentSearches;

    public RecentSearchRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        recentSearchDao = database.recentSearchDao();
        listLiveRecentSearches = recentSearchDao.getLast(3);
    }

    public LiveData<List<RecentSearch>> getListLiveRecentSearches() {
        return listLiveRecentSearches;
    }

    public void insert(RecentSearch recentSearch) {
        InsertThreadSafe insert = new InsertThreadSafe(recentSearchDao, recentSearch);
        Thread thread = new Thread(insert);
        thread.start();
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

    public void deleteAll() {
        DeleteAllThreadSafe delete = new DeleteAllThreadSafe(recentSearchDao);
        Thread thread = new Thread(delete);
        thread.start();
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
}
