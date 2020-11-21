package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Song;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM song")
    LiveData<List<Song>> getAll();

    @Query("SELECT * FROM song WHERE title LIKE '%' || :title || '%'")
    LiveData<List<Song>> searchSong(String title);

    @Query("SELECT EXISTS(SELECT * FROM song WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Song song);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Song> songs);

    @Delete
    void delete(Song song);
}