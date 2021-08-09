package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Server;

import java.util.List;

@Dao
public interface ServerDao {
    @Query("SELECT * FROM server")
    LiveData<List<Server>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Server server);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Server> servers);

    @Delete
    void delete(Server server);

    @Query("DELETE FROM server")
    void deleteAll();
}