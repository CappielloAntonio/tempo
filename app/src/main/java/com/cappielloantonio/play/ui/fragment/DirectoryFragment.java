package com.cappielloantonio.play.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentDirectoryBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.subsonic.models.Artist;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.adapter.MusicDirectoryAdapter;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.viewmodel.DirectoryViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.Objects;

@UnstableApi
public class DirectoryFragment extends Fragment implements ClickCallback {
    private static final String TAG = "DirectoryFragment";

    private FragmentDirectoryBinding bind;
    private MainActivity activity;
    private DirectoryViewModel directoryViewModel;

    private MusicDirectoryAdapter musicDirectoryAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentDirectoryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        directoryViewModel = new ViewModelProvider(requireActivity()).get(DirectoryViewModel.class);

        initAppBar();
        initButtons();
        initDirectoryListView();
        init();

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

    private void init() {
        Artist artist = getArguments().getParcelable(Constants.MUSIC_INDEX_OBJECT);

        if (artist != null) {
            directoryViewModel.setMusicDirectoryId(artist.getId());
            directoryViewModel.setMusicDirectoryName(artist.getName());
        }

        directoryViewModel.loadMusicDirectory(getViewLifecycleOwner());
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (bind != null)
            bind.toolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());

        if (bind != null)
            bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                if ((bind.directoryInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                    directoryViewModel.getDirectory().observe(getViewLifecycleOwner(), directory -> {
                        if (directory != null) {
                            bind.toolbar.setTitle(directory.getName());
                        }
                    });
                } else {
                    bind.toolbar.setTitle(R.string.empty_string);
                }
            });

        directoryViewModel.getDirectory().observe(getViewLifecycleOwner(), directory -> {
            if (directory != null) {
                bind.directoryTitleLabel.setText(directory.getName());
            }
        });
    }

    private void initButtons() {
        directoryViewModel.getDirectory().observe(getViewLifecycleOwner(), directory -> {
            if (directory != null && directory.getParentId() != null && !Objects.equals(directory.getParentId(), "-1")) {
                bind.directoryBackImageView.setVisibility(View.VISIBLE);
            } else {
                bind.directoryBackImageView.setVisibility(View.GONE);
            }
        });

        bind.directoryBackImageView.setOnClickListener(v -> directoryViewModel.goBack());
    }

    private void initDirectoryListView() {
        bind.directoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.directoryRecyclerView.setHasFixedSize(true);

        musicDirectoryAdapter = new MusicDirectoryAdapter(this);
        bind.directoryRecyclerView.setAdapter(musicDirectoryAdapter);

        directoryViewModel.getDirectory().observe(getViewLifecycleOwner(), directory -> {
            if (directory != null) {
                musicDirectoryAdapter.setItems(directory.getChildren());
            } else {
                musicDirectoryAdapter.setItems(Collections.emptyList());
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
        MediaManager.startQueue(mediaBrowserListenableFuture, bundle.getParcelableArrayList(Constants.TRACKS_OBJECT), bundle.getInt(Constants.ITEM_POSITION));
    }

    @Override
    public void onMusicDirectoryClick(Bundle bundle) {
        Child child = bundle.getParcelable(Constants.MUSIC_DIRECTORY_OBJECT);

        if (child != null) {
            directoryViewModel.setMusicDirectoryId(child.getId());
            directoryViewModel.setMusicDirectoryName(child.getTitle());
        }
    }

    @Override
    public void onMusicDirectoryLongClick(Bundle bundle) {
        Toast.makeText(requireContext(), "Long click!", Toast.LENGTH_SHORT).show();
    }
}