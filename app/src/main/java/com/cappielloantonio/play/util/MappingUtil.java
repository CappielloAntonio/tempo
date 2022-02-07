package com.cappielloantonio.play.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.MimeTypes;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.PodcastChannel;
import com.cappielloantonio.play.model.Queue;
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
    public static ArrayList<Media> mapSong(List<Child> children) {
        ArrayList<Media> songs = new ArrayList();

        for (Child child : children) {
            songs.add(new Media(child));
        }

        return songs;
    }

    public static Media mapSong(Child child) {
        return new Media(child);
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

    public static ArrayList<Media> mapQueue(List<Queue> queueList) {
        ArrayList<Media> media = new ArrayList();

        for (Queue item : queueList) {
            media.add(new Media(item));
        }

        return media;
    }

    public static Queue mapMediaToQueue(Media media, int trackOrder) {
        return new Queue(trackOrder, media.getId(), media.getTitle(), media.getAlbumId(), media.getAlbumName(), media.getArtistId(), media.getArtistName(), media.getCoverArtId(), media.getDuration(), 0, 0, media.getStreamId(), media.getChannelId(), media.getPublishDate(), media.getType());
    }

    public static List<Queue> mapMediaToQueue(List<Media> media) {
        List<Queue> queue = new ArrayList<>();

        for (int counter = 0; counter < media.size(); counter++) {
            queue.add(mapMediaToQueue(media.get(counter), counter));
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

    public static ArrayList<Media> mapDownloadToSong(List<Download> downloads) {
        ArrayList<Media> songs = new ArrayList();

        for (Download download : downloads) {
            Media song = new Media(download);
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

    public static ArrayList<Download> mapDownload(List<Media> songs, String playlistId, String playlistName) {
        ArrayList<Download> downloads = new ArrayList();

        for (Media song : songs) {
            downloads.add(new Download(song, playlistId, playlistName));
        }

        return downloads;
    }

    public static Download mapDownload(Media song, String playlistId, String playlistName) {
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
    public static MediaItem mapMediaItem(Context context, Media media, boolean stream) {
        boolean isDownloaded = DownloadUtil.getDownloadTracker(context).isDownloaded(MusicUtil.getDownloadUri(media.getId()));

        Bundle bundle = new Bundle();
        bundle.putString("id", media.getId());
        bundle.putString("albumId", media.getAlbumId());
        bundle.putString("artistId", media.getArtistId());
        bundle.putString("mediaType", media.getType());

        return new MediaItem.Builder()
                .setMediaId(media.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setMediaUri(getUri(context, media, stream && !isDownloaded))
                                .setTitle(MusicUtil.getReadableString(media.getTitle()))
                                .setTrackNumber(media.getTrackNumber())
                                .setDiscNumber(media.getDiscNumber())
                                .setReleaseYear(media.getYear())
                                .setAlbumTitle(MusicUtil.getReadableString(media.getAlbumName()))
                                .setArtist(MusicUtil.getReadableString(media.getArtistName()))
                                .setExtras(bundle)
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(getUri(context, media, stream && !isDownloaded))
                .build();
    }

    private static Uri getUri(Context context, Media media, boolean stream) {
        switch (media.getType()) {
            case Media.MEDIA_TYPE_MUSIC:
                if (stream) {
                    return MusicUtil.getStreamUri(context, media.getId());
                }
                else {
                    return MusicUtil.getDownloadUri(media.getId());
                }
            case Media.MEDIA_TYPE_PODCAST:
                if(stream) {
                    return MusicUtil.getStreamUri(context, media.getStreamId());
                }
                else {
                    return MusicUtil.getDownloadUri(media.getStreamId());
                }
            default:
                return MusicUtil.getStreamUri(context, media.getId());
        }
    }

    public static ArrayList<MediaItem> mapMediaItems(Context context, List<Media> items, boolean stream) {
        ArrayList<MediaItem> mediaItems = new ArrayList();

        for (int i = 0; i < items.size(); i++) {
            mediaItems.add(mapMediaItem(context, items.get(i), stream));
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

    public static ArrayList<Media> mapPodcastEpisode(List<com.cappielloantonio.play.subsonic.models.PodcastEpisode> subsonicPodcastEpisodes) {
        ArrayList<Media> podcastEpisodes = new ArrayList();

        for (com.cappielloantonio.play.subsonic.models.PodcastEpisode subsonicPodcastEpisode : subsonicPodcastEpisodes) {
            podcastEpisodes.add(mapPodcastEpisode(subsonicPodcastEpisode));
        }

        return podcastEpisodes;
    }

    public static Media mapPodcastEpisode(com.cappielloantonio.play.subsonic.models.PodcastEpisode subsonicPodcastEpisode) {
        return new Media(subsonicPodcastEpisode);
    }
}
