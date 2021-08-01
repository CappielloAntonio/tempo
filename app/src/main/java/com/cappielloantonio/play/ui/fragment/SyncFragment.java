package com.cappielloantonio.play.ui.fragment;

import android.content.Context;

import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.util.SyncUtil;

import java.util.List;

public class SyncFragment {
    private Context context;

    private void syncLibraries() {
        SyncUtil.getLibraries(context, new MediaCallback() {
            @Override
            public void onError(Exception exception) {
            }

            @Override
            public void onLoadMedia(List<?> media) {
            }
        });
    }

    private void syncAlbums(int size, int offset, int page) {
        SyncUtil.getAlbums(context, new MediaCallback() {
            @Override
            public void onError(Exception exception) {
            }

            @Override
            public void onLoadMedia(List<?> media) {
                // syncViewModel.addToAlbumList((List<AlbumID3>) media);

                if (media.size() == size) {
                    syncAlbums(size, size * (page + 1), page + 1);
                } else {
                }
            }
        }, size, offset);
    }

    private void syncArtists() {
        SyncUtil.getArtists(context, new MediaCallback() {
            @Override
            public void onError(Exception exception) {
            }

            @Override
            public void onLoadMedia(List<?> media) {
                // syncViewModel.setArtistList((ArrayList<ArtistID3>) media);
            }
        });
    }

    private void syncGenres() {
        SyncUtil.getGenres(context, new MediaCallback() {
            @Override
            public void onError(Exception exception) {
            }

            @Override
            public void onLoadMedia(List<?> media) {
                // syncViewModel.setGenreList((ArrayList<Genre>) media);
            }
        });
    }

    private void syncPlaylist() {
        SyncUtil.getPlaylists(context, new MediaCallback() {
            @Override
            public void onError(Exception exception) {
            }

            @Override
            public void onLoadMedia(List<?> media) {
                // syncViewModel.setPlaylistList((ArrayList<Playlist>) media);
            }
        });
    }

    private void syncSongsPerPlaylist(List<Playlist> playlists) {
        /* for (Playlist playlist : playlists) {
            SyncUtil.getSongsPerPlaylist(context(), new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    loadSectorInfo(SONG_X_PLAYLIST, exception.getMessage(), false);
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    playlistSongRepository.insertAll((ArrayList<PlaylistSongCross>) media);
                    loadSectorInfo(SONG_X_PLAYLIST, "Playlist processed: " + playlist.getName(), true);
                }
            }, playlist.getId());
        } */
    }

    private void syncSongs() {
        /* for (AlbumID3 album : syncViewModel.getAlbumList()) {
            SyncUtil.getSongs(context, syncViewModel.getCatalogue(), new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    syncViewModel.addToSongList((ArrayList<Song>) media);
                }
            }, album);
        }*/
    }

    private void syncDownloadedSong() {
        /*songRepository.getListLiveDownloadedSong().observe(requireActivity(), songs -> {
            for (Song song : songs) {
                if (!DownloadUtil.getDownloadTracker(context()).isDownloaded(song)) {
                    song.setOffline(false);
                    songRepository.setOfflineStatus(song);
                }
            }
        });*/
    }
}
