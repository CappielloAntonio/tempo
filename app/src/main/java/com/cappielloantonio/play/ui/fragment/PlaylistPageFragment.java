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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentPlaylistPageBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.adapter.SongHorizontalAdapter;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlaylistPageViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@UnstableApi
public class PlaylistPageFragment extends Fragment implements ClickCallback {
    private FragmentPlaylistPageBinding bind;
    private MainActivity activity;
    private PlaylistPageViewModel playlistPageViewModel;

    private SongHorizontalAdapter songHorizontalAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.playlist_page_menu, menu);
        initMenuOption(menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentPlaylistPageBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        playlistPageViewModel = new ViewModelProvider(requireActivity()).get(PlaylistPageViewModel.class);

        init();
        initAppBar();
        initMusicButton();
        initBackCover();
        initSongsView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
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
        if (item.getItemId() == R.id.action_download_playlist) {
            playlistPageViewModel.getPlaylistSongLiveList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
                if (isVisible() && getActivity() != null) {
                    DownloadUtil.getDownloadTracker(requireContext()).download(
                            MappingUtil.mapMediaItems(songs, false),
                            songs.stream().map(child -> {
                                Download toDownload = new Download(child);
                                toDownload.setPlaylistId(playlistPageViewModel.getPlaylist().getId());
                                toDownload.setPlaylistName(playlistPageViewModel.getPlaylist().getName());
                                return toDownload;
                            }).collect(Collectors.toList())
                    );
                }
            });
            return true;
        } else if (item.getItemId() == R.id.action_pin_playlist) {
            playlistPageViewModel.setPinned(true);
            return true;
        } else if (item.getItemId() == R.id.action_unpin_playlist) {
            playlistPageViewModel.setPinned(false);
            return true;
        }

        return false;
    }

    private void init() {
        playlistPageViewModel.setPlaylist(requireArguments().getParcelable(Constants.PLAYLIST_OBJECT));
        playlistPageViewModel.setOffline(requireArguments().getBoolean("is_offline"));
    }

    private void initMenuOption(Menu menu) {
        playlistPageViewModel.isPinned(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), isPinned -> {
            menu.findItem(R.id.action_unpin_playlist).setVisible(isPinned);
            menu.findItem(R.id.action_pin_playlist).setVisible(!isPinned);
        });
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.animToolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bind.animToolbar.setTitle(MusicUtil.getReadableString(playlistPageViewModel.getPlaylist().getName()));

        bind.playlistNameLabel.setText(MusicUtil.getReadableString(playlistPageViewModel.getPlaylist().getName()));
        bind.playlistSongCountLabel.setText(getString(R.string.playlist_song_count, playlistPageViewModel.getPlaylist().getSongCount()));
        bind.playlistDurationLabel.setText(getString(R.string.playlist_duration, MusicUtil.getReadableDurationString(playlistPageViewModel.getPlaylist().getDuration(), false)));

        if (playlistPageViewModel.isOffline()) {
            bind.playlistSongCountLabel.setVisibility(View.GONE);
            bind.playlistDurationLabel.setVisibility(View.GONE);
        }

        bind.animToolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        Objects.requireNonNull(bind.animToolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initMusicButton() {
        playlistPageViewModel.getPlaylistSongLiveList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
            if (bind != null) {
                bind.playlistPagePlayButton.setOnClickListener(v -> {
                    MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), songs, 0);
                    activity.setBottomSheetInPeek(true);
                });

                bind.playlistPageShuffleButton.setOnClickListener(v -> {
                    Collections.shuffle(songs);
                    MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), songs, 0);
                    activity.setBottomSheetInPeek(true);
                });
            }
        });
    }

    private void initBackCover() {
        playlistPageViewModel.getPlaylistSongLiveList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
            if (bind != null) {
                Collections.shuffle(songs);

                // Pic top-left
                CustomGlideRequest.Builder
                        .from(requireContext(), songs.size() > 0 ? songs.get(0).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.PLAYLIST_PIC, null)
                        .build()
                        .transform(new CenterCrop(), new GranularRoundedCorners(CustomGlideRequest.CORNER_RADIUS, 0, 0, 0))
                        .into(bind.playlistCoverImageViewTopLeft);

                // Pic top-right
                CustomGlideRequest.Builder
                        .from(requireContext(), songs.size() > 1 ? songs.get(1).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.PLAYLIST_PIC, null)
                        .build()
                        .transform(new CenterCrop(), new GranularRoundedCorners(0, CustomGlideRequest.CORNER_RADIUS, 0, 0))
                        .into(bind.playlistCoverImageViewTopRight);

                // Pic bottom-left
                CustomGlideRequest.Builder
                        .from(requireContext(), songs.size() > 2 ? songs.get(2).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.PLAYLIST_PIC, null)
                        .build()
                        .transform(new CenterCrop(), new GranularRoundedCorners(0, 0, 0, CustomGlideRequest.CORNER_RADIUS))
                        .into(bind.playlistCoverImageViewBottomLeft);

                // Pic bottom-right
                CustomGlideRequest.Builder
                        .from(requireContext(), songs.size() > 3 ? songs.get(3).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.PLAYLIST_PIC, null)
                        .build()
                        .transform(new CenterCrop(), new GranularRoundedCorners(0, 0, CustomGlideRequest.CORNER_RADIUS, 0))
                        .into(bind.playlistCoverImageViewBottomRight);
            }
        });
    }

    private void initSongsView() {
        bind.songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songRecyclerView.setHasFixedSize(true);

        songHorizontalAdapter = new SongHorizontalAdapter(this, true);
        bind.songRecyclerView.setAdapter(songHorizontalAdapter);

        playlistPageViewModel.getPlaylistSongLiveList(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> songHorizontalAdapter.setItems(songs));
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @Override
    public void onMediaClick(Bundle bundle) {
        MediaManager.startQueue(mediaBrowserListenableFuture, requireContext(), bundle.getParcelableArrayList(Constants.TRACKS_OBJECT), bundle.getInt(Constants.ITEM_POSITION));
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onMediaLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songBottomSheetDialog, bundle);
    }
}