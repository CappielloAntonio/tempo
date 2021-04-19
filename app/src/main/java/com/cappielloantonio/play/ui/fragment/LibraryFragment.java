package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumAdapter;
import com.cappielloantonio.play.adapter.ArtistAdapter;
import com.cappielloantonio.play.adapter.GenreAdapter;
import com.cappielloantonio.play.adapter.PlaylistAdapter;
import com.cappielloantonio.play.databinding.FragmentLibraryBinding;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.LibraryViewModel;

public class LibraryFragment extends Fragment {
    private static final String TAG = "LibraryFragment";

    private FragmentLibraryBinding bind;
    private MainActivity activity;
    private LibraryViewModel libraryViewModel;

    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private GenreAdapter genreAdapter;
    private PlaylistAdapter playlistAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentLibraryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        libraryViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);

        init();
        initAlbumView();
        initArtistView();
        initGenreView();
        initPlaylistView();
        initCatalogueSyncCheck();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setBottomNavigationBarVisibility(true);
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
    }

    private void initAlbumView() {
        bind.albumRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.albumRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter(requireContext());
        bind.albumRecyclerView.setAdapter(albumAdapter);
        libraryViewModel.getAlbumSample().observe(requireActivity(), albums -> albumAdapter.setItems(albums));
    }

    private void initArtistView() {
        bind.artistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.artistRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistAdapter(requireContext());
        bind.artistRecyclerView.setAdapter(artistAdapter);
        libraryViewModel.getArtistSample().observe(requireActivity(), artists -> artistAdapter.setItems(artists));
    }

    private void initGenreView() {
        bind.genreRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false));
        bind.genreRecyclerView.setHasFixedSize(true);

        genreAdapter = new GenreAdapter(requireContext());
        genreAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.BY_GENRE, Song.BY_GENRE);
            bundle.putParcelable("genre_object", genreAdapter.getItem(position));
            activity.navController.navigate(R.id.action_libraryFragment_to_songListPageFragment, bundle);
        });
        bind.genreRecyclerView.setAdapter(genreAdapter);
        libraryViewModel.getGenreSample().observe(requireActivity(), genres -> genreAdapter.setItems(genres));
    }

    private void initPlaylistView() {
        bind.playlistRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bind.playlistRecyclerView.setHasFixedSize(true);

        playlistAdapter = new PlaylistAdapter(requireContext());
        bind.playlistRecyclerView.setAdapter(playlistAdapter);
        libraryViewModel.getPlaylistList().observe(requireActivity(), playlists -> {
            bind.libraryPlaylistSector.setVisibility(playlists.size() > 0 ? View.VISIBLE : View.GONE);
            playlistAdapter.setItems(playlists);
        });
    }

    private void initCatalogueSyncCheck() {
        if (!PreferenceUtil.getInstance(requireContext()).getSongGenreSync()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Sync song's genres otherwise nothing will be shown in each genre category")
                    .setTitle("Song's genres not synchronized")
                    .setNegativeButton(R.string.ignore, null)
                    .setPositiveButton("Sync", (dialog, id) -> activity.goToSync())
                    .show();
        }
    }
}
