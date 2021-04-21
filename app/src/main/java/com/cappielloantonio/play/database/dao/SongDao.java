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

    @Query("SELECT * FROM song")
    List<Song> getAllList();

    @Query("SELECT * FROM song WHERE title LIKE '%' || :title || '%' LIMIT :limit")
    LiveData<List<Song>> searchSong(String title, int limit);

    // Da utilizzare in caso si decidesse di migliorare il viewpager nella home
    @Query("SELECT * FROM song WHERE id IN (:pseudoRandomNumber)")
    LiveData<List<Song>> getDiscoverySample(List<Integer> pseudoRandomNumber);

    @Query("SELECT * FROM song ORDER BY added DESC LIMIT :number")
    LiveData<List<Song>> getRecentlyAddedSample(int number);

    @Query("SELECT * FROM song WHERE last_play != 0 ORDER BY last_play DESC LIMIT :number")
    LiveData<List<Song>> getRecentlyPlayedSample(int number);

    @Query("SELECT * FROM song WHERE play_count != 0 ORDER BY play_count DESC LIMIT :number")
    LiveData<List<Song>> getMostPlayedSample(int number);

    @Query("SELECT song.* FROM song INNER JOIN song_artist_cross ON song.id = song_artist_cross.song_id AND song_artist_cross.artist_id = :artistID ORDER BY play_count DESC LIMIT :number")
    LiveData<List<Song>> getArtistTopSongsSample(String artistID, int number);

    @Query("SELECT song.* FROM song INNER JOIN song_artist_cross ON song.id = song_artist_cross.song_id AND song_artist_cross.artist_id = :artistID ORDER BY play_count DESC")
    LiveData<List<Song>> getArtistTopSongs(String artistID);

    @Query("SELECT song.* FROM song INNER JOIN song_artist_cross ON song.id = song_artist_cross.song_id AND song_artist_cross.artist_id = :artistID ORDER BY RANDOM() LIMIT :number")
    List<Song> getArtistRandomSongs(String artistID, int number);

    @Query("SELECT * FROM song WHERE albumId = :albumID ORDER BY trackNumber ASC")
    LiveData<List<Song>> getLiveAlbumSong(String albumID);

    @Query("SELECT song.* FROM song INNER JOIN playlist_song_cross ON song.id = playlist_song_cross.song_id AND playlist_song_cross.playlist_id = :playlistID")
    LiveData<List<Song>> getLivePlaylistSong(String playlistID);

    @Query("SELECT * FROM song WHERE albumId = :albumID ORDER BY trackNumber ASC")
    List<Song> getAlbumSong(String albumID);

    @Query("SELECT song.* FROM song INNER JOIN song_genre_cross ON song.id = song_genre_cross.song_id AND song_genre_cross.genre_id = :genreID")
    LiveData<List<Song>> getSongByGenre(String genreID);

    @Query("SELECT song.* FROM song INNER JOIN song_genre_cross ON song.id = song_genre_cross.song_id AND song_genre_cross.genre_id IN (:filters) GROUP BY song.id")
    LiveData<List<Song>> getFilteredSong(ArrayList<String> filters);

    @Query("SELECT * FROM song WHERE favorite = 1 LIMIT :number")
    LiveData<List<Song>> getFavoriteSongSample(int number);

    @Query("SELECT * FROM song WHERE favorite = 1")
    LiveData<List<Song>> getFavoriteSong();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Song> songs);

    @Query("DELETE FROM song")
    void deleteAll();

    @Update
    void update(Song song);

    @Query("SELECT * FROM song WHERE id IN (:ids)")
    List<Song> getSongsByID(List<String> ids);

    @Query("SELECT * FROM song ORDER BY RANDOM() LIMIT :number")
    List<Song> random(int number);

    @Query("SELECT title FROM song WHERE title LIKE :query || '%' OR title like '% ' || :query || '%' GROUP BY title LIMIT :number")
    List<String> searchSuggestions(String query, int number);

    @Query("SELECT year FROM song WHERE year != 0 GROUP BY year")
    List<Integer> getYearList();

    @Query("SELECT * FROM song WHERE year = :year")
    LiveData<List<Song>> getSongsByYear(int year);
}