package com.cappielloantonio.tempo.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.tempo.model.Server;

import java.util.List;

@Dao
public interface ServerDao {
    @Query("SELECT * FROM server")
    LiveData<List<Server>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Server server);

    @Delete
    void delete(Server server);
}