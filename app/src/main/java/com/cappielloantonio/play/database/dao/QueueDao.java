package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Queue;

import java.util.List;

@Dao
public interface QueueDao {
    @Query("SELECT * FROM queue WHERE server = :server")
    LiveData<List<Queue>> getAll(String server);

    @Query("SELECT * FROM queue WHERE server = :server")
    List<Queue> getAllSimple(String server);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Queue> songQueueObject);

    @Query("DELETE FROM queue WHERE queue.track_order = :position AND server = :server")
    void deleteByPosition(int position, String server);

    @Query("DELETE FROM queue WHERE server = :server")
    void deleteAll(String server);

    @Query("SELECT COUNT(*) FROM queue;")
    int count();
}