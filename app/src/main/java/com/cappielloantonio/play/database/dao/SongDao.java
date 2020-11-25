package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cappielloantonio.play.model.Song;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM song")
    LiveData<List<Song>> getAll();

    @Query("SELECT * FROM song WHERE title LIKE '%' || :title || '%'")
    LiveData<List<Song>> searchSong(String title);

    @Query("SELECT * FROM song ORDER BY RANDOM() LIMIT :number")
    LiveData<List<Song>> getDiscoverSample(int number);

    @Query("SELECT * FROM song ORDER BY added DESC LIMIT :number")
    LiveData<List<Song>> getRecentlyAddedSample(int number);

    @Query("SELECT * FROM song WHERE last_play != 0 ORDER BY last_play DESC LIMIT :number")
    LiveData<List<Song>> getRecentlyPlayedSample(int number);

    @Query("SELECT * FROM song WHERE play_count != 0 ORDER BY play_count DESC LIMIT :number")
    LiveData<List<Song>> getMostPlayedSample(int number);

    @Query("SELECT * FROM song WHERE play_count != 0 AND artistId = :artistID ORDER BY play_count DESC LIMIT :number")
    LiveData<List<Song>> getArtistTopSongsSample(String artistID, int number);

    @Query("SELECT * FROM song WHERE artistId = :artistID ORDER BY play_count DESC")
    LiveData<List<Song>> getArtistTopSongs(String artistID);

    @Query("SELECT * FROM song WHERE albumId = :albumID ORDER BY trackNumber ASC")
    LiveData<List<Song>> getAlbumSong(String albumID);

    @Query("SELECT * FROM song INNER Join song_genre_cross ON song.id = song_genre_cross.song_id AND song_genre_cross.genre_id = :genreID")
    LiveData<List<Song>> getSongByGenre(String genreID);

    @Query("SELECT * FROM song INNER Join song_genre_cross ON song.id = song_genre_cross.song_id AND song_genre_cross.genre_id IN (:filters) GROUP BY song.id")
    LiveData<List<Song>> getFilteredSong(ArrayList<String> filters);

    @Query("SELECT EXISTS(SELECT * FROM song WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Song song);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Song> songs);

    @Delete
    void delete(Song song);

    @Update
    public void update(Song song);

    @Query("SELECT * FROM song ORDER BY RANDOM() LIMIT :number")
    List<Song> random(int number);
}