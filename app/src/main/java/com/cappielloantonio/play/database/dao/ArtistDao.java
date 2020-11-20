package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;

import java.util.List;

@Dao
public interface ArtistDao {
    @Query("SELECT * FROM artist")
    LiveData<List<Artist>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM artist WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Artist artist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Artist> artists);

    @Delete
    void delete(Artist artist);
}