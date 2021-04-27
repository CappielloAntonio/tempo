package com.cappielloantonio.play.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cappielloantonio.play.database.dao.AlbumArtistCrossDao;
import com.cappielloantonio.play.database.dao.AlbumDao;
import com.cappielloantonio.play.database.dao.ArtistDao;
import com.cappielloantonio.play.database.dao.GenreDao;
import com.cappielloantonio.play.database.dao.PlaylistDao;
import com.cappielloantonio.play.database.dao.PlaylistSongCrossDao;
import com.cappielloantonio.play.database.dao.QueueDao;
import com.cappielloantonio.play.database.dao.RecentSearchDao;
import com.cappielloantonio.play.database.dao.SongArtistCrossDao;
import com.cappielloantonio.play.database.dao.SongDao;
import com.cappielloantonio.play.database.dao.SongGenreCrossDao;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.AlbumArtistCross;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.PlaylistSongCross;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.RecentSearch;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongArtistCross;
import com.cappielloantonio.play.model.SongGenreCross;

@Database(entities = {Album.class, Artist.class, Genre.class, Playlist.class, Song.class, RecentSearch.class, SongGenreCross.class, Queue.class, AlbumArtistCross.class, SongArtistCross.class, PlaylistSongCross.class}, version = 12, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    private final static String DB_NAME = "play_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null && context != null) {
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

    public abstract SongGenreCrossDao songGenreCrossDao();

    public abstract AlbumArtistCrossDao albumArtistCrossDao();

    public abstract SongArtistCrossDao songArtistCrossDao();

    public abstract PlaylistSongCrossDao playlistSongCrossDao();

    public abstract QueueDao queueDao();
}
