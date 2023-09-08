package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;

import com.cappielloantonio.tempo.databinding.InnerFragmentPlayerCoverBinding;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.dialog.PlaylistChooserDialog;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.PlayerBottomSheetViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

@UnstableApi
public class PlayerCoverFragment extends Fragment {
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;
    private InnerFragmentPlayerCoverBinding bind;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    private final Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = InnerFragmentPlayerCoverBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        initOverlay();
        initInnerButton();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeBrowser();
        bindMediaController();
        toggleOverlayVisibility(false);
    }

    @Override
    public void onStop() {
        releaseBrowser();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initTapButtonHideTransition() {
        bind.nowPlayingTapButton.setVisibility(View.VISIBLE);

        handler.removeCallbacksAndMessages(null);

        final Runnable runnable = () -> bind.nowPlayingTapButton.setVisibility(View.GONE);
        handler.postDelayed(runnable, 10000);
    }

    private void initOverlay() {
        bind.nowPlayingSongCoverImageView.setOnClickListener(view -> toggleOverlayVisibility(true));
        bind.nowPlayingSongCoverButtonGroup.setOnClickListener(view -> toggleOverlayVisibility(false));
        bind.nowPlayingTapButton.setOnClickListener(view -> toggleOverlayVisibility(true));
    }

    private void toggleOverlayVisibility(boolean isVisible) {
        Transition transition = new Fade();
        transition.setDuration(200);
        transition.addTarget(bind.nowPlayingSongCoverButtonGroup);

        TransitionManager.beginDelayedTransition(bind.getRoot(), transition);
        bind.nowPlayingSongCoverButtonGroup.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        bind.nowPlayingTapButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);

        bind.innerButtonBottomRight.setVisibility(Preferences.isSyncronizationEnabled() ? View.VISIBLE : View.GONE);
        bind.innerButtonBottomRightAlternative.setVisibility(Preferences.isSyncronizationEnabled() ? View.GONE : View.VISIBLE);

        if (!isVisible) initTapButtonHideTransition();
    }

    private void initInnerButton() {
        playerBottomSheetViewModel.getLiveMedia().observe(getViewLifecycleOwner(), song -> {
            if (song != null && bind != null) {
                bind.innerButtonTopLeft.setOnClickListener(view -> {
                    DownloadUtil.getDownloadTracker(requireContext()).download(
                            MappingUtil.mapDownload(song),
                            new Download(song)
                    );
                });

                bind.innerButtonTopRight.setOnClickListener(view -> {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constants.TRACK_OBJECT, song);

                            PlaylistChooserDialog dialog = new PlaylistChooserDialog();
                            dialog.setArguments(bundle);
                            dialog.show(requireActivity().getSupportFragmentManager(), null);
                        }
                );

                bind.innerButtonBottomLeft.setOnClickListener(view -> {
                    playerBottomSheetViewModel.getMediaInstantMix(getViewLifecycleOwner(), song).observe(getViewLifecycleOwner(), media -> {
                        MediaManager.enqueue(mediaBrowserListenableFuture, media, true);
                    });
                });

                bind.innerButtonBottomRight.setOnClickListener(view -> {
                    if (playerBottomSheetViewModel.savePlayQueue()) {
                        Snackbar.make(requireView(), "Salvato", Snackbar.LENGTH_LONG).show();
                    }
                });

                bind.innerButtonBottomRightAlternative.setOnClickListener(view -> {
                    if (getActivity() != null) {
                        PlayerBottomSheetFragment playerBottomSheetFragment = (PlayerBottomSheetFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("PlayerBottomSheet");
                        if (playerBottomSheetFragment != null) {
                            playerBottomSheetFragment.goToLyricsPage();
                        }
                    }
                });
            }
        });
    }

    private void initializeBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void bindMediaController() {
        mediaBrowserListenableFuture.addListener(() -> {
            try {
                MediaBrowser mediaBrowser = mediaBrowserListenableFuture.get();
                setMediaBrowserListener(mediaBrowser);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }

    private void setMediaBrowserListener(MediaBrowser mediaBrowser) {
        setCover(mediaBrowser.getMediaMetadata());

        mediaBrowser.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                setCover(mediaMetadata);
                toggleOverlayVisibility(false);
            }
        });
    }

    private void setCover(MediaMetadata mediaMetadata) {
        CustomGlideRequest.Builder
                .from(requireContext(), mediaMetadata.extras != null ? mediaMetadata.extras.getString("coverArtId") : null, CustomGlideRequest.ResourceType.Song)
                .build()
                .into(bind.nowPlayingSongCoverImageView);
    }
}