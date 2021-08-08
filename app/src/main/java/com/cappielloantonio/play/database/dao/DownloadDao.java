package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Download;

import java.util.List;

@Dao
public interface DownloadDao {
    @Query("SELECT * FROM download")
    List<Download> getAll();

    @Query("SELECT * FROM download WHERE server=:server LIMIT :size")
    LiveData<List<Download>> getSample(int size, String server);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Download download);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Download> downloads);

    @Query("DELETE FROM download WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM download WHERE server=:server")
    void deleteAll(String server);
}