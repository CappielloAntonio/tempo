package com.cappielloantonio.tempo.ui.fragment;

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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentDirectoryBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.interfaces.DialogClickCallback;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.MusicDirectoryAdapter;
import com.cappielloantonio.tempo.ui.dialog.DownloadDirectoryDialog;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.viewmodel.DirectoryViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.stream.Collectors;

@UnstableApi
public class DirectoryFragment extends Fragment implements ClickCallback {
    private static final String TAG = "DirectoryFragment";

    private FragmentDirectoryBinding bind;
    private MainActivity activity;
    private DirectoryViewModel directoryViewModel;

    private MusicDirectoryAdapter musicDirectoryAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    private MenuItem menuItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.directory_page_menu, menu);

        menuItem = menu.getItem(0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentDirectoryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        directoryViewModel = new ViewModelProvider(requireActivity()).get(DirectoryViewModel.class);

        initAppBar();
        initDirectoryListView();

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
        if (item.getItemId() == R.id.action_download_directory) {
            DownloadDirectoryDialog dialog = new DownloadDirectoryDialog(new DialogClickCallback() {
                @Override
                public void onPositiveClick() {
                    directoryViewModel.loadMusicDirectory(getArguments().getString(Constants.MUSIC_DIRECTORY_ID)).observe(getViewLifecycleOwner(), directory -> {
                        if (isVisible() && getActivity() != null) {
                            List<Child> songs = directory.getChildren().stream().filter(child -> !child.isDir()).collect(Collectors.toList());
                            DownloadUtil.getDownloadTracker(requireContext()).download(
                                    MappingUtil.mapDownloads(songs),
                                    songs.stream().map(Download::new).collect(Collectors.toList())
                            );
                        }
                    });
                }
            });

            dialog.show(activity.getSupportFragmentManager(), null);

            return true;
        }

        return false;
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (bind != null) {
            bind.toolbar.setNavigationOnClickListener(v -> activity.navController.navigateUp());
            bind.directoryBackImageView.setOnClickListener(v -> activity.navController.navigateUp());
        }
    }

    private void initDirectoryListView() {
        bind.directoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.directoryRecyclerView.setHasFixedSize(true);

        musicDirectoryAdapter = new MusicDirectoryAdapter(this);
        bind.directoryRecyclerView.setAdapter(musicDirectoryAdapter);
        directoryViewModel.loadMusicDirectory(getArguments().getString(Constants.MUSIC_DIRECTORY_ID)).observe(getViewLifecycleOwner(), directory -> {
            bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
                if ((bind.directoryInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                    bind.toolbar.setTitle(directory.getName());
                } else {
                    bind.toolbar.setTitle(R.string.empty_string);
                }
            });

            bind.directoryTitleLabel.setText(directory.getName());

            musicDirectoryAdapter.setItems(directory.getChildren());

            menuItem.setVisible(
                    directory.getChildren() != null && directory.getChildren()
                            .stream()
                            .filter(child -> !child.isDir())
                            .findFirst()
                            .orElse(null) != null
            );
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
        Navigation.findNavController(requireView()).navigate(R.id.directoryFragment, bundle);
    }
}