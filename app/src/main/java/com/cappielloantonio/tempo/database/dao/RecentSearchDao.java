package com.cappielloantonio.tempo.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.tempo.model.RecentSearch;

import java.util.List;

@Dao
public interface RecentSearchDao {
    @Query("SELECT * FROM recent_search ORDER BY search DESC")
    List<String> getRecent();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecentSearch search);

    @Delete
    void delete(RecentSearch search);
}