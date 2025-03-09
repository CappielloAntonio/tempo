package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentPlaylistPageBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.SongHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.PlaylistPageViewModel;
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
        inflater.inflate(R.menu.playlist_page_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                songHorizontalAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setPadding(-32, 0, 0, 0);

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
            playlistPageViewModel.getPlaylistSongLiveList().observe(getViewLifecycleOwner(), songs -> {
                if (isVisible() && getActivity() != null) {
                    DownloadUtil.getDownloadTracker(requireContext()).download(
                        MappingUtil.mapDownloads(songs),
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
        } else if (item.getItemId() == R.id.action_add_to_queue) {
            playlistPageViewModel.getPlaylistSongLiveList().observe(getViewLifecycleOwner(), songs -> {
                MediaManager.enqueue(mediaBrowserListenableFuture, songs, false);
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

        bind.animToolbar.setTitle(playlistPageViewModel.getPlaylist().getName());

        bind.playlistNameLabel.setText(playlistPageViewModel.getPlaylist().getName());
        bind.playlistSongCountLabel.setText(getString(R.string.playlist_song_count, playlistPageViewModel.getPlaylist().getSongCount()));
        bind.playlistDurationLabel.setText(getString(R.string.playlist_duration, MusicUtil.getReadableDurationString(playlistPageViewModel.getPlaylist().getDuration(), false)));

        bind.animToolbar.setNavigationOnClickListener(v -> {
            hideKeyboard(v);
            activity.navController.navigateUp();
        });

        Objects.requireNonNull(bind.animToolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initMusicButton() {
        playlistPageViewModel.getPlaylistSongLiveList().observe(getViewLifecycleOwner(), songs -> {
            if (bind != null) {
                bind.playlistPagePlayButton.setOnClickListener(v -> {
                    MediaManager.startQueue(mediaBrowserListenableFuture, songs.subList(0, Math.min(100, songs.size())), 0);
                    activity.setBottomSheetInPeek(true);
                });

                bind.playlistPageShuffleButton.setOnClickListener(v -> {
                    Collections.shuffle(songs);
                    MediaManager.startQueue(mediaBrowserListenableFuture, songs.subList(0, Math.min(100, songs.size())), 0);
                    activity.setBottomSheetInPeek(true);
                });
            }
        });
    }

    private void initBackCover() {
        playlistPageViewModel.getPlaylistSongLiveList().observe(requireActivity(), songs -> {
            if (bind != null && songs != null && !songs.isEmpty()) {
                Collections.shuffle(songs);

                // Pic top-left
                CustomGlideRequest.Builder
                        .from(requireContext(), !songs.isEmpty() ? songs.get(0).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.ResourceType.Song)
                        .build()
                        .transform(new GranularRoundedCorners(CustomGlideRequest.CORNER_RADIUS, 0, 0, 0))
                        .into(bind.playlistCoverImageViewTopLeft);

                // Pic top-right
                CustomGlideRequest.Builder
                        .from(requireContext(), songs.size() > 1 ? songs.get(1).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.ResourceType.Song)
                        .build()
                        .transform(new GranularRoundedCorners(0, CustomGlideRequest.CORNER_RADIUS, 0, 0))
                        .into(bind.playlistCoverImageViewTopRight);

                // Pic bottom-left
                CustomGlideRequest.Builder
                        .from(requireContext(), songs.size() > 2 ? songs.get(2).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.ResourceType.Song)
                        .build()
                        .transform(new GranularRoundedCorners(0, 0, 0, CustomGlideRequest.CORNER_RADIUS))
                        .into(bind.playlistCoverImageViewBottomLeft);

                // Pic bottom-right
                CustomGlideRequest.Builder
                        .from(requireContext(), songs.size() > 3 ? songs.get(3).getCoverArtId() : playlistPageViewModel.getPlaylist().getCoverArtId(), CustomGlideRequest.ResourceType.Song)
                        .build()
                        .transform(new GranularRoundedCorners(0, 0, CustomGlideRequest.CORNER_RADIUS, 0))
                        .into(bind.playlistCoverImageViewBottomRight);
            }
        });
    }

    private void initSongsView() {
        bind.songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.songRecyclerView.setHasFixedSize(true);

        songHorizontalAdapter = new SongHorizontalAdapter(this, true, false, null);
        bind.songRecyclerView.setAdapter(songHorizontalAdapter);

        playlistPageViewModel.getPlaylistSongLiveList().observe(getViewLifecycleOwner(), songs -> songHorizontalAdapter.setItems(songs));
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @Override
    public void onMediaClick(Bundle bundle) {
        MediaManager.startQueue(mediaBrowserListenableFuture, bundle.getParcelableArrayList(Constants.TRACKS_OBJECT), bundle.getInt(Constants.ITEM_POSITION));
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onMediaLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songBottomSheetDialog, bundle);
    }
}