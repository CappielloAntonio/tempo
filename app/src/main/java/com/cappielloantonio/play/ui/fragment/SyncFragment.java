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

import com.cappielloantonio.play.databinding.FragmentSyncBinding;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.util.SyncUtil;

import org.jellyfin.apiclient.model.dto.BaseItemDto;

import java.util.ArrayList;
import java.util.List;

public class SyncFragment extends Fragment {
    private static final String TAG = "SyncFragment";

    private MainActivity activity;
    private FragmentSyncBinding bind;

    private ArrayList<Integer> progressing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSyncBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        syncLibraries();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void showProgressBar() {
        bind.loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void syncLibraries() {
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
                        SyncUtil.musicLibrary = itemDto;
                }

                startSyncing();
            }
        });
    }

    private void startSyncing() {
        showProgressBar();
        syncAlbums();
        syncArtists();
        syncGenres();
        syncPlaylist();
        syncSongs();
    }

    private void syncAlbums() {
        SyncUtil.getAlbums(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                AlbumRepository repository = new AlbumRepository(activity.getApplication());
                repository.insertAll((ArrayList<Album>) media);
                setProgress(true);
            }
        });
    }

    private void syncArtists() {
        SyncUtil.getArtists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                ArtistRepository repository = new ArtistRepository(activity.getApplication());
                repository.insertAll((ArrayList<Artist>) media);
                setProgress(true);
            }
        });
    }

    private void syncGenres() {
        SyncUtil.getGenres(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                GenreRepository repository = new GenreRepository(activity.getApplication());
                repository.insertAll((ArrayList<Genre>) media);
                setProgress(true);
            }
        });
    }

    private void syncPlaylist() {
        SyncUtil.getPlaylists(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                PlaylistRepository repository = new PlaylistRepository(activity.getApplication());
                repository.insertAll((ArrayList<Playlist>) media);
                setProgress(true);
            }
        });
    }

    private void syncSongs() {
        SyncUtil.getSongs(requireContext(), new MediaCallback() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
                setProgress(false);
            }

            @Override
            public void onLoadMedia(List<?> media) {
                SongRepository repository = new SongRepository(activity.getApplication());
                repository.insertAll((ArrayList<Song>) media);
                setProgress(true);
            }
        });
    }

    private void setProgress(boolean step) {
        if (step) {
            progressing.add(25);
            bind.loadingProgressBar.setProgress(bind.loadingProgressBar.getProgress() + 25, true);
        } else {
            progressing.add(0);
        }

        countProgress();
    }

    private void countProgress() {
        if (progressing.size() == 5) {
            if (bind.loadingProgressBar.getProgress() == 100)
                terminate();
            else
                Toast.makeText(requireContext(), "Sync error", Toast.LENGTH_SHORT).show();
        }
    }

    private void terminate() {
        PreferenceUtil.getInstance(requireContext()).setSync(true);
        activity.goToHome();
    }
}
