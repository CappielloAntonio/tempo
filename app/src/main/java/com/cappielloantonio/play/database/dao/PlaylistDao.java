package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.subsonic.models.Playlist;

import java.util.List;

@Dao
public interface PlaylistDao {
    // @Query("SELECT * FROM playlist WHERE server=:serverId")
    // LiveData<List<Playlist>> getAll(String serverId);

    @Query("SELECT * FROM playlist")
    LiveData<List<Playlist>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Playlist playlist);

    @Delete
    void delete(Playlist playlist);
}