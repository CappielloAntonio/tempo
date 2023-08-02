package com.cappielloantonio.tempo.database;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.database.converter.DateConverters;
import com.cappielloantonio.tempo.database.dao.ChronologyDao;
import com.cappielloantonio.tempo.database.dao.DownloadDao;
import com.cappielloantonio.tempo.database.dao.FavoriteDao;
import com.cappielloantonio.tempo.database.dao.QueueDao;
import com.cappielloantonio.tempo.database.dao.RecentSearchDao;
import com.cappielloantonio.tempo.database.dao.ServerDao;
import com.cappielloantonio.tempo.model.Chronology;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.model.Favorite;
import com.cappielloantonio.tempo.model.Queue;
import com.cappielloantonio.tempo.model.RecentSearch;
import com.cappielloantonio.tempo.model.Server;

@Database(
        version = 2,
        entities = {Queue.class, Server.class, RecentSearch.class, Download.class, Chronology.class, Favorite.class},
        autoMigrations = {@AutoMigration(from = 1, to = 2)}
)
@TypeConverters({DateConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private final static String DB_NAME = "tempo_db";
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

    public abstract FavoriteDao favoriteDao();
}
