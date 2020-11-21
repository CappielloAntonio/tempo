package com.cappielloantonio.play.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cappielloantonio.play.database.dao.AlbumDao;
import com.cappielloantonio.play.database.dao.ArtistDao;
import com.cappielloantonio.play.database.dao.GenreDao;
import com.cappielloantonio.play.database.dao.PlaylistDao;
import com.cappielloantonio.play.database.dao.RecentSearchDao;
import com.cappielloantonio.play.database.dao.SongDao;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Song;

@Database(entities = {Album.class, Artist.class, Genre.class, Playlist.class, Song.class, RecentSearch.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";

    private static AppDatabase instance;
    private final static String DB_NAME = "play_db";

    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract AlbumDao albumDao();

    public abstract ArtistDao artistDao();

    public abstract GenreDao genreDao();

    public abstract PlaylistDao playlistDao();

    public abstract SongDao songDao();

    public abstract RecentSearchDao recentSearchDao();
}
