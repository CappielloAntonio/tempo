package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.App;
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
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.util.SyncUtil;
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
    private SongArtistRepository songArtistRepository;
    private AlbumArtistRepository albumArtistRepository;
    private PlaylistSongRepository playlistSongRepository;

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
        songArtistRepository = new SongArtistRepository(activity.getApplication());
        albumArtistRepository = new AlbumArtistRepository(activity.getApplication());
        playlistSongRepository = new PlaylistSongRepository(activity.getApplication());

        init();
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
        bind.loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void syncLibraries() {
        SyncUtil.getLibraries(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
            }

            @Override
            public void onLoadMedia(List<?> media) {
                List<BaseItemDto> libraries = (List<BaseItemDto>) media;

                for (BaseItemDto itemDto : libraries) {
                    if (itemDto.getCollectionType().equals("music"))
                        PreferenceUtil.getInstance(requireContext()).setMusicLibraryID(itemDto.getId());
                }

                startSyncing();
            }
        });
    }

    private void startSyncing() {
        if(syncViewModel.isSyncAlbum()) syncAlbums();
        if(syncViewModel.isSyncArtist()) syncArtists();
        if(syncViewModel.isSyncGenres()) syncGenres();
        if(syncViewModel.isSyncPlaylist()) syncPlaylist();
        if(syncViewModel.isSyncSong()) syncSongs();
    }

    private void syncAlbums() {
        SyncUtil.getAlbums(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                animateProgressBar(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                albumRepository.insertAll((ArrayList<Album>) media);
                syncAlbumArtistCross((ArrayList<Album>) media);
                animateProgressBar(true);
            }
        });
    }

    private void syncArtists() {
        SyncUtil.getArtists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                animateProgressBar(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                artistRepository.insertAll((ArrayList<Artist>) media);
                animateProgressBar(true);
            }
        });
    }

    private void syncGenres() {
        SyncUtil.getGenres(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                animateProgressBar(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                genreRepository.insertAll((ArrayList<Genre>) media);
                animateProgressBar(true);

                if(syncViewModel.isCrossSyncSongGenre()) syncSongsPerGenre((ArrayList<Genre>) media);

            }
        });
    }

    private void syncPlaylist() {
        SyncUtil.getPlaylists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                animateProgressBar(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                playlistRepository.insertAll((ArrayList<Playlist>) media);
                syncSongsPerPlaylist((ArrayList<Playlist>) media);
                animateProgressBar(true);
            }
        });
    }

    private void syncSongs() {
        SyncUtil.getSongs(requireContext(), syncViewModel.getCatalogue(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                animateProgressBar(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                songRepository.deleteAllSongGenreCross();
                songRepository.insertAll((ArrayList<Song>) media);
                syncSongArtistCross((ArrayList<Song>) media);
                PreferenceUtil.getInstance(requireContext()).setSongNumber(media.size());
                animateProgressBar(true);
            }
        });
    }

    private void syncSongsPerGenre(List<Genre> genres) {
        for (Genre genre : genres) {
            SyncUtil.getSongsPerGenre(requireContext(), new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError: " + exception.getMessage());
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    songRepository.insertSongPerGenre((ArrayList<SongGenreCross>) media);
                }
            }, genre.id);
        }

        animateProgressBar(true);
        PreferenceUtil.getInstance(requireContext()).setSongGenreSync(true);
    }

    private void syncSongsPerPlaylist(List<Playlist> playlists) {
        for (Playlist playlist : playlists) {
            SyncUtil.getSongsPerPlaylist(requireContext(), new MediaCallback() {
                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError: " + exception.getMessage());
                }

                @Override
                public void onLoadMedia(List<?> media) {
                    playlistSongRepository.insertAll((ArrayList<PlaylistSongCross>) media);
                }
            }, playlist.getId());
        }
    }


    private void animateProgressBar(boolean step) {
        syncViewModel.setProgress(step);
        bind.loadingProgressBar.setProgress(syncViewModel.getProgressBarInfo(), true);
        countProgress();
    }

    private void countProgress() {
        if (syncViewModel.getProgress() == syncViewModel.getStep()) {
            if (syncViewModel.getProgressBarInfo() < 100)
                Toast.makeText(requireContext(), "Sync error", Toast.LENGTH_SHORT).show();

            terminate();
        }
    }

    private void terminate() {
        PreferenceUtil.getInstance(requireContext()).setSync(true);
        activity.goToHome();
    }

    // ---------------------------------------------------------------------------------------------
    /*
     * Sincronizzazzione dell'album con gli artisti che lo hanno prodotto | isProduced = true
     * Sincronizzazzione dell'album con gli artisti che hanno collaborato per la sua produzione | isProduced = false
     */
    private void syncAlbumArtistCross(ArrayList<Album> albums) {
        List<AlbumArtistCross> crosses = new ArrayList<>();

        for(Album album: albums) {
            List<Artist> artists = new ArrayList<>();

            if(album.albumArtists.size() > 0) {
                for (Artist artist: album.albumArtists) {
                    artists.add(artist);
                    crosses.add(new AlbumArtistCross(album.getId(), artist.getId(), true));
                }
            }

            if(album.artistItems.size() > 0) {
                for (Artist artist: album.artistItems) {
                    if(!artists.contains(artist)) {
                        crosses.add(new AlbumArtistCross(album.getId(), artist.getId(), false));
                    }
                }
            }
        }

        albumArtistRepository.insertAll(crosses);
    }

    private void syncSongArtistCross(ArrayList<Song> songs) {
        List<SongArtistCross> crosses = new ArrayList<>();

        for(Song song: songs) {
            List<Artist> artists = new ArrayList<>();

            if(song.albumArtists.size() > 0) {
                for (Artist artist: song.albumArtists) {
                    artists.add(artist);
                    crosses.add(new SongArtistCross(song.getId(), artist.getId()));
                }
            }

            if(song.artistItems.size() > 0) {
                for (Artist artist: song.artistItems) {
                    if(!artists.contains(artist)) {
                        crosses.add(new SongArtistCross(song.getId(), artist.getId()));
                    }
                }
            }
        }

        songArtistRepository.insertAll(crosses);
    }
}
