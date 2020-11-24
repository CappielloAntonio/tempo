package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongGenreCross;

import java.util.List;

@Dao
public interface SongGenreCrossDao {
    @Query("SELECT * FROM song_genre_cross")
    LiveData<List<SongGenreCross>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM song_genre_cross WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SongGenreCross songGenreCross);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SongGenreCross> songGenreCrosses);

    @Delete
    void delete(SongGenreCross songGenreCross);

    @Update
    void update(SongGenreCross songGenreCross);
}