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
    @Query("SELECT * FROM song JOIN queue ON song.id = queue.id")
    LiveData<List<Song>> getAll();

    @Query("SELECT * FROM song JOIN queue ON song.id = queue.id")
    List<Song> getAllSimple();

    @Query("SELECT * FROM song JOIN queue ON song.id = queue.id WHERE queue.rowid = :position")
    Song getSongByIndex(int position);

    @Query("SELECT * FROM song JOIN queue ON song.id = queue.id WHERE queue.last_played != 0 ORDER BY queue.last_played DESC LIMIT 1")
    LiveData<Song> getLastPlayedSong();

    @Query("UPDATE queue SET last_played = :timestamp WHERE queue.rowid = :position")
    void setLastPlayedSong(int position, long timestamp);

    @Query("UPDATE queue SET last_played = :timestamp WHERE queue.id = :songID")
    void setLastPlayedSong(String songID, long timestamp);

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