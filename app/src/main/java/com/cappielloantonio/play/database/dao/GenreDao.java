package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;

import java.util.List;

@Dao
public interface GenreDao {
    @Query("SELECT * FROM genre")
    LiveData<List<Genre>> getAll();

    @Query("SELECT * FROM genre")
    List<Genre> getGenreList();

    @Query("SELECT * FROM genre ORDER BY RANDOM() LIMIT :number;")
    LiveData<List<Genre>> getSample(int number);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Genre> genres);

    @Query("DELETE FROM genre")
    void deleteAll();

    @Query("SELECT * FROM genre WHERE name LIKE '%' || :name || '%' LIMIT :limit")
    LiveData<List<Genre>> searchGenre(String name, int limit);

    @Query("SELECT name FROM genre WHERE name LIKE :query || '%' OR name like '% ' || :query || '%' GROUP BY name LIMIT :number")
    List<String> searchSuggestions(String query, int number);
}