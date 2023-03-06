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

    @Query("SELECT * FROM download WHERE server=:server AND playlist_id IS NULL GROUP BY artist LIMIT :size")
    LiveData<List<Download>> getSampleArtist(int size, String server);

    @Query("SELECT * FROM download WHERE server=:server AND playlist_id IS NULL GROUP BY album LIMIT :size")
    LiveData<List<Download>> getSampleAlbum(int size, String server);

    @Query("SELECT * FROM download WHERE server=:server AND playlist_id IS NOT NULL GROUP BY playlist_id LIMIT :size")
    LiveData<List<Download>> getSamplePlaylist(int size, String server);

    @Query("SELECT * FROM download WHERE server=:server LIMIT :size")
    LiveData<List<Download>> getSample(int size, String server);

    @Query("SELECT * FROM download WHERE server=:server AND artist=:artistId")
    LiveData<List<Download>> getAllFromArtist(String server, String artistId);

    @Query("SELECT * FROM download WHERE server=:server AND album=:albumId ORDER BY track ASC")
    LiveData<List<Download>> getAllFromAlbum(String server, String albumId);

    @Query("SELECT * FROM download WHERE server=:server AND playlist_id=:playlistId")
    LiveData<List<Download>> getAllFromPlaylist(String server, String playlistId);

    @Query("SELECT * FROM download WHERE server=:server AND playlist_id IS NOT NULL GROUP BY playlist_id")
    LiveData<List<Download>> getAllPlaylists(String server);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Download download);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Download> downloads);

    @Query("DELETE FROM download WHERE media_id = :mediaId")
    void delete(String mediaId);

    @Query("DELETE FROM download WHERE server=:server")
    void deleteAll(String server);
}