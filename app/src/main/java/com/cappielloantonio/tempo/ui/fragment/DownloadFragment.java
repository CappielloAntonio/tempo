package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentDownloadBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.DownloadHorizontalAdapter;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.viewmodel.DownloadViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;

@UnstableApi
public class DownloadFragment extends Fragment implements ClickCallback {
    private FragmentDownloadBinding bind;
    private MainActivity activity;
    private DownloadViewModel downloadViewModel;

    private DownloadHorizontalAdapter downloadHorizontalAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    private MaterialToolbar materialToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentDownloadBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        downloadViewModel = new ViewModelProvider(requireActivity()).get(DownloadViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAppBar();
        initDownloadedSongView();
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

    private void initAppBar() {
        materialToolbar = bind.getRoot().findViewById(R.id.toolbar);

        activity.setSupportActionBar(materialToolbar);
        Objects.requireNonNull(materialToolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initDownloadedSongView() {
        bind.downloadedTracksRecyclerView.setHasFixedSize(true);

        downloadHorizontalAdapter = new DownloadHorizontalAdapter(this);
        bind.downloadedTracksRecyclerView.setAdapter(downloadHorizontalAdapter);
        downloadViewModel.getDownloadedTracks(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                if (songs.isEmpty()) {
                    if (bind != null) {
                        bind.emptyDownloadLayout.setVisibility(View.VISIBLE);
                        bind.fragmentDownloadNestedScrollView.setVisibility(View.GONE);

                        bind.downloadDownloadedTracksPlaceholder.placeholder.setVisibility(View.VISIBLE);
                        bind.downloadDownloadedTracksSector.setVisibility(View.GONE);
                    }
                } else {
                    if (bind != null) {
                        bind.emptyDownloadLayout.setVisibility(View.GONE);
                        bind.fragmentDownloadNestedScrollView.setVisibility(View.VISIBLE);

                        bind.downloadDownloadedTracksPlaceholder.placeholder.setVisibility(View.GONE);
                        bind.downloadDownloadedTracksSector.setVisibility(View.VISIBLE);

                        bind.downloadedTracksRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                        downloadHorizontalAdapter.setItems(songs);
                    }
                }

                if (bind != null) bind.loadingProgressBar.setVisibility(View.GONE);
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
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onMediaLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.songBottomSheetDialog, bundle);
    }
}
