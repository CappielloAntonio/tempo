package com.cappielloantonio.play.util;

import android.content.Context;
import android.os.Bundle;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.PlaylistSongCross;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongGenreCross;

import org.jellyfin.apiclient.interaction.Response;
import org.jellyfin.apiclient.model.dto.BaseItemDto;
import org.jellyfin.apiclient.model.dto.BaseItemType;
import org.jellyfin.apiclient.model.playlists.PlaylistItemQuery;
import org.jellyfin.apiclient.model.querying.ArtistsQuery;
import org.jellyfin.apiclient.model.querying.ItemFields;
import org.jellyfin.apiclient.model.querying.ItemQuery;
import org.jellyfin.apiclient.model.querying.ItemsByNameQuery;
import org.jellyfin.apiclient.model.querying.ItemsResult;
import org.jellyfin.apiclient.model.querying.SimilarItemsQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SyncUtil {
    public static final String SONG = "song";
    public static final String ALBUM = "album";
    public static final String ARTIST = "artist";
    private static final String TAG = "SyncUtil";

    public static void getLibraries(Context context, MediaCallback callback) {
        String id = App.getApiClientInstance(context).getCurrentUserId();

        App.getApiClientInstance(context).GetUserViews(id, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                List<BaseItemDto> libraries = new ArrayList<>();
                libraries.addAll(Arrays.asList(result.getItems()));

                callback.onLoadMedia(libraries);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void getSongs(Context context, Map<Integer, Song> currentCatalogue, MediaCallback callback) {
        ItemQuery query = new ItemQuery();

        query.setIncludeItemTypes(new String[]{"Audio"});
        query.setFields(new ItemFields[]{ItemFields.MediaSources});
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setRecursive(true);
        query.setParentId(PreferenceUtil.getInstance(context).getMusicLibraryID());

        App.getApiClientInstance(context).GetItemsAsync(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                ArrayList<Song> songs = new ArrayList<>();

                for (BaseItemDto itemDto : result.getItems()) {
                    songs.add(updateSongData(currentCatalogue, new Song(itemDto)));
                }

                callback.onLoadMedia(songs);
            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }

    public static void getAlbums(Context context, MediaCallback callback) {
        ItemQuery query = new ItemQuery();

        query.setIncludeItemTypes(new String[]{"MusicAlbum"});
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setRecursive(true);
        query.setParentId(PreferenceUtil.getInstance(context).getMusicLibraryID());

        App.getApiClientInstance(context).GetItemsAsync(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                List<Album> albums = new ArrayList<>();
                for (BaseItemDto itemDto : result.getItems()) {
                    albums.add(new Album(itemDto));
                }

                callback.onLoadMedia(albums);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void getArtists(Context context, MediaCallback callback) {
        ArtistsQuery query = new ArtistsQuery();

        query.setFields(new ItemFields[]{ItemFields.Genres});
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setRecursive(true);
        query.setParentId(PreferenceUtil.getInstance(context).getMusicLibraryID());

        App.getApiClientInstance(context).GetAlbumArtistsAsync(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                List<Artist> artists = new ArrayList<>();
                for (BaseItemDto itemDto : result.getItems()) {
                    artists.add(new Artist(itemDto));
                }

                callback.onLoadMedia(artists);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void getPlaylists(Context context, MediaCallback callback) {
        ItemQuery query = new ItemQuery();

        query.setIncludeItemTypes(new String[]{"Playlist"});
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setRecursive(true);
        query.setParentId(PreferenceUtil.getInstance(context).getMusicLibraryID());

        App.getApiClientInstance(context).GetItemsAsync(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                List<Playlist> playlists = new ArrayList<>();
                for (BaseItemDto itemDto : result.getItems()) {
                    playlists.add(new Playlist(itemDto));
                }

                callback.onLoadMedia(playlists);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void getGenres(Context context, MediaCallback callback) {
        ItemsByNameQuery query = new ItemsByNameQuery();

        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setRecursive(true);
        query.setParentId(PreferenceUtil.getInstance(context).getMusicLibraryID());

        App.getApiClientInstance(context).GetGenresAsync(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                List<Genre> genres = new ArrayList<>();
                for (BaseItemDto itemDto : result.getItems()) {
                    genres.add(new Genre(itemDto));
                }

                callback.onLoadMedia(genres);
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void getSongsPerGenre(Context context, MediaCallback callback, String genreId) {
        ItemQuery query = new ItemQuery();

        query.setIncludeItemTypes(new String[]{"Audio"});
        query.setFields(new ItemFields[]{ItemFields.MediaSources});
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setRecursive(true);
        query.setParentId(PreferenceUtil.getInstance(context).getMusicLibraryID());
        query.setGenreIds(new String[]{genreId});

        App.getApiClientInstance(context).GetItemsAsync(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                ArrayList<SongGenreCross> crosses = new ArrayList<>();

                for (BaseItemDto itemDto : result.getItems()) {
                    crosses.add(new SongGenreCross(itemDto.getId(), genreId));
                }

                callback.onLoadMedia(crosses);
            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }

    public static void getSongsPerPlaylist(Context context, MediaCallback callback, String playlistId) {
        PlaylistItemQuery query = new PlaylistItemQuery();

        query.setId(playlistId);
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setFields(new ItemFields[]{ItemFields.MediaSources});


        App.getApiClientInstance(context).GetPlaylistItems(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                ArrayList<PlaylistSongCross> crosses = new ArrayList<>();

                for (BaseItemDto itemDto : result.getItems()) {
                    crosses.add(new PlaylistSongCross(playlistId, itemDto.getId()));
                }

                callback.onLoadMedia(crosses);
            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }

    public static void getInstantMix(Context context, MediaCallback callback, String resultType, String itemID, int limit) {
        SimilarItemsQuery query = new SimilarItemsQuery();

        query.setId(itemID);
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setFields(new ItemFields[]{ItemFields.MediaSources});
        query.setLimit(limit);

        App.getApiClientInstance(context).GetInstantMixFromItem(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                List<Object> items = new ArrayList<>();

                for (BaseItemDto itemDto : result.getItems()) {
                    if (resultType.equals(ARTIST) && itemDto.getBaseItemType() == BaseItemType.MusicArtist) {
                        items.add(new Artist(itemDto));
                    } else if (resultType.equals(ALBUM) && itemDto.getBaseItemType() == BaseItemType.MusicAlbum) {
                        items.add(new Album(itemDto));
                    } else if (resultType.equals(SONG) && itemDto.getBaseItemType() == BaseItemType.Audio) {
                        items.add(new Song(itemDto));
                    }
                }

                callback.onLoadMedia(items);
            }

            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }
        });
    }

    public static Bundle getSyncBundle(Boolean syncAlbum, Boolean syncArtist, Boolean syncGenres, Boolean syncPlaylist, Boolean syncSong, Boolean crossSyncSongGenre) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("sync_album", syncAlbum);
        bundle.putBoolean("sync_artist", syncArtist);
        bundle.putBoolean("sync_genres", syncGenres);
        bundle.putBoolean("sync_playlist", syncPlaylist);
        bundle.putBoolean("sync_song", syncSong);
        bundle.putBoolean("cross_sync_song_genre", crossSyncSongGenre);

        return bundle;
    }

    private static Song updateSongData(Map<Integer, Song> library, Song newSong) {
        if (library.containsKey(newSong.hashCode())) {
            Song oldSong = library.get(newSong.hashCode());
            newSong.setFavorite(oldSong.isFavorite());
            newSong.setAdded(oldSong.getAdded());
            newSong.setLastPlay(oldSong.getLastPlay());
            newSong.setPlayCount(oldSong.getPlayCount());
            newSong.setOffline(oldSong.isOffline());
        }

        return newSong;
    }
}
