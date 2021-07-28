package com.cappielloantonio.play.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.database.dao.RecentSearchDao;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Song;

@Database(entities = {Queue.class, RecentSearch.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";

    private final static String DB_NAME = "play_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null && context != null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract QueueDao queueDao();

    public abstract RecentSearchDao recentSearchDao();
}
