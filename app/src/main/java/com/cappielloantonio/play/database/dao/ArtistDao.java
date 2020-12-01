package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Artist;

import java.util.List;

@Dao
public interface ArtistDao {
    @Query("SELECT * FROM artist")
    LiveData<List<Artist>> getAll();

    @Query("SELECT * FROM artist ORDER BY RANDOM() LIMIT :number;")
    LiveData<List<Artist>> getSample(int number);

    @Query("SELECT * FROM artist WHERE name LIKE '%' || :name || '%'")
    LiveData<List<Artist>> searchArtist(String name);

    @Query("SELECT EXISTS(SELECT * FROM artist WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Artist artist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Artist> artists);

    @Delete
    void delete(Artist artist);

    @Query("SELECT name FROM artist WHERE name LIKE :query || '%' OR name like '% ' || :query || '%' GROUP BY name LIMIT :number")
    List<String> searchSuggestions(String query, int number);

    @Query("DELETE FROM artist")
    void deleteAll();

    @Query("SELECT * FROM artist WHERE id = :id")
    Artist getArtistByID(String id);
}