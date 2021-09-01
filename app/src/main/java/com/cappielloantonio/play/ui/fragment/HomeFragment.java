package com.cappielloantonio.play.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.cappielloantonio.play.adapter.ArtistHorizontalAdapter;
import com.cappielloantonio.play.adapter.DiscoverSongAdapter;
import com.cappielloantonio.play.adapter.SimilarTrackAdapter;
import com.cappielloantonio.play.adapter.TrackAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.adapter.YearAdapter;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.HomeViewModel;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";

    private FragmentHomeBinding bind;
    private MainActivity activity;
    private HomeViewModel homeViewModel;

    private DiscoverSongAdapter discoverSongAdapter;
    private AlbumAdapter recentlyAddedAlbumAdapter;
    private AlbumAdapter recentlyPlayedAlbumAdapter;
    private AlbumAdapter mostPlayedAlbumAdapter;
    private YearAdapter yearAdapter;
    private SongHorizontalAdapter starredSongAdapter;
    private AlbumHorizontalAdapter starredAlbumAdapter;
    private ArtistHorizontalAdapter starredArtistAdapter;
    private TrackAdapter dowanloadedMusicAdapter;
    private SimilarTrackAdapter similarMusicAdapter;

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

        bind = FragmentHomeBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAppBar();
        initDiscoverSongSlideView();
        initSimilarSongView();
        initMostPlayedAlbumView();
        initRecentPlayedAlbumView();
        initStarredTracksView();
        initStarredAlbumsView();
        initStarredArtistsView();
        initYearSongView();
        initRecentAddedAlbumView();
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setBottomNavigationBarVisibility(true);
        activity.setBottomSheetVisibility(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                activity.navController.navigate(R.id.action_homeFragment_to_searchFragment);
                return true;
            case R.id.action_settings:
                activity.navController.navigate(R.id.action_homeFragment_to_settingsFragment);
                return true;
            default:
                break;
        }

        return false;
    }

    private void init() {
        bind.recentlyAddedAlbumsTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Album.RECENTLY_ADDED, Album.RECENTLY_ADDED);
            activity.navController.navigate(R.id.action_homeFragment_to_albumListPageFragment, bundle);
        });

        bind.recentlyPlayedAlbumsTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Album.RECENTLY_PLAYED, Album.RECENTLY_PLAYED);
            activity.navController.navigate(R.id.action_homeFragment_to_albumListPageFragment, bundle);
        });

        bind.mostPlayedAlbumsTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Album.MOST_PLAYED, Album.MOST_PLAYED);
            activity.navController.navigate(R.id.action_homeFragment_to_albumListPageFragment, bundle);
        });

        bind.starredTracksTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.STARRED, Song.STARRED);
            activity.navController.navigate(R.id.action_homeFragment_to_songListPageFragment, bundle);
        });

        bind.starredAlbumsTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Album.STARRED, Album.STARRED);
            activity.navController.navigate(R.id.action_homeFragment_to_albumListPageFragment, bundle);
        });

        bind.starredArtistsTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Artist.STARRED, Artist.STARRED);
            activity.navController.navigate(R.id.action_homeFragment_to_artistListPageFragment, bundle);
        });

        bind.musicDiscoveryTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshDiscoverySongSample(requireActivity());
            return true;
        });

        bind.similarTracksTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshSimilarSongSample(requireActivity());
            return true;
        });

        bind.recentlyPlayedAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshRecentlyPlayedAlbumList(requireActivity());
            return true;
        });

        bind.mostPlayedAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshMostPlayedAlbums(requireActivity());
            return true;
        });

        bind.recentlyAddedAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshMostRecentlyAddedAlbums(requireActivity());
            return true;
        });

        bind.starredTracksTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshStarredTracks(requireActivity());
            return true;
        });

        bind.starredAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshStarredAlbums(requireActivity());
            return true;
        });

        bind.starredArtistsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshStarredArtists(requireActivity());
            return true;
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);
        Objects.requireNonNull(bind.toolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initDiscoverSongSlideView() {
        bind.discoverSongViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        discoverSongAdapter = new DiscoverSongAdapter(activity, requireContext());
        bind.discoverSongViewPager.setAdapter(discoverSongAdapter);
        bind.discoverSongViewPager.setOffscreenPageLimit(1);
        homeViewModel.getDiscoverSongSample().observe(requireActivity(), songs -> {
            if (songs == null) {
                if (bind != null) bind.homeDiscoveryPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeDiscoverSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeDiscoveryPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeDiscoverSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);

                discoverSongAdapter.setItems(songs);
            }
        });

        setDiscoverSongSlideViewOffset(20, 16);
    }

    private void initSimilarSongView() {
        bind.similarTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.similarTracksRecyclerView.setHasFixedSize(true);

        similarMusicAdapter = new SimilarTrackAdapter(activity, requireContext());
        bind.similarTracksRecyclerView.setAdapter(similarMusicAdapter);
        homeViewModel.getStarredTracksSample().observe(requireActivity(), songs -> {
            if (songs == null) {
                if (bind != null) bind.homeSimilarTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeSimilarTracksSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeSimilarTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeSimilarTracksSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);

                similarMusicAdapter.setItems(songs);
            }
        });

        CustomLinearSnapHelper similarSongSnapHelper = new CustomLinearSnapHelper();
        similarSongSnapHelper.attachToRecyclerView(bind.similarTracksRecyclerView);
    }

    private void initMostPlayedAlbumView() {
        bind.mostPlayedAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.mostPlayedAlbumsRecyclerView.setHasFixedSize(true);

        mostPlayedAlbumAdapter = new AlbumAdapter(requireContext());
        bind.mostPlayedAlbumsRecyclerView.setAdapter(mostPlayedAlbumAdapter);
        homeViewModel.getMostPlayedAlbums(requireActivity()).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.homeMostPlayedAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeMostPlayedAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeMostPlayedAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeMostPlayedAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                if (albums.size() < 10) reorder();

                mostPlayedAlbumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper mostPlayedAlbumSnapHelper = new CustomLinearSnapHelper();
        mostPlayedAlbumSnapHelper.attachToRecyclerView(bind.mostPlayedAlbumsRecyclerView);
    }

    private void initRecentPlayedAlbumView() {
        bind.recentlyPlayedAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.recentlyPlayedAlbumsRecyclerView.setHasFixedSize(true);

        recentlyPlayedAlbumAdapter = new AlbumAdapter(requireContext());
        bind.recentlyPlayedAlbumsRecyclerView.setAdapter(recentlyPlayedAlbumAdapter);
        homeViewModel.getRecentlyPlayedAlbumList(requireActivity()).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.homeRecentlyPlayedAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeRecentlyPlayedAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeRecentlyPlayedAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeRecentlyPlayedAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);

                recentlyPlayedAlbumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper recentPlayedAlbumSnapHelper = new CustomLinearSnapHelper();
        recentPlayedAlbumSnapHelper.attachToRecyclerView(bind.recentlyPlayedAlbumsRecyclerView);
    }

    private void initYearSongView() {
        bind.yearsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.yearsRecyclerView.setHasFixedSize(true);

        yearAdapter = new YearAdapter(requireContext());
        yearAdapter.setClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.BY_YEAR, Song.BY_YEAR);
            bundle.putInt("year_object", yearAdapter.getItem(position));
            activity.navController.navigate(R.id.action_homeFragment_to_songListPageFragment, bundle);
        });
        bind.yearsRecyclerView.setAdapter(yearAdapter);
        homeViewModel.getYearList(requireActivity()).observe(requireActivity(), years -> {
            if (years == null) {
                if (bind != null) bind.homeFlashbackPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeFlashbackSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeFlashbackPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeFlashbackSector.setVisibility(!years.isEmpty() ? View.VISIBLE : View.GONE);

                yearAdapter.setItems(years);
            }
        });

        CustomLinearSnapHelper yearSnapHelper = new CustomLinearSnapHelper();
        yearSnapHelper.attachToRecyclerView(bind.yearsRecyclerView);
    }

    private void initStarredTracksView() {
        bind.starredTracksRecyclerView.setHasFixedSize(true);

        starredSongAdapter = new SongHorizontalAdapter(activity, requireContext(), true);
        bind.starredTracksRecyclerView.setAdapter(starredSongAdapter);
        homeViewModel.getStarredTracks(requireActivity()).observe(requireActivity(), songs -> {
            if (songs == null) {
                if (bind != null) bind.homeStarredTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeStarredTracksSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeStarredTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeStarredTracksSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null) bind.starredTracksRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(songs.size(), 5), GridLayoutManager.HORIZONTAL, false));

                starredSongAdapter.setItems(songs);
            }
        });

        SnapHelper starredTrackSnapHelper = new PagerSnapHelper();
        starredTrackSnapHelper.attachToRecyclerView(bind.starredTracksRecyclerView);

        bind.starredTracksRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
    }

    private void initStarredAlbumsView() {
        bind.starredAlbumsRecyclerView.setHasFixedSize(true);

        starredAlbumAdapter = new AlbumHorizontalAdapter(requireContext());
        bind.starredAlbumsRecyclerView.setAdapter(starredAlbumAdapter);
        homeViewModel.getStarredAlbums(requireActivity()).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.homeStarredAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeStarredAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeStarredAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeStarredAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null) bind.starredAlbumsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(albums.size(), 5), GridLayoutManager.HORIZONTAL, false));

                starredAlbumAdapter.setItems(albums);
            }
        });

        SnapHelper starredAlbumSnapHelper = new PagerSnapHelper();
        starredAlbumSnapHelper.attachToRecyclerView(bind.starredAlbumsRecyclerView);

        bind.starredAlbumsRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
    }

    private void initStarredArtistsView() {
        bind.starredArtistsRecyclerView.setHasFixedSize(true);

        starredArtistAdapter = new ArtistHorizontalAdapter(requireContext(), false);
        bind.starredArtistsRecyclerView.setAdapter(starredArtistAdapter);
        homeViewModel.getStarredArtists(requireActivity()).observe(requireActivity(), artists -> {
            if (artists == null) {
                if (bind != null) bind.homeStarredArtistsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeStarredArtistsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeStarredArtistsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeStarredArtistsSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null) bind.starredArtistsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(artists.size(), 5), GridLayoutManager.HORIZONTAL, false));

                starredArtistAdapter.setItems(artists);
            }
        });

        SnapHelper starredArtistSnapHelper = new PagerSnapHelper();
        starredArtistSnapHelper.attachToRecyclerView(bind.starredArtistsRecyclerView);

        bind.starredArtistsRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
    }

    private void initRecentAddedAlbumView() {
        bind.recentlyAddedAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.recentlyAddedAlbumsRecyclerView.setHasFixedSize(true);

        recentlyAddedAlbumAdapter = new AlbumAdapter(requireContext());
        bind.recentlyAddedAlbumsRecyclerView.setAdapter(recentlyAddedAlbumAdapter);
        homeViewModel.getMostRecentlyAddedAlbums(requireActivity()).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.homeRecentlyAddedAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeRecentlyAddedAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeRecentlyAddedAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeRecentlyAddedAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);

                recentlyAddedAlbumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper recentAddedAlbumSnapHelper = new CustomLinearSnapHelper();
        recentAddedAlbumSnapHelper.attachToRecyclerView(bind.recentlyAddedAlbumsRecyclerView);
    }

    private void setDiscoverSongSlideViewOffset(float pageOffset, float pageMargin) {
        bind.discoverSongViewPager.setPageTransformer((page, position) -> {
            float myOffset = position * -(2 * pageOffset + pageMargin);
            if (bind.discoverSongViewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(bind.discoverSongViewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.setTranslationX(-myOffset);
                } else {
                    page.setTranslationX(myOffset);
                }
            } else {
                page.setTranslationY(myOffset);
            }
        });
    }

    /*
     * Il layout di default prevede questa sequenza:
     * - Discovery - Most_played - Last_played - Year - Favorite - Downloaded - Recently_added
     *
     * Se però non ho ancora ascoltato nessuna canzone e quindi Most_played e Last_played sono vuoti, modifico come segue
     * - Discovery - Recently_added - Year - Favorite - Downloaded - Most_played - Last_played
     */
    public void reorder() {
        if (bind != null) {
            bind.homeLinearLayoutContainer.removeAllViews();
            bind.homeLinearLayoutContainer.addView(bind.homeDiscoverSector);
            bind.homeLinearLayoutContainer.addView(bind.homeSimilarTracksSector);
            bind.homeLinearLayoutContainer.addView(bind.homeRecentlyAddedAlbumsSector);
            bind.homeLinearLayoutContainer.addView(bind.homeFlashbackSector);
            bind.homeLinearLayoutContainer.addView(bind.homeStarredTracksSector);
            bind.homeLinearLayoutContainer.addView(bind.homeStarredAlbumsSector);
            bind.homeLinearLayoutContainer.addView(bind.homeStarredArtistsSector);
            bind.homeLinearLayoutContainer.addView(bind.homeMostPlayedAlbumsSector);
            bind.homeLinearLayoutContainer.addView(bind.homeRecentlyPlayedAlbumsSector);
        }
    }
}
