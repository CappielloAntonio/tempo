package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Playlist;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    LiveData<List<Playlist>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM playlist WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Playlist playlist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Playlist> playlists);

    @Delete
    void delete(Playlist playlist);
}