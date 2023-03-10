package com.cappielloantonio.play.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.AlbumHorizontalAdapter;
import com.cappielloantonio.play.adapter.ArtistHorizontalAdapter;
import com.cappielloantonio.play.adapter.PlaylistHorizontalAdapter;
import com.cappielloantonio.play.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentDownloadBinding;
import com.cappielloantonio.play.helper.recyclerview.DotsIndicatorDecoration;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.DownloadViewModel;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;

@UnstableApi
public class DownloadFragment extends Fragment implements ClickCallback {
    private FragmentDownloadBinding bind;
    private MainActivity activity;
    private DownloadViewModel downloadViewModel;

    private ArtistHorizontalAdapter downloadedArtistAdapter;
    private AlbumHorizontalAdapter downloadedAlbumAdapter;
    private SongHorizontalAdapter downloadedTrackAdapter;
    private PlaylistHorizontalAdapter playlistHorizontalAdapter;

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
        initDownloadedPlaylistSlideView();
        initPlaceholder();
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
            activity.navController.navigate(R.id.action_downloadFragment_to_searchFragment);
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            activity.navController.navigate(R.id.action_downloadFragment_to_settingsFragment);
            return true;
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
            bundle.putString(Media.DOWNLOADED, Media.DOWNLOADED);
            activity.navController.navigate(R.id.action_downloadFragment_to_songListPageFragment, bundle);
        });

        bind.downloadedPlaylistTextViewClickable.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Playlist.DOWNLOADED, Playlist.DOWNLOADED);
            activity.navController.navigate(R.id.action_downloadFragment_to_playlistCatalogueFragment, bundle);
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);
        Objects.requireNonNull(bind.toolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initDownloadedArtistView() {
        bind.downloadedArtistRecyclerView.setHasFixedSize(true);

        downloadedArtistAdapter = new ArtistHorizontalAdapter(requireContext(), this);
        bind.downloadedArtistRecyclerView.setAdapter(downloadedArtistAdapter);
        downloadViewModel.getDownloadedArtists(getViewLifecycleOwner(), 20).observe(getViewLifecycleOwner(), artists -> {
            if (artists == null) {
                if (bind != null)
                    bind.downloadDownloadedArtistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.downloadDownloadedArtistSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.downloadDownloadedArtistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.downloadDownloadedArtistSector.setVisibility(!artists.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.downloadedArtistRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(artists.size(), 5), GridLayoutManager.HORIZONTAL, false));

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

        downloadedAlbumAdapter = new AlbumHorizontalAdapter(requireContext(), this, true);
        bind.downloadedAlbumRecyclerView.setAdapter(downloadedAlbumAdapter);
        downloadViewModel.getDownloadedAlbums(getViewLifecycleOwner(), 20).observe(getViewLifecycleOwner(), albums -> {
            if (albums == null) {
                if (bind != null)
                    bind.downloadDownloadedAlbumPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.downloadDownloadedAlbumSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.downloadDownloadedAlbumPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.downloadDownloadedAlbumSector.setVisibility(!albums.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.downloadedAlbumRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(albums.size(), 5), GridLayoutManager.HORIZONTAL, false));

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

        downloadedTrackAdapter = new SongHorizontalAdapter(requireContext(), this, true);
        bind.downloadedTracksRecyclerView.setAdapter(downloadedTrackAdapter);
        downloadViewModel.getDownloadedTracks(getViewLifecycleOwner(), 20).observe(getViewLifecycleOwner(), songs -> {
            if (songs == null) {
                if (bind != null)
                    bind.downloadDownloadedTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.downloadDownloadedTracksSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.downloadDownloadedTracksPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.downloadDownloadedTracksSector.setVisibility(!songs.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.downloadedTracksRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), UIUtil.getSpanCount(songs.size(), 5), GridLayoutManager.HORIZONTAL, false));

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

    private void initDownloadedPlaylistSlideView() {
        bind.downloadedPlaylistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.downloadedPlaylistRecyclerView.setHasFixedSize(true);

        playlistHorizontalAdapter = new PlaylistHorizontalAdapter(requireContext(), this);
        bind.downloadedPlaylistRecyclerView.setAdapter(playlistHorizontalAdapter);
        downloadViewModel.getDownloadedPlaylists(getViewLifecycleOwner(), 5).observe(getViewLifecycleOwner(), playlists -> {
            if (playlists == null) {
                if (bind != null)
                    bind.downloadDownloadedPlaylistPlaceholder.placeholder.setVisibility(View.VISIBLE);
                if (bind != null) bind.downloadDownloadedPlaylistSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.downloadDownloadedPlaylistPlaceholder.placeholder.setVisibility(View.GONE);
                if (bind != null)
                    bind.downloadDownloadedPlaylistSector.setVisibility(!playlists.isEmpty() ? View.VISIBLE : View.GONE);

                // TODO
                // playlistHorizontalAdapter.setItems(playlists);
            }
        });
    }

    private void initPlaceholder() {
        downloadViewModel.getDownloadedTracks(getViewLifecycleOwner(), 20).observe(getViewLifecycleOwner(), songs -> {
            if ((songs != null && !songs.isEmpty())) {
                if (bind != null) bind.emptyDownloadLayout.setVisibility(View.GONE);
                if (bind != null) bind.fragmentDownloadNestedScrollView.setVisibility(View.VISIBLE);
            } else {
                if (bind != null) bind.emptyDownloadLayout.setVisibility(View.VISIBLE);
                if (bind != null) bind.fragmentDownloadNestedScrollView.setVisibility(View.GONE);
            }
        });
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @Override
    public void onMediaClick(Bundle bundle) {
        MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), bundle.getParcelableArrayList("songs_object"), bundle.getInt("position"));
        activity.setBottomSheetInPeek(true);
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
        Navigation.findNavController(requireView()).navigate(R.id.albumListPageFragment, bundle);
    }

    @Override
    public void onArtistLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.artistBottomSheetDialog, bundle);
    }

    @Override
    public void onPlaylistClick(Bundle bundle) {
        bundle.putBoolean("is_offline", true);
        Navigation.findNavController(requireView()).navigate(R.id.playlistPageFragment, bundle);
    }
}
