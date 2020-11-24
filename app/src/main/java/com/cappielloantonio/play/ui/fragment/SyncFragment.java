package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jellyfin.apiclient.model.dto.BaseItemDto;

import java.util.ArrayList;
import java.util.List;

public class SyncFragment extends Fragment {
    private static final String TAG = "SyncFragment";

    private MainActivity activity;
    private FragmentSyncBinding bind;

    private ArrayList<Integer> progressing;
    private List<Genre> genres;

    private int stepMax = 5;
    private int increment = 25;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSyncBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

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
        bind.loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void syncLibraries() {
        Log.d(TAG, "syncLibraries");
        progressing = new ArrayList<>();

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
        syncAlbums();
        syncArtists();
        syncGenres();
        syncPlaylist();
        syncSongs();
    }

    private void syncAlbums() {
        Log.d(TAG, "syncAlbums");
        SyncUtil.getAlbums(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false, "Album");
            }

            @Override
            public void onLoadMedia(List<?> media) {
                AlbumRepository repository = new AlbumRepository(activity.getApplication());
                repository.insertAll((ArrayList<Album>) media);
                setProgress(true, "Album");
            }
        });
    }

    private void syncArtists() {
        Log.d(TAG, "syncArtists");
        SyncUtil.getArtists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false, "Artist");
            }

            @Override
            public void onLoadMedia(List<?> media) {
                ArtistRepository repository = new ArtistRepository(activity.getApplication());
                repository.insertAll((ArrayList<Artist>) media);
                setProgress(true, "Artist");
            }
        });
    }

    private void syncGenres() {
        Log.d(TAG, "syncGenres");
        SyncUtil.getGenres(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false, "Genres");
            }

            @Override
            public void onLoadMedia(List<?> media) {
                GenreRepository repository = new GenreRepository(activity.getApplication());
                repository.insertAll((ArrayList<Genre>) media);
                setProgress(true, "Genres");
            }
        });
    }

    private void syncPlaylist() {
        Log.d(TAG, "syncPlaylist");
        SyncUtil.getPlaylists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false, "PlayList");
            }

            @Override
            public void onLoadMedia(List<?> media) {
                PlaylistRepository repository = new PlaylistRepository(activity.getApplication());
                repository.insertAll((ArrayList<Playlist>) media);
                setProgress(true, "PlayList");
            }
        });
    }

    private void syncSongs() {
        Log.d(TAG, "syncSongs");
        SyncUtil.getSongs(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false, "Songs");
            }

            @Override
            public void onLoadMedia(List<?> media) {
                SongRepository repository = new SongRepository(activity.getApplication());
                repository.insertAll((ArrayList<Song>) media);
                setProgress(true, "Songs");
            }
        });
    }


    private void setProgress(boolean step, String who) {
        if (step) {
            Log.d(TAG, "setProgress " + who + ": adding " + increment);
            progressing.add(increment);
            bind.loadingProgressBar.setProgress(bind.loadingProgressBar.getProgress() + increment, true);
        } else {
            Log.d(TAG, "setProgress" + who + ": adding " + 0);
            progressing.add(0);
        }

        countProgress();
    }

    private void countProgress() {
        if (progressing.size() == stepMax) {
            if (bind.loadingProgressBar.getProgress() == 100)
                terminate();
            else
                Toast.makeText(requireContext(), "Sync error", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "countProgress: SIZE: " + progressing.size() + " - SUM: " + bind.loadingProgressBar.getProgress());
    }

    private void terminate() {
        PreferenceUtil.getInstance(requireContext()).setSync(true);
        activity.goToHome();
    }
}
