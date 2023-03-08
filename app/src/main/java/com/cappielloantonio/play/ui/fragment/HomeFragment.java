package com.cappielloantonio.play.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumAdapter;
import com.cappielloantonio.play.adapter.AlbumHorizontalAdapter;
import com.cappielloantonio.play.adapter.ArtistAdapter;
import com.cappielloantonio.play.adapter.ArtistHorizontalAdapter;
import com.cappielloantonio.play.adapter.DiscoverSongAdapter;
import com.cappielloantonio.play.adapter.GridTrackAdapter;
import com.cappielloantonio.play.adapter.PodcastEpisodeAdapter;
import com.cappielloantonio.play.adapter.SimilarTrackAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.adapter.YearAdapter;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.helper.recyclerview.GridItemDecoration;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.Playlist;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.HomeViewModel;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Objects;

@UnstableApi
public class HomeFragment extends Fragment implements ClickCallback {
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding bind;
    private MainActivity activity;
    private HomeViewModel homeViewModel;

    private DiscoverSongAdapter discoverSongAdapter;
    private SimilarTrackAdapter similarMusicAdapter;
    private ArtistAdapter radioArtistAdapter;
    private ArtistAdapter bestOfArtistAdapter;
    private SongHorizontalAdapter starredSongAdapter;
    private AlbumHorizontalAdapter starredAlbumAdapter;
    private ArtistHorizontalAdapter starredArtistAdapter;
    private AlbumAdapter recentlyAddedAlbumAdapter;
    private AlbumAdapter recentlyPlayedAlbumAdapter;
    private AlbumAdapter mostPlayedAlbumAdapter;
    private AlbumHorizontalAdapter newReleasesAlbumAdapter;
    private YearAdapter yearAdapter;
    private PodcastEpisodeAdapter podcastEpisodeAdapter;
    private GridTrackAdapter gridTrackAdapter;

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
        CastButtonFactory.setUpMediaRouteButton(requireContext(), menu, R.id.media_route_menu_item);
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
        initArtistRadio();
        initArtistBestOf();
        initStarredTracksView();
        initStarredAlbumsView();
        initStarredArtistsView();
        initMostPlayedAlbumView();
        initRecentPlayedAlbumView();
        initNewReleasesView();
        initYearSongView();
        initRecentAddedAlbumView();
        initPinnedPlaylistsView();
        initNewestPodcastsView();
        initGridView();
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
        activity.setBottomNavigationBarVisibility(true);
        activity.setBottomSheetVisibility(true);
    }

    @Override
    public void onStop() {
        releaseMediaBrowser();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            activity.navController.navigate(R.id.action_homeFragment_to_searchFragment);
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            activity.navController.navigate(R.id.action_homeFragment_to_settingsFragment);
            return true;
        }

        return false;
    }

    private void init() {
        bind.musicDiscoveryTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshDiscoverySongSample(getViewLifecycleOwner());
            return true;
        });

        bind.similarTracksTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshSimilarSongSample(getViewLifecycleOwner());
            return true;
        });

        bind.radioArtistTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshRadioArtistSample(getViewLifecycleOwner());
            return true;
        });

        bind.bestOfArtistTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshBestOfArtist(getViewLifecycleOwner());
            return true;
        });

        bind.starredTracksTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Media.STARRED, Media.STARRED);
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

        bind.starredTracksTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshStarredTracks(getViewLifecycleOwner());
            return true;
        });

        bind.starredAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshStarredAlbums(getViewLifecycleOwner());
            return true;
        });

        bind.starredArtistsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshStarredArtists(getViewLifecycleOwner());
            return true;
        });

        bind.recentlyPlayedAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshRecentlyPlayedAlbumList(getViewLifecycleOwner());
            return true;
        });

        bind.mostPlayedAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshMostPlayedAlbums(getViewLifecycleOwner());
            return true;
        });

        bind.recentlyAddedAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshMostRecentlyAddedAlbums(getViewLifecycleOwner());
            return true;
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);
        Objects.requireNonNull(bind.toolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initDiscoverSongSlideView() {
        bind.discoverSongViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        discoverSongAdapter = new DiscoverSongAdapter(requireContext(), this);
        bind.discoverSongViewPager.setAdapter(discoverSongAdapter);
        bind.discoverSongViewPager.setOffscreenPageLimit(1);
        homeViewModel.getDiscoverSongSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
            if (songs == null) {
                if (bind != null)
                    bind.homeDiscoveryPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeDiscoverSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeDiscoveryPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeDiscoverSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);

                discoverSongAdapter.setItems(songs);
            }
        });

        setSlideViewOffset(bind.discoverSongViewPager, 20, 16);
    }

    private void initSimilarSongView() {
        bind.similarTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.similarTracksRecyclerView.setHasFixedSize(true);

        similarMusicAdapter = new SimilarTrackAdapter(requireContext(), this);
        bind.similarTracksRecyclerView.setAdapter(similarMusicAdapter);
        homeViewModel.getStarredTracksSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
            if (songs == null) {
                if (bind != null)
                    bind.homeSimilarTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeSimilarTracksSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeSimilarTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeSimilarTracksSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);

                similarMusicAdapter.setItems(songs);
            }
        });

        CustomLinearSnapHelper similarSongSnapHelper = new CustomLinearSnapHelper();
        similarSongSnapHelper.attachToRecyclerView(bind.similarTracksRecyclerView);
    }

    private void initArtistRadio() {
        bind.radioArtistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.radioArtistRecyclerView.setHasFixedSize(true);

        radioArtistAdapter = new ArtistAdapter(requireContext(), this, true, false);
        bind.radioArtistRecyclerView.setAdapter(radioArtistAdapter);
        homeViewModel.getStarredArtistsSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), artists -> {
            if (artists == null) {
                if (bind != null)
                    bind.homeRadioArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeRadioArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeRadioArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeRadioArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);

                radioArtistAdapter.setItems(artists);
            }
        });

        CustomLinearSnapHelper artistRadioSnapHelper = new CustomLinearSnapHelper();
        artistRadioSnapHelper.attachToRecyclerView(bind.radioArtistRecyclerView);
    }

    private void initArtistBestOf() {
        bind.bestOfArtistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.bestOfArtistRecyclerView.setHasFixedSize(true);

        bestOfArtistAdapter = new ArtistAdapter(requireContext(), this, false, true);
        bind.bestOfArtistRecyclerView.setAdapter(bestOfArtistAdapter);
        homeViewModel.getBestOfArtists(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), artists -> {
            if (artists == null) {
                if (bind != null)
                    bind.homeBestOfArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeBestOfArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeBestOfArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeBestOfArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);

                bestOfArtistAdapter.setItems(artists);
            }
        });

        CustomLinearSnapHelper artistBestOfSnapHelper = new CustomLinearSnapHelper();
        artistBestOfSnapHelper.attachToRecyclerView(bind.bestOfArtistRecyclerView);
    }

    private void initStarredTracksView() {
        bind.starredTracksRecyclerView.setHasFixedSize(true);

        starredSongAdapter = new SongHorizontalAdapter(requireContext(), this, true);
        bind.starredTracksRecyclerView.setAdapter(starredSongAdapter);
        homeViewModel.getStarredTracks(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
            if (songs == null) {
                if (bind != null)
                    bind.starredTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.starredTracksSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.starredTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.starredTracksSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.starredTracksRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(songs.size(), 5), GridLayoutManager.HORIZONTAL, false));

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

        starredAlbumAdapter = new AlbumHorizontalAdapter(requireContext(), this, false);
        bind.starredAlbumsRecyclerView.setAdapter(starredAlbumAdapter);
        homeViewModel.getStarredAlbums(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.starredAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.starredAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.starredAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.starredAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.starredAlbumsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(albums.size(), 5), GridLayoutManager.HORIZONTAL, false));

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

        starredArtistAdapter = new ArtistHorizontalAdapter(requireContext(), this);
        bind.starredArtistsRecyclerView.setAdapter(starredArtistAdapter);
        homeViewModel.getStarredArtists(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), artists -> {
            if (artists == null) {
                if (bind != null)
                    bind.starredArtistsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.starredArtistsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.starredArtistsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.starredArtistsSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.starredArtistsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(artists.size(), 5), GridLayoutManager.HORIZONTAL, false));

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

    private void initNewReleasesView() {
        bind.newReleasesRecyclerView.setHasFixedSize(true);

        newReleasesAlbumAdapter = new AlbumHorizontalAdapter(requireContext(), this, false);
        bind.newReleasesRecyclerView.setAdapter(newReleasesAlbumAdapter);
        homeViewModel.getRecentlyReleasedAlbums(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.homeNewReleasesPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeNewReleasesSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeNewReleasesPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeNewReleasesSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.newReleasesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(albums.size(), 5), GridLayoutManager.HORIZONTAL, false));

                newReleasesAlbumAdapter.setItems(albums);
            }
        });

        SnapHelper newReleasesSnapHelper = new PagerSnapHelper();
        newReleasesSnapHelper.attachToRecyclerView(bind.newReleasesRecyclerView);

        bind.newReleasesRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
    }

    private void initYearSongView() {
        bind.yearsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.yearsRecyclerView.setHasFixedSize(true);

        yearAdapter = new YearAdapter(requireContext(), this);
        bind.yearsRecyclerView.setAdapter(yearAdapter);
        homeViewModel.getYearList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), years -> {
            if (years == null) {
                if (bind != null)
                    bind.homeFlashbackPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeFlashbackSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeFlashbackPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeFlashbackSector.setVisibility(!years.isEmpty() ? View.VISIBLE : View.GONE);

                yearAdapter.setItems(years);
            }
        });

        CustomLinearSnapHelper yearSnapHelper = new CustomLinearSnapHelper();
        yearSnapHelper.attachToRecyclerView(bind.yearsRecyclerView);
    }

    private void initMostPlayedAlbumView() {
        bind.mostPlayedAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.mostPlayedAlbumsRecyclerView.setHasFixedSize(true);

        mostPlayedAlbumAdapter = new AlbumAdapter(requireContext(), this);
        bind.mostPlayedAlbumsRecyclerView.setAdapter(mostPlayedAlbumAdapter);
        homeViewModel.getMostPlayedAlbums(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.homeMostPlayedAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeMostPlayedAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeMostPlayedAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeMostPlayedAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                // if (albums.size() < 5) reorder();

                mostPlayedAlbumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper mostPlayedAlbumSnapHelper = new CustomLinearSnapHelper();
        mostPlayedAlbumSnapHelper.attachToRecyclerView(bind.mostPlayedAlbumsRecyclerView);
    }

    private void initRecentPlayedAlbumView() {
        bind.recentlyPlayedAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.recentlyPlayedAlbumsRecyclerView.setHasFixedSize(true);

        recentlyPlayedAlbumAdapter = new AlbumAdapter(requireContext(), this);
        bind.recentlyPlayedAlbumsRecyclerView.setAdapter(recentlyPlayedAlbumAdapter);
        homeViewModel.getRecentlyPlayedAlbumList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.homeRecentlyPlayedAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeRecentlyPlayedAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeRecentlyPlayedAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeRecentlyPlayedAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);

                recentlyPlayedAlbumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper recentPlayedAlbumSnapHelper = new CustomLinearSnapHelper();
        recentPlayedAlbumSnapHelper.attachToRecyclerView(bind.recentlyPlayedAlbumsRecyclerView);
    }

    private void initRecentAddedAlbumView() {
        bind.recentlyAddedAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.recentlyAddedAlbumsRecyclerView.setHasFixedSize(true);

        recentlyAddedAlbumAdapter = new AlbumAdapter(requireContext(), this);
        bind.recentlyAddedAlbumsRecyclerView.setAdapter(recentlyAddedAlbumAdapter);
        homeViewModel.getMostRecentlyAddedAlbums(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.homeRecentlyAddedAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeRecentlyAddedAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeRecentlyAddedAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.homeRecentlyAddedAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);

                recentlyAddedAlbumAdapter.setItems(albums);
            }
        });

        CustomLinearSnapHelper recentAddedAlbumSnapHelper = new CustomLinearSnapHelper();
        recentAddedAlbumSnapHelper.attachToRecyclerView(bind.recentlyAddedAlbumsRecyclerView);
    }

    public void initPinnedPlaylistsView() {
        homeViewModel.getPinnedPlaylistList(getViewLifecycleOwner(), 5, true).observe(getViewLifecycleOwner(), playlists -> {
            if (bind != null && playlists != null) {
                for (Playlist playlist : playlists) {
                    int playlistViewHashCode = playlist.getId().hashCode();
                    if (requireView().findViewById(playlistViewHashCode) == null) {
                        View genericPlaylistView = activity.getLayoutInflater().inflate(R.layout.generic_playlist_sector, null);
                        genericPlaylistView.setId(playlistViewHashCode);
                        genericPlaylistView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        TextView genericPlaylistTitleTextView = genericPlaylistView.findViewById(R.id.generic_playlist_title_text_view);
                        TextView genericPlaylistCickableTextView = genericPlaylistView.findViewById(R.id.generic_playlist_text_view_clickable);
                        RecyclerView genericPlaylistRecyclerView = genericPlaylistView.findViewById(R.id.generic_playlist_recycler_view);

                        genericPlaylistTitleTextView.setText(MusicUtil.getReadableString(playlist.getName()));
                        genericPlaylistRecyclerView.setHasFixedSize(true);

                        SongHorizontalAdapter trackAdapter = new SongHorizontalAdapter(requireContext(), this, true);
                        genericPlaylistRecyclerView.setAdapter(trackAdapter);

                        homeViewModel.getPlaylistSongLiveList(playlist.getId()).observe(getViewLifecycleOwner(), songs -> {
                            if (songs.size() > 0) {
                                int songsNumber = Math.min(20, songs.size());

                                genericPlaylistRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(songsNumber, 5), GridLayoutManager.HORIZONTAL, false));
                                trackAdapter.setItems(songs.subList(0, songsNumber));
                            }
                        });

                        genericPlaylistCickableTextView.setOnClickListener(view -> {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("playlist_object", playlist);
                            bundle.putBoolean("is_offline", false);
                            activity.navController.navigate(R.id.action_homeFragment_to_playlistPageFragment, bundle);
                        });

                        SnapHelper genericPlaylistSnapHelper = new PagerSnapHelper();
                        genericPlaylistSnapHelper.attachToRecyclerView(genericPlaylistRecyclerView);

                        genericPlaylistRecyclerView.addItemDecoration(
                                new DotsIndicatorDecoration(
                                        getResources().getDimensionPixelSize(R.dimen.radius),
                                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                                        requireContext().getResources().getColor(R.color.titleTextColor, null))
                        );


                        bind.homeLinearLayoutContainer.addView(genericPlaylistView);
                    }
                }
            }
        });
    }

    private void initNewestPodcastsView() {
        bind.newestPodcastsViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        podcastEpisodeAdapter = new PodcastEpisodeAdapter(requireContext(), this);
        bind.newestPodcastsViewPager.setAdapter(podcastEpisodeAdapter);
        bind.newestPodcastsViewPager.setOffscreenPageLimit(1);
        homeViewModel.getNewestPodcastEpisodes(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), podcastEpisodes -> {
            if (podcastEpisodes == null) {
                if (bind != null) bind.homeNewestPodcastsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeNewestPodcastsSector.setVisibility(!podcastEpisodes.isEmpty() ? View.VISIBLE : View.GONE);

                podcastEpisodeAdapter.setItems(podcastEpisodes);
            }
        });

        setSlideViewOffset(bind.newestPodcastsViewPager, 20, 16);
    }

    private void initGridView() {
        bind.gridTracksRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        bind.gridTracksRecyclerView.addItemDecoration(new GridItemDecoration(3, 8, false));
        bind.gridTracksRecyclerView.setHasFixedSize(true);

        gridTrackAdapter = new GridTrackAdapter(requireContext(), this);
        bind.gridTracksRecyclerView.setAdapter(gridTrackAdapter);

        homeViewModel.getGridSongSample(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), chronologies -> {
            if (chronologies == null || chronologies.size() == 0) {
                if (bind != null) bind.homeGridTracksSector.setVisibility(View.GONE);
                if (bind != null) bind.afterGridDivider.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeGridTracksSector.setVisibility(View.VISIBLE);
                if (bind != null) bind.afterGridDivider.setVisibility(View.VISIBLE);
                gridTrackAdapter.setItems(chronologies);
            }
        });
    }

    private void setSlideViewOffset(ViewPager2 viewPager, float pageOffset, float pageMargin) {
        viewPager.setPageTransformer((page, position) -> {
            float myOffset = position * -(2 * pageOffset + pageMargin);
            if (viewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.setTranslationX(-myOffset);
                } else {
                    page.setTranslationX(myOffset);
                }
            } else {
                page.setTranslationY(myOffset);
            }
        });
    }

    public void reorder() {
        if (bind != null) {
            // bind.homeLinearLayoutContainer.removeAllViews();
            // bind.homeLinearLayoutContainer.addView(bind.homeDiscoverSector);

            // bind.homeLinearLayoutContainer.addView(bind.homeSimilarTracksSector);
            // bind.homeLinearLayoutContainer.addView(bind.homeRadioArtistSector);
            // bind.homeLinearLayoutContainer.addView(bind.homeGridTracksSector);
            // bind.homeLinearLayoutContainer.addView(bind.starredTracksSector);
            // bind.homeLinearLayoutContainer.addView(bind.starredAlbumsSector);
            // bind.homeLinearLayoutContainer.addView(bind.starredArtistsSector);

            // bind.homeLinearLayoutContainer.addView(bind.homeRecentlyAddedAlbumsSector);
            // bind.homeLinearLayoutContainer.addView(bind.homeFlashbackSector);
            // bind.homeLinearLayoutContainer.addView(bind.homeMostPlayedAlbumsSector);
            // bind.homeLinearLayoutContainer.addView(bind.homeRecentlyPlayedAlbumsSector);
            // bind.homeLinearLayoutContainer.addView(bind.homeNewestPodcastsSector);
        }
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @Override
    public void onMediaClick(Bundle bundle) {
        if (bundle.containsKey("is_mix")) {
            MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), bundle.getParcelable("song_object"));
            activity.setBottomSheetInPeek(true);

            if (mediaBrowserListenableFuture != null) {
                homeViewModel.getMediaInstantMix(getViewLifecycleOwner(), bundle.getParcelable("song_object")).observe(getViewLifecycleOwner(), songs -> {
                    if (songs.size() > 0) {
                        MediaManager.enqueue(mediaBrowserListenableFuture, requireContext(), songs, true);
                    }
                });
            }
        } else if (bundle.containsKey("is_chronology")) {
            List<Child> media = bundle.getParcelableArrayList("songs_object");
            MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), media, bundle.getInt("position"));
            activity.setBottomSheetInPeek(true);
        } else {
            MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), bundle.getParcelableArrayList("songs_object"), bundle.getInt("position"));
            activity.setBottomSheetInPeek(true);
        }
    }

    @Override
    public void onMediaLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songBottomSheetDialog, bundle);
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
        if (bundle.containsKey("is_mix") && bundle.getBoolean("is_mix")) {
            Snackbar.make(requireView(), R.string.artist_adapter_radio_station_starting, Snackbar.LENGTH_LONG)
                    .setAnchorView(activity.bind.playerBottomSheet)
                    .show();

            if (mediaBrowserListenableFuture != null) {
                homeViewModel.getArtistInstantMix(bundle.getParcelable("artist_object")).observe(getViewLifecycleOwner(), songs -> {
                    if (songs.size() > 0) {
                        MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), songs, 0);
                        activity.setBottomSheetInPeek(true);
                    }
                });
            }
        } else if (bundle.containsKey("is_best_of") && bundle.getBoolean("is_best_of")) {
            if (mediaBrowserListenableFuture != null) {
                homeViewModel.getArtistBestOf(bundle.getParcelable("artist_object")).observe(getViewLifecycleOwner(), songs -> {
                    if (songs.size() > 0) {
                        MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), songs, 0);
                        activity.setBottomSheetInPeek(true);
                    }
                });
            }
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.artistPageFragment, bundle);
        }
    }

    @Override
    public void onArtistLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistBottomSheetDialog, bundle);
    }

    @Override
    public void onYearClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songListPageFragment, bundle);
    }

    @Override
    public void onPodcastClick(Bundle bundle) {
        MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), bundle.getParcelable("podcast_object"));
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onPodcastLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.podcastBottomSheetDialog, bundle);
    }
}
