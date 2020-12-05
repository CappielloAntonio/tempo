package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;

import java.util.List;

@Dao
public interface QueueDao {
    @Query("SELECT * FROM song JOIN queue ON song.id = queue.song_id")
    LiveData<List<Song>> getAll();

    @Query("SELECT * FROM song JOIN queue ON song.id = queue.song_id")
    List<Song> getAllSimple();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Queue songQueueObject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Queue> songQueueObject);

    @Delete
    void delete(Queue songQueueObject);

    @Query("DELETE FROM queue")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM queue;")
    int count();
}