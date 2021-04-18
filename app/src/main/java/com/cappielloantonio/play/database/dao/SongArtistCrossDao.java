package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cappielloantonio.play.model.AlbumArtistCross;
import com.cappielloantonio.play.model.SongArtistCross;

import java.util.List;

@Dao
public interface SongArtistCrossDao {
    @Query("SELECT * FROM song_artist_cross")
    LiveData<List<SongArtistCross>> getAll();

    @Query("SELECT EXISTS(SELECT * FROM song_artist_cross WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SongArtistCross songArtistCross);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SongArtistCross> songArtistCross);

    @Delete
    void delete(SongArtistCross songArtistCross);

    @Update
    void update(SongArtistCross songArtistCross);

    @Query("DELETE FROM song_artist_cross")
    void deleteAll();
}