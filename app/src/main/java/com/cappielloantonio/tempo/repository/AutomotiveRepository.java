package com.cappielloantonio.tempo.repository;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.session.LibraryResult;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.database.AppDatabase;
import com.cappielloantonio.tempo.database.dao.SessionMediaItemDao;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.model.SessionMediaItem;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.Artist;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.Directory;
import com.cappielloantonio.tempo.subsonic.models.Index;
import com.cappielloantonio.tempo.subsonic.models.InternetRadioStation;
import com.cappielloantonio.tempo.subsonic.models.MusicFolder;
import com.cappielloantonio.tempo.subsonic.models.Playlist;
import com.cappielloantonio.tempo.subsonic.models.PodcastEpisode;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutomotiveRepository {
    private final SessionMediaItemDao sessionMediaItemDao = AppDatabase.getInstance().sessionMediaItemDao();

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getAlbums(String prefix, String type, int size) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getAlbumList2(type, size, 0, null, null)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbumList2() != null && response.body().getSubsonicResponse().getAlbumList2().getAlbums() != null) {
                            List<AlbumID3> albums = response.body().getSubsonicResponse().getAlbumList2().getAlbums();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (AlbumID3 album : albums) {
                                Uri artworkUri = Uri.parse(CustomGlideRequest.createUrl(album.getCoverArtId(), Preferences.getImageSize()));

                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(album.getName())
                                        .setAlbumTitle(album.getName())
                                        .setArtist(album.getArtist())
                                        .setGenre(album.getGenre())
                                        .setIsBrowsable(true)
                                        .setIsPlayable(false)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_ALBUM)
                                        .setArtworkUri(artworkUri)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(prefix + album.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri("")
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getStarredSongs() {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getStarred2() != null && response.body().getSubsonicResponse().getStarred2().getSongs() != null) {
                            List<Child> songs = response.body().getSubsonicResponse().getStarred2().getSongs();

                            setChildrenMetadata(songs);

                            List<MediaItem> mediaItems = MappingUtil.mapMediaItems(songs);

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getStarredAlbums(String prefix) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getStarred2() != null && response.body().getSubsonicResponse().getStarred2().getAlbums() != null) {
                            List<AlbumID3> albums = response.body().getSubsonicResponse().getStarred2().getAlbums();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (AlbumID3 album : albums) {
                                Uri artworkUri = Uri.parse(CustomGlideRequest.createUrl(album.getCoverArtId(), Preferences.getImageSize()));

                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(album.getName())
                                        .setArtist(album.getArtist())
                                        .setGenre(album.getGenre())
                                        .setIsBrowsable(true)
                                        .setIsPlayable(false)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_ALBUM)
                                        .setArtworkUri(artworkUri)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(prefix + album.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri("")
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getStarredArtists(String prefix, boolean playFromThere) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getStarred2() != null && response.body().getSubsonicResponse().getStarred2().getArtists() != null) {
                            List<ArtistID3> artists = response.body().getSubsonicResponse().getStarred2().getArtists();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (ArtistID3 artist : artists) {
                                Uri artworkUri = Uri.parse(CustomGlideRequest.createUrl(artist.getCoverArtId(), Preferences.getImageSize()));

                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(artist.getName())
                                        .setIsBrowsable(!playFromThere)
                                        .setIsPlayable(playFromThere)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_PLAYLIST)
                                        .setArtworkUri(artworkUri)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(prefix + artist.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri("")
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getMusicFolders(String prefix) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getMusicFolders()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getMusicFolders() != null && response.body().getSubsonicResponse().getMusicFolders().getMusicFolders() != null) {
                            List<MusicFolder> musicFolders = response.body().getSubsonicResponse().getMusicFolders().getMusicFolders();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (MusicFolder musicFolder : musicFolders) {
                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(musicFolder.getName())
                                        .setIsBrowsable(true)
                                        .setIsPlayable(false)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(prefix + musicFolder.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri("")
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getIndexes(String prefix, String id) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getIndexes(id, null)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getIndexes() != null) {
                            List<MediaItem> mediaItems = new ArrayList<>();

                            if (response.body().getSubsonicResponse().getIndexes().getIndices() != null) {
                                List<Index> indices = response.body().getSubsonicResponse().getIndexes().getIndices();

                                for (Index index : indices) {
                                    if (index.getArtists() != null) {
                                        for (Artist artist : index.getArtists()) {
                                            MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                                    .setTitle(artist.getName())
                                                    .setIsBrowsable(true)
                                                    .setIsPlayable(false)
                                                    .setMediaType(MediaMetadata.MEDIA_TYPE_ARTIST)
                                                    .build();

                                            MediaItem mediaItem = new MediaItem.Builder()
                                                    .setMediaId(prefix + artist.getId())
                                                    .setMediaMetadata(mediaMetadata)
                                                    .setUri("")
                                                    .build();

                                            mediaItems.add(mediaItem);
                                        }
                                    }
                                }
                            }

                            if (response.body().getSubsonicResponse().getIndexes().getChildren() != null) {
                                List<Child> children = response.body().getSubsonicResponse().getIndexes().getChildren();

                                for (Child song : children) {
                                    Uri artworkUri = Uri.parse(CustomGlideRequest.createUrl(song.getCoverArtId(), Preferences.getImageSize()));

                                    MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                            .setTitle(song.getTitle())
                                            .setAlbumTitle(song.getAlbum())
                                            .setArtist(song.getArtist())
                                            .setIsBrowsable(false)
                                            .setIsPlayable(true)
                                            .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                                            .setArtworkUri(artworkUri)
                                            .build();

                                    MediaItem mediaItem = new MediaItem.Builder()
                                            .setMediaId(prefix + song.getId())
                                            .setMediaMetadata(mediaMetadata)
                                            .setUri(MusicUtil.getStreamUri(song.getId()))
                                            .build();

                                    mediaItems.add(mediaItem);
                                }

                                setChildrenMetadata(children);
                            }

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getDirectories(String prefix, String id) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getMusicDirectory(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getDirectory() != null && response.body().getSubsonicResponse().getDirectory().getChildren() != null) {
                            Directory directory = response.body().getSubsonicResponse().getDirectory();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (Child child : directory.getChildren()) {
                                Uri artworkUri = Uri.parse(CustomGlideRequest.createUrl(child.getCoverArtId(), Preferences.getImageSize()));

                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(child.getTitle())
                                        .setIsBrowsable(child.isDir())
                                        .setIsPlayable(!child.isDir())
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                                        .setArtworkUri(artworkUri)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(child.isDir() ? prefix + child.getId() : child.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri(!child.isDir() ? MusicUtil.getStreamUri(child.getId()) : Uri.parse(""))
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            setChildrenMetadata(directory.getChildren().stream().filter(child -> !child.isDir()).collect(Collectors.toList()));

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getPlaylists(String prefix) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getPlaylistClient()
                .getPlaylists()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getPlaylists() != null && response.body().getSubsonicResponse().getPlaylists().getPlaylists() != null) {
                            List<Playlist> playlists = response.body().getSubsonicResponse().getPlaylists().getPlaylists();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (Playlist playlist : playlists) {
                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(playlist.getName())
                                        .setIsBrowsable(true)
                                        .setIsPlayable(false)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_PLAYLIST)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(prefix + playlist.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri("")
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getNewestPodcastEpisodes(String prefix, int count) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getPodcastClient()
                .getNewestPodcasts(count)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getNewestPodcasts() != null && response.body().getSubsonicResponse().getNewestPodcasts().getEpisodes() != null) {
                            List<PodcastEpisode> episodes = response.body().getSubsonicResponse().getNewestPodcasts().getEpisodes();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (PodcastEpisode episode : episodes) {
                                Uri artworkUri = Uri.parse(CustomGlideRequest.createUrl(episode.getCoverArtId(), Preferences.getImageSize()));

                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(episode.getTitle())
                                        .setIsBrowsable(false)
                                        .setIsPlayable(true)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_PODCAST_EPISODE)
                                        .setArtworkUri(artworkUri)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(prefix + episode.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri(MusicUtil.getStreamUri(episode.getStreamId()))
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            setPodcastEpisodesMetadata(episodes);

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getInternetRadioStations(String prefix) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getInternetRadioClient()
                .getInternetRadioStations()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getInternetRadioStations() != null && response.body().getSubsonicResponse().getInternetRadioStations().getInternetRadioStations() != null) {

                            List<InternetRadioStation> radioStations = response.body().getSubsonicResponse().getInternetRadioStations().getInternetRadioStations();

                            List<MediaItem> mediaItems = new ArrayList<>();

                            for (InternetRadioStation radioStation : radioStations) {
                                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                        .setTitle(radioStation.getName())
                                        .setIsBrowsable(false)
                                        .setIsPlayable(true)
                                        .setMediaType(MediaMetadata.MEDIA_TYPE_RADIO_STATION)
                                        .build();

                                MediaItem mediaItem = new MediaItem.Builder()
                                        .setMediaId(prefix + radioStation.getId())
                                        .setMediaMetadata(mediaMetadata)
                                        .setUri(radioStation.getStreamUrl())
                                        .build();

                                mediaItems.add(mediaItem);
                            }

                            setInternetRadioStationsMetadata(radioStations);

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getAlbumTracks(String id) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getAlbum(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbum() != null && response.body().getSubsonicResponse().getAlbum().getSongs() != null) {

                            List<Child> tracks = response.body().getSubsonicResponse().getAlbum().getSongs();

                            setChildrenMetadata(tracks);

                            List<MediaItem> mediaItems = MappingUtil.mapMediaItems(tracks);

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        } else {
                            listenableFuture.set(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> getPlaylistSongs(String id) {
        final SettableFuture<LibraryResult<ImmutableList<MediaItem>>> listenableFuture = SettableFuture.create();

        App.getSubsonicClientInstance(false)
                .getPlaylistClient()
                .getPlaylist(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getPlaylist() != null && response.body().getSubsonicResponse().getPlaylist().getEntries() != null) {
                            List<Child> tracks = response.body().getSubsonicResponse().getPlaylist().getEntries();

                            setChildrenMetadata(tracks);

                            List<MediaItem> mediaItems = MappingUtil.mapMediaItems(tracks);

                            LibraryResult<ImmutableList<MediaItem>> libraryResult = LibraryResult.ofItemList(ImmutableList.copyOf(mediaItems), null);

                            listenableFuture.set(libraryResult);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        listenableFuture.setException(t);
                    }
                });

        return listenableFuture;
    }

    public void setChildrenMetadata(List<Child> children) {
        long timestamp = System.currentTimeMillis();
        ArrayList<SessionMediaItem> sessionMediaItems = new ArrayList<>();

        for (Child child : children) {
            SessionMediaItem sessionMediaItem = new SessionMediaItem(child);
            sessionMediaItem.setTimestamp(timestamp);
            sessionMediaItems.add(sessionMediaItem);
        }

        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(sessionMediaItemDao, sessionMediaItems);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public void setPodcastEpisodesMetadata(List<PodcastEpisode> podcastEpisodes) {
        long timestamp = System.currentTimeMillis();
        ArrayList<SessionMediaItem> sessionMediaItems = new ArrayList<>();

        for (PodcastEpisode podcastEpisode : podcastEpisodes) {
            SessionMediaItem sessionMediaItem = new SessionMediaItem(podcastEpisode);
            sessionMediaItem.setTimestamp(timestamp);
            sessionMediaItems.add(sessionMediaItem);
        }

        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(sessionMediaItemDao, sessionMediaItems);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public void setInternetRadioStationsMetadata(List<InternetRadioStation> internetRadioStations) {
        long timestamp = System.currentTimeMillis();
        ArrayList<SessionMediaItem> sessionMediaItems = new ArrayList<>();

        for (InternetRadioStation internetRadioStation : internetRadioStations) {
            SessionMediaItem sessionMediaItem = new SessionMediaItem(internetRadioStation);
            sessionMediaItem.setTimestamp(timestamp);
            sessionMediaItems.add(sessionMediaItem);
        }

        InsertAllThreadSafe insertAll = new InsertAllThreadSafe(sessionMediaItemDao, sessionMediaItems);
        Thread thread = new Thread(insertAll);
        thread.start();
    }

    public SessionMediaItem getSessionMediaItem(String id) {
        SessionMediaItem sessionMediaItem = null;

        GetMediaItemThreadSafe getMediaItemThreadSafe = new GetMediaItemThreadSafe(sessionMediaItemDao, id);
        Thread thread = new Thread(getMediaItemThreadSafe);
        thread.start();

        try {
            thread.join();
            sessionMediaItem = getMediaItemThreadSafe.getSessionMediaItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return sessionMediaItem;
    }

    public List<MediaItem> getMetadatas(long timestamp) {
        List<MediaItem> mediaItems = Collections.emptyList();

        GetMediaItemsThreadSafe getMediaItemsThreadSafe = new GetMediaItemsThreadSafe(sessionMediaItemDao, timestamp);
        Thread thread = new Thread(getMediaItemsThreadSafe);
        thread.start();

        try {
            thread.join();
            mediaItems = getMediaItemsThreadSafe.getMediaItems();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mediaItems;
    }

    public void deleteMetadata() {
        DeleteAllThreadSafe delete = new DeleteAllThreadSafe(sessionMediaItemDao);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class GetMediaItemThreadSafe implements Runnable {
        private final SessionMediaItemDao sessionMediaItemDao;
        private final String id;

        private SessionMediaItem sessionMediaItem;

        public GetMediaItemThreadSafe(SessionMediaItemDao sessionMediaItemDao, String id) {
            this.sessionMediaItemDao = sessionMediaItemDao;
            this.id = id;
        }

        @Override
        public void run() {
            sessionMediaItem = sessionMediaItemDao.get(id);
        }

        public SessionMediaItem getSessionMediaItem() {
            return sessionMediaItem;
        }
    }

    private static class GetMediaItemsThreadSafe implements Runnable {
        private final SessionMediaItemDao sessionMediaItemDao;
        private final Long timestamp;

        private List<MediaItem> mediaItems = new ArrayList<>();

        public GetMediaItemsThreadSafe(SessionMediaItemDao sessionMediaItemDao, Long timestamp) {
            this.sessionMediaItemDao = sessionMediaItemDao;
            this.timestamp = timestamp;
        }

        @Override
        public void run() {
            List<SessionMediaItem> sessionMediaItems = sessionMediaItemDao.get(timestamp);
            sessionMediaItems.forEach(sessionMediaItem -> mediaItems.add(sessionMediaItem.getMediaItem()));
        }

        public List<MediaItem> getMediaItems() {
            return mediaItems;
        }
    }

    /* private static class InsertThreadSafe implements Runnable {
        private final SessionMediaItemDao sessionMediaItemDao;
        private final List<Child> children;
        private final List<PodcastEpisode> podcastEpisodes;
        private final List<InternetRadioStation> internetRadioStations;
        private final long timestamp;

        public InsertThreadSafe(SessionMediaItemDao sessionMediaItemDao, List<Child> children, List<PodcastEpisode> podcastEpisodes, List<InternetRadioStation> internetRadioStations, long timestamp) {
            this.sessionMediaItemDao = sessionMediaItemDao;
            this.children = children;
            this.podcastEpisodes = podcastEpisodes;
            this.internetRadioStations = internetRadioStations;
            this.timestamp = timestamp;
        }

        @Override
        public void run() {
            if (children != null) {
                SessionMediaItem sessionMediaItem = new SessionMediaItem(children);
                sessionMediaItem.setTimestamp(timestamp);
                sessionMediaItemDao.insert(sessionMediaItem);
            }

            if (podcastEpisodes != null) {
                SessionMediaItem sessionMediaItem = new SessionMediaItem(podcastEpisodes);
                sessionMediaItem.setTimestamp(timestamp);
                sessionMediaItemDao.insert(sessionMediaItem);
            }

            if (internetRadioStations != null) {
                SessionMediaItem sessionMediaItem = new SessionMediaItem(internetRadioStations);
                sessionMediaItem.setTimestamp(timestamp);
                sessionMediaItemDao.insert(sessionMediaItem);
            }
        }
    } */

    private static class InsertAllThreadSafe implements Runnable {
        private final SessionMediaItemDao sessionMediaItemDao;
        private final List<SessionMediaItem> sessionMediaItems;

        public InsertAllThreadSafe(SessionMediaItemDao sessionMediaItemDao, List<SessionMediaItem> sessionMediaItems) {
            this.sessionMediaItemDao = sessionMediaItemDao;
            this.sessionMediaItems = sessionMediaItems;
        }

        @Override
        public void run() {
            sessionMediaItemDao.insertAll(sessionMediaItems);
        }
    }

    private static class DeleteAllThreadSafe implements Runnable {
        private final SessionMediaItemDao sessionMediaItemDao;

        public DeleteAllThreadSafe(SessionMediaItemDao sessionMediaItemDao) {
            this.sessionMediaItemDao = sessionMediaItemDao;
        }

        @Override
        public void run() {
            sessionMediaItemDao.deleteAll();
        }
    }
}
