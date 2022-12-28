package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Chronology;

import java.util.List;

@Dao
public interface ChronologyDao {
    @Query("SELECT * FROM chronology WHERE timestamp >= :startDate AND timestamp < :endDate  GROUP BY id ORDER BY COUNT(id) DESC LIMIT 9")
    LiveData<List<Chronology>> getAllFrom(long startDate, long endDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Chronology chronologyObject);
}