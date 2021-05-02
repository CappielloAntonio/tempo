package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentSyncBinding;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.AlbumArtistCross;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.PlaylistSongCross;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongArtistCross;
import com.cappielloantonio.play.model.SongGenreCross;
import com.cappielloantonio.play.repository.AlbumArtistRepository;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.repository.PlaylistSongRepository;
import com.cappielloantonio.play.repository.SongArtistRepository;
import com.cappielloantonio.play.repository.SongGenreRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.util.SyncUtil;
import com.cappielloantonio.play.util.Util;
import com.cappielloantonio.play.viewmodel.SyncViewModel;

import org.jellyfin.apiclient.model.dto.BaseItemDto;

import java.util.ArrayList;
import java.util.List;

public class SyncFragment extends Fragment {
    private static final String TAG = "SyncFragment";

    private MainActivity activity;
    private FragmentSyncBinding bind;
    private SyncViewModel syncViewModel;

    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private PlaylistRepository playlistRepository;
    private GenreRepository genreRepository;
    private SongGenreRepository songGenreRepository;
    private SongArtistRepository songArtistRepository;
    private AlbumArtistRepository albumArtistRepository;
    private PlaylistSongRepository playlistSongRepository;

    private final int LIBRARIES = 0;
    private final int ALBUMS = 1;
    private final int ARTISTS = 2;
    private final int GENRES = 3;
    private final int PLAYLISTS = 4;
    private final int SONGS = 5;
    private final int SONG_X_GENRE = 6;
    private final int SONG_X_PLAYLIST = 7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSyncBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        syncViewModel = new ViewModelProvider(requireActivity()).get(SyncViewModel.class);

        songRepository = new SongRepository(activity.getApplication());
        albumRepository = new AlbumRepository(activity.getApplication());
        artistRepository = new ArtistRepository(activity.getApplication());
        playlistRepository = new PlaylistRepository(activity.getApplication());
        genreRepository = new GenreRepository(activity.getApplication());
        songGenreRepository = new SongGenreRepository(activity.getApplication());
        songArtistRepository = new SongArtistRepository(activity.getApplication());
        albumArtistRepository = new AlbumArtistRepository(activity.getApplication());
        playlistSongRepository = new PlaylistSongRepository(activity.getApplication());

        init();
        initView();
        initButtonListeners();
        syncLibraries();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        syncViewModel.setArguemnts(getArguments());

