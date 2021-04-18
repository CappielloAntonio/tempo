package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cappielloantonio.play.model.PlaylistSongCross;
import com.cappielloantonio.play.model.SongArtistCross;

import java.util.List;

@Dao
public interface PlaylistSongCrossDao {
    @Query("SELECT * FROM playlist_song_cross")
    LiveData<List<PlaylistSongCross>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM playlist_song_cross WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaylistSongCross playlistSongCross);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PlaylistSongCross> playlistSongCrosses);

    @Delete
    void delete(PlaylistSongCross playlistSongCross);

    @Update
    void update(PlaylistSongCross playlistSongCross);

    @Query("DELETE FROM playlist_song_cross")
    void deleteAll();
}