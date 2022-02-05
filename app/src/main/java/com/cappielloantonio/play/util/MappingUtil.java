package com.cappielloantonio.play.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.MimeTypes;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.PodcastChannel;
import com.cappielloantonio.play.model.PodcastEpisode;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.AlbumInfo;
import com.cappielloantonio.play.subsonic.models.AlbumWithSongsID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.ArtistInfo2;
import com.cappielloantonio.play.subsonic.models.ArtistWithAlbumsID3;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.Genre;
import com.cappielloantonio.play.subsonic.models.SimilarArtistID3;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {
    public static ArrayList<Song> mapSong(List<Child> children) {
        ArrayList<Song> songs = new ArrayList();

        for (Child child : children) {
            songs.add(new Song(child));
        }

        return songs;
    }

    public static Song mapSong(Child child) {
        return new Song(child);
    }

    public static ArrayList<Album> mapAlbum(List<AlbumID3> albumID3List) {
        ArrayList<Album> albums = new ArrayList();

        for (AlbumID3 albumID3 : albumID3List) {
            albums.add(new Album(albumID3));
        }

        return albums;
    }

    public static Album mapAlbum(AlbumWithSongsID3 albumWithSongsID3) {
        return new Album(albumWithSongsID3);
    }

    public static Album mapAlbum(AlbumInfo albumInfo) {
        return new Album(albumInfo);
    }

    public static ArrayList<Artist> mapArtist(List<ArtistID3> artistID3List) {
        ArrayList<Artist> artists = new ArrayList();

        for (ArtistID3 artistID3 : artistID3List) {
            artists.add(new Artist(artistID3));
        }

        return artists;
    }

    public static Artist mapArtist(ArtistInfo2 artistInfo2) {
        return new Artist(artistInfo2);
    }

    public static Artist mapArtist(ArtistWithAlbumsID3 artistWithAlbumsID3) {
        return new Artist(artistWithAlbumsID3);
    }

    public static Artist mapArtistWithAlbum(ArtistWithAlbumsID3 artistWithAlbumsID3) {
        return new Artist(artistWithAlbumsID3);
    }

    public static ArrayList<Artist> mapSimilarArtist(List<SimilarArtistID3> similarArtistID3s) {
        ArrayList<Artist> artists = new ArrayList();

        for (SimilarArtistID3 similarArtistID3 : similarArtistID3s) {
            artists.add(new Artist(similarArtistID3));
        }

        return artists;
    }

    public static ArrayList<Song> mapQueue(List<Queue> queueList) {
        ArrayList<Song> songs = new ArrayList();

        for (Queue item : queueList) {
            songs.add(new Song(item));
        }

        return songs;
    }

    public static Queue mapSongToQueue(Song song, int trackOrder) {
        return new Queue(trackOrder, song.getId(), song.getTitle(), song.getAlbumId(), song.getAlbumName(), song.getArtistId(), song.getArtistName(), song.getPrimary(), song.getDuration(), 0, 0);
    }

    public static List<Queue> mapSongsToQueue(List<Song> songs) {
        List<Queue> queue = new ArrayList<>();

        for (int counter = 0; counter < songs.size(); counter++) {
            queue.add(mapSongToQueue(songs.get(counter), counter));
        }

        return queue;
    }

    public static ArrayList<Playlist> mapPlaylist(List<com.cappielloantonio.play.subsonic.models.Playlist> playlists) {
        ArrayList<Playlist> playlist = new ArrayList();

        for (com.cappielloantonio.play.subsonic.models.Playlist item : playlists) {
            playlist.add(new Playlist(item));
        }

        return playlist;
    }

    public static ArrayList<Song> mapDownloadToSong(List<Download> downloads) {
        ArrayList<Song> songs = new ArrayList();

        for (Download download : downloads) {
            Song song = new Song(download);
            if (!songs.contains(song)) {
                songs.add(song);
            }
        }

        return songs;
    }

    public static ArrayList<Album> mapDownloadToAlbum(List<Download> downloads) {
        ArrayList<Album> albums = new ArrayList();

        for (Download download : downloads) {
            Album album = new Album(download);
            if (!albums.contains(album)) {
                albums.add(album);
            }
        }

        return albums;
    }

    public static ArrayList<Artist> mapDownloadToArtist(List<Download> downloads) {
        ArrayList<Artist> artists = new ArrayList();

        for (Download download : downloads) {
            Artist artist = new Artist(download);
            if (!artists.contains(artist)) {
                artists.add(artist);
            }
        }

        return artists;
    }

    public static ArrayList<Playlist> mapDownloadToPlaylist(List<Download> downloads) {
        ArrayList<Playlist> playlists = new ArrayList();

        for (Download download : downloads) {
            playlists.add(new Playlist(download.getPlaylistId(), download.getPlaylistName()));
        }

        return playlists;
    }

    public static ArrayList<Download> mapDownload(List<Song> songs, String playlistId, String playlistName) {
        ArrayList<Download> downloads = new ArrayList();

        for (Song song : songs) {
            downloads.add(new Download(song, playlistId, playlistName));
        }

        return downloads;
    }

    public static Download mapDownload(Song song, String playlistId, String playlistName) {
        return new Download(song, playlistId, playlistName);
    }

    public static ArrayList<com.cappielloantonio.play.model.Genre> mapGenre(List<Genre> genreList) {
        ArrayList<com.cappielloantonio.play.model.Genre> genres = new ArrayList();

        for (Genre genre : genreList) {
            genres.add(new com.cappielloantonio.play.model.Genre(genre));
        }

        return genres;
    }

    @SuppressLint("UnsafeOptInUsageError")
    public static MediaItem mapMediaItem(Context context, Song song, boolean stream) {
        boolean isDownloaded = DownloadUtil.getDownloadTracker(context).isDownloaded(MusicUtil.getDownloadUri(song.getId()));

        Bundle bundle = new Bundle();
        bundle.putString("id", song.getId());
        bundle.putString("albumId", song.getAlbumId());
        bundle.putString("artistId", song.getArtistId());

        return new MediaItem.Builder()
                .setMediaId(song.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setMediaUri(stream && !isDownloaded ? MusicUtil.getStreamUri(context, song.getId()) : MusicUtil.getDownloadUri(song.getId()))
                                .setTitle(MusicUtil.getReadableString(song.getTitle()))
                                .setTrackNumber(song.getTrackNumber())
                                .setDiscNumber(song.getDiscNumber())
                                .setReleaseYear(song.getYear())
                                .setAlbumTitle(MusicUtil.getReadableString(song.getAlbumName()))
                                .setArtist(MusicUtil.getReadableString(song.getArtistName()))
                                .setExtras(bundle)
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(stream && !isDownloaded ? MusicUtil.getStreamUri(context, song.getId()) : MusicUtil.getDownloadUri(song.getId()))
                .build();
    }

    @SuppressLint("UnsafeOptInUsageError")
    public static MediaItem mapMediaItem(Context context, PodcastEpisode podcastEpisode, boolean stream) {
        boolean isDownloaded = DownloadUtil.getDownloadTracker(context).isDownloaded(MusicUtil.getDownloadUri(podcastEpisode.getId()));

        Bundle bundle = new Bundle();
        bundle.putString("id", podcastEpisode.getId());
        bundle.putString("albumId", podcastEpisode.getAlbumId());
        bundle.putString("artistId", podcastEpisode.getArtistId());

        return new MediaItem.Builder()
                .setMediaId(podcastEpisode.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setMediaUri(stream && !isDownloaded ? MusicUtil.getStreamUri(context, podcastEpisode.getId()) : MusicUtil.getDownloadUri(podcastEpisode.getId()))
                                .setTitle(MusicUtil.getReadableString(podcastEpisode.getTitle()))
                                .setReleaseYear(podcastEpisode.getYear())
                                .setArtist(MusicUtil.getReadableString(podcastEpisode.getArtist()))
                                .setExtras(bundle)
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(stream && !isDownloaded ? MusicUtil.getStreamUri(context, podcastEpisode.getId()) : MusicUtil.getDownloadUri(podcastEpisode.getId()))
                .build();
    }

    public static ArrayList<MediaItem> mapMediaItems(Context context, List<?> items, boolean stream) {
        ArrayList<MediaItem> mediaItems = new ArrayList();

        for(int i = 0; i < items.size(); i++) {
            if(items.get(i) instanceof Song) {
                mediaItems.add(mapMediaItem(context, (Song) items.get(i), stream));
            }

            if(items.get(i) instanceof PodcastEpisode) {
                mediaItems.add(mapMediaItem(context, (PodcastEpisode) items.get(i), stream));
            }
        }

        return mediaItems;
    }

    public static ArrayList<PodcastChannel> mapPodcastChannel(List<com.cappielloantonio.play.subsonic.models.PodcastChannel> subsonicPodcastChannels) {
        ArrayList<PodcastChannel> podcastChannels = new ArrayList();

        for (com.cappielloantonio.play.subsonic.models.PodcastChannel subsonicPodcastChannel : subsonicPodcastChannels) {
            podcastChannels.add(mapPodcastChannel(subsonicPodcastChannel));
        }

        return podcastChannels;
    }

    public static PodcastChannel mapPodcastChannel(com.cappielloantonio.play.subsonic.models.PodcastChannel subsonicPodcastChannel) {
        return new PodcastChannel(subsonicPodcastChannel);
    }

    public static ArrayList<PodcastEpisode> mapPodcastEpisode(List<com.cappielloantonio.play.subsonic.models.PodcastEpisode> subsonicPodcastEpisodes) {
        ArrayList<PodcastEpisode> podcastEpisodes = new ArrayList();

        for (com.cappielloantonio.play.subsonic.models.PodcastEpisode subsonicPodcastEpisode : subsonicPodcastEpisodes) {
            podcastEpisodes.add(mapPodcastEpisode(subsonicPodcastEpisode));
        }

        return podcastEpisodes;
    }

    public static PodcastEpisode mapPodcastEpisode(com.cappielloantonio.play.subsonic.models.PodcastEpisode subsonicPodcastEpisode) {
        return new PodcastEpisode(subsonicPodcastEpisode);
    }
}
