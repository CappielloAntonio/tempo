package com.cappielloantonio.play.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cappielloantonio.play.model.Album;

import java.util.List;

@Dao
public interface AlbumDao {
    @Query("SELECT * FROM album")
    LiveData<List<Album>> getAll();

    // @Query("SELECT * FROM album WHERE artistId = :artistId;")
    @Query("SELECT album.* FROM album INNER JOIN album_artist_cross ON album.id = album_artist_cross.album_id AND album_artist_cross.artist_id = :artistId")
    LiveData<List<Album>> getArtistAlbums(String artistId);

    @Query("SELECT * FROM album ORDER BY RANDOM() LIMIT :number;")
    LiveData<List<Album>> getSample(int number);

    @Query("SELECT * FROM album WHERE title LIKE '%' || :name || '%'")
    LiveData<List<Album>> searchAlbum(String name);

    @Query("SELECT EXISTS(SELECT * FROM album WHERE id = :id)")
    boolean exist(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Album album);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Album> albums);

    @Delete
    void delete(Album album);

    @Query("SELECT title FROM album WHERE title LIKE :query || '%' OR title like '% ' || :query || '%' GROUP BY title LIMIT :number")
    List<String> searchSuggestions(String query, int number);

    @Query("DELETE FROM album")
    void deleteAll();

    @Query("SELECT * FROM album WHERE id = :id")
    Album getAlbumByID(String id);
}