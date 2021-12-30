package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Server;

import java.util.List;

@Dao
public interface QueueDao {
    @Query("SELECT * FROM queue")
    LiveData<List<Queue>> getAll();

    @Query("SELECT * FROM queue")
    List<Queue> getAllSimple();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Queue songQueueObject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Queue> songQueueObjects);

    @Query("DELETE FROM queue WHERE queue.track_order=:position")
    void deleteByPosition(int position);

    @Query("DELETE FROM queue")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM queue")
    int count();

    @Query("UPDATE queue SET last_play=:timestamp WHERE id=:id")
    void setLastPlay(String id, long timestamp);
}