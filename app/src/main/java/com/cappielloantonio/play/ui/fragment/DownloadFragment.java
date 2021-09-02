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
import com.cappielloantonio.play.adapter.ArtistHorizontalAdapter;
import com.cappielloantonio.play.adapter.DiscoverSongAdapter;
import com.cappielloantonio.play.adapter.SimilarTrackAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.adapter.TrackAdapter;
import com.cappielloantonio.play.adapter.YearAdapter;
import com.cappielloantonio.play.databinding.FragmentDownloadBinding;
import com.cappielloantonio.play.databinding.FragmentHomeBinding;
import com.cappielloantonio.play.helper.recyclerview.CustomLinearSnapHelper;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.DownloadViewModel;
import com.cappielloantonio.play.viewmodel.HomeViewModel;

import java.util.Objects;

public class DownloadFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";

    private FragmentDownloadBinding bind;
    private MainActivity activity;
    private DownloadViewModel downloadViewModel;

    private ArtistHorizontalAdapter downloadedArtistAdapter;
    private AlbumHorizontalAdapter downloadedAlbumAdapter;
    private SongHorizontalAdapter downloadedTrackAdapter;

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

        bind = FragmentDownloadBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        downloadViewModel = new ViewModelProvider(requireActivity()).get(DownloadViewModel.class);

        init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAppBar();
        initDownloadedArtistView();
        initDownloadedAlbumView();
        initDownloadedSongView();
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
                activity.navController.navigate(R.id.action_downloadFragment_to_searchFragment);
                return true;
            case R.id.action_settings:
                activity.navController.navigate(R.id.action_downloadFragment_to_settingsFragment);
                return true;
            default:
                break;
        }

        return false;
    }

    private void init() {
        bind.downloadedArtistTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Artist.DOWNLOADED, Artist.DOWNLOADED);
            activity.navController.navigate(R.id.action_downloadFragment_to_artistListPageFragment, bundle);
        });

        bind.downloadedAlbumTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Album.DOWNLOADED, Album.DOWNLOADED);
            activity.navController.navigate(R.id.action_downloadFragment_to_albumListPageFragment, bundle);
        });

        bind.downloadedTracksTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Song.DOWNLOADED, Song.DOWNLOADED);
            activity.navController.navigate(R.id.action_downloadFragment_to_songListPageFragment, bundle);
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);
        Objects.requireNonNull(bind.toolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initDownloadedArtistView() {
        bind.downloadedArtistRecyclerView.setHasFixedSize(true);

        downloadedArtistAdapter = new ArtistHorizontalAdapter(requireContext(), false);
        bind.downloadedArtistRecyclerView.setAdapter(downloadedArtistAdapter);
        downloadViewModel.getDownloadedArtists(requireActivity(), 20).observe(requireActivity(), artists -> {
            if (artists == null) {
                if (bind != null) bind.downloadDownloadedArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.downloadDownloadedArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null && !artists.isEmpty()) bind.emptyDownloadLayout.setVisibility(View.GONE);
                if (bind != null && !artists.isEmpty()) bind.fragmentDownloadNestedScrollView.setVisibility(View.VISIBLE);

                if (bind != null) bind.downloadDownloadedArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.downloadDownloadedArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null) bind.downloadedArtistRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(artists.size(), 5), GridLayoutManager.HORIZONTAL, false));

                downloadedArtistAdapter.setItems(artists);
            }
        });

        SnapHelper starredArtistSnapHelper = new PagerSnapHelper();
        starredArtistSnapHelper.attachToRecyclerView(bind.downloadedArtistRecyclerView);

        bind.downloadedArtistRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
    }

    private void initDownloadedAlbumView() {
        bind.downloadedAlbumRecyclerView.setHasFixedSize(true);

        downloadedAlbumAdapter = new AlbumHorizontalAdapter(requireContext(), true);
        bind.downloadedAlbumRecyclerView.setAdapter(downloadedAlbumAdapter);
        downloadViewModel.getDownloadedAlbums(requireActivity(), 20).observe(requireActivity(), albums -> {
            if (albums == null) {
                if (bind != null) bind.downloadDownloadedAlbumPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.downloadDownloadedAlbumSector.setVisibility(View.GONE);
            } else {
                if (bind != null && !albums.isEmpty()) bind.emptyDownloadLayout.setVisibility(View.GONE);
                if (bind != null && !albums.isEmpty()) bind.fragmentDownloadNestedScrollView.setVisibility(View.VISIBLE);

                if (bind != null) bind.downloadDownloadedAlbumPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.downloadDownloadedAlbumSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null) bind.downloadedAlbumRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(albums.size(), 5), GridLayoutManager.HORIZONTAL, false));

                downloadedAlbumAdapter.setItems(albums);
            }
        });

        SnapHelper starredAlbumSnapHelper = new PagerSnapHelper();
        starredAlbumSnapHelper.attachToRecyclerView(bind.downloadedAlbumRecyclerView);

        bind.downloadedAlbumRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
    }

    private void initDownloadedSongView() {
        bind.downloadedTracksRecyclerView.setHasFixedSize(true);

        downloadedTrackAdapter = new SongHorizontalAdapter(activity, requireContext(), true);
        bind.downloadedTracksRecyclerView.setAdapter(downloadedTrackAdapter);
        downloadViewModel.getDownloadedTracks(requireActivity(), 20).observe(requireActivity(), songs -> {
            if (songs == null) {
                if (bind != null) bind.downloadDownloadedTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.downloadDownloadedTracksSector.setVisibility(View.GONE);
            } else {
                if (bind != null && !songs.isEmpty()) bind.emptyDownloadLayout.setVisibility(View.GONE);
                if (bind != null && !songs.isEmpty()) bind.fragmentDownloadNestedScrollView.setVisibility(View.VISIBLE);

                if (bind != null) bind.downloadDownloadedTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null) bind.downloadDownloadedTracksSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null) bind.downloadedTracksRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(songs.size(), 5), GridLayoutManager.HORIZONTAL, false));

                downloadedTrackAdapter.setItems(songs);
            }
        });

        SnapHelper starredTrackSnapHelper = new PagerSnapHelper();
        starredTrackSnapHelper.attachToRecyclerView(bind.downloadedTracksRecyclerView);

        bind.downloadedTracksRecyclerView.addItemDecoration(
                new DotsIndicatorDecoration(
                        getResources().getDimensionPixelSize(R.dimen.radius),
                        getResources().getDimensionPixelSize(R.dimen.radius) * 4,
                        getResources().getDimensionPixelSize(R.dimen.dots_height),
                        requireContext().getResources().getColor(R.color.titleTextColor, null),
                        requireContext().getResources().getColor(R.color.titleTextColor, null))
        );
    }
}
