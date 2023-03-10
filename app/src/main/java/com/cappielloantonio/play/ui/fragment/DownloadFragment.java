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

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.ui.adapter.DownloadHorizontalAdapter;
import com.cappielloantonio.play.databinding.FragmentDownloadBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.viewmodel.DownloadViewModel;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;

@UnstableApi
public class DownloadFragment extends Fragment implements ClickCallback {
    private FragmentDownloadBinding bind;
    private MainActivity activity;
    private DownloadViewModel downloadViewModel;

    private DownloadHorizontalAdapter downloadHorizontalAdapter;

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

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);
        Objects.requireNonNull(bind.toolbar.getOverflowIcon()).setTint(requireContext().getResources().getColor(R.color.titleTextColor, null));
    }

    private void initDownloadedSongView() {
        bind.downloadedTracksRecyclerView.setHasFixedSize(true);

        downloadHorizontalAdapter = new DownloadHorizontalAdapter(this);
        bind.downloadedTracksRecyclerView.setAdapter(downloadHorizontalAdapter);
        downloadViewModel.getDownloadedTracks(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), songs -> {
            if (songs == null || songs.isEmpty()) {
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
}
