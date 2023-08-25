package com.cappielloantonio.tempo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentLibraryBinding;
import com.cappielloantonio.tempo.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.interfaces.PlaylistCallback;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.AlbumAdapter;
import com.cappielloantonio.tempo.ui.adapter.ArtistAdapter;
import com.cappielloantonio.tempo.ui.adapter.GenreAdapter;
import com.cappielloantonio.tempo.ui.adapter.MusicFolderAdapter;
import com.cappielloantonio.tempo.ui.adapter.PlaylistHorizontalAdapter;
import com.cappielloantonio.tempo.ui.dialog.PlaylistEditorDialog;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.LibraryViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

@UnstableApi
public class LibraryFragment extends Fragment implements ClickCallback {
    private static final String TAG = "LibraryFragment";

    private FragmentLibraryBinding bind;
    private MainActivity activity;
    private LibraryViewModel libraryViewModel;

    private MusicFolderAdapter musicFolderAdapter;
    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private GenreAdapter genreAdapter;
    private PlaylistHorizontalAdapter playlistHorizontalAdapter;

    private MaterialToolbar materialToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentLibraryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        libraryViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);

        init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAppBar();
        initMusicFolderView();
        initAlbumView();
        initArtistView();
        initGenreView();
        initPlaylistView();
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setBottomNavigationBarVisibility(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPlaylistView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        bind.albumCatalogueTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_libraryFragment_to_albumCatalogueFragment));
        bind.artistCatalogueTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_libraryFragment_to_artistCatalogueFragment));
        bind.genreCatalogueTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_libraryFragment_to_genreCatalogueFragment));
        bind.playlistCatalogueTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PLAYLIST_ALL, Constants.PLAYLIST_ALL);
            activity.navController.navigate(R.id.action_libraryFragment_to_playlistCatalogueFragment, bundle);
        });

        bind.albumCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshAlbumSample(getViewLifecycleOwner());
            return true;
        });
        bind.artistCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshArtistSample(getViewLifecycleOwner());
            return true;
        });
        bind.genreCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshGenreSample(getViewLifecycleOwner());
            return true;
        });
        bind.playlistCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshPlaylistSample(getViewLifecycleOwner());
            return true;
        });
    }

    private void initAppBar() {
        materialToolbar = bind.getRoot().findViewById(R.id.toolbar);

        activity.setSupportActionBar(materialToolbar);
        Objects.requireNonNull(materialToolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initMusicFolderView() {
        if (!Preferences.isMusicDirectorySectionVisible()) {
            bind.libraryMusicFolderSector.setVisibility(View.GONE);
            return;
        }

        bind.musicFolderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.musicFolderRecyclerView.setHasFixedSize(true);

        musicFolderAdapter = new MusicFolderAdapter(this);
        bind.musicFolderRecyclerView.setAdapter(musicFolderAdapter);
        libraryViewModel.getMusicFolders(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), musicFolders -> {
            if (musicFolders == null) {
                if (bind != null)
                    bind.libraryMusicFolderPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryMusicFolderSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.libraryMusicFolderPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.libraryMusicFolderSector.setVisibility(!musicFolders.isEmpty() ? View.VISIBLE : View.GONE);

                musicFolderAdapter.setItems(musicFolders);
            }
        });
    }

    private void initAlbumView() {
        bind.albumRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.albumRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter(this);
        bind.albumRecyclerView.setAdapter(albumAdapter);
        libraryViewModel.getAlbumSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.libraryAlbumPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryAlbumSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryAlbumPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.libraryAlbumSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);

                albumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper albumSnapHelper = new CustomLinearSnapHelper();
        albumSnapHelper.attachToRecyclerView(bind.albumRecyclerView);
    }

    private void initArtistView() {
        bind.artistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.artistRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistAdapter(this, false, false);
        bind.artistRecyclerView.setAdapter(artistAdapter);
        libraryViewModel.getArtistSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), artists -> {
            if (artists == null) {
                if (bind != null)
                    bind.libraryArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.libraryArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.libraryArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);

                artistAdapter.setItems(artists);
            }
        });

        CustomLinearSnapHelper artistSnapHelper = new CustomLinearSnapHelper();
        artistSnapHelper.attachToRecyclerView(bind.artistRecyclerView);
    }

    private void initGenreView() {
        bind.genreRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false));
        bind.genreRecyclerView.setHasFixedSize(true);

        genreAdapter = new GenreAdapter(this);
        bind.genreRecyclerView.setAdapter(genreAdapter);

        libraryViewModel.getGenreSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), genres -> {
            if (genres == null) {
                if (bind != null)
                    bind.libraryGenrePlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryGenresSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryGenrePlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.libraryGenresSector.setVisibility(!genres.isEmpty() ? View.VISIBLE : View.GONE);

                genreAdapter.setItems(genres);
            }
        });

        CustomLinearSnapHelper genreSnapHelper = new CustomLinearSnapHelper();
        genreSnapHelper.attachToRecyclerView(bind.genreRecyclerView);
    }

    private void initPlaylistView() {
        bind.playlistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playlistRecyclerView.setHasFixedSize(true);

        playlistHorizontalAdapter = new PlaylistHorizontalAdapter(this);
        bind.playlistRecyclerView.setAdapter(playlistHorizontalAdapter);
        libraryViewModel.getPlaylistSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), playlists -> {
            if (playlists == null) {
                if (bind != null)
                    bind.libraryPlaylistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryPlaylistSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.libraryPlaylistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.libraryPlaylistSector.setVisibility(!playlists.isEmpty() ? View.VISIBLE : View.GONE);

                playlistHorizontalAdapter.setItems(playlists);
            }
        });
    }

    private void refreshPlaylistView() {
        final Handler handler = new Handler();
        final Runnable runnable = () -> libraryViewModel.refreshPlaylistSample(getViewLifecycleOwner());
        handler.postDelayed(runnable, 100);
    }

    @Override
    public void onAlbumClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumPageFragment, bundle);
    }

    @Override
    public void onAlbumLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.albumBottomSheetDialog, bundle);
    }

    @Override
    public void onArtistClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistPageFragment, bundle);
    }

    @Override
    public void onArtistLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistBottomSheetDialog, bundle);
    }

    @Override
    public void onGenreClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songListPageFragment, bundle);
    }

    @Override
    public void onPlaylistClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.playlistPageFragment, bundle);
    }

    @Override
    public void onPlaylistLongClick(Bundle bundle) {
        PlaylistEditorDialog dialog = new PlaylistEditorDialog(new PlaylistCallback() {
            @Override
            public void onDismiss() {
                refreshPlaylistView();
            }
        });

        dialog.setArguments(bundle);
        dialog.show(activity.getSupportFragmentManager(), null);
    }

    @Override
    public void onMusicFolderClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.indexFragment, bundle);
    }
}
