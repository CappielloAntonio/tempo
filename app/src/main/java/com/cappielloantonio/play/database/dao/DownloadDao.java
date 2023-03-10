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
    LiveData<List<Download>> getAll();

    @Query("SELECT * FROM download WHERE playlist_id IS NULL GROUP BY artist LIMIT :size")
    LiveData<List<Download>> getSampleArtist(int size);

    @Query("SELECT * FROM download WHERE playlist_id IS NULL GROUP BY album LIMIT :size")
    LiveData<List<Download>> getSampleAlbum(int size);

    @Query("SELECT * FROM download WHERE playlist_id IS NOT NULL GROUP BY playlist_id LIMIT :size")
    LiveData<List<Download>> getSamplePlaylist(int size);

    @Query("SELECT * FROM download LIMIT :size")
    LiveData<List<Download>> getSample(int size);

    @Query("SELECT * FROM download WHERE artist=:artistId")
    LiveData<List<Download>> getAllFromArtist(String artistId);

    @Query("SELECT * FROM download WHERE album=:albumId ORDER BY track ASC")
    LiveData<List<Download>> getAllFromAlbum(String albumId);

    @Query("SELECT * FROM download WHERE playlist_id=:playlistId")
    LiveData<List<Download>> getAllFromPlaylist(String playlistId);

    @Query("SELECT * FROM download WHERE playlist_id IS NOT NULL GROUP BY playlist_id")
    LiveData<List<Download>> getAllPlaylists();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Download download);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Download> downloads);

    @Query("DELETE FROM download WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM download")
    void deleteAll();
}