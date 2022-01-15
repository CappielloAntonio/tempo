package com.cappielloantonio.play.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumHorizontalAdapter;
import com.cappielloantonio.play.adapter.ArtistHorizontalAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentStarredBinding;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.StarredViewModel;
import com.google.common.util.concurrent.ListenableFuture;

public class StarredFragment extends Fragment {
    private FragmentStarredBinding bind;
    private MainActivity activity;
    private StarredViewModel starredViewModel;

    private SongHorizontalAdapter starredSongAdapter;
    private AlbumHorizontalAdapter starredAlbumAdapter;
    private ArtistHorizontalAdapter starredArtistAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentStarredBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        starredViewModel = new ViewModelProvider(requireActivity()).get(StarredViewModel.class);

        init();
        initAppBar();
        initStarredTracksView();
        initStarredAlbumsView();
        initStarredArtistsView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
        activity.setBottomNavigationBarVisibility(false);
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

    private void init() {
        bind.starredTracksTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.STARRED, Song.STARRED);
            activity.navController.navigate(R.id.action_starredFragment_to_songListPageFragment, bundle);
        });

        bind.starredAlbumsTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Album.STARRED, Album.STARRED);
            activity.navController.navigate(R.id.action_starredFragment_to_albumListPageFragment, bundle);
        });

        bind.starredArtistsTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Artist.STARRED, Artist.STARRED);
            activity.navController.navigate(R.id.action_starredFragment_to_artistListPageFragment, bundle);
        });

        bind.starredTracksTextViewRefreshable.setOnLongClickListener(v -> {
            starredViewModel.refreshStarredTracks(requireActivity());
            return true;
        });

        bind.starredAlbumsTextViewRefreshable.setOnLongClickListener(v -> {
            starredViewModel.refreshStarredAlbums(requireActivity());
            return true;
        });

        bind.starredArtistsTextViewRefreshable.setOnLongClickListener(v -> {
            starredViewModel.refreshStarredArtists(requireActivity());
            return true;
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bind.toolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        bind.toolbar.setTitle(R.string.starred_title);
    }

    private void initStarredTracksView() {
        bind.starredTracksRecyclerView.setHasFixedSize(true);

        starredSongAdapter = new SongHorizontalAdapter(activity, requireContext(), true);
        bind.starredTracksRecyclerView.setAdapter(starredSongAdapter);
        starredViewModel.getStarredTracks(requireActivity()).observe(requireActivity(), songs -> {
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
        starredViewModel.getStarredAlbums(requireActivity()).observe(requireActivity(), albums -> {
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
        starredViewModel.getStarredArtists(requireActivity()).observe(requireActivity(), artists -> {
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

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void setMediaBrowserListenableFuture() {
        starredSongAdapter.setMediaBrowserListenableFuture(mediaBrowserListenableFuture);
    }
}