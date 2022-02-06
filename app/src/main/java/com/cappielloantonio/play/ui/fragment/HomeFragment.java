package com.cappielloantonio.play.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
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
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
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
import com.cappielloantonio.play.adapter.PodcastEpisodeAdapter;
import com.cappielloantonio.play.adapter.SimilarTrackAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.adapter.YearAdapter;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.HomeViewModel;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding bind;
    private MainActivity activity;
    private HomeViewModel homeViewModel;

    private DiscoverSongAdapter discoverSongAdapter;
    private SimilarTrackAdapter similarMusicAdapter;
    private ArtistAdapter radioArtistAdapter;
    private SongHorizontalAdapter starredSongAdapter;
    private AlbumHorizontalAdapter starredAlbumAdapter;
    private ArtistHorizontalAdapter starredArtistAdapter;
    private AlbumAdapter recentlyAddedAlbumAdapter;
    private AlbumAdapter recentlyPlayedAlbumAdapter;
    private AlbumAdapter mostPlayedAlbumAdapter;
    private AlbumHorizontalAdapter newReleasesAlbumAdapter;
    private YearAdapter yearAdapter;
    private PodcastEpisodeAdapter podcastEpisodeAdapter;

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
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
        activity.setBottomNavigationBarVisibility(true);
        activity.setBottomSheetVisibility(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        setMediaBrowserListenableFuture();
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
            homeViewModel.refreshDiscoverySongSample(requireActivity());
            return true;
        });

        bind.similarTracksTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshSimilarSongSample(requireActivity());
            return true;
        });

        bind.recentlyRadioArtistTextViewRefreshable.setOnLongClickListener(v -> {
            homeViewModel.refreshRadioArtistSample(requireActivity());
            return true;
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

        setSlideViewOffset(bind.discoverSongViewPager, 20, 16);
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

    private void initArtistRadio() {
        bind.radioArtistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        bind.radioArtistRecyclerView.setHasFixedSize(true);

        radioArtistAdapter = new ArtistAdapter((MainActivity) requireActivity(), requireContext());
        bind.radioArtistRecyclerView.setAdapter(radioArtistAdapter);
        homeViewModel.getStarredArtistsSample().observe(requireActivity(), artists -> {
            if (artists == null) {
                if (bind != null) bind.homeRadioArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeRadioArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeRadioArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeRadioArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);

                radioArtistAdapter.setItems(artists);
            }
        });

        CustomLinearSnapHelper artistRadioSnapHelper = new CustomLinearSnapHelper();
        artistRadioSnapHelper.attachToRecyclerView(bind.radioArtistRecyclerView);
    }

    private void initStarredTracksView() {
        bind.starredTracksRecyclerView.setHasFixedSize(true);

        starredSongAdapter = new SongHorizontalAdapter(activity, requireContext(), true);
        bind.starredTracksRecyclerView.setAdapter(starredSongAdapter);
        homeViewModel.getStarredTracks(requireActivity()).observe(requireActivity(), songs -> {
            if (songs == null) {
                if (bind != null) bind.starredTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.starredTracksSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.starredTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.starredTracksSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
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

        starredAlbumAdapter = new AlbumHorizontalAdapter(requireContext(), false);
        bind.starredAlbumsRecyclerView.setAdapter(starredAlbumAdapter);
        homeViewModel.getStarredAlbums(requireActivity()).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.starredAlbumsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.starredAlbumsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.starredAlbumsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.starredAlbumsSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
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

        starredArtistAdapter = new ArtistHorizontalAdapter(requireContext(), false);
        bind.starredArtistsRecyclerView.setAdapter(starredArtistAdapter);
        homeViewModel.getStarredArtists(requireActivity()).observe(requireActivity(), artists -> {
            if (artists == null) {
                if (bind != null) bind.starredArtistsPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.starredArtistsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.starredArtistsPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.starredArtistsSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);
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

        newReleasesAlbumAdapter = new AlbumHorizontalAdapter(requireContext(), false);
        bind.newReleasesRecyclerView.setAdapter(newReleasesAlbumAdapter);
        homeViewModel.getRecentlyReleasedAlbums(requireActivity()).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.homeNewReleasesPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.homeNewReleasesSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeNewReleasesPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.homeNewReleasesSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
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

    public void initPinnedPlaylistsView() {
        homeViewModel.getPinnedPlaylistList(requireActivity(), 5, true).observe(requireActivity(), playlists -> {
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

                        SongHorizontalAdapter trackAdapter = new SongHorizontalAdapter(activity, requireContext(), true);
                        genericPlaylistRecyclerView.setAdapter(trackAdapter);

                        homeViewModel.getPlaylistSongLiveList(playlist.getId()).observe(requireActivity(), songs -> {
                            if (songs.size() > 0) {
                                int songsNumber = Math.min(20, songs.size());

                                genericPlaylistRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(songsNumber, 5), GridLayoutManager.HORIZONTAL, false));
                                trackAdapter.setItems(songs.subList(0, songsNumber));
                            }
                        });

                        trackAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);

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

        podcastEpisodeAdapter = new PodcastEpisodeAdapter(activity, requireContext());
        bind.newestPodcastsViewPager.setAdapter(podcastEpisodeAdapter);
        bind.newestPodcastsViewPager.setOffscreenPageLimit(1);
        homeViewModel.getNewestPodcastEpisodes(requireActivity()).observe(requireActivity(), podcastEpisodes -> {
            if (podcastEpisodes == null) {
                if (bind != null) bind.homeNewestPodcastsSector.setVisibility(View.GONE);
            } else {
                if (bind != null) bind.homeNewestPodcastsSector.setVisibility(!podcastEpisodes.isEmpty() ? View.VISIBLE : View.GONE);

                podcastEpisodeAdapter.setItems(podcastEpisodes);
            }
        });

        setSlideViewOffset(bind.newestPodcastsViewPager, 20, 16);
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
            bind.homeLinearLayoutContainer.removeAllViews();
            bind.homeLinearLayoutContainer.addView(bind.homeDiscoverSector);
            bind.homeLinearLayoutContainer.addView(bind.homeSimilarTracksSector);
            bind.homeLinearLayoutContainer.addView(bind.homeRecentlyAddedAlbumsSector);
            bind.homeLinearLayoutContainer.addView(bind.homeFlashbackSector);
            bind.homeLinearLayoutContainer.addView(bind.homeMostPlayedAlbumsSector);
            bind.homeLinearLayoutContainer.addView(bind.homeRecentlyPlayedAlbumsSector);
            bind.homeLinearLayoutContainer.addView(bind.homeNewestPodcastsSector);
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void setMediaBrowserListenableFuture() {
        discoverSongAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
        similarMusicAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
        starredSongAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
        radioArtistAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
    }
}
