package com.cappielloantonio.play.util;

import android.content.Context;
import android.os.Bundle;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.PlaylistSongCross;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.IndexID3;
import com.cappielloantonio.play.subsonic.models.MusicFolder;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import org.jellyfin.apiclient.interaction.Response;
import org.jellyfin.apiclient.model.dto.BaseItemDto;
import org.jellyfin.apiclient.model.playlists.PlaylistItemQuery;
import org.jellyfin.apiclient.model.querying.ItemFields;
import org.jellyfin.apiclient.model.querying.ItemsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class SyncUtil {
    private static final String TAG = "SyncUtil";

    public static final String SONG = "song";
    public static final String ALBUM = "album";
    public static final String ARTIST = "artist";

    public static void getLibraries(Context context, MediaCallback callback) {
        App.getSubsonicClientInstance(context, false)
                .getBrowsingClient()
                .getMusicFolders()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                            String errorMessage = response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage();
                            callback.onError(new Exception(errorMessage));
                        } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            boolean musicFolderFound = false;

                            for (MusicFolder folder : response.body().getMusicFolders().getMusicFolders()) {
                                if (folder.getName().equals("music")) {
                                    PreferenceUtil.getInstance(context).setMusicLibraryID(String.valueOf(folder.getId()));
                                    musicFolderFound = true;
                                }
                            }

                            if (musicFolderFound) callback.onLoadMedia(null);
                        } else {
                            callback.onError(new Exception("Empty response"));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public static void getSongs(Context context, Map<Integer, Song> currentCatalogue, MediaCallback callback, AlbumID3 album) {
        App.getSubsonicClientInstance(context, false)
                .getBrowsingClient()
                .getAlbum(album.getId())
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                            String errorMessage = response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage();
                            callback.onError(new Exception(errorMessage));
                        } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Song> songList = new ArrayList<>(MappingUtil.mapSong(response.body().getAlbum().getSongs()));
                            updateSongData(currentCatalogue, songList);
                            callback.onLoadMedia(songList);
                        } else {
                            callback.onError(new Exception("Empty response"));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public static void getAlbums(Context context, MediaCallback callback, int size, int offset) {
        App.getSubsonicClientInstance(context, false)
                .getAlbumSongListClient()
                .getAlbumList2("alphabeticalByName", size, offset)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                            String errorMessage = response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage();
                            callback.onError(new Exception(errorMessage));
                        } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<AlbumID3> albumList = new ArrayList<>();
                            albumList.addAll(response.body().getAlbumList2().getAlbums());
                            callback.onLoadMedia(albumList);
                        } else {
                            callback.onError(new Exception("Empty response"));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public static void getArtists(Context context, MediaCallback callback) {
        App.getSubsonicClientInstance(context, false)
                .getBrowsingClient()
                .getArtists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                            String errorMessage = response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage();
                            callback.onError(new Exception(errorMessage));
                        } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<ArtistID3> artistList = new ArrayList<>();

                            for (IndexID3 index : response.body().getArtists().getIndices()) {
                                artistList.addAll(index.getArtists());
                            }

                            callback.onLoadMedia(artistList);
                        } else {
                            callback.onError(new Exception("Empty response"));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public static void getPlaylists(Context context, MediaCallback callback) {
        App.getSubsonicClientInstance(context, false)
                .getPlaylistClient()
                .getPlaylists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                            String errorMessage = response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage();
                            callback.onError(new Exception(errorMessage));
                        } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<com.cappielloantonio.play.subsonic.models.Playlist> playlistList = new ArrayList<>();
                            playlistList.addAll(response.body().getPlaylists().getPlaylists());
                            callback.onLoadMedia(playlistList);
                        } else {
                            callback.onError(new Exception("Empty response"));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public static void getGenres(Context context, MediaCallback callback) {
        App.getSubsonicClientInstance(context, false)
                .getBrowsingClient()
                .getGenres()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, retrofit2.Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.FAILED)) {
                            String errorMessage = response.body().getError().getCode().getValue() + " - " + response.body().getError().getMessage();
                            callback.onError(new Exception(errorMessage));
                        } else if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<com.cappielloantonio.play.subsonic.models.Genre> genreList = new ArrayList<>();
                            genreList.addAll(response.body().getGenres().getGenres());
                            callback.onLoadMedia(genreList);
                        } else {
                            callback.onError(new Exception("Empty response"));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
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
                int itemNumber = 0;
                ArrayList<PlaylistSongCross> crosses = new ArrayList<>();

                for (BaseItemDto itemDto : result.getItems()) {
                    crosses.add(new PlaylistSongCross(playlistId, itemDto.getId(), itemNumber++));
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
        /*SimilarItemsQuery query = new SimilarItemsQuery();

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
        });*/
    }

    public static void getSimilarItems(Context context, MediaCallback callback, String resultType, String itemID, int limit) {
        /*SimilarItemsQuery query = new SimilarItemsQuery();

        query.setId(itemID);
        query.setUserId(App.getApiClientInstance(context).getCurrentUserId());
        query.setFields(new ItemFields[]{ItemFields.MediaSources});
        query.setLimit(limit);

        App.getApiClientInstance(context).GetSimilarItems(query, new Response<ItemsResult>() {
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
        });*/
    }

    public static Bundle getSyncBundle(Boolean syncAlbum, Boolean syncArtist, Boolean syncGenres, Boolean syncPlaylist, Boolean syncSong) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("sync_album", syncAlbum);
        bundle.putBoolean("sync_artist", syncArtist);
        bundle.putBoolean("sync_genres", syncGenres);
        bundle.putBoolean("sync_playlist", syncPlaylist);
        bundle.putBoolean("sync_song", syncSong);

        return bundle;
    }

    private static void updateSongData(Map<Integer, Song> library, List<Song> songs) {
        for (Song song: songs) {
            if (library.containsKey(song.hashCode())) {
                Song oldSong = library.get(song.hashCode());
                song.setFavorite(oldSong.isFavorite());
                song.setAdded(oldSong.getAdded());
                song.setLastPlay(oldSong.getLastPlay());
                song.setPlayCount(oldSong.getPlayCount());
                song.setOffline(oldSong.isOffline());
            }
        }
    }
}
