package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Playlist;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    LiveData<List<Playlist>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Playlist> playlists);

    @Query("DELETE FROM playlist")
    void deleteAll();

    @Query("SELECT * FROM playlist ORDER BY RANDOM() LIMIT :number")
    List<Playlist> random(int number);
}