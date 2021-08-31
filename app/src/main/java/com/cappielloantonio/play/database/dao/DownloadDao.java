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
    @Query("SELECT * FROM download WHERE server=:server")
    LiveData<List<Download>> getAll(String server);

    @Query("SELECT * FROM download WHERE server=:server GROUP BY artistName LIMIT :size")
    LiveData<List<Download>> getSampleArtist(int size, String server);

    @Query("SELECT * FROM download WHERE server=:server GROUP BY albumName LIMIT :size")
    LiveData<List<Download>> getSampleAlbum(int size, String server);

    @Query("SELECT * FROM download WHERE server=:server LIMIT :size")
    LiveData<List<Download>> getSample(int size, String server);

    @Query("SELECT * FROM download WHERE server=:server AND artistId=:artistId")
    LiveData<List<Download>> getAllFromArtist(String server, String artistId);

    @Query("SELECT * FROM download WHERE server=:server AND albumId=:albumId")
    LiveData<List<Download>> getAllFromAlbum(String server, String albumId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Download download);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Download> downloads);

    @Query("DELETE FROM download WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM download WHERE server=:server")
    void deleteAll(String server);
}