        bind.syncingDateLabel.setText(Util.getDate());
    }

    private void initView() {
        if (!syncViewModel.isSyncAlbum()) bind.syncAlbumsSector.setVisibility(View.GONE);
        if (!syncViewModel.isSyncArtist()) bind.syncArtistsSector.setVisibility(View.GONE);
        if (!syncViewModel.isSyncGenres()) bind.syncGenresSector.setVisibility(View.GONE);
        if (!syncViewModel.isSyncPlaylist()) bind.syncPlaylistsSector.setVisibility(View.GONE);
        if (!syncViewModel.isSyncPlaylist()) bind.syncSongXPlaylistSector.setVisibility(View.GONE);
        if (!syncViewModel.isSyncSong()) bind.syncSongsSector.setVisibility(View.GONE);
        if (!syncViewModel.isCrossSyncSongGenre())
            bind.syncSongXGenreSector.setVisibility(View.GONE);
    }

    private void initButtonListeners() {
        bind.syncLibrariesRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(LIBRARIES);
            syncLibraries();
        });
        bind.syncAlbumsRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(ALBUMS);
            syncAlbums();
        });
        bind.syncArtistsRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(ARTISTS);
            syncArtists();
        });
        bind.syncGenresRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(GENRES);
            syncGenres();
        });
        bind.syncPlaylistsRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(PLAYLISTS);
            syncPlaylist();
        });
        bind.syncSongsRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(SONGS);
            syncSongs();
        });
        bind.syncSongXGenreRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(SONG_X_GENRE);
            syncSongsPerGenre(syncViewModel.getGenreList());
        });
        bind.syncSongXPlaylistRetryImageView.setOnClickListener(v -> {
            resetSectorInfo(SONG_X_PLAYLIST);
            syncSongsPerPlaylist(syncViewModel.getPlaylistList());
        });
        bind.syncingGoHomeImageView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Make sure each category has been properly synchronized")
                    .setTitle("Go home")
                    .setNegativeButton(R.string.ignore, null)
                    .setPositiveButton("Go", (dialog, id) -> {
                        PreferenceUtil.getInstance(requireContext()).setSync(true);
                        activity.goToHome();
                    })
                    .show();
        });
    }

    private void syncLibraries() {
        SyncUtil.getLibraries(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                loadSectorInfo(LIBRARIES, exception.getMessage(), false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                List<BaseItemDto> libraries = (List<BaseItemDto>) media;

                for (BaseItemDto itemDto : libraries) {
                    if (itemDto.getCollectionType().equals("music")) {
                        PreferenceUtil.getInstance(requireContext()).setMusicLibraryID(itemDto.getId());
                    }
                }

                loadSectorInfo(LIBRARIES, "", true);
            }
        });
    }

    private void syncAlbums() {
        SyncUtil.getAlbums(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                loadSectorInfo(ALBUMS, exception.getMessage(), false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                syncViewModel.setAlbumList((ArrayList<Album>) media);
                albumRepository.insertAll(syncViewModel.getAlbumList());
                syncAlbumArtistCross(syncViewModel.getAlbumList());
                loadSectorInfo(ALBUMS, "Found " + syncViewModel.getAlbumList().size() + " elements", true);
            }
        });
    }

    private void syncArtists() {
        SyncUtil.getArtists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                loadSectorInfo(ARTISTS, exception.getMessage(), false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                syncViewModel.setArtistList((ArrayList<Artist>) media);
                artistRepository.insertAll(syncViewModel.getArtistList());
                loadSectorInfo(ARTISTS, "Found " + syncViewModel.getArtistList().size() + " elements", true);
            }
        });
    }

    private void syncGenres() {
        SyncUtil.getGenres(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                loadSectorInfo(GENRES, exception.getMessage(), false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                syncViewModel.setGenreList((ArrayList<Genre>) media);
                songGenreRepository.deleteAll();
                genreRepository.insertAll(syncViewModel.getGenreList());
                loadSectorInfo(GENRES, "Found " + syncViewModel.getGenreList().size() + " elements", true);
            }
        });
    }

    private void syncPlaylist() {
        SyncUtil.getPlaylists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                loadSectorInfo(PLAYLISTS, exception.getMessage(), false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                syncViewModel.setPlaylistList((ArrayList<Playlist>) media);
                playlistSongRepository.deleteAll();
                playlistRepository.insertAll(syncViewModel.getPlaylistList());
                loadSectorInfo(PLAYLISTS, "Found " + syncViewModel.getPlaylistList().size() + " elements", true);
            }
        });
    }

    private void syncSongs() {
        SyncUtil.getSongs(requireContext(), syncViewModel.getCatalogue(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                loadSectorInfo(SONGS, exception.getMessage(), false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                syncViewModel.setSongList((ArrayList<Song>) media);
                songRepository.insertAll(syncViewModel.getSongList());
                syncSongArtistCross(syncViewModel.getSongList());
                syncDownloadedSong();
                loadSectorInfo(SONGS, "Found " + syncViewModel.getSongList().size() + " elements", true);
                PreferenceUtil.getInstance(requireContext()).setSongNumber(syncViewModel.getSongList().size());
            }
        });
    }

    private void syncSongsPerGenre(List<Genre> genres) {
        for (Genre genre : genres) {
            SyncUtil.getSongsPerGenre(requireContext(), new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    loadSectorInfo(SONG_X_GENRE, exception.getMessage(), false);
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    songGenreRepository.insertAll((ArrayList<SongGenreCross>) media);
                    loadSectorInfo(SONG_X_GENRE, "Genre processed: " + genre.getName(), true);
                }
            }, genre.id);
        }

        PreferenceUtil.getInstance(requireContext()).setSongGenreSync(true);
    }

    private void syncSongsPerPlaylist(List<Playlist> playlists) {
        for (Playlist playlist : playlists) {
            SyncUtil.getSongsPerPlaylist(requireContext(), new MediaCallback() {
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
        }
    }

    // ---------------------------------------------------------------------------------------------
    /*
     * Sincronizzazzione dell'album con gli artisti che lo hanno prodotto | isProduced = true
     * Sincronizzazzione dell'album con gli artisti che hanno collaborato per la sua produzione | isProduced = false
     */
    private void syncAlbumArtistCross(ArrayList<Album> albums) {
        List<AlbumArtistCross> crosses = new ArrayList<>();

        for (Album album : albums) {
            List<Artist> artists = new ArrayList<>();

            if (album.albumArtists.size() > 0) {
                for (Artist artist : album.albumArtists) {
                    artists.add(artist);
                    crosses.add(new AlbumArtistCross(album.getId(), artist.getId(), true));
                }
            }

            if (album.artistItems.size() > 0) {
                for (Artist artist : album.artistItems) {
                    if (!artists.contains(artist)) {
                        crosses.add(new AlbumArtistCross(album.getId(), artist.getId(), false));
                    }
                }
            }
        }

        albumArtistRepository.insertAll(crosses);
    }

    private void syncSongArtistCross(ArrayList<Song> songs) {
        List<SongArtistCross> crosses = new ArrayList<>();

        for (Song song : songs) {
            List<Artist> artists = new ArrayList<>();

            if (song.albumArtists.size() > 0) {
                for (Artist artist : song.albumArtists) {
                    artists.add(artist);
                    crosses.add(new SongArtistCross(song.getId(), artist.getId()));
                }
            }

            if (song.artistItems.size() > 0) {
                for (Artist artist : song.artistItems) {
                    if (!artists.contains(artist)) {
                        crosses.add(new SongArtistCross(song.getId(), artist.getId()));
                    }
                }
            }
        }

        songArtistRepository.insertAll(crosses);
    }

    private void syncDownloadedSong() {
        songRepository.getListLiveDownloadedSong().observe(requireActivity(), songs -> {
            for (Song song : songs) {
                if (!DownloadUtil.getDownloadTracker(requireContext()).isDownloaded(song)) {
                    song.setOffline(false);
                    songRepository.setOfflineStatus(song);
                }
            }
        });
    }

    private void loadSectorInfo(int sector, String message, boolean isSuccess) {
        switch (sector) {
            case LIBRARIES:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncLibrariesStatusLabel.setText("Status: SUCCESS");
                        bind.syncLibrariesDetailLabel.setVisibility(View.GONE);
                        bind.syncLibrariesRetryImageView.setVisibility(View.GONE);
                    }
                    syncNextSector(LIBRARIES);
                } else {
                    if (bind != null) {
                        bind.syncLibrariesStatusLabel.setText("Status: ERROR");
                        bind.syncLibrariesDetailLabel.setText(message);
                        bind.syncLibrariesDetailLabel.setVisibility(View.VISIBLE);
                        bind.syncLibrariesRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case ALBUMS:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncAlbumsStatusLabel.setText("Status: SUCCESS");
                        bind.syncAlbumsDetailLabel.setText(message);
                        bind.syncAlbumsRetryImageView.setVisibility(View.GONE);
                    }
                    syncViewModel.increaseProggress();
                    checkStep();
                    syncNextSector(ALBUMS);
                } else {
                    if (bind != null) {
                        bind.syncLibrariesStatusLabel.setText("Status: ERROR");
                        bind.syncAlbumsDetailLabel.setText(message);
                        bind.syncAlbumsRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case ARTISTS:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncArtistsStatusLabel.setText("Status: SUCCESS");
                        bind.syncArtistsDetailLabel.setText(message);
                        bind.syncArtistsRetryImageView.setVisibility(View.GONE);
                    }
                    syncViewModel.increaseProggress();
                    checkStep();
                    syncNextSector(ARTISTS);
                } else {
                    if (bind != null) {
                        bind.syncArtistsStatusLabel.setText("Status: ERROR");
                        bind.syncArtistsDetailLabel.setText(message);
                        bind.syncArtistsRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case GENRES:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncGenresStatusLabel.setText("Status: SUCCESS");
                        bind.syncGenresDetailLabel.setText(message);
                        bind.syncGenresRetryImageView.setVisibility(View.GONE);
                    }
                    syncViewModel.increaseProggress();
                    checkStep();
                    syncNextSector(GENRES);
                } else {
                    if (bind != null) {
                        bind.syncGenresStatusLabel.setText("Status: ERROR");
                        bind.syncGenresDetailLabel.setText(message);
                        bind.syncGenresRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case PLAYLISTS:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncPlaylistsStatusLabel.setText("Status: SUCCESS");
                        bind.syncPlaylistsDetailLabel.setText(message);
                        bind.syncPlaylistsRetryImageView.setVisibility(View.GONE);
                    }
                    syncViewModel.increaseProggress();
                    checkStep();
                    syncNextSector(PLAYLISTS);
                } else {
                    if (bind != null) {
                        bind.syncPlaylistsStatusLabel.setText("Status: ERROR");
                        bind.syncPlaylistsDetailLabel.setText(message);
                        bind.syncPlaylistsRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case SONGS:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncSongsStatusLabel.setText("Status: SUCCESS");
                        bind.syncSongsDetailLabel.setText(message);
                        bind.syncSongsRetryImageView.setVisibility(View.GONE);
                    }
                    syncViewModel.increaseProggress();
                    checkStep();
                    syncNextSector(SONGS);
                } else {
                    if (bind != null) {
                        bind.syncSongsStatusLabel.setText("Status: ERROR");
                        bind.syncSongsDetailLabel.setText(message);
                        bind.syncSongsRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case SONG_X_GENRE:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncSongXGenreStatusLabel.setText("Status: SUCCESS");
                        bind.syncSongXGenreDetailLabel.setText(message);
                        bind.syncSongXGenreRetryImageView.setVisibility(View.GONE);
                    }
                    checkStep();
                } else {
                    if (bind != null) {
                        bind.syncSongXGenreStatusLabel.setText("Status: ERROR");
                        bind.syncSongXGenreDetailLabel.setText(message);
                        bind.syncSongXGenreRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case SONG_X_PLAYLIST:
                if (isSuccess) {
                    if (bind != null) {
                        bind.syncSongXPlaylistStatusLabel.setText("Status: SUCCESS");
                        bind.syncSongXPlaylistDetailLabel.setText(message);
                        bind.syncSongXPlaylistRetryImageView.setVisibility(View.GONE);
                    }
                    checkStep();
                } else {
                    if (bind != null) {
                        bind.syncSongXPlaylistStatusLabel.setText("Status: ERROR");
                        bind.syncSongXPlaylistDetailLabel.setText(message);
                        bind.syncSongXPlaylistRetryImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    private void resetSectorInfo(int sector) {
        if (bind != null) {
            switch (sector) {
                case LIBRARIES:
                    bind.syncLibrariesStatusLabel.setText("Loading...");
                    bind.syncLibrariesDetailLabel.setText(R.string.label_placeholder);
                    bind.syncLibrariesDetailLabel.setVisibility(View.GONE);
                    bind.syncLibrariesRetryImageView.setVisibility(View.GONE);
                    break;
                case ALBUMS:
                    bind.syncAlbumsStatusLabel.setText("Loading...");
                    bind.syncAlbumsDetailLabel.setText(R.string.label_placeholder);
                    bind.syncAlbumsRetryImageView.setVisibility(View.GONE);
                    break;
                case ARTISTS:
                    bind.syncArtistsStatusLabel.setText("Loading...");
                    bind.syncArtistsDetailLabel.setText(R.string.label_placeholder);
                    bind.syncArtistsRetryImageView.setVisibility(View.GONE);
                    break;
                case GENRES:
                    bind.syncGenresStatusLabel.setText("Loading...");
                    bind.syncGenresDetailLabel.setText(R.string.label_placeholder);
                    bind.syncGenresRetryImageView.setVisibility(View.GONE);
                    break;
                case PLAYLISTS:
                    bind.syncPlaylistsStatusLabel.setText("Loading...");
                    bind.syncPlaylistsDetailLabel.setText(R.string.label_placeholder);
                    bind.syncPlaylistsRetryImageView.setVisibility(View.GONE);
                    break;
                case SONGS:
                    bind.syncSongsStatusLabel.setText("Loading...");
                    bind.syncSongsDetailLabel.setText(R.string.label_placeholder);
                    bind.syncSongsRetryImageView.setVisibility(View.GONE);
                    break;
                case SONG_X_GENRE:
                    bind.syncSongXGenreStatusLabel.setText("Loading...");
                    bind.syncSongXGenreDetailLabel.setText(R.string.label_placeholder);
                    bind.syncSongXGenreRetryImageView.setVisibility(View.GONE);
                    break;
                case SONG_X_PLAYLIST:
                    bind.syncSongXPlaylistStatusLabel.setText("Loading...");
                    bind.syncSongXPlaylistDetailLabel.setText(R.string.label_placeholder);
                    bind.syncSongXPlaylistRetryImageView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void syncNextSector(int sector) {
        switch (sector) {
            case LIBRARIES:
                if (syncViewModel.isSyncAlbum()) syncAlbums();
                else syncPlaylist();
                break;
            case ALBUMS:
                syncArtists();
                break;
            case ARTISTS:
                syncGenres();
                break;
            case GENRES:
                syncPlaylist();
                break;
            case PLAYLISTS:
                if (syncViewModel.isSyncSong()) syncSongs();
                else syncSongsPerPlaylist(syncViewModel.getPlaylistList());
                break;
            case SONGS:
                syncSongsPerGenre(syncViewModel.getGenreList());
                syncSongsPerPlaylist(syncViewModel.getPlaylistList());
                break;
            case SONG_X_GENRE | SONG_X_PLAYLIST:
                break;
        }
    }

    private void checkStep() {
        if (syncViewModel.getStep() == syncViewModel.getProgress()) {
            bind.syncingGoHomeImageView.setVisibility(View.VISIBLE);
        }
    }
}
