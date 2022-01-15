package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumAdapter;
import com.cappielloantonio.play.adapter.AlbumHorizontalAdapter;
import com.cappielloantonio.play.adapter.ArtistAdapter;
import com.cappielloantonio.play.adapter.GenreAdapter;
import com.cappielloantonio.play.adapter.PlaylistAdapter;
import com.cappielloantonio.play.adapter.PlaylistDialogHorizontalAdapter;
import com.cappielloantonio.play.adapter.PlaylistHorizontalAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentLibraryBinding;
import com.cappielloantonio.play.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.LibraryViewModel;
import com.google.android.gms.cast.framework.CastButtonFactory;

import java.util.Objects;

public class LibraryFragment extends Fragment {
    private static final String TAG = "LibraryFragment";

    private FragmentLibraryBinding bind;
    private MainActivity activity;
    private LibraryViewModel libraryViewModel;

    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private GenreAdapter genreAdapter;

    private PlaylistHorizontalAdapter playlistHorizontalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_page_menu, menu);
        CastButtonFactory.setUpMediaRouteButton(requireContext(), menu, R.id.media_route_menu_item);
    }

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
        initNewReleasesShortcut();
        initStarredShortcut();
        initGenresShortcut();
        initAlbumView();
        initArtistView();
        initGenreView();
        initPlaylistSlideView();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            activity.navController.navigate(R.id.action_libraryFragment_to_searchFragment);
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            activity.navController.navigate(R.id.action_libraryFragment_to_settingsFragment);
            return true;
        }

        return false;
    }

    private void init() {
        bind.albumCatalogueTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_libraryFragment_to_albumCatalogueFragment));
        bind.artistCatalogueTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_libraryFragment_to_artistCatalogueFragment));
        bind.genreCatalogueTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_libraryFragment_to_genreCatalogueFragment));
        bind.playlistCatalogueTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Playlist.ALL, Playlist.ALL);
            activity.navController.navigate(R.id.action_libraryFragment_to_playlistCatalogueFragment, bundle);
        });

        bind.albumCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshAlbumSample(requireActivity());
            return true;
        });
        bind.artistCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshArtistSample(requireActivity());
            return true;
        });
        bind.genreCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshGenreSample(requireActivity());
            return true;
        });
        bind.playlistCatalogueSampleTextViewRefreshable.setOnLongClickListener(view -> {
            libraryViewModel.refreshPlaylistSample(requireActivity());
            return true;
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);
        Objects.requireNonNull(bind.toolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initNewReleasesShortcut() {
        bind.libraryNewReleasesSector.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Album.NEW_RELEASES, Album.NEW_RELEASES);
            activity.navController.navigate(R.id.action_libraryFragment_to_albumListPageFragment, bundle);
        });
    }

    private void initStarredShortcut() {
        bind.libraryStarredSector.setOnClickListener(view -> {
            activity.navController.navigate(R.id.action_libraryFragment_to_starredFragment);
        });
    }

    private void initGenresShortcut() {
        bind.libraryGenresShortcutSector.setOnClickListener(view -> {
            activity.navController.navigate(R.id.action_libraryFragment_to_genreCatalogueFragment);
        });
    }

    private void initAlbumView() {
        bind.albumRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.albumRecyclerView.setHasFixedSize(true);

        albumAdapter = new AlbumAdapter(requireContext());
        bind.albumRecyclerView.setAdapter(albumAdapter);
        libraryViewModel.getAlbumSample().observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.libraryAlbumPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryAlbumSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryAlbumPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.libraryAlbumSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);

                albumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper albumSnapHelper = new CustomLinearSnapHelper();
        albumSnapHelper.attachToRecyclerView(bind.albumRecyclerView);
    }

    private void initArtistView() {
        bind.artistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.artistRecyclerView.setHasFixedSize(true);

        artistAdapter = new ArtistAdapter(requireContext());
        bind.artistRecyclerView.setAdapter(artistAdapter);
        libraryViewModel.getArtistSample().observe(requireActivity(), artists -> {
            if (artists == null) {
                if (bind != null) bind.libraryArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.libraryArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);

                artistAdapter.setItems(artists);
            }
        });

        CustomLinearSnapHelper artistSnapHelper = new CustomLinearSnapHelper();
        artistSnapHelper.attachToRecyclerView(bind.artistRecyclerView);
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
        libraryViewModel.getGenreSample().observe(requireActivity(), genres -> {
            if (genres == null) {
                if (bind != null) bind.libraryGenrePlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryGenresSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryGenrePlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.libraryGenresSector.setVisibility(!genres.isEmpty() ? View.VISIBLE : View.GONE);

                genreAdapter.setItems(genres);
            }
        });

        CustomLinearSnapHelper genreSnapHelper = new CustomLinearSnapHelper();
        genreSnapHelper.attachToRecyclerView(bind.genreRecyclerView);
    }

    private void initPlaylistSlideView() {
        bind.playlistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playlistRecyclerView.setHasFixedSize(true);

        playlistHorizontalAdapter = new PlaylistHorizontalAdapter(activity, requireContext());
        bind.playlistRecyclerView.setAdapter(playlistHorizontalAdapter);
        libraryViewModel.getPlaylistSample().observe(requireActivity(), playlists -> {
            if (playlists == null) {
                if (bind != null) bind.libraryPlaylistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryPlaylistSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryPlaylistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.libraryPlaylistSector.setVisibility(!playlists.isEmpty() ? View.VISIBLE : View.GONE);

                playlistHorizontalAdapter.setItems(playlists);
            }
        });
    }
}
