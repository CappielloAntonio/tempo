package com.cappielloantonio.play.util;

import android.content.Context;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;

import org.jellyfin.apiclient.interaction.Response;
import org.jellyfin.apiclient.model.dto.BaseItemDto;
import org.jellyfin.apiclient.model.querying.ArtistsQuery;
import org.jellyfin.apiclient.model.querying.ItemFields;
import org.jellyfin.apiclient.model.querying.ItemQuery;
import org.jellyfin.apiclient.model.querying.ItemsByNameQuery;
import org.jellyfin.apiclient.model.querying.ItemsResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyncUtil {
    public static BaseItemDto musicLibrary;

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

    public static void getSongs(Context context, MediaCallback callback) {
        ItemQuery query = new ItemQuery();

        query.setIncludeItemTypes(new String[]{"Audio"});
        query.setFields(new ItemFields[]{ItemFields.MediaSources});
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setRecursive(true);
        query.setParentId(musicLibrary.getId());

        App.getApiClientInstance(context).GetItemsAsync(query, new Response<ItemsResult>() {
            @Override
            public void onResponse(ItemsResult result) {
                ArrayList<Song> songs = new ArrayList<>();

                for (BaseItemDto itemDto : result.getItems()) {
                    songs.add(new Song(itemDto));
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
        query.setParentId(musicLibrary.getId());

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
        query.setParentId(musicLibrary.getId());

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
        query.setParentId(musicLibrary.getId());

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
        query.setParentId(musicLibrary.getId());

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
}
