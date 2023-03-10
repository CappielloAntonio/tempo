package com.cappielloantonio.play.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
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

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.databinding.InnerFragmentPlayerCoverBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.dialog.PlaylistChooserDialog;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

@UnstableApi
public class PlayerCoverFragment extends Fragment {
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;
    private InnerFragmentPlayerCoverBinding bind;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

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

    private void initOverlay() {
        bind.nowPlayingSongCoverImageView.setOnClickListener(view -> toggleOverlayVisibility(true));
        bind.nowPlayingSongCoverButtonGroup.setOnClickListener(view -> toggleOverlayVisibility(false));
    }

    private void toggleOverlayVisibility(boolean isVisible) {
        Transition transition = new Fade();
        transition.setDuration(200);
        transition.addTarget(bind.nowPlayingSongCoverButtonGroup);

        TransitionManager.beginDelayedTransition(bind.getRoot(), transition);
        bind.nowPlayingSongCoverButtonGroup.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void initInnerButton() {
        playerBottomSheetViewModel.getLiveMedia().observe(getViewLifecycleOwner(), song -> {
            if (song != null && bind != null) {
                bind.innerButtonTopLeft.setOnClickListener(view -> {
                    DownloadUtil.getDownloadTracker(requireContext()).download(
                            MappingUtil.mapMediaItem(song, false),
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
                MediaBrowser mediaBrowseri = mediaBrowserListenableFuture.get();
                setMediaBrowserListener(mediaBrowseri);
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
                .from(requireContext(), mediaMetadata.extras != null ? mediaMetadata.extras.getString("coverArtId") : null, CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(bind.nowPlayingSongCoverImageView);
    }
}