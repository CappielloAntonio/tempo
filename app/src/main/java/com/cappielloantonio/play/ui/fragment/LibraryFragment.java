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
import androidx.media3.session.MediaBrowser;
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
import com.cappielloantonio.play.databinding.FragmentLibraryBinding;
import com.cappielloantonio.play.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.LibraryViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;

public class LibraryFragment extends Fragment {
    private static final String TAG = "LibraryFragment";

    private FragmentLibraryBinding bind;
    private MainActivity activity;
    private LibraryViewModel libraryViewModel;

    private AlbumHorizontalAdapter newRelesesAlbumAdapter;
    private AlbumAdapter albumAdapter;
    private ArtistAdapter artistAdapter;
    private GenreAdapter genreAdapter;
    private PlaylistAdapter playlistAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_page_menu, menu);
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
        initNewReleasesView();
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

    private void initNewReleasesView() {
        bind.newReleasesRecyclerView.setHasFixedSize(true);

        newRelesesAlbumAdapter = new AlbumHorizontalAdapter(requireContext(), false);
        bind.newReleasesRecyclerView.setAdapter(newRelesesAlbumAdapter);
        libraryViewModel.getRecentlyReleasedAlbums(requireActivity()).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.libraryNewReleasesPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryNewReleasesSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryNewReleasesPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.libraryNewReleasesSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.newReleasesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(albums.size(), 5), GridLayoutManager.HORIZONTAL, false));

                newRelesesAlbumAdapter.setItems(albums);
            }
        });

        SnapHelper starredAlbumSnapHelper = new PagerSnapHelper();
        starredAlbumSnapHelper.attachToRecyclerView(bind.newReleasesRecyclerView);

        bind.newReleasesRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
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
        bind.playlistViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        playlistAdapter = new PlaylistAdapter(activity, requireContext(), false);
        bind.playlistViewPager.setAdapter(playlistAdapter);
        bind.playlistViewPager.setOffscreenPageLimit(3);
        libraryViewModel.getPlaylistSample().observe(requireActivity(), playlists -> {
            if (playlists == null) {
                if (bind != null) bind.libraryPlaylistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.libraryPlaylistSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.libraryPlaylistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.libraryPlaylistSector.setVisibility(!playlists.isEmpty() ? View.VISIBLE : View.GONE);

                playlistAdapter.setItems(playlists);
            }
        });

        setDiscoverSongSlideViewOffset(20, 16);
    }

    private void setDiscoverSongSlideViewOffset(float pageOffset, float pageMargin) {
        bind.playlistViewPager.setPageTransformer((page, position) -> {
            float myOffset = position * -(2 * pageOffset + pageMargin);
            if (bind.playlistViewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(bind.playlistViewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.setTranslationX(-myOffset);
                } else {
                    page.setTranslationX(myOffset);
                }
            } else {
                page.setTranslationY(myOffset);
            }
        });
    }
}
