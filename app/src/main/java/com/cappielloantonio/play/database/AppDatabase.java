package com.cappielloantonio.play.database;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.database.converter.DateConverters;
import com.cappielloantonio.play.database.dao.ChronologyDao;
import com.cappielloantonio.play.database.dao.DownloadDao;
import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.database.dao.RecentSearchDao;
import com.cappielloantonio.play.database.dao.ServerDao;
import com.cappielloantonio.play.model.Chronology;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Server;

@Database(
        version = 63,
        entities = {Queue.class, Server.class, RecentSearch.class, Download.class, Chronology.class}
        // autoMigrations = {@AutoMigration(from = 62, to = 63)}
)
@TypeConverters({DateConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private final static String DB_NAME = "play_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(App.getContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract QueueDao queueDao();

    public abstract ServerDao serverDao();

    public abstract RecentSearchDao recentSearchDao();

    public abstract DownloadDao downloadDao();

    public abstract ChronologyDao chronologyDao();
}
