package com.cappielloantonio.play.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentPlayerBottomSheetBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.fragment.pager.PlayerControllerVerticalPager;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;
import com.google.android.material.elevation.SurfaceColors;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

@OptIn(markerClass = UnstableApi.class)
public class PlayerBottomSheetFragment extends Fragment {
    private FragmentPlayerBottomSheetBinding bind;

    private PlayerBottomSheetViewModel playerBottomSheetViewModel;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    private Handler progressBarHandler;
    private Runnable progressBarRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = FragmentPlayerBottomSheetBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        customizeBottomSheetBackground();
        initViewPager();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
        bindMediaController();
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

    private void customizeBottomSheetBackground() {
        bind.playerHeaderLayout.getRoot().setBackgroundColor(SurfaceColors.getColorForElevation(requireContext(), 2));
    }

    private void initViewPager() {
        bind.playerBodyLayout.playerBodyBottomSheetViewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        bind.playerBodyLayout.playerBodyBottomSheetViewPager.setAdapter(new PlayerControllerVerticalPager(this));
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaController.releaseFuture(mediaBrowserListenableFuture);
    }

    private void bindMediaController() {
        mediaBrowserListenableFuture.addListener(() -> {
            try {
                MediaBrowser mediaBrowser = mediaBrowserListenableFuture.get();

                setMediaControllerListener(mediaBrowser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }

    private void setMediaControllerListener(MediaBrowser mediaBrowser) {
        defineProgressBarHandler(mediaBrowser);
        setMediaControllerUI(mediaBrowser);
        setMetadata(mediaBrowser.getMediaMetadata());
        setContentDuration(mediaBrowser.getContentDuration());
        setPlayingState(mediaBrowser.isPlaying());
        setHeaderMediaController();
        setHeaderNextButtonState(mediaBrowser.hasNextMediaItem());

        mediaBrowser.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                setMediaControllerUI(mediaBrowser);
                setMetadata(mediaMetadata);
                setContentDuration(mediaBrowser.getContentDuration());
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                setPlayingState(isPlaying);
            }

            @Override
            public void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {
                Player.Listener.super.onSkipSilenceEnabledChanged(skipSilenceEnabled);
            }

            @Override
            public void onEvents(Player player, Player.Events events) {
                setHeaderNextButtonState(mediaBrowser.hasNextMediaItem());
            }
        });
    }

    private void setMetadata(MediaMetadata mediaMetadata) {
        if (mediaMetadata.extras != null) {
            playerBottomSheetViewModel.setLiveMedia(getViewLifecycleOwner(), mediaMetadata.extras.getString("type"), mediaMetadata.extras.getString("id"));
            playerBottomSheetViewModel.setLiveArtist(getViewLifecycleOwner(), mediaMetadata.extras.getString("type"), mediaMetadata.extras.getString("artistId"));

            bind.playerHeaderLayout.playerHeaderMediaTitleLabel.setText(MusicUtil.getReadableString(mediaMetadata.extras.getString("title")));
            bind.playerHeaderLayout.playerHeaderMediaArtistLabel.setText(MusicUtil.getReadableString(mediaMetadata.extras.getString("artist")));

            CustomGlideRequest.Builder
                    .from(requireContext(), mediaMetadata.extras.getString("coverArtId"), CustomGlideRequest.SONG_PIC, null)
                    .build()
                    .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                    .into(bind.playerHeaderLayout.playerHeaderMediaCoverImage);
        }
    }

    private void setMediaControllerUI(MediaBrowser mediaBrowser) {
        if (mediaBrowser.getMediaMetadata().extras != null) {
            switch (mediaBrowser.getMediaMetadata().extras.getString("type", Media.MEDIA_TYPE_MUSIC)) {
                case Media.MEDIA_TYPE_PODCAST:
                    bind.playerHeaderLayout.playerHeaderFastForwardMediaButton.setVisibility(View.VISIBLE);
                    bind.playerHeaderLayout.playerHeaderRewindMediaButton.setVisibility(View.VISIBLE);
                    bind.playerHeaderLayout.playerHeaderNextMediaButton.setVisibility(View.GONE);
                    break;
                case Media.MEDIA_TYPE_MUSIC:
                default:
                    bind.playerHeaderLayout.playerHeaderFastForwardMediaButton.setVisibility(View.GONE);
                    bind.playerHeaderLayout.playerHeaderRewindMediaButton.setVisibility(View.GONE);
                    bind.playerHeaderLayout.playerHeaderNextMediaButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void setContentDuration(long duration) {
        bind.playerHeaderLayout.playerHeaderSeekBar.setMax((int) (duration / 1000));
    }

    private void setProgress(MediaBrowser mediaBrowser) {
        if (bind != null)
            bind.playerHeaderLayout.playerHeaderSeekBar.setProgress((int) (mediaBrowser.getCurrentPosition() / 1000), true);
    }

    private void setPlayingState(boolean isPlaying) {
        bind.playerHeaderLayout.playerHeaderButton.setChecked(isPlaying);
        runProgressBarHandler(isPlaying);
    }

    private void setHeaderMediaController() {
        bind.playerHeaderLayout.playerHeaderButton.setOnClickListener(view -> bind.getRoot().findViewById(R.id.exo_play_pause).performClick());
        bind.playerHeaderLayout.playerHeaderNextMediaButton.setOnClickListener(view -> bind.getRoot().findViewById(R.id.exo_next).performClick());
        bind.playerHeaderLayout.playerHeaderRewindMediaButton.setOnClickListener(view -> bind.getRoot().findViewById(R.id.exo_rew).performClick());
        bind.playerHeaderLayout.playerHeaderFastForwardMediaButton.setOnClickListener(view -> bind.getRoot().findViewById(R.id.exo_ffwd).performClick());
    }

    private void setHeaderNextButtonState(boolean isEnabled) {
        bind.playerHeaderLayout.playerHeaderNextMediaButton.setEnabled(isEnabled);
        bind.playerHeaderLayout.playerHeaderNextMediaButton.setAlpha(isEnabled ? (float) 1.0 : (float) 0.3);
    }

    public View getPlayerHeader() {
        return requireView().findViewById(R.id.player_header_layout);
    }

    public void goBackToFirstPage() {
        bind.playerBodyLayout.playerBodyBottomSheetViewPager.setCurrentItem(0, false);
        goToControllerPage();
    }

    public void goToControllerPage() {
        PlayerControllerVerticalPager playerControllerVerticalPager = (PlayerControllerVerticalPager) bind.playerBodyLayout.playerBodyBottomSheetViewPager.getAdapter();
        if (playerControllerVerticalPager != null) {
            PlayerControllerFragment playerControllerFragment = (PlayerControllerFragment) playerControllerVerticalPager.getRegisteredFragment(0);
            if (playerControllerFragment != null) {
                playerControllerFragment.goToControllerPage();
            }
        }
    }

    public void goToLyricsPage() {
        PlayerControllerVerticalPager playerControllerVerticalPager = (PlayerControllerVerticalPager) bind.playerBodyLayout.playerBodyBottomSheetViewPager.getAdapter();
        if (playerControllerVerticalPager != null) {
            PlayerControllerFragment playerControllerFragment = (PlayerControllerFragment) playerControllerVerticalPager.getRegisteredFragment(0);
            if (playerControllerFragment != null) {
                playerControllerFragment.goToLyricsPage();
            }
        }
    }

    private void defineProgressBarHandler(MediaBrowser mediaBrowser) {
        progressBarHandler = new Handler();
        progressBarRunnable = () -> {
            setProgress(mediaBrowser);
            progressBarHandler.postDelayed(progressBarRunnable, 1000);
        };
    }

    private void runProgressBarHandler(boolean isPlaying) {
        if (isPlaying) {
            progressBarHandler.postDelayed(progressBarRunnable, 1000);
        } else {
            progressBarHandler.removeCallbacks(progressBarRunnable);
        }
    }
}
