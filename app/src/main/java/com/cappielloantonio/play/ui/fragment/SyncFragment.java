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
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.model.SongGenreCross;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSyncBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        syncViewModel = new ViewModelProvider(requireActivity()).get(SyncViewModel.class);

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
                AlbumRepository repository = new AlbumRepository(activity.getApplication());
                repository.insertAll((ArrayList<Album>) media);
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
                ArtistRepository repository = new ArtistRepository(activity.getApplication());
                repository.insertAll((ArrayList<Artist>) media);
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
                GenreRepository repository = new GenreRepository(activity.getApplication());
                repository.insertAll((ArrayList<Genre>) media);
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
                PlaylistRepository repository = new PlaylistRepository(activity.getApplication());
                repository.insertAll((ArrayList<Playlist>) media);
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
                SongRepository repository = new SongRepository(activity.getApplication());
                repository.deleteAllSongGenreCross();
                repository.insertAll((ArrayList<Song>) media);
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
                    SongRepository repository = new SongRepository(App.getInstance());
                    repository.insertSongPerGenre((ArrayList<SongGenreCross>) media);
                }
            }, genre.id);
        }

        animateProgressBar(true);
        PreferenceUtil.getInstance(requireContext()).setSongGenreSync(true);
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
}
