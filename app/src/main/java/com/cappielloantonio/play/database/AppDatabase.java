package com.cappielloantonio.play.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cappielloantonio.play.database.dao.DownloadDao;
import com.cappielloantonio.play.database.dao.PlaylistDao;
import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.database.dao.RecentSearchDao;
import com.cappielloantonio.play.database.dao.ServerDao;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Server;

@SuppressLint("RestrictedApi")
@Database(
        version = 25,
        entities = {Queue.class, Server.class, RecentSearch.class, Download.class, Playlist.class}
        // autoMigrations = { @AutoMigration(from = 23, to = 24) }
)
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

    public abstract ServerDao serverDao();

    public abstract RecentSearchDao recentSearchDao();

    public abstract DownloadDao downloadDao();

    public abstract PlaylistDao playlistDao();
}
