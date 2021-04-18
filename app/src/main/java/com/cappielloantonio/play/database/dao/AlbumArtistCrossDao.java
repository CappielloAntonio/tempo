package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cappielloantonio.play.model.AlbumArtistCross;
import com.cappielloantonio.play.model.SongGenreCross;

import java.util.List;

@Dao
public interface AlbumArtistCrossDao {
    @Query("SELECT * FROM album_artist_cross")
    LiveData<List<AlbumArtistCross>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM album_artist_cross WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AlbumArtistCross albumArtistCross);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AlbumArtistCross> albumArtistCrosses);

    @Delete
    void delete(AlbumArtistCross albumArtistCross);

    @Update
    void update(AlbumArtistCross albumArtistCross);

    @Query("DELETE FROM album_artist_cross")
    void deleteAll();
